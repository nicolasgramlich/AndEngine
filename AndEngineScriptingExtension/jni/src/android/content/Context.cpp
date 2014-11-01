#include "src/android/content/Context.h"
#include "src/AndEngineScriptingExtension.h"

static jclass sContextClass;

static jmethodID sGetAssetsMethod;

JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_ContextProxy_nativeInitClass(JNIEnv* pJNIEnv, jclass pJClass) {
	sContextClass = (jclass)JNI_ENV()->NewGlobalRef(JNI_ENV()->FindClass("android/content/Context"));

	sGetAssetsMethod = JNI_ENV()->GetMethodID(sContextClass, "getAssets", "()Landroid/content/res/AssetManager;");
}

// ===========================================================
// org.andengine.extension.scripting.engine.ContextProxy
// ===========================================================

Context::Context(jobject pContext) {
	this->mUnwrapped = pContext;
}

AssetManager* Context::getAssetManager() {
	return new AssetManager(JNI_ENV()->CallObjectMethod(this->mUnwrapped, sGetAssetsMethod));
}
