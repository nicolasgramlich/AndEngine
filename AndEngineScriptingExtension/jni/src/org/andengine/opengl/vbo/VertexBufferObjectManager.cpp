#include <cstdlib>
#include "src/org/andengine/opengl/vbo/VertexBufferObjectManager.h"

static jclass sVertexBufferObjectManagerClass;
static jmethodID sConstructor;

JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_opengl_vbo_VertexBufferObjectManagerProxy_nativeInitClass(JNIEnv* pJNIEnv, jclass pJClass) {
	sVertexBufferObjectManagerClass = (jclass)JNI_ENV()->NewGlobalRef(pJClass);
	sConstructor = JNI_ENV()->GetMethodID(sVertexBufferObjectManagerClass, "<init>", "(J)V");
}

VertexBufferObjectManager::VertexBufferObjectManager(jobject pVertexBufferObjectManagerProxy) {
	this->mUnwrapped = pVertexBufferObjectManagerProxy;
}
jobject VertexBufferObjectManager::unwrap() {
	return this->mUnwrapped;
}
VertexBufferObjectManager::VertexBufferObjectManager() {
	this->mUnwrapped = JNI_ENV()->NewObject(sVertexBufferObjectManagerClass, sConstructor, (jlong)this);
}

