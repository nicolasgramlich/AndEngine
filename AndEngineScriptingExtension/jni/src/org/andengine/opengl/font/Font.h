#ifndef Font_H
#define Font_H

#include <jni.h>
#include "src/Wrapper.h"
#include "src/org/andengine/opengl/font/FontManager.h"
#include "src/org/andengine/opengl/texture/Texture.h"

extern "C" {
	// ===========================================================
	// org.andengine.extension.scripting.opengl.font.FontProxy
	// ===========================================================

	JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_opengl_font_FontProxy_nativeInitClass(JNIEnv*, jclass);
}

class FontManager; // Forward declaration!

class Font : public Wrapper {
	protected:
		/* Constructors. */
		Font(FontManager*, Texture*, jobject, float, bool, int);

	public:
		/* Methods. */
		void load();
		void unload();
};

#endif
