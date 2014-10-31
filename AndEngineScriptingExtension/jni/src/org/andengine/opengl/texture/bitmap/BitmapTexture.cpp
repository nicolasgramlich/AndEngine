#include <cstdlib>
#include "src/org/andengine/opengl/texture/bitmap/BitmapTexture.h"

static jclass sBitmapTextureClass;
static jmethodID sConstructor____org_andengine_opengl_texture_TextureManager____org_andengine_util_adt_io_in_IInputStreamOpener__;
static jmethodID sConstructor____org_andengine_opengl_texture_TextureManager____org_andengine_util_adt_io_in_IInputStreamOpener____org_andengine_opengl_texture_bitmap_BitmapTextureFormat__;
static jmethodID sConstructor____org_andengine_opengl_texture_TextureManager____org_andengine_util_adt_io_in_IInputStreamOpener____org_andengine_opengl_texture_TextureOptions__;
static jmethodID sConstructor____org_andengine_opengl_texture_TextureManager____org_andengine_util_adt_io_in_IInputStreamOpener____org_andengine_opengl_texture_bitmap_BitmapTextureFormat____org_andengine_opengl_texture_TextureOptions__;
static jmethodID sConstructor____org_andengine_opengl_texture_TextureManager____org_andengine_util_adt_io_in_IInputStreamOpener____org_andengine_opengl_texture_bitmap_BitmapTextureFormat____org_andengine_opengl_texture_TextureOptions____org_andengine_opengl_texture_ITextureState__istener__;
static jmethodID sMethod__GetWidth;
static jmethodID sMethod__GetHeight;
static jmethodID sMethod__Load;
static jmethodID sMethod__Load____org_andengine_opengl_util_G__State__;
static jmethodID sMethod__Unload____org_andengine_opengl_util_G__State__;
static jmethodID sMethod__Unload;

JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_opengl_texture_bitmap_BitmapTextureProxy_nativeInitClass(JNIEnv* pJNIEnv, jclass pJClass) {
	sBitmapTextureClass = (jclass)JNI_ENV()->NewGlobalRef(pJClass);
	sConstructor____org_andengine_opengl_texture_TextureManager____org_andengine_util_adt_io_in_IInputStreamOpener__ = JNI_ENV()->GetMethodID(sBitmapTextureClass, "<init>", "(JLorg/andengine/opengl/texture/TextureManager;Lorg/andengine/util/adt/io/in/IInputStreamOpener;)V");
	sConstructor____org_andengine_opengl_texture_TextureManager____org_andengine_util_adt_io_in_IInputStreamOpener____org_andengine_opengl_texture_bitmap_BitmapTextureFormat__ = JNI_ENV()->GetMethodID(sBitmapTextureClass, "<init>", "(JLorg/andengine/opengl/texture/TextureManager;Lorg/andengine/util/adt/io/in/IInputStreamOpener;Lorg/andengine/opengl/texture/bitmap/BitmapTextureFormat;)V");
	sConstructor____org_andengine_opengl_texture_TextureManager____org_andengine_util_adt_io_in_IInputStreamOpener____org_andengine_opengl_texture_TextureOptions__ = JNI_ENV()->GetMethodID(sBitmapTextureClass, "<init>", "(JLorg/andengine/opengl/texture/TextureManager;Lorg/andengine/util/adt/io/in/IInputStreamOpener;Lorg/andengine/opengl/texture/TextureOptions;)V");
	sConstructor____org_andengine_opengl_texture_TextureManager____org_andengine_util_adt_io_in_IInputStreamOpener____org_andengine_opengl_texture_bitmap_BitmapTextureFormat____org_andengine_opengl_texture_TextureOptions__ = JNI_ENV()->GetMethodID(sBitmapTextureClass, "<init>", "(JLorg/andengine/opengl/texture/TextureManager;Lorg/andengine/util/adt/io/in/IInputStreamOpener;Lorg/andengine/opengl/texture/bitmap/BitmapTextureFormat;Lorg/andengine/opengl/texture/TextureOptions;)V");
	sConstructor____org_andengine_opengl_texture_TextureManager____org_andengine_util_adt_io_in_IInputStreamOpener____org_andengine_opengl_texture_bitmap_BitmapTextureFormat____org_andengine_opengl_texture_TextureOptions____org_andengine_opengl_texture_ITextureState__istener__ = JNI_ENV()->GetMethodID(sBitmapTextureClass, "<init>", "(JLorg/andengine/opengl/texture/TextureManager;Lorg/andengine/util/adt/io/in/IInputStreamOpener;Lorg/andengine/opengl/texture/bitmap/BitmapTextureFormat;Lorg/andengine/opengl/texture/TextureOptions;Lorg/andengine/opengl/texture/ITextureStateListener;)V");
	sMethod__GetWidth = JNI_ENV()->GetMethodID(sBitmapTextureClass, "getWidth", "()I");
	sMethod__GetHeight = JNI_ENV()->GetMethodID(sBitmapTextureClass, "getHeight", "()I");
	sMethod__Load = JNI_ENV()->GetMethodID(sBitmapTextureClass, "load", "()V");
	sMethod__Load____org_andengine_opengl_util_G__State__ = JNI_ENV()->GetMethodID(sBitmapTextureClass, "load", "(Lorg/andengine/opengl/util/GLState;)V");
	sMethod__Unload____org_andengine_opengl_util_G__State__ = JNI_ENV()->GetMethodID(sBitmapTextureClass, "unload", "(Lorg/andengine/opengl/util/GLState;)V");
	sMethod__Unload = JNI_ENV()->GetMethodID(sBitmapTextureClass, "unload", "()V");
}

