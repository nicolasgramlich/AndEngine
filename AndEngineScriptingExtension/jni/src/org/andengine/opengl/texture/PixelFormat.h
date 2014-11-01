#ifndef PixelFormat_H
#define PixelFormat_H

#include <jni.h>
#include "src/AndEngineScriptingExtension.h"
#include "src/Wrapper.h"

extern "C" {
	JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_opengl_texture_PixelFormatProxy_nativeInitClass(JNIEnv*, jclass);
}

class PixelFormat : public Wrapper {

	public:
		PixelFormat(jobject);
		static PixelFormat* UNDEFINED;
		static PixelFormat* RGBA_4444;
		static PixelFormat* RGBA_5551;
		static PixelFormat* RGBA_8888;
		static PixelFormat* RGB_565;
		static PixelFormat* A_8;
		static PixelFormat* I_8;
		static PixelFormat* AI_88;

};
#endif

