#include <cstdlib>
#include "src/org/andengine/opengl/texture/region/BaseTextureRegion.h"

static jclass sBaseTextureRegionClass;
static jmethodID sConstructor____org_andengine_opengl_texture_ITexture__;

JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_opengl_texture_region_BaseTextureRegionProxy_nativeInitClass(JNIEnv* pJNIEnv, jclass pJClass) {
	sBaseTextureRegionClass = (jclass)JNI_ENV()->NewGlobalRef(pJClass);
	sConstructor____org_andengine_opengl_texture_ITexture__ = JNI_ENV()->GetMethodID(sBaseTextureRegionClass, "<init>", "(JLorg/andengine/opengl/texture/ITexture;)V");
}

BaseTextureRegion::BaseTextureRegion(jobject pBaseTextureRegionProxy) {
	this->mUnwrapped = pBaseTextureRegionProxy;
}
jobject BaseTextureRegion::unwrap() {
	return this->mUnwrapped;
}
BaseTextureRegion::BaseTextureRegion(ITexture* pTexture) {
	this->mUnwrapped = JNI_ENV()->NewObject(sBaseTextureRegionClass, sConstructor____org_andengine_opengl_texture_ITexture__, (jlong)this, pTexture->unwrap());
}
BaseTextureRegion::BaseTextureRegion() {

}

