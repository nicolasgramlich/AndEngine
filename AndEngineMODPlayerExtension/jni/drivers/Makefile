
DRV	= file.c oss.c hpux.c bsd.c solaris.c oss_seq.c alsa.c alsa05.c \
	  esd.c sgi.c os2_dart.c netbsd.c openbsd.c aix.c nas.c arts.c qnx.c \
	  win32.c smix.c wav.c osx.c beos.c alsa_seq.c pulseaudio.c \
	  amiga.c sndio.c

DRIVERS_OBJS	= file.o wav.o
DRIVERS_LOBJS	= smix.lo
DRIVERS_DFILES	= Makefile $(DRV) debug.c
DRIVERS_PATH	= src/drivers

# Don't include platform-specific drivers in DRIVERS_OBJS so we can generate
# foreign makefiles from it (such as MSVC .dsp files, etc)

D_OBJS	= $(addprefix $(DRIVERS_PATH)/,$(DRIVERS_OBJS) $(DRIVERS))
D_LOBJS	= $(addprefix $(DRIVERS_PATH)/,$(DRIVERS_LOBJS))

dist-drivers:
	mkdir -p $(DIST)/$(DRIVERS_PATH)
	cp -RPp $(addprefix $(DRIVERS_PATH)/,$(DRIVERS_DFILES)) $(DIST)/$(DRIVERS_PATH)

$(DRIVERS_PATH)/beos.o: src/drivers/beos.c
	g++ -c $(XCFLAGS) -o $@ $+

$(DRIVERS_PATH)/arts.o: src/drivers/arts.c
	$(CC) $(CFLAGS) $(XCFLAGS) `artsc-config --cflags` -o $@ $+

