#include <cstdlib>
#include "src/org/andengine/entity/shape/Shape.h"

static jclass sShapeClass;
static jmethodID sConstructor__FF__org_andengine_opengl_shader_ShaderProgram__;

JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_entity_shape_ShapeProxy_nativeInitClass(JNIEnv* pJNIEnv, jclass pJClass) {
	sShapeClass = (jclass)JNI_ENV()->NewGlobalRef(pJClass);
	sConstructor__FF__org_andengine_opengl_shader_ShaderProgram__ = JNI_ENV()->GetMethodID(sShapeClass, "<init>", "(JFFLorg/andengine/opengl/shader/ShaderProgram;)V");
}

Shape::Shape(jobject pShapeProxy) {
	this->mUnwrapped = pShapeProxy;
}
jobject Shape::unwrap() {
	return this->mUnwrapped;
}
Shape::Shape(jfloat pX, jfloat pY, ShaderProgram* pShaderProgram) {
	this->mUnwrapped = JNI_ENV()->NewObject(sShapeClass, sConstructor__FF__org_andengine_opengl_shader_ShaderProgram__, (jlong)this, pX, pY, pShaderProgram->unwrap());
}
Shape::Shape() {

}

