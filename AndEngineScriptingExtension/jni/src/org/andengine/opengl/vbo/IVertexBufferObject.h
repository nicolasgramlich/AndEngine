#ifndef IVertexBufferObject_H
#define IVertexBufferObject_H

#include <memory>
#include <jni.h>
#include "src/org/andengine/util/IDisposable.h"
#include "src/org/andengine/opengl/vbo/VertexBufferObjectManager.h"

class IVertexBufferObject : public IDisposable {

	public:
		virtual ~IVertexBufferObject() { };
		virtual jobject unwrap() = 0;
		virtual VertexBufferObjectManager* getVertexBufferObjectManager() = 0;

};
#endif

