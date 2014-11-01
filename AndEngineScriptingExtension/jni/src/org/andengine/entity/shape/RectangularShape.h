#ifndef RectangularShape_H
#define RectangularShape_H

#include <memory>
#include <jni.h>
#include "src/AndEngineScriptingExtension.h"
#include "src/org/andengine/entity/shape/Shape.h"
#include "src/org/andengine/entity/shape/IAreaShape.h"
#include "src/org/andengine/opengl/shader/ShaderProgram.h"

extern "C" {
	JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_entity_shape_RectangularShapeProxy_nativeInitClass(JNIEnv*, jclass);
}

class RectangularShape : public Shape, public IAreaShape {

	public:
		RectangularShape(jobject);
		virtual jobject unwrap();
		RectangularShape(jfloat, jfloat, jfloat, jfloat, ShaderProgram*);
		RectangularShape();

	protected:

	private:

};
#endif

