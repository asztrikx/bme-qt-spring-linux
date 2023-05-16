# setting up
	sudo apt update && sudo apt install devscripts build-essential fakeroot util-linux binutils lintian debhelper dh-make

# in workdir
	debuild -us -uc


sudo dpkg-buildpackage -us -uc

dh_make -s -y --createorig

