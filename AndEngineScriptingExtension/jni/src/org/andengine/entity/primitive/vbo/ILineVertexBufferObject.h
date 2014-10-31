#ifndef ILineVertexBufferObject_H
#define ILineVertexBufferObject_H

#include <memory>
#include <jni.h>
#include "src/org/andengine/opengl/vbo/IVertexBufferObject.h"
#include "src/org/andengine/opengl/vbo/VertexBufferObjectManager.h"

class ILineVertexBufferObject : public IVertexBufferObject {

	public:
		virtual ~ILineVertexBufferObject() { };
		virtual jobject unwrap() = 0;
		virtual VertexBufferObjectManager* getVertexBufferObjectManager() = 0;

};
#endif

