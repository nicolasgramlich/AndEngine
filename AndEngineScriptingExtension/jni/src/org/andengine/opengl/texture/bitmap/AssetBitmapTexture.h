#ifndef AssetBitmapTexture_H
#define AssetBitmapTexture_H

#include <memory>
#include <jni.h>
#include "src/AndEngineScriptingExtension.h"
#include "src/org/andengine/opengl/texture/bitmap/BitmapTexture.h"
#include "src/org/andengine/opengl/texture/TextureManager.h"
#include "src/android/content/res/AssetManager.h"
#include "src/org/andengine/opengl/texture/TextureManager.h"
#include "src/android/content/res/AssetManager.h"
#include "src/org/andengine/opengl/texture/bitmap/BitmapTextureFormat.h"
#include "src/org/andengine/opengl/texture/TextureManager.h"
#include "src/android/content/res/AssetManager.h"
#include "src/org/andengine/opengl/texture/TextureOptions.h"
#include "src/org/andengine/opengl/texture/TextureManager.h"
#include "src/android/content/res/AssetManager.h"
#include "src/org/andengine/opengl/texture/bitmap/BitmapTextureFormat.h"
#include "src/org/andengine/opengl/texture/TextureOptions.h"
#include "src/org/andengine/opengl/texture/TextureManager.h"
#include "src/android/content/res/AssetManager.h"
#include "src/org/andengine/opengl/texture/bitmap/BitmapTextureFormat.h"
#include "src/org/andengine/opengl/texture/TextureOptions.h"
#include "src/org/andengine/opengl/texture/ITextureStateListener.h"
#include "src/org/andengine/opengl/util/GLState.h"
#include "src/org/andengine/opengl/util/GLState.h"

extern "C" {
	JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_opengl_texture_bitmap_AssetBitmapTextureProxy_nativeInitClass(JNIEnv*, jclass);
}

class AssetBitmapTexture : public BitmapTexture {

	public:
		AssetBitmapTexture(jobject);
		virtual jobject unwrap();
		AssetBitmapTexture(TextureManager*, AssetManager*, jstring);
		AssetBitmapTexture(TextureManager*, AssetManager*, jstring, BitmapTextureFormat*);
		AssetBitmapTexture(TextureManager*, AssetManager*, jstring, TextureOptions*);
		AssetBitmapTexture(TextureManager*, AssetManager*, jstring, BitmapTextureFormat*, TextureOptions*);
		AssetBitmapTexture(TextureManager*, AssetManager*, jstring, BitmapTextureFormat*, TextureOptions*, ITextureStateListener*);
		AssetBitmapTexture();
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

