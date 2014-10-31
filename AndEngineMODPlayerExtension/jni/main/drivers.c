
#ifdef HAVE_CONFIG_H
#include "config.h"
#endif

#include "xmp.h"

extern struct xmp_drv_info drv_file;
extern struct xmp_drv_info drv_wav;
extern struct xmp_drv_info drv_osx;
extern struct xmp_drv_info drv_solaris;
extern struct xmp_drv_info drv_hpux;
extern struct xmp_drv_info drv_bsd;
extern struct xmp_drv_info drv_netbsd;
extern struct xmp_drv_info drv_openbsd;
extern struct xmp_drv_info drv_sgi;
extern struct xmp_drv_info drv_aix;
extern struct xmp_drv_info drv_oss_seq;
extern struct xmp_drv_info drv_oss;
extern struct xmp_drv_info drv_alsa;
extern struct xmp_drv_info drv_alsa05;
extern struct xmp_drv_info drv_net;
extern struct xmp_drv_info drv_esd;
extern struct xmp_drv_info drv_arts;
extern struct xmp_drv_info drv_nas;
extern struct xmp_drv_info drv_pulseaudio;
extern struct xmp_drv_info drv_os2dart;
extern struct xmp_drv_info drv_qnx;
extern struct xmp_drv_info drv_beos;
extern struct xmp_drv_info drv_amiga;
extern struct xmp_drv_info drv_win32;


void init_drivers()
{
#ifdef DRIVER_OSX
    xmp_drv_register(&drv_osx);
#endif
#ifdef DRIVER_WIN32
    xmp_drv_register(&drv_win32);
#endif
#ifdef DRIVER_SOLARIS
    xmp_drv_register(&drv_solaris);
#endif
#ifdef DRIVER_HPUX
    xmp_drv_register(&drv_hpux);
#endif
#ifdef DRIVER_BSD
    xmp_drv_register(&drv_bsd);
#endif
#ifdef DRIVER_NETBSD
    xmp_drv_register(&drv_netbsd);
#endif
#ifdef DRIVER_OPENBSD
    xmp_drv_register(&drv_openbsd);
#endif
#ifdef DRIVER_SGI
    xmp_drv_register(&drv_sgi);
#endif
#ifdef DRIVER_OSS_SEQ
    xmp_drv_register(&drv_oss_seq);
#endif
#ifdef DRIVER_OSS
    xmp_drv_register(&drv_oss);
#endif
#ifdef DRIVER_ALSA
    xmp_drv_register(&drv_alsa);
#endif
#ifdef DRIVER_ALSA05
    xmp_drv_register(&drv_alsa05);
#endif
#ifdef DRIVER_QNX
    xmp_drv_register(&drv_qnx);
#endif
#ifdef DRIVER_BEOS
    xmp_drv_register(&drv_beos);
#endif
#ifdef DRIVER_AMIGA
    xmp_drv_register(&drv_amiga);
#endif
#ifdef DRIVER_NET
    xmp_drv_register(&drv_net);
#endif
#ifdef DRIVER_OS2DART
    xmp_drv_register(&drv_os2dart);
#endif
#ifdef DRIVER_PULSEAUDIO
    xmp_drv_register(&drv_pulseaudio);
#endif
#ifdef DRIVER_ARTS
    xmp_drv_register(&drv_arts);
#endif
#ifdef DRIVER_ESD
    xmp_drv_register(&drv_esd);
#endif
#ifdef DRIVER_NAS
    xmp_drv_register(&drv_nas);
#endif
    xmp_drv_register(&drv_file);
    xmp_drv_register(&drv_wav);

}

