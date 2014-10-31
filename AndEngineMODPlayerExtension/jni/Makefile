
VERSION	= 3.3.0-pre
DIST    = xmp-$(VERSION)
MODULES = "graff-strange_land.xm"
DFILES  = README INSTALL configure configure.in Makefile Makefile.rules.in \
	  scripts $(MODULES)
DDIRS	= docs drivers etc include loaders misc player plugin prowiz win32 \
	  tunenet main
LIB	= libxmp.a
V	= 0

all: binaries

include Makefile.rules

# This is the "lib" part
include src/include/Makefile
include src/loaders/Makefile
include src/loaders/prowizard/Makefile
include src/misc/Makefile
include src/player/Makefile
include src/win32/Makefile

LOBJS = $(OBJS:.o=.lo)

# These are the applications
include src/drivers/Makefile
include src/main/Makefile
include src/plugin/Makefile
include src/plugin/tunenet/Makefile
include docs/Makefile
include etc/Makefile

sinclude src/test/Makefile

XCFLAGS = -Isrc/include -DSYSCONFDIR=\"$(SYSCONFDIR)\" -DVERSION=\"$(VERSION)\"

.SUFFIXES: .c .o .lo .a .so .dll

# Implicit rules for object generation. Position-independent code is intended
# to be used in plugins, so it's also built thread-safe and with the callback
# driver only.

.c.o:
	@CMD='$(CC) $(CFLAGS) $(XCFLAGS) -o $*.o $<'; \
	if [ "$(V)" -gt 0 ]; then echo $$CMD; else echo CC $*.o ; fi; \
	eval $$CMD

.c.lo:
	@CMD='$(CC) $(CFLAGS) -fPIC -D_REENTRANT $(XCFLAGS) -o $*.lo $<'; \
	if [ "$(V)" -gt 0 ]; then echo $$CMD; else echo CC $*.lo ; fi; \
	eval $$CMD


binaries: src/main/xmp $(PLUGINS)

# Legacy libxmp.a creation, in case someone needs it
#$(LIB): $(OBJS)
#	@CMD='$(AR) r $@ $(LOBJS)'; \
#	if [ "$(V)" -gt 0 ]; then echo $$CMD; else echo AR $@ ; fi; \
#	eval $$CMD
#	ranlib libxmp.a

clean:
	@rm -f $(OBJS) $(LIB) $(OBJS:.o=.lo) $(M_OBJS) $(D_OBJS) $(CLEAN)

install: install-xmp install-etc install-docs $(addprefix install-, $(PLUGINS))
	@echo
	@echo "  Installation complete. To customize, copy $(SYSCONFDIR)/xmp.conf"
	@echo "  and $(SYSCONFDIR)/modules.conf to \$$HOME/.xmp/"
	@echo


depend:
	@echo Building dependencies...
	@echo > $@
	@for i in $(OBJS) $(D_OBJS) $(M_OBJS); do \
	    c="$${i%.o}.c"; l="$${i%.o}.lo"; \
	    $(CC) $(CFLAGS) $(XCFLAGS) -MM -MG $$c | \
		sed "s!^.*\.o:!$$i $$l:!" >> $@ ; \
	done
	    
dist: dist-prepare dist-subdirs

dist-prepare:
	rm -Rf $(DIST) $(DIST).tar.gz
	mkdir -p $(DIST)
	cp -RPp $(DFILES) $(DIST)/

dist-subdirs: $(addprefix dist-,$(DDIRS))
	chmod -R u+w $(DIST)/*
	tar cvf - $(DIST) | gzip -9c > $(DIST).tar.gz
	rm -Rf $(DIST)
	ls -l $(DIST).tar.gz

$(OBJS): Makefile.rules

$(LOBJS): Makefile.rules

sinclude depend

