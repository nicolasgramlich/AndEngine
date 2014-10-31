/* xfdmaster.library decruncher for XMP
 * Copyright (C) 2007 Chris Young
 */

#ifdef AMIGA
#define __USE_INLINE__
#include <proto/exec.h>
#include <proto/xfdmaster.h>
#include <exec/types.h>
#include <stdio.h>
#include <sys/stat.h>

struct Library *xfdMasterBase;
#ifdef __amigaos4__
struct xfdMasterIFace *IxfdMaster;
struct ExecIFace *IExec;
// = (struct ExecIFace *)(*(struct ExecBase **)4)->MainInterface;
#endif

struct xfdBufferInfo *open_xfd()
{
	IExec = (struct ExecIFace *)(*(struct ExecBase **)4)->MainInterface;

	if(xfdMasterBase = OpenLibrary("xfdmaster.library",38))
	{
#ifdef __amigaos4__
		if(IxfdMaster = (struct xfdMasterIFace *)GetInterface(xfdMasterBase,"main",1,NULL))
		{
#endif
			return(struct xfdBufferInfo *)xfdAllocObject(XFDOBJ_BUFFERINFO);
#ifdef __amigaos4__
		}
#endif
	}
	close_xfd(NULL);
	return NULL;
}

void close_xfd(struct xfdBufferInfo *xfdobj)
{
	if(xfdobj)
	{
		xfdFreeObject((APTR)xfdobj);
		xfdobj=NULL;
	}
#ifdef __amigaos4__
	if(IxfdMaster)
	{
		DropInterface((struct Interface *)IxfdMaster);
		IxfdMaster=NULL;
	}
#endif
	if(xfdMasterBase)
	{
		CloseLibrary(xfdMasterBase);
		xfdMasterBase=NULL;
	}
}

int decrunch_xfd (FILE *f1, FILE *f2)
{
    struct xfdBufferInfo *xfdobj;
    uint8 *packed;
    int plen,ret=-1;
    struct stat st;

    if (f2 == NULL)
	return -1;

    fstat(fileno(f1), &st);
    plen = st.st_size;

    packed = AllocVec(plen,MEMF_CLEAR);
    if (!packed) return -1;

    fread(packed,plen,1,f1);

	if(xfdobj=open_xfd())
	{
		xfdobj->xfdbi_SourceBufLen = plen;
		xfdobj->xfdbi_SourceBuffer = packed;
		xfdobj->xfdbi_Flags = XFDFB_RECOGEXTERN | XFDFB_RECOGTARGETLEN;
		xfdobj->xfdbi_PackerFlags = XFDPFB_RECOGLEN;
		if(xfdRecogBuffer(xfdobj))
		{
			xfdobj->xfdbi_TargetBufMemType = MEMF_ANY;
			if(xfdDecrunchBuffer(xfdobj))
			{
				if(fwrite(xfdobj->xfdbi_TargetBuffer,1,xfdobj->xfdbi_TargetBufSaveLen,f2) == xfdobj->xfdbi_TargetBufSaveLen) ret=0;
				FreeMem(xfdobj->xfdbi_TargetBuffer,xfdobj->xfdbi_TargetBufLen);
			}
			else
			{
				ret=-1;
			}
		}
		close_xfd(xfdobj);
	}
	FreeVec(packed);
	return(ret);
}

char *test_xfd	(unsigned char *buffer, int length)
{
	char *ret = NULL;

	struct xfdBufferInfo *xfdobj;

	if(xfdobj=open_xfd())
	{
		xfdobj->xfdbi_SourceBuffer = buffer;
		xfdobj->xfdbi_SourceBufLen = length;
		xfdobj->xfdbi_Flags = XFDFB_RECOGTARGETLEN | XFDFB_RECOGEXTERN;

		if(xfdRecogBuffer(xfdobj))
		{
			ret = xfdobj->xfdbi_PackerName;
		}
		close_xfd(xfdobj);
	}
	return(ret);
}

#endif /* AMIGA */