BitmapTexture::BitmapTexture(jobject pBitmapTextureProxy) {
	this->mUnwrapped = pBitmapTextureProxy;
}
jobject BitmapTexture::unwrap() {
	return this->mUnwrapped;
}
BitmapTexture::BitmapTexture(TextureManager* pTextureManager, IInputStreamOpener* pInputStreamOpener) {
	this->mUnwrapped = JNI_ENV()->NewObject(sBitmapTextureClass, sConstructor____org_andengine_opengl_texture_TextureManager____org_andengine_util_adt_io_in_IInputStreamOpener__, (jlong)this, pTextureManager->unwrap(), pInputStreamOpener->unwrap());
}
BitmapTexture::BitmapTexture(TextureManager* pTextureManager, IInputStreamOpener* pInputStreamOpener, BitmapTextureFormat* pBitmapTextureFormat) {
	this->mUnwrapped = JNI_ENV()->NewObject(sBitmapTextureClass, sConstructor____org_andengine_opengl_texture_TextureManager____org_andengine_util_adt_io_in_IInputStreamOpener____org_andengine_opengl_texture_bitmap_BitmapTextureFormat__, (jlong)this, pTextureManager->unwrap(), pInputStreamOpener->unwrap(), pBitmapTextureFormat->unwrap());
}
BitmapTexture::BitmapTexture(TextureManager* pTextureManager, IInputStreamOpener* pInputStreamOpener, TextureOptions* pTextureOptions) {
	this->mUnwrapped = JNI_ENV()->NewObject(sBitmapTextureClass, sConstructor____org_andengine_opengl_texture_TextureManager____org_andengine_util_adt_io_in_IInputStreamOpener____org_andengine_opengl_texture_TextureOptions__, (jlong)this, pTextureManager->unwrap(), pInputStreamOpener->unwrap(), pTextureOptions->unwrap());
}
BitmapTexture::BitmapTexture(TextureManager* pTextureManager, IInputStreamOpener* pInputStreamOpener, BitmapTextureFormat* pBitmapTextureFormat, TextureOptions* pTextureOptions) {
	this->mUnwrapped = JNI_ENV()->NewObject(sBitmapTextureClass, sConstructor____org_andengine_opengl_texture_TextureManager____org_andengine_util_adt_io_in_IInputStreamOpener____org_andengine_opengl_texture_bitmap_BitmapTextureFormat____org_andengine_opengl_texture_TextureOptions__, (jlong)this, pTextureManager->unwrap(), pInputStreamOpener->unwrap(), pBitmapTextureFormat->unwrap(), pTextureOptions->unwrap());
}
BitmapTexture::BitmapTexture(TextureManager* pTextureManager, IInputStreamOpener* pInputStreamOpener, BitmapTextureFormat* pBitmapTextureFormat, TextureOptions* pTextureOptions, ITextureStateListener* pTextureStateListener) {
	this->mUnwrapped = JNI_ENV()->NewObject(sBitmapTextureClass, sConstructor____org_andengine_opengl_texture_TextureManager____org_andengine_util_adt_io_in_IInputStreamOpener____org_andengine_opengl_texture_bitmap_BitmapTextureFormat____org_andengine_opengl_texture_TextureOptions____org_andengine_opengl_texture_ITextureState__istener__, (jlong)this, pTextureManager->unwrap(), pInputStreamOpener->unwrap(), pBitmapTextureFormat->unwrap(), pTextureOptions->unwrap(), pTextureStateListener->unwrap());
}
BitmapTexture::BitmapTexture() {

}
jint BitmapTexture::getWidth() {
	return JNI_ENV()->CallIntMethod(this->mUnwrapped, sMethod__GetWidth);
}
jint BitmapTexture::getHeight() {
	return JNI_ENV()->CallIntMethod(this->mUnwrapped, sMethod__GetHeight);
}
void BitmapTexture::load() {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__Load);
}
void BitmapTexture::load(GLState* pGLState) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__Load____org_andengine_opengl_util_G__State__, pGLState->unwrap());
}
void BitmapTexture::unload(GLState* pGLState) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__Unload____org_andengine_opengl_util_G__State__, pGLState->unwrap());
}
void BitmapTexture::unload() {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__Unload);
}

