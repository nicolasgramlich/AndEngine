#include <cstdlib>
#include "src/org/andengine/entity/shape/RectangularShape.h"

static jclass sRectangularShapeClass;
static jmethodID sConstructor__FFFF__org_andengine_opengl_shader_ShaderProgram__;

JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_entity_shape_RectangularShapeProxy_nativeInitClass(JNIEnv* pJNIEnv, jclass pJClass) {
	sRectangularShapeClass = (jclass)JNI_ENV()->NewGlobalRef(pJClass);
	sConstructor__FFFF__org_andengine_opengl_shader_ShaderProgram__ = JNI_ENV()->GetMethodID(sRectangularShapeClass, "<init>", "(JFFFFLorg/andengine/opengl/shader/ShaderProgram;)V");
}

RectangularShape::RectangularShape(jobject pRectangularShapeProxy) {
	this->mUnwrapped = pRectangularShapeProxy;
}
jobject RectangularShape::unwrap() {
	return this->mUnwrapped;
}
RectangularShape::RectangularShape(jfloat pX, jfloat pY, jfloat pWidth, jfloat pHeight, ShaderProgram* pShaderProgram) {
	this->mUnwrapped = JNI_ENV()->NewObject(sRectangularShapeClass, sConstructor__FFFF__org_andengine_opengl_shader_ShaderProgram__, (jlong)this, pX, pY, pWidth, pHeight, pShaderProgram->unwrap());
}
RectangularShape::RectangularShape() {

}

