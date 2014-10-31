#ifndef VertexBufferObjectManager_H
#define VertexBufferObjectManager_H

#include <memory>
#include <jni.h>
#include "src/AndEngineScriptingExtension.h"
#include "src/Wrapper.h"

extern "C" {
	JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_opengl_vbo_VertexBufferObjectManagerProxy_nativeInitClass(JNIEnv*, jclass);
}

class VertexBufferObjectManager : public Wrapper {

	public:
		VertexBufferObjectManager(jobject);
		virtual jobject unwrap();
		VertexBufferObjectManager();

	protected:

	private:

};
#endif

