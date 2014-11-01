#ifndef BitmapTextureFormat_H
#define BitmapTextureFormat_H

#include <jni.h>
#include "src/AndEngineScriptingExtension.h"
#include "src/Wrapper.h"

extern "C" {
	JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_opengl_texture_bitmap_BitmapTextureFormatProxy_nativeInitClass(JNIEnv*, jclass);
}

class BitmapTextureFormat : public Wrapper {

	public:
		BitmapTextureFormat(jobject);
		static BitmapTextureFormat* RGBA_8888;
		static BitmapTextureFormat* RGB_565;
		static BitmapTextureFormat* RGBA_4444;
		static BitmapTextureFormat* A_8;

};
#endif

