#ifndef TextureOptions_H
#define TextureOptions_H

#include <memory>
#include <jni.h>
#include "src/AndEngineScriptingExtension.h"
#include "src/Wrapper.h"

extern "C" {
	JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_opengl_texture_TextureOptionsProxy_nativeInitClass(JNIEnv*, jclass);
}

class TextureOptions : public Wrapper {

	public:
		TextureOptions(jobject);
		virtual jobject unwrap();
		TextureOptions(jint, jint, jint, jint, jboolean);
		TextureOptions();

	protected:

	private:

};
#endif

