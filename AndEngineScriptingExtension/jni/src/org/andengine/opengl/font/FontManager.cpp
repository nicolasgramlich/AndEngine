#include <cstdlib>
#include "src/org/andengine/opengl/font/FontManager.h"

static jclass sFontManagerClass;
static jmethodID sConstructor;

JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_opengl_font_FontManagerProxy_nativeInitClass(JNIEnv* pJNIEnv, jclass pJClass) {
	sFontManagerClass = (jclass)JNI_ENV()->NewGlobalRef(pJClass);
	sConstructor = JNI_ENV()->GetMethodID(sFontManagerClass, "<init>", "(J)V");
}

FontManager::FontManager(jobject pFontManagerProxy) {
	this->mUnwrapped = pFontManagerProxy;
}
jobject FontManager::unwrap() {
	return this->mUnwrapped;
}
FontManager::FontManager() {
	this->mUnwrapped = JNI_ENV()->NewObject(sFontManagerClass, sConstructor, (jlong)this);
}

