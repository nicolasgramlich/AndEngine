#include <cstdlib>
#include "src/org/andengine/engine/camera/Camera.h"

static jclass sCameraClass;
static jmethodID sConstructor__FFFF;
static jmethodID sMethod__GetWidth;
static jmethodID sMethod__GetHeight;
static jmethodID sMethod__GetRotation;
static jmethodID sMethod__SetRotation__F;

JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_engine_camera_CameraProxy_nativeInitClass(JNIEnv* pJNIEnv, jclass pJClass) {
	sCameraClass = (jclass)JNI_ENV()->NewGlobalRef(pJClass);
	sConstructor__FFFF = JNI_ENV()->GetMethodID(sCameraClass, "<init>", "(JFFFF)V");
	sMethod__GetWidth = JNI_ENV()->GetMethodID(sCameraClass, "getWidth", "()F");
	sMethod__GetHeight = JNI_ENV()->GetMethodID(sCameraClass, "getHeight", "()F");
	sMethod__GetRotation = JNI_ENV()->GetMethodID(sCameraClass, "getRotation", "()F");
	sMethod__SetRotation__F = JNI_ENV()->GetMethodID(sCameraClass, "setRotation", "(F)V");
}

Camera::Camera(jobject pCameraProxy) {
	this->mUnwrapped = pCameraProxy;
}
jobject Camera::unwrap() {
	return this->mUnwrapped;
}
Camera::Camera(jfloat pX, jfloat pY, jfloat pWidth, jfloat pHeight) {
	this->mUnwrapped = JNI_ENV()->NewObject(sCameraClass, sConstructor__FFFF, (jlong)this, pX, pY, pWidth, pHeight);
}
Camera::Camera() {

}
jfloat Camera::getWidth() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetWidth);
}
jfloat Camera::getHeight() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetHeight);
}
jfloat Camera::getRotation() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetRotation);
}
void Camera::setRotation(jfloat pRotation) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetRotation__F, pRotation);
}

