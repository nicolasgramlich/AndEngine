#ifndef ISpriteVertexBufferObject_H
#define ISpriteVertexBufferObject_H

#include <memory>
#include <jni.h>
#include "src/org/andengine/opengl/vbo/IVertexBufferObject.h"
#include "src/org/andengine/opengl/vbo/VertexBufferObjectManager.h"

class ISpriteVertexBufferObject : public IVertexBufferObject {

	public:
		virtual ~ISpriteVertexBufferObject() { };
		virtual jobject unwrap() = 0;
		virtual VertexBufferObjectManager* getVertexBufferObjectManager() = 0;

};
#endif

