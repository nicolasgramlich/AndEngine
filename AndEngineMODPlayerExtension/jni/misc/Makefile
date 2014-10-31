
MISC_OBJS	= control.o info.o readdata.o load.o ppdepack.o unsqsh.o \
		  mmcmp.o readrle.o readhuff.o readlzw.o unarc.o arcfs.o \
		  oxm.o mkstemp.o xfd.o crc32.o inflate.o muse.o unlzx.o \
		  s404_dec.o
MISC_DFILES	= Makefile $(MISC_OBJS:.o=.c)
MISC_PATH	= src/misc

OBJS += $(addprefix $(MISC_PATH)/,$(MISC_OBJS))

dist-misc:
	mkdir -p $(DIST)/$(MISC_PATH)
	cp -RPp $(addprefix $(MISC_PATH)/,$(MISC_DFILES)) $(DIST)/$(MISC_PATH)
