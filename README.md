# charDriver_socketApp_yocto

[Port-addressable linux device driver with yocto! - YouTube](https://youtu.be/B-kaz8u_9iA)

## File tree

├── **charDriver**<br>
&nbsp;│   ├── *Contains files related to the circular buffer and the device driver*<br>
├── **meta-socket_app**<br>
&nbsp;│   ├── *Contains files related to the yocto layer, image, and recipes*<br>
├── **poky**<br>
&nbsp;│   ├── *Contains files, on kirkstone branch*<br>
├── **socketApp**<br>
&nbsp;│   ├── *Contains files related to the socket app*<br>



##  Project features
### The project consists of 3 applications, that support each other to make a port-addressable driver
 #### 1. The circular buffer:

- The circular buffer can support up to 10 entries
- Works as a FIFO (first in first out) buffer
- If the FIFO is full and we add a new entry, the oldest entry in the buffer will get overwritten
- Implemented a read function that allows the user to access the buffer elements like a continuous string of arrays
- The buffer supports uncomplete writes
	- If a string is terminated with a newline character '\n', it is considered a complete write 
	- If a string is not terminated with a newline character '\n', it is considered a complete write, and the next string will append to this one to make a single entry in the buffer
#### 2. The device driver:
- Simple character driver that supports open, release, read, write, llseek, and ioctl operations
- Uses mutex locks to avoid race conditions and data corruption
- Uses printk to write kernel messages about operations happening at the moment
- Read and write operations uses the circular buffer to write into or read from
- llseek is used to change the current offset of the file using SEEK_SET, SEEK_CUR, and SEEK_END
- Implemented a single ioctl command that is used to move the buffer's offset in the circular buffer, to allow the user to read any entry and at any offset in that entry
#### 3. The socket app
- Opens a stream socket bound to port 9000
- Listens on port 9000 and creates a thread for each established connection
- Writes data to a predefined location and replies with all the data in that location/buffer
- Uses mutex locks to avoid race conditions and data corruption
- Uses syslog to log information about connections
- Has 2 modes of operation which can be changed by commenting a #define
	1. Driver mode: writes all incoming data to the device driver we are using
	2. Temp file mode: writes all incoming data to a temp file (/var/tmp/aesdsocketdata)
- Adds timestamp every 10 seconds when used in Temp file mode
- Tested with valgrind for any memory leaks
- Handles SIGINT and SIGTERM interrupts to allow the app to shutdown properly

## General Setup
**Host machine:** 	Ubuntu 20.04 LTS  
**Target machine:** BeagleBone Black<br>
**Yocto release:**		 Kirkstone

## Deployment

 1. Clone yocto repository (poky), select "kirkstone" branch
 2. Add the "meta-socket_app" layer to yocto, you can follow these steps
	 1. Change your working directory to yocto folder (poky)
	 2. Run ```source oe-init-build-env``` in the terminal to prepare the build environment
	 3. Run `bitbake-layers add-layer ../../meta-socket_app`, where (../../meta-socket_app is the relative path to the layer directory from yocto's build directory)
	 4. You can double check if the layer was added by running `bitbake-layers show-layers`
 3. After adding the layer, go to poky/build/conf/local.conf and set the MACHINE variable to your target architecture, I will choose `beaglebone-yocto`
 4. Run `bitbake omar-core-image-minimal` to start building your image (The build may take more than one hour, you can run it overnight or whenever you're not using your host"
 5. After the build is done, you'll find your image at poky/build/tmp/deploy/images/beaglebone-yocto/ (If you choosed a different MACHINE in step 3 you will find a different folder in images folder with a different name
 6. Mount your SD card to your host, mine is mounted as /dev/sdd (your SD card will mostly have a different name, make sure to double check with `lsblk`) 
 7. Open a terminal at the folder mentioned at point 5 and run `sudo dd if=omar-core-image-minimal-beaglebone-yocto.wic  of=/dev/sdd` 
 8. Remove the SD card from your host and install it in your target and restart your system (make sure to check how your system boots from SD card)
 
 



