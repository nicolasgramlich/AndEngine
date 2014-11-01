#include <cstdlib>
#include "src/org/andengine/opengl/texture/TextureOptions.h"

static jclass sTextureOptionsClass;
static jmethodID sConstructor__IIIIZ;

JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_opengl_texture_TextureOptionsProxy_nativeInitClass(JNIEnv* pJNIEnv, jclass pJClass) {
	sTextureOptionsClass = (jclass)JNI_ENV()->NewGlobalRef(pJClass);
	sConstructor__IIIIZ = JNI_ENV()->GetMethodID(sTextureOptionsClass, "<init>", "(JIIIIZ)V");
}

TextureOptions::TextureOptions(jobject pTextureOptionsProxy) {
	this->mUnwrapped = pTextureOptionsProxy;
}
jobject TextureOptions::unwrap() {
	return this->mUnwrapped;
}
TextureOptions::TextureOptions(jint pMinFilter, jint pMagFilter, jint pWrapT, jint pWrapS, jboolean pPreMultiplyAlpha) {
	this->mUnwrapped = JNI_ENV()->NewObject(sTextureOptionsClass, sConstructor__IIIIZ, (jlong)this, pMinFilter, pMagFilter, pWrapT, pWrapS, pPreMultiplyAlpha);
}
TextureOptions::TextureOptions() {

}

