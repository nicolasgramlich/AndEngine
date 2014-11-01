#ifndef ShaderProgram_H
#define ShaderProgram_H

#include <memory>
#include <jni.h>
#include "src/AndEngineScriptingExtension.h"
#include "src/Wrapper.h"
#include "src/org/andengine/opengl/shader/source/IShaderSource.h"
#include "src/org/andengine/opengl/shader/source/IShaderSource.h"

extern "C" {
	JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_opengl_shader_ShaderProgramProxy_nativeInitClass(JNIEnv*, jclass);
}

class ShaderProgram : public Wrapper {

	public:
		ShaderProgram(jobject);
		virtual jobject unwrap();
		ShaderProgram(jstring, jstring);
		ShaderProgram(IShaderSource*, IShaderSource*);
		ShaderProgram();

	protected:

	private:

};
#endif

