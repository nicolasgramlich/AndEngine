#ifndef DrawType_H
#define DrawType_H

#include <jni.h>
#include "src/AndEngineScriptingExtension.h"
#include "src/Wrapper.h"

extern "C" {
	JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_opengl_vbo_DrawTypeProxy_nativeInitClass(JNIEnv*, jclass);
}

class DrawType : public Wrapper {

	public:
		DrawType(jobject);
		static DrawType* STATIC;
		static DrawType* DYNAMIC;
		static DrawType* STREAM;

};
#endif

