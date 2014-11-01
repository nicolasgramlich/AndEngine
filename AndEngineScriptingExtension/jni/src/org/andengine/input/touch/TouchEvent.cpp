#include <cstdlib>
#include "src/org/andengine/input/touch/TouchEvent.h"

static jclass sTouchEventClass;
static jmethodID sConstructor;
static jmethodID sMethod__GetY;
static jmethodID sMethod__GetX;

JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_input_touch_TouchEventProxy_nativeInitClass(JNIEnv* pJNIEnv, jclass pJClass) {
	sTouchEventClass = (jclass)JNI_ENV()->NewGlobalRef(pJClass);
	sConstructor = JNI_ENV()->GetMethodID(sTouchEventClass, "<init>", "(J)V");
	sMethod__GetY = JNI_ENV()->GetMethodID(sTouchEventClass, "getY", "()F");
	sMethod__GetX = JNI_ENV()->GetMethodID(sTouchEventClass, "getX", "()F");
}

TouchEvent::TouchEvent(jobject pTouchEventProxy) {
	this->mUnwrapped = pTouchEventProxy;
}
jobject TouchEvent::unwrap() {
	return this->mUnwrapped;
}
TouchEvent::TouchEvent() {
	this->mUnwrapped = JNI_ENV()->NewObject(sTouchEventClass, sConstructor, (jlong)this);
}
jfloat TouchEvent::getY() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetY);
}
jfloat TouchEvent::getX() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetX);
}

