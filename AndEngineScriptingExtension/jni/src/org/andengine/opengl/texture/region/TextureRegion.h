#ifndef TextureRegion_H
#define TextureRegion_H

#include <memory>
#include <jni.h>
#include "src/AndEngineScriptingExtension.h"
#include "src/org/andengine/opengl/texture/region/BaseTextureRegion.h"
#include "src/org/andengine/opengl/texture/ITexture.h"
#include "src/org/andengine/opengl/texture/ITexture.h"
#include "src/org/andengine/opengl/texture/ITexture.h"
#include "src/org/andengine/opengl/texture/ITexture.h"

extern "C" {
	JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_opengl_texture_region_TextureRegionProxy_nativeInitClass(JNIEnv*, jclass);
}

class TextureRegion : public BaseTextureRegion {

	public:
		TextureRegion(jobject);
		virtual jobject unwrap();
		TextureRegion(ITexture*, jfloat, jfloat, jfloat, jfloat, jboolean);
		TextureRegion(ITexture*, jfloat, jfloat, jfloat, jfloat);
		TextureRegion(ITexture*, jfloat, jfloat, jfloat, jfloat, jfloat);
		TextureRegion(ITexture*, jfloat, jfloat, jfloat, jfloat, jfloat, jboolean);
		TextureRegion();
		virtual jfloat getWidth();
		virtual jfloat getHeight();

	protected:

	private:

};
#endif

