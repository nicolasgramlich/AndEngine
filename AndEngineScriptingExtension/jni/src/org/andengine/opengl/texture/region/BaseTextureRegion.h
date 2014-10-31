#ifndef BaseTextureRegion_H
#define BaseTextureRegion_H

#include <memory>
#include <jni.h>
#include "src/AndEngineScriptingExtension.h"
#include "src/Wrapper.h"
#include "src/org/andengine/opengl/texture/region/ITextureRegion.h"
#include "src/org/andengine/opengl/texture/ITexture.h"

extern "C" {
	JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_opengl_texture_region_BaseTextureRegionProxy_nativeInitClass(JNIEnv*, jclass);
}

class BaseTextureRegion : public Wrapper, public ITextureRegion {

	public:
		BaseTextureRegion(jobject);
		virtual jobject unwrap();
		BaseTextureRegion(ITexture*);
		BaseTextureRegion();

	protected:

	private:

};
#endif

