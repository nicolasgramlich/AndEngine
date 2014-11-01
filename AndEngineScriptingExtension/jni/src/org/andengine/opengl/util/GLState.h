#ifndef GLState_H
#define GLState_H

#include <memory>
#include <jni.h>
#include "src/AndEngineScriptingExtension.h"
#include "src/Wrapper.h"

extern "C" {
	JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_opengl_util_GLStateProxy_nativeInitClass(JNIEnv*, jclass);
}

class GLState : public Wrapper {

	public:
		GLState(jobject);
		virtual jobject unwrap();
		GLState();

	protected:

	private:

};
#endif

