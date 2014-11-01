
LOADERS	= xm_load.o mod_load.o s3m_load.o stm_load.o 669_load.o far_load.o \
	  mtm_load.o ptm_load.o okt_load.o alm_load.o amd_load.o rad_load.o \
	  ult_load.o mdl_load.o it_load.o stx_load.o pt3_load.o sfx_load.o \
	  flt_load.o st_load.o emod_load.o imf_load.o digi_load.o fnk_load.o \
	  ice_load.o hsc_load.o liq_load.o ims_load.o masi_load.o amf_load.o \
	  psm_load.o stim_load.o mmd1_load.o mmd3_load.o \
	  rtm_load.o dmf_load.o tcb_load.o dt_load.o gtk_load.o no_load.o \
	  arch_load.o sym_load.o dtt_load.o mgt_load.o med3_load.o \
	  med4_load.o ssmt_load.o dbm_load.o umx_load.o gdm_load.o \
	  coco_load.o pw_load.o gal5_load.o gal4_load.o mfp_load.o \
	  polly_load.o

LOADERS_OBJS	= common.o iff.o itsex.o asif.o voltable.o $(LOADERS)
LOADERS_DFILES	= Makefile $(LOADERS_OBJS:.o=.c) ftm_load.c med2_load.c \
		  asif.h far.h iff.h imf.h it.h liq.h load.h med.h mod.h \
		  mtm.h ptm.h rtm.h s3m.h stm.h stx.h xm.h
LOADERS_PATH	= src/loaders

OBJS += $(addprefix $(LOADERS_PATH)/,$(LOADERS_OBJS))

dist-loaders:
	mkdir -p $(DIST)/$(LOADERS_PATH)
	cp -RPp $(addprefix $(LOADERS_PATH)/,$(LOADERS_DFILES)) $(DIST)/$(LOADERS_PATH)

