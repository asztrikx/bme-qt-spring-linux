obj-m += busservice_gpio_driver.o

KDIR = /lib/modules/$(shell uname -r)/build

all:
	@echo
	@echo "---------------Welcome to VecsesIot Kerner Driver Compiler---------------"
	@echo
	@echo "Compiling driver!"
	@echo
	@echo
	make -C $(KDIR) M=$(shell pwd) modules
	@echo
	@echo "Done."

clean:
	@echo "Cleaning folder!"
	make -C $(KDIR) M=$(shell pwd) clean
	@echo "Done."
