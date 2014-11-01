#include <cstdlib>
#include "src/org/andengine/opengl/texture/region/TextureRegion.h"

static jclass sTextureRegionClass;
static jmethodID sConstructor____org_andengine_opengl_texture_ITexture__FFFFZ;
static jmethodID sConstructor____org_andengine_opengl_texture_ITexture__FFFF;
static jmethodID sConstructor____org_andengine_opengl_texture_ITexture__FFFFF;
static jmethodID sConstructor____org_andengine_opengl_texture_ITexture__FFFFFZ;
static jmethodID sMethod__GetWidth;
static jmethodID sMethod__GetHeight;

JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_opengl_texture_region_TextureRegionProxy_nativeInitClass(JNIEnv* pJNIEnv, jclass pJClass) {
	sTextureRegionClass = (jclass)JNI_ENV()->NewGlobalRef(pJClass);
	sConstructor____org_andengine_opengl_texture_ITexture__FFFFZ = JNI_ENV()->GetMethodID(sTextureRegionClass, "<init>", "(JLorg/andengine/opengl/texture/ITexture;FFFFZ)V");
	sConstructor____org_andengine_opengl_texture_ITexture__FFFF = JNI_ENV()->GetMethodID(sTextureRegionClass, "<init>", "(JLorg/andengine/opengl/texture/ITexture;FFFF)V");
	sConstructor____org_andengine_opengl_texture_ITexture__FFFFF = JNI_ENV()->GetMethodID(sTextureRegionClass, "<init>", "(JLorg/andengine/opengl/texture/ITexture;FFFFF)V");
	sConstructor____org_andengine_opengl_texture_ITexture__FFFFFZ = JNI_ENV()->GetMethodID(sTextureRegionClass, "<init>", "(JLorg/andengine/opengl/texture/ITexture;FFFFFZ)V");
	sMethod__GetWidth = JNI_ENV()->GetMethodID(sTextureRegionClass, "getWidth", "()F");
	sMethod__GetHeight = JNI_ENV()->GetMethodID(sTextureRegionClass, "getHeight", "()F");
}

TextureRegion::TextureRegion(jobject pTextureRegionProxy) {
	this->mUnwrapped = pTextureRegionProxy;
}
jobject TextureRegion::unwrap() {
	return this->mUnwrapped;
}
TextureRegion::TextureRegion(ITexture* pTexture, jfloat pTextureX, jfloat pTextureY, jfloat pTextureWidth, jfloat pTextureHeight, jboolean pRotated) {
	this->mUnwrapped = JNI_ENV()->NewObject(sTextureRegionClass, sConstructor____org_andengine_opengl_texture_ITexture__FFFFZ, (jlong)this, pTexture->unwrap(), pTextureX, pTextureY, pTextureWidth, pTextureHeight, pRotated);
}
TextureRegion::TextureRegion(ITexture* pTexture, jfloat pTextureX, jfloat pTextureY, jfloat pTextureWidth, jfloat pTextureHeight) {
	this->mUnwrapped = JNI_ENV()->NewObject(sTextureRegionClass, sConstructor____org_andengine_opengl_texture_ITexture__FFFF, (jlong)this, pTexture->unwrap(), pTextureX, pTextureY, pTextureWidth, pTextureHeight);
}
TextureRegion::TextureRegion(ITexture* pTexture, jfloat pTextureX, jfloat pTextureY, jfloat pTextureWidth, jfloat pTextureHeight, jfloat pScale) {
	this->mUnwrapped = JNI_ENV()->NewObject(sTextureRegionClass, sConstructor____org_andengine_opengl_texture_ITexture__FFFFF, (jlong)this, pTexture->unwrap(), pTextureX, pTextureY, pTextureWidth, pTextureHeight, pScale);
}
TextureRegion::TextureRegion(ITexture* pTexture, jfloat pTextureX, jfloat pTextureY, jfloat pTextureWidth, jfloat pTextureHeight, jfloat pScale, jboolean pRotated) {
	this->mUnwrapped = JNI_ENV()->NewObject(sTextureRegionClass, sConstructor____org_andengine_opengl_texture_ITexture__FFFFFZ, (jlong)this, pTexture->unwrap(), pTextureX, pTextureY, pTextureWidth, pTextureHeight, pScale, pRotated);
}
TextureRegion::TextureRegion() {

}
jfloat TextureRegion::getWidth() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetWidth);
}
jfloat TextureRegion::getHeight() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetHeight);
}

