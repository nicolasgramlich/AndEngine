#include <cstdlib>
#include "src/org/andengine/opengl/util/GLState.h"

static jclass sGLStateClass;
static jmethodID sConstructor;

JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_opengl_util_GLStateProxy_nativeInitClass(JNIEnv* pJNIEnv, jclass pJClass) {
	sGLStateClass = (jclass)JNI_ENV()->NewGlobalRef(pJClass);
	sConstructor = JNI_ENV()->GetMethodID(sGLStateClass, "<init>", "(J)V");
}

GLState::GLState(jobject pGLStateProxy) {
	this->mUnwrapped = pGLStateProxy;
}
jobject GLState::unwrap() {
	return this->mUnwrapped;
}
GLState::GLState() {
	this->mUnwrapped = JNI_ENV()->NewObject(sGLStateClass, sConstructor, (jlong)this);
}

