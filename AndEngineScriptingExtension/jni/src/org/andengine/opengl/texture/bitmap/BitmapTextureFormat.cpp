#include <cstdlib>
#include "src/org/andengine/opengl/texture/bitmap/BitmapTextureFormat.h"

static jclass sBitmapTextureFormatClass;
BitmapTextureFormat* BitmapTextureFormat::RGBA_8888 = NULL;
BitmapTextureFormat* BitmapTextureFormat::RGB_565 = NULL;
BitmapTextureFormat* BitmapTextureFormat::RGBA_4444 = NULL;
BitmapTextureFormat* BitmapTextureFormat::A_8 = NULL;

JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_opengl_texture_bitmap_BitmapTextureFormatProxy_nativeInitClass(JNIEnv* pJNIEnv, jclass pJClass) {
	sBitmapTextureFormatClass = (jclass)JNI_ENV()->NewGlobalRef(JNI_ENV()->FindClass("org/andengine/opengl/texture/bitmap/BitmapTextureFormat"));
	jfieldID BitmapTextureFormat_RGBA_8888_ID = JNI_ENV()->GetStaticFieldID(sBitmapTextureFormatClass, "RGBA_8888", "Lorg/andengine/opengl/texture/bitmap/BitmapTextureFormat;");
	BitmapTextureFormat::RGBA_8888 = new BitmapTextureFormat(JNI_ENV()->GetStaticObjectField(sBitmapTextureFormatClass, BitmapTextureFormat_RGBA_8888_ID));
	jfieldID BitmapTextureFormat_RGB_565_ID = JNI_ENV()->GetStaticFieldID(sBitmapTextureFormatClass, "RGB_565", "Lorg/andengine/opengl/texture/bitmap/BitmapTextureFormat;");
	BitmapTextureFormat::RGB_565 = new BitmapTextureFormat(JNI_ENV()->GetStaticObjectField(sBitmapTextureFormatClass, BitmapTextureFormat_RGB_565_ID));
	jfieldID BitmapTextureFormat_RGBA_4444_ID = JNI_ENV()->GetStaticFieldID(sBitmapTextureFormatClass, "RGBA_4444", "Lorg/andengine/opengl/texture/bitmap/BitmapTextureFormat;");
	BitmapTextureFormat::RGBA_4444 = new BitmapTextureFormat(JNI_ENV()->GetStaticObjectField(sBitmapTextureFormatClass, BitmapTextureFormat_RGBA_4444_ID));
	jfieldID BitmapTextureFormat_A_8_ID = JNI_ENV()->GetStaticFieldID(sBitmapTextureFormatClass, "A_8", "Lorg/andengine/opengl/texture/bitmap/BitmapTextureFormat;");
	BitmapTextureFormat::A_8 = new BitmapTextureFormat(JNI_ENV()->GetStaticObjectField(sBitmapTextureFormatClass, BitmapTextureFormat_A_8_ID));
}

BitmapTextureFormat::BitmapTextureFormat(jobject pBitmapTextureFormatProxy) {
	this->mUnwrapped = pBitmapTextureFormatProxy;
}

