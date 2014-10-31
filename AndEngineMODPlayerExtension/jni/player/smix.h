#ifndef __XMP_SMIX_H
#define __XMP_SMIX_H

void smix_resetvar(struct xmp_context *);
void smix_setpatch(struct xmp_context *, int, int);
void smix_voicepos(struct xmp_context *, int, int, int);
void smix_setnote(struct xmp_context *, int, int);
void smix_setbend(struct xmp_context *, int, int);

#endif
