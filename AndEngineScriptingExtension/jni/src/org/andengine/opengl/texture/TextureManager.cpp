#include <cstdlib>
#include "src/org/andengine/opengl/texture/TextureManager.h"

static jclass sTextureManagerClass;
static jmethodID sConstructor;

JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_opengl_texture_TextureManagerProxy_nativeInitClass(JNIEnv* pJNIEnv, jclass pJClass) {
	sTextureManagerClass = (jclass)JNI_ENV()->NewGlobalRef(pJClass);
	sConstructor = JNI_ENV()->GetMethodID(sTextureManagerClass, "<init>", "(J)V");
}

TextureManager::TextureManager(jobject pTextureManagerProxy) {
	this->mUnwrapped = pTextureManagerProxy;
}
jobject TextureManager::unwrap() {
	return this->mUnwrapped;
}
TextureManager::TextureManager() {
	this->mUnwrapped = JNI_ENV()->NewObject(sTextureManagerClass, sConstructor, (jlong)this);
}

