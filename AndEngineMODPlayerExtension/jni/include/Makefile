
INCLUDE_DFILES	= Makefile config.h.in xmp.h common.h effects.h period.h \
		  driver.h mixer.h xxm.h getopt.h convert.h list.h \
		  readhuff.h readrle.h readlzw.h loader.h
INCLUDE_PATH	= src/include

dist-include:
	mkdir -p $(DIST)/$(INCLUDE_PATH)
	cp -RPp $(addprefix $(INCLUDE_PATH)/,$(INCLUDE_DFILES)) $(DIST)/$(INCLUDE_PATH)


