# setting up
	sudo apt update && sudo apt install devscripts build-essential fakeroot util-linux binutils lintian debhelper dh-make

# in workdir
Commands to use in the working directory
## creating base package
dh_make -s -y --createorig
## building package
sudo dpkg-buildpackage -us -uc

# Running application
Steps to run the application
## Installing application
sudo dpkg -i ...
## Exporting Environment Variable
export BASEADDRESS=<ip of the backend>
## Running program
neXtBus

# Testing functionality
dmesg | tail / dmesg -w
raspi-gpio get 37,35,33,31,29,40,38,36,32,22