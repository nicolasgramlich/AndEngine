#include "src/org/andengine/opengl/font/Font.h"
#include "src/AndEngineScriptingExtension.h"

static jclass sFontClass;

static jmethodID sConstructor;

static jmethodID sLoadMethod;
static jmethodID sUnloadMethod;

JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_opengl_font_FontProxy_nativeInitClass(JNIEnv* pJNIEnv, jclass pJClass) {
	sFontClass = (jclass)JNI_ENV()->NewGlobalRef(pJClass);

	sConstructor = JNI_ENV()->GetMethodID(sFontClass, "<init>", "(JLorg/andengine/opengl/font/FontManager;Lorg/andengine/opengl/texture/ITexture;Landroid/graphics/Typeface;FZI)V");
	sLoadMethod = JNI_ENV()->GetMethodID(sFontClass, "load", "()V");
	sUnloadMethod = JNI_ENV()->GetMethodID(sFontClass, "unload", "()V");
}

// ===========================================================
// org.andengine.extension.scripting.opengl.font.FontProxy
// ===========================================================

Font::Font(FontManager* pFontManager, Texture* pTexture, jobject pTypeface, float pSize, bool pAntiAlias, int pColorARGBPackedInt) {
	this->mUnwrapped = JNI_ENV()->NewObject(sFontClass, sConstructor, (jlong)this, pFontManager->unwrap(), pTexture->unwrap(), pTypeface, pSize, pAntiAlias, pColorARGBPackedInt);
}

void Font::load() {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sLoadMethod);
}

void Font::unload() {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sUnloadMethod);
}
