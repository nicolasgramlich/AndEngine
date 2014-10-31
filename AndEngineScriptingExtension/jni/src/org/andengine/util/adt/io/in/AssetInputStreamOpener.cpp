#include <cstdlib>
#include "src/org/andengine/util/adt/io/in/AssetInputStreamOpener.h"

static jclass sAssetInputStreamOpenerClass;
static jmethodID sConstructor____android_content_res_AssetManager____java_lang_String__;

JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_util_adt_io_in_AssetInputStreamOpenerProxy_nativeInitClass(JNIEnv* pJNIEnv, jclass pJClass) {
	sAssetInputStreamOpenerClass = (jclass)JNI_ENV()->NewGlobalRef(pJClass);
	sConstructor____android_content_res_AssetManager____java_lang_String__ = JNI_ENV()->GetMethodID(sAssetInputStreamOpenerClass, "<init>", "(JLandroid/content/res/AssetManager;Ljava/lang/String;)V");
}

AssetInputStreamOpener::AssetInputStreamOpener(jobject pAssetInputStreamOpenerProxy) {
	this->mUnwrapped = pAssetInputStreamOpenerProxy;
}
jobject AssetInputStreamOpener::unwrap() {
	return this->mUnwrapped;
}
AssetInputStreamOpener::AssetInputStreamOpener(AssetManager* pAssetManager, jstring pAssetPath) {
	this->mUnwrapped = JNI_ENV()->NewObject(sAssetInputStreamOpenerClass, sConstructor____android_content_res_AssetManager____java_lang_String__, (jlong)this, pAssetManager->unwrap(), pAssetPath);
}
AssetInputStreamOpener::AssetInputStreamOpener() {

}

