#include <cstdlib>
#include "src/org/andengine/opengl/texture/Texture.h"

static jclass sTextureClass;
static jmethodID sConstructor____org_andengine_opengl_texture_TextureManager____org_andengine_opengl_texture_PixelFormat____org_andengine_opengl_texture_TextureOptions____org_andengine_opengl_texture_ITextureState__istener__;

JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_opengl_texture_TextureProxy_nativeInitClass(JNIEnv* pJNIEnv, jclass pJClass) {
	sTextureClass = (jclass)JNI_ENV()->NewGlobalRef(pJClass);
	sConstructor____org_andengine_opengl_texture_TextureManager____org_andengine_opengl_texture_PixelFormat____org_andengine_opengl_texture_TextureOptions____org_andengine_opengl_texture_ITextureState__istener__ = JNI_ENV()->GetMethodID(sTextureClass, "<init>", "(JLorg/andengine/opengl/texture/TextureManager;Lorg/andengine/opengl/texture/PixelFormat;Lorg/andengine/opengl/texture/TextureOptions;Lorg/andengine/opengl/texture/ITextureStateListener;)V");
}

Texture::Texture(jobject pTextureProxy) {
	this->mUnwrapped = pTextureProxy;
}
jobject Texture::unwrap() {
	return this->mUnwrapped;
}
Texture::Texture(TextureManager* pTextureManager, PixelFormat* pPixelFormat, TextureOptions* pTextureOptions, ITextureStateListener* pTextureStateListener) {
	this->mUnwrapped = JNI_ENV()->NewObject(sTextureClass, sConstructor____org_andengine_opengl_texture_TextureManager____org_andengine_opengl_texture_PixelFormat____org_andengine_opengl_texture_TextureOptions____org_andengine_opengl_texture_ITextureState__istener__, (jlong)this, pTextureManager->unwrap(), pPixelFormat->unwrap(), pTextureOptions->unwrap(), pTextureStateListener->unwrap());
}
Texture::Texture() {

}

