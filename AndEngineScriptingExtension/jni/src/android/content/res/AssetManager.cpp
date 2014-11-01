#include "src/android/content/res/AssetManager.h"
#include "src/AndEngineScriptingExtension.h"

static jclass sAssetManagerClass;

JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_AssetManagerProxy_nativeInitClass(JNIEnv* pJNIEnv, jclass pJClass) {
	sAssetManagerClass = (jclass)JNI_ENV()->NewGlobalRef(JNI_ENV()->FindClass("android/content/res/AssetManager"));
}

// ===========================================================
// org.andengine.extension.scripting.engine.AssetManagerProxy
// ===========================================================

AssetManager::AssetManager(jobject pAssetManager) {
	this->mUnwrapped = pAssetManager;
}
