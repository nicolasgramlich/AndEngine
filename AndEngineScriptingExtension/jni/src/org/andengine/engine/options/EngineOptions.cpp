#include <cstdlib>
#include "src/org/andengine/engine/options/EngineOptions.h"

static jclass sEngineOptionsClass;
static jmethodID sConstructor__Z__org_andengine_engine_options_ScreenOrientation____org_andengine_engine_options_resolutionpolicy_IResolutionPolicy____org_andengine_engine_camera_Camera__;

JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_engine_options_EngineOptionsProxy_nativeInitClass(JNIEnv* pJNIEnv, jclass pJClass) {
	sEngineOptionsClass = (jclass)JNI_ENV()->NewGlobalRef(pJClass);
	sConstructor__Z__org_andengine_engine_options_ScreenOrientation____org_andengine_engine_options_resolutionpolicy_IResolutionPolicy____org_andengine_engine_camera_Camera__ = JNI_ENV()->GetMethodID(sEngineOptionsClass, "<init>", "(JZLorg/andengine/engine/options/ScreenOrientation;Lorg/andengine/engine/options/resolutionpolicy/IResolutionPolicy;Lorg/andengine/engine/camera/Camera;)V");
}

EngineOptions::EngineOptions(jobject pEngineOptionsProxy) {
	this->mUnwrapped = pEngineOptionsProxy;
}
jobject EngineOptions::unwrap() {
	return this->mUnwrapped;
}
EngineOptions::EngineOptions(jboolean pFullscreen, ScreenOrientation* pScreenOrientation, IResolutionPolicy* pResolutionPolicy, Camera* pCamera) {
	this->mUnwrapped = JNI_ENV()->NewObject(sEngineOptionsClass, sConstructor__Z__org_andengine_engine_options_ScreenOrientation____org_andengine_engine_options_resolutionpolicy_IResolutionPolicy____org_andengine_engine_camera_Camera__, (jlong)this, pFullscreen, pScreenOrientation->unwrap(), pResolutionPolicy->unwrap(), pCamera->unwrap());
}
EngineOptions::EngineOptions() {

}

