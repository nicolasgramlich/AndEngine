#include <cstdlib>
#include "src/org/andengine/opengl/texture/PixelFormat.h"

static jclass sPixelFormatClass;
PixelFormat* PixelFormat::UNDEFINED = NULL;
PixelFormat* PixelFormat::RGBA_4444 = NULL;
PixelFormat* PixelFormat::RGBA_5551 = NULL;
PixelFormat* PixelFormat::RGBA_8888 = NULL;
PixelFormat* PixelFormat::RGB_565 = NULL;
PixelFormat* PixelFormat::A_8 = NULL;
PixelFormat* PixelFormat::I_8 = NULL;
PixelFormat* PixelFormat::AI_88 = NULL;

JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_opengl_texture_PixelFormatProxy_nativeInitClass(JNIEnv* pJNIEnv, jclass pJClass) {
	sPixelFormatClass = (jclass)JNI_ENV()->NewGlobalRef(JNI_ENV()->FindClass("org/andengine/opengl/texture/PixelFormat"));
	jfieldID PixelFormat_UNDEFINED_ID = JNI_ENV()->GetStaticFieldID(sPixelFormatClass, "UNDEFINED", "Lorg/andengine/opengl/texture/PixelFormat;");
	PixelFormat::UNDEFINED = new PixelFormat(JNI_ENV()->GetStaticObjectField(sPixelFormatClass, PixelFormat_UNDEFINED_ID));
	jfieldID PixelFormat_RGBA_4444_ID = JNI_ENV()->GetStaticFieldID(sPixelFormatClass, "RGBA_4444", "Lorg/andengine/opengl/texture/PixelFormat;");
	PixelFormat::RGBA_4444 = new PixelFormat(JNI_ENV()->GetStaticObjectField(sPixelFormatClass, PixelFormat_RGBA_4444_ID));
	jfieldID PixelFormat_RGBA_5551_ID = JNI_ENV()->GetStaticFieldID(sPixelFormatClass, "RGBA_5551", "Lorg/andengine/opengl/texture/PixelFormat;");
	PixelFormat::RGBA_5551 = new PixelFormat(JNI_ENV()->GetStaticObjectField(sPixelFormatClass, PixelFormat_RGBA_5551_ID));
	jfieldID PixelFormat_RGBA_8888_ID = JNI_ENV()->GetStaticFieldID(sPixelFormatClass, "RGBA_8888", "Lorg/andengine/opengl/texture/PixelFormat;");
	PixelFormat::RGBA_8888 = new PixelFormat(JNI_ENV()->GetStaticObjectField(sPixelFormatClass, PixelFormat_RGBA_8888_ID));
	jfieldID PixelFormat_RGB_565_ID = JNI_ENV()->GetStaticFieldID(sPixelFormatClass, "RGB_565", "Lorg/andengine/opengl/texture/PixelFormat;");
	PixelFormat::RGB_565 = new PixelFormat(JNI_ENV()->GetStaticObjectField(sPixelFormatClass, PixelFormat_RGB_565_ID));
	jfieldID PixelFormat_A_8_ID = JNI_ENV()->GetStaticFieldID(sPixelFormatClass, "A_8", "Lorg/andengine/opengl/texture/PixelFormat;");
	PixelFormat::A_8 = new PixelFormat(JNI_ENV()->GetStaticObjectField(sPixelFormatClass, PixelFormat_A_8_ID));
	jfieldID PixelFormat_I_8_ID = JNI_ENV()->GetStaticFieldID(sPixelFormatClass, "I_8", "Lorg/andengine/opengl/texture/PixelFormat;");
	PixelFormat::I_8 = new PixelFormat(JNI_ENV()->GetStaticObjectField(sPixelFormatClass, PixelFormat_I_8_ID));
	jfieldID PixelFormat_AI_88_ID = JNI_ENV()->GetStaticFieldID(sPixelFormatClass, "AI_88", "Lorg/andengine/opengl/texture/PixelFormat;");
	PixelFormat::AI_88 = new PixelFormat(JNI_ENV()->GetStaticObjectField(sPixelFormatClass, PixelFormat_AI_88_ID));
}

PixelFormat::PixelFormat(jobject pPixelFormatProxy) {
	this->mUnwrapped = pPixelFormatProxy;
}

