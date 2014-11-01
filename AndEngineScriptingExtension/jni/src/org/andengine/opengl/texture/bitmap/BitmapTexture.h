#ifndef BitmapTexture_H
#define BitmapTexture_H

#include <memory>
#include <jni.h>
#include "src/AndEngineScriptingExtension.h"
#include "src/org/andengine/opengl/texture/Texture.h"
#include "src/org/andengine/opengl/texture/TextureManager.h"
#include "src/org/andengine/util/adt/io/in/IInputStreamOpener.h"
#include "src/org/andengine/opengl/texture/TextureManager.h"
#include "src/org/andengine/util/adt/io/in/IInputStreamOpener.h"
#include "src/org/andengine/opengl/texture/bitmap/BitmapTextureFormat.h"
#include "src/org/andengine/opengl/texture/TextureManager.h"
#include "src/org/andengine/util/adt/io/in/IInputStreamOpener.h"
#include "src/org/andengine/opengl/texture/TextureOptions.h"
#include "src/org/andengine/opengl/texture/TextureManager.h"
#include "src/org/andengine/util/adt/io/in/IInputStreamOpener.h"
#include "src/org/andengine/opengl/texture/bitmap/BitmapTextureFormat.h"
#include "src/org/andengine/opengl/texture/TextureOptions.h"
#include "src/org/andengine/opengl/texture/TextureManager.h"
#include "src/org/andengine/util/adt/io/in/IInputStreamOpener.h"
#include "src/org/andengine/opengl/texture/bitmap/BitmapTextureFormat.h"
#include "src/org/andengine/opengl/texture/TextureOptions.h"
#include "src/org/andengine/opengl/texture/ITextureStateListener.h"
#include "src/org/andengine/opengl/util/GLState.h"
#include "src/org/andengine/opengl/util/GLState.h"

extern "C" {
	JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_opengl_texture_bitmap_BitmapTextureProxy_nativeInitClass(JNIEnv*, jclass);
}

class BitmapTexture : public Texture {

	public:
		BitmapTexture(jobject);
		virtual jobject unwrap();
		BitmapTexture(TextureManager*, IInputStreamOpener*);
		BitmapTexture(TextureManager*, IInputStreamOpener*, BitmapTextureFormat*);
		BitmapTexture(TextureManager*, IInputStreamOpener*, TextureOptions*);
		BitmapTexture(TextureManager*, IInputStreamOpener*, BitmapTextureFormat*, TextureOptions*);
		BitmapTexture(TextureManager*, IInputStreamOpener*, BitmapTextureFormat*, TextureOptions*, ITextureStateListener*);
		BitmapTexture();
		virtual jint getWidth();
		virtual jint getHeight();
		virtual void load();
		virtual void load(GLState*);
		virtual void unload(GLState*);
		virtual void unload();

	protected:

	private:

};
#endif

