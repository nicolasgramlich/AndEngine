#ifndef TextureManager_H
#define TextureManager_H

#include <memory>
#include <jni.h>
#include "src/AndEngineScriptingExtension.h"
#include "src/Wrapper.h"

extern "C" {
	JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_opengl_texture_TextureManagerProxy_nativeInitClass(JNIEnv*, jclass);
}

class TextureManager : public Wrapper {

	public:
		TextureManager(jobject);
		virtual jobject unwrap();
		TextureManager();

	protected:

	private:

};
#endif

