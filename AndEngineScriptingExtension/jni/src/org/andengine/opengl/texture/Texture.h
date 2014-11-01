#ifndef Texture_H
#define Texture_H

#include <memory>
#include <jni.h>
#include "src/AndEngineScriptingExtension.h"
#include "src/Wrapper.h"
#include "src/org/andengine/opengl/texture/ITexture.h"
#include "src/org/andengine/opengl/texture/TextureManager.h"
#include "src/org/andengine/opengl/texture/PixelFormat.h"
#include "src/org/andengine/opengl/texture/TextureOptions.h"
#include "src/org/andengine/opengl/texture/ITextureStateListener.h"

extern "C" {
	JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_opengl_texture_TextureProxy_nativeInitClass(JNIEnv*, jclass);
}

class Texture : public Wrapper, public ITexture {

	public:
		Texture(jobject);
		virtual jobject unwrap();
		Texture(TextureManager*, PixelFormat*, TextureOptions*, ITextureStateListener*);
		Texture();

	protected:

	private:

};
#endif

