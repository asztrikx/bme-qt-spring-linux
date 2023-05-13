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
	static int pin_HIGH = INT_MAX;
	static const char* driver_name = "VecsesIot GPIO driver";

	// pins to be used by user
	static unsigned int gpio_pin_nums[] = {37, 35, 33, 31, 29, 40, 38, 36, 32, 22};

	// Turning one GPIO pin HIGH and all other used pin LOW
	static void gpio_turn_pin(unsigned int pin, int value)
	{
		pin_HIGH = value ? pin : INT_MAX;
		// On newer Raspberry Pi models the GPIO control registers are separated. 
		// The first 32 pins are mapped to bits 0-31 of the SET register, while the remaining pins
		// are mapped to bits 0-21 of another register.
		unsigned int* gpio_SET_register = (unsigned int*)((char*)gpio_registers + (pin > 31? 0x20 : 0x1c));
		pin = pin > 31 ? pin - 32 : pin;


		// drive all user gpio pins LOW
		for(int i = 0; i < sizeof(gpio_pin_nums) / sizeof(int); ++i)
		{
			unsigned int* gpio_CLEAR_register = (unsigned int*)((char*)gpio_registers + (gpio_pin_nums[i] > 31 ? 0x2c : 0x28));
			*gpio_CLEAR_register |= (1 << (gpio_pin_nums[i] > 31 ? gpio_pin_nums[i]-32 : gpio_pin_nums[i]));
		}

		

		// drive the selected pin HIGH
		if(value)
		{
			*gpio_SET_register = 0;
			*gpio_SET_register |= (1 << pin);
		}

		return;
	}

	// User reading proc file
	ssize_t proc_read(struct file *file, char __user *user, size_t size, loff_t *off)
	{
		// concatenating return string
		char msg[300] = "\n\nVecsesIot GPIO driver working correctly!\nInput: {pin_index}:{value}\nAvailable pins:";
		size_t length;
		char tmp[100];
		for(int i = 0; i < sizeof(gpio_pin_nums) / sizeof(int); ++i)
		{
			sprintf(tmp, "%d->%d, ", i, gpio_pin_nums[i]);
			strcat(msg, tmp);
		}
		if(pin_HIGH == INT_MAX)
		{
			sprintf(tmp, "\nCurrently all user pins are set to LOW!\n");
			strcat(msg, tmp);
		}
		else
		{
			sprintf(tmp, "\nCurrently GPIO%d is set to HIGH.\n", pin_HIGH);
			strcat(msg, tmp);
		}
		length = strlen(msg);

		// checking if we need to print anything
		if(*off >= length)
			return 0;

		// setting size to read size
		if(size > length - *off)
			size = length - *off;

		// printing to user
		if(copy_to_user(user, msg + *off, size))
			return -1;

		//setting the offset of next write
		*off += size;

		return  size;
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
			printk("%s: Invalid data format! %s\n", driver_name, data_buffer);
			return -1;
		}

		// check for input range
		if (pin > sizeof(gpio_pin_nums)/sizeof(int) || pin < 0)
		{
			printk("%s: Invalid pin number %d!\n", driver_name, pin);
			return -pin;
		}

		// map pin_index to phisical pin
		pin = gpio_pin_nums[pin];

		// checking value range
		if (value != 0 && value != 1)
		{
			printk("%s: Invalid on/off value %d!\n", driver_name, value);
			return -value;
		}

		// turning HIGH GPIO pin
		gpio_turn_pin(pin, value);
		printk("%s: Pin %d set to %d\n", driver_name, pin, value);
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
		printk("%s: Welcome to VecsesIot linux driver!\n", driver_name);
		
		gpio_registers = (int*)ioremap(GPIO_ADDRESS, PAGE_SIZE);
		if (gpio_registers == NULL)
		{
			printk("%s: Failed to map GPIO memory to driver!\n", driver_name);
			return -1;
		}
		
		printk("%s: Successfully mapped in GPIO memory\n", driver_name);
		
		// create an entry in the proc-fs
		proc = proc_create("busservice", 0666, NULL, &proc_fops);
		if (proc == NULL)
		{
			printk("%s: Failed to create proc file!\n", driver_name);
			return -1;
		}

		// setting used pins to output
		for(int i = 0; i < sizeof(gpio_pin_nums) / sizeof(int); ++i)
		{
			unsigned int gpio_reg_index = gpio_pin_nums[i]/10;
			unsigned int gpio_reg_pin = gpio_pin_nums[i]%10;
			unsigned int* gpio_reg = gpio_registers + gpio_reg_index;
			*gpio_reg &= ~(7 << (gpio_reg_pin*3));
			*gpio_reg |= (1 << (gpio_reg_pin*3));
		}
		printk("%s: Sucessfully set all used pins to output!", driver_name);

		return 0;
	}

	// Driver stopped
	static void __exit driver_exit(void)
	{
		printk("%s: Stopping VecsesIot driver!\n\n", driver_name);
		iounmap(gpio_registers);
		proc_remove(proc);
		return;
	}

	module_init(driver_init);
	module_exit(driver_exit);