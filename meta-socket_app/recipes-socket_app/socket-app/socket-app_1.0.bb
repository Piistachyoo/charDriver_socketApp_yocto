# See https://git.yoctoproject.org/poky/tree/meta/files/common-licenses
SUMMARY = "An app that opens a socket at port 9000"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

# TODO: Set this  with the path to your assignments rep.  Use ssh protocol and see lecture notes
# about how to setup ssh-agent for passwordless access
SRC_URI = "git://github.com/Piistachyoo/charDriver_socketApp_yocto;protocol=ssh;branch=main"


# TODO: set to reference a specific commit hash in your assignment repo
PV = "1.2+git${SRCPV}"
SRCREV = "af34d0d299d54b1628c4d057bdbc5b99909e710f"

# This sets your staging directory based on WORKDIR, where WORKDIR is defined at 
# https://docs.yoctoproject.org/ref-manual/variables.html?highlight=workdir#term-WORKDIR
# We reference the "server" directory here to build from the "server" directory
# in your assignments repo
S = "${WORKDIR}/git/socketApp"

# TODO: Add the aesdsocket application and any other files you need to install
# See https://git.yoctoproject.org/poky/plain/meta/conf/bitbake.conf?h=kirkstone
FILES:${PN} += "${bindir}/aesdsocket"

# TODO: customize these as necessary for any libraries you need for your application
# (and remove comment)
TARGET_CFLAGS += "-g -Wall -Werror -pthread"
TARGET_LDFLAGS += "-lpthread -lrt "

do_configure () {
	:
}

do_compile () {
	oe_runmake clean
	oe_runmake
}

do_install () {
	# TODO: Install your binaries/scripts here.
	# Be sure to install the target directory with install -d first
	# Yocto variables ${D} and ${S} are useful here, which you can read about at 
	# https://docs.yoctoproject.org/ref-manual/variables.html?highlight=workdir#term-D
	# and
	# https://docs.yoctoproject.org/ref-manual/variables.html?highlight=workdir#term-S
	# See example at https://github.com/cu-ecen-aeld/ecen5013-yocto/blob/ecen5013-hello-world/meta-ecen5013/recipes-ecen5013/ecen5013-hello-world/ecen5013-hello-world_git.bb
	install -d ${D}${bindir}
	install -d ${D}${sysconfdir}/init.d/
	install -d ${D}${sysconfdir}/rcS.d
	install -d ${D}${sysconfdir}/rc0.d
	install -d ${D}${sysconfdir}/rc6.d
	install -m 0755 ${S}/aesdsocket-start-stop ${D}${sysconfdir}/init.d/aesdsocket-start-stop
	install -m 0755 ${S}/aesdsocket-start-stop ${D}${sysconfdir}/rcS.d/S120aesdsocket
	install -m 0755 ${S}/aesdsocket-start-stop ${D}${sysconfdir}/rc0.d/K01aesdsocket
	install -m 0755 ${S}/aesdsocket-start-stop ${D}${sysconfdir}/rc6.d/K01aesdsocket
	install -m 0755 ${S}/aesdsocket ${D}${bindir}/aesdsocket
}


