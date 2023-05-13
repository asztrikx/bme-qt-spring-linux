#include <linux/kernel.h>
#include <linux/init.h>
#include <linux/module.h>
#include <linux/proc_fs.h>
#include <linux/slab.h>
#include <asm/io.h>

MODULE_LICENSE("GPL");
MODULE_AUTHOR("VecsesIot team");
MODULE_DESCRIPTION("GPIO driver for bus service display module.");
MODULE_VERSION("1.0");

#define GPIO_ADDRESS 0x3F200000

#define MAX_PROC_SIZE 20
static struct proc_dir_entry *proc = NULL;

static char data_buffer[MAX_PROC_SIZE+1] = {0};
static unsigned int *gpio_registers = NULL;

// pins to be used by user
static unsigned int gpio_pin_nums[] = {37, 35, 33, 31, 29, 40, 38, 36, 32, 22};

// Turning one GPIO pin HIGH and all other used pin LOW
static int gpio_turn_pin(unsigned int pin, int value)
{
	unsigned int* gpio_turn_register = (unsigned int*)((char*)gpio_registers + (value ? 0x1c : 0x28));

	// drive all user gpio pins LOW
	for(int i = 0; i < sizeof(gpio_pin_nums) / sizeof(int); ++i)
		*gpio_turn_register &= (0 << gpio_pin_nums[i]);

	// drive the selected pin HIGH
	*gpio_turn_register |= (1 << pin);
	return 0;
}

// User reading proc file
ssize_t proc_read(struct file *file, char __user *user, size_t size, loff_t *off)
{
	char* msg = "VecsesIot GPIO driver working correctly!\n\nInput: {pin_index}:{value}\nAvailable pins: 0->37, 1->35, 2->33, 3->31, 4->29, 5->40, 6->38, 7->36, 8->32, 9->22";
	return copy_to_user(user, msg, 157 ? 0 : 157);
}

// User writing proc file
ssize_t proc_write(struct file *file, const char __user *user, size_t size, loff_t *off)
{
	unsigned int pin = 0;
	unsigned int value = 0;

	// getting input
	memset(data_buffer, 0x0, sizeof(data_buffer));

	// prevent overflow
	if (size > MAX_PROC_SIZE)
	{
		size = MAX_PROC_SIZE;
	}

	// coping input from user
	if (copy_from_user(data_buffer, user, size))
		return 0;

	// parsing input
	if (sscanf(data_buffer, "%d:%d", &pin, &value) != 2)
	{
		printk("Invalid data format!\n");
		return -1;
	}

	// check for input range
	if (pin > sizeof(gpio_pin_nums)/sizeof(int) || pin < 0)
	{
		printk("Invalid pin number %d!\n", pin);
		return -pin;
	}

	// map pin_index to phisical pin
	pin = gpio_pin_nums[pin];

	// checking value range
	if (value != 0 && value != 1)
	{
		printk("Invalid on/off value %d!\n", value);
		return -value;
	}

	// turning HIGH GPIO pin
	gpio_turn_pin(pin, value);
	printk("Pin %d set to %d\n", pin, value);
	return size;
}

static const struct proc_ops proc_fops = 
{
	.proc_read = proc_read,
	.proc_write = proc_write,
};

// Driver started
static int __init driver_init(void)
{
	printk("Welcome to VecsesIot linux driver!\n");
	
	gpio_registers = (int*)ioremap(GPIO_ADDRESS, PAGE_SIZE);
	if (gpio_registers == NULL)
	{
		printk("Failed to map GPIO memory to driver!\n");
		return -1;
	}
	
	printk("Successfully mapped in GPIO memory\n");
	
	// create an entry in the proc-fs
	proc = proc_create("busservice", 0666, NULL, &proc_fops);
	if (proc == NULL)
	{
		printk("Failed to create proc file!\n");
		return -1;
	}

	// setting used pins to output
	for(int i = 0; i < sizeof(gpio_pin_nums) / sizeof(int); ++i)
	{
		unsigned int gpio_reg_index = i/10;
		unsigned int gpio_reg_pin = i%10;
		unsigned int* gpio_reg = gpio_registers + gpio_reg_index;
		*gpio_reg &= ~(7 << (gpio_reg_pin*3));
		*gpio_reg |= (1 << (gpio_reg_pin*3));
	}
	printk("Sucessfully set all used pins to output!");

	return 0;
}

// Driver stopped
static void __exit driver_exit(void)
{
	printk("Stopping VecsesIot driver!\n\n");
	iounmap(gpio_registers);
	proc_remove(proc);
	return;
}

module_init(driver_init);
module_exit(driver_exit);