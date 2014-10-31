#ifndef IRectangleVertexBufferObject_H
#define IRectangleVertexBufferObject_H

#include <memory>
#include <jni.h>
#include "src/org/andengine/opengl/vbo/IVertexBufferObject.h"
#include "src/org/andengine/opengl/vbo/VertexBufferObjectManager.h"

class IRectangleVertexBufferObject : public IVertexBufferObject {

	public:
		virtual ~IRectangleVertexBufferObject() { };
		virtual jobject unwrap() = 0;
		virtual VertexBufferObjectManager* getVertexBufferObjectManager() = 0;

};
#endif

