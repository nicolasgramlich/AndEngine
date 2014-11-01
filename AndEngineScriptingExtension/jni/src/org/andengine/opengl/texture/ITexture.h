#ifndef ITexture_H
#define ITexture_H

#include <memory>
#include <jni.h>
#include "src/org/andengine/opengl/util/GLState.h"
#include "src/org/andengine/opengl/util/GLState.h"

class ITexture {

	public:
		virtual ~ITexture() { };
		virtual jobject unwrap() = 0;
		virtual void load() = 0;
		virtual void load(GLState*) = 0;
		virtual void unload(GLState*) = 0;
		virtual void unload() = 0;
		virtual jint getWidth() = 0;
		virtual jint getHeight() = 0;

};
#endif

