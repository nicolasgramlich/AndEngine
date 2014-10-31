#ifndef Shape_H
#define Shape_H

#include <memory>
#include <jni.h>
#include "src/AndEngineScriptingExtension.h"
#include "src/org/andengine/entity/Entity.h"
#include "src/org/andengine/entity/shape/IShape.h"
#include "src/org/andengine/opengl/shader/ShaderProgram.h"

extern "C" {
	JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_entity_shape_ShapeProxy_nativeInitClass(JNIEnv*, jclass);
}

class Shape : public Entity, public IShape {

	public:
		Shape(jobject);
		virtual jobject unwrap();
		Shape(jfloat, jfloat, ShaderProgram*);
		Shape();

	protected:

	private:

};
#endif

