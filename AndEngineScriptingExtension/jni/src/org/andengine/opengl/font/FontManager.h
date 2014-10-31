#ifndef FontManager_H
#define FontManager_H

#include <memory>
#include <jni.h>
#include "src/AndEngineScriptingExtension.h"
#include "src/Wrapper.h"

extern "C" {
	JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_opengl_font_FontManagerProxy_nativeInitClass(JNIEnv*, jclass);
}

class FontManager : public Wrapper {

	public:
		FontManager(jobject);
		virtual jobject unwrap();
		FontManager();

	protected:

	private:

};
#endif

