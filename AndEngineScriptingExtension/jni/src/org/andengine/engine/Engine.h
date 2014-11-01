#ifndef Engine_H
#define Engine_H

#include <memory>
#include <jni.h>
#include "src/AndEngineScriptingExtension.h"
#include "src/Wrapper.h"
#include "src/org/andengine/engine/options/EngineOptions.h"
#include "src/org/andengine/opengl/vbo/VertexBufferObjectManager.h"
#include "src/org/andengine/opengl/texture/TextureManager.h"
#include "src/org/andengine/opengl/font/FontManager.h"

extern "C" {
	JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_engine_EngineProxy_nativeInitClass(JNIEnv*, jclass);
}

class VertexBufferObjectManager; // Forward declaration
class FontManager; // Forward declaration
class TextureManager; // Forward declaration

class Engine : public Wrapper {

	public:
		Engine(jobject);
		virtual jobject unwrap();
		Engine(EngineOptions*);
		Engine();
		virtual VertexBufferObjectManager* getVertexBufferObjectManager();
		virtual TextureManager* getTextureManager();
		virtual FontManager* getFontManager();

	protected:

	private:

};
#endif

