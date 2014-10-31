#include <cstdlib>
#include "src/org/andengine/opengl/shader/ShaderProgram.h"

static jclass sShaderProgramClass;
static jmethodID sConstructor____java_lang_String____java_lang_String__;
static jmethodID sConstructor____org_andengine_opengl_shader_source_IShaderSource____org_andengine_opengl_shader_source_IShaderSource__;

JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_opengl_shader_ShaderProgramProxy_nativeInitClass(JNIEnv* pJNIEnv, jclass pJClass) {
	sShaderProgramClass = (jclass)JNI_ENV()->NewGlobalRef(pJClass);
	sConstructor____java_lang_String____java_lang_String__ = JNI_ENV()->GetMethodID(sShaderProgramClass, "<init>", "(JLjava/lang/String;Ljava/lang/String;)V");
	sConstructor____org_andengine_opengl_shader_source_IShaderSource____org_andengine_opengl_shader_source_IShaderSource__ = JNI_ENV()->GetMethodID(sShaderProgramClass, "<init>", "(JLorg/andengine/opengl/shader/source/IShaderSource;Lorg/andengine/opengl/shader/source/IShaderSource;)V");
}

ShaderProgram::ShaderProgram(jobject pShaderProgramProxy) {
	this->mUnwrapped = pShaderProgramProxy;
}
jobject ShaderProgram::unwrap() {
	return this->mUnwrapped;
}
ShaderProgram::ShaderProgram(jstring pVertexShaderSource, jstring pFragmentShaderSource) {
	this->mUnwrapped = JNI_ENV()->NewObject(sShaderProgramClass, sConstructor____java_lang_String____java_lang_String__, (jlong)this, pVertexShaderSource, pFragmentShaderSource);
}
ShaderProgram::ShaderProgram(IShaderSource* pVertexShaderSource, IShaderSource* pFragmentShaderSource) {
	this->mUnwrapped = JNI_ENV()->NewObject(sShaderProgramClass, sConstructor____org_andengine_opengl_shader_source_IShaderSource____org_andengine_opengl_shader_source_IShaderSource__, (jlong)this, pVertexShaderSource->unwrap(), pFragmentShaderSource->unwrap());
}
ShaderProgram::ShaderProgram() {

}

