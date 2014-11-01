#include <cstdlib>
#include "src/org/andengine/util/color/Color.h"

static jclass sColorClass;
static jmethodID sConstructor____org_andengine_util_color_Color__;
static jmethodID sConstructor__FFF;
static jmethodID sConstructor__FFFF;
static jmethodID sMethod__GetRed;
static jmethodID sMethod__GetGreen;
static jmethodID sMethod__GetBlue;
static jmethodID sMethod__GetAlpha;
static jmethodID sMethod__SetRed__F;
static jmethodID sMethod__SetGreen__F;
static jmethodID sMethod__SetBlue__F;
static jmethodID sMethod__SetAlpha__F;

JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_util_color_ColorProxy_nativeInitClass(JNIEnv* pJNIEnv, jclass pJClass) {
	sColorClass = (jclass)JNI_ENV()->NewGlobalRef(pJClass);
	sConstructor____org_andengine_util_color_Color__ = JNI_ENV()->GetMethodID(sColorClass, "<init>", "(JLorg/andengine/util/color/Color;)V");
	sConstructor__FFF = JNI_ENV()->GetMethodID(sColorClass, "<init>", "(JFFF)V");
	sConstructor__FFFF = JNI_ENV()->GetMethodID(sColorClass, "<init>", "(JFFFF)V");
	sMethod__GetRed = JNI_ENV()->GetMethodID(sColorClass, "getRed", "()F");
	sMethod__GetGreen = JNI_ENV()->GetMethodID(sColorClass, "getGreen", "()F");
	sMethod__GetBlue = JNI_ENV()->GetMethodID(sColorClass, "getBlue", "()F");
	sMethod__GetAlpha = JNI_ENV()->GetMethodID(sColorClass, "getAlpha", "()F");
	sMethod__SetRed__F = JNI_ENV()->GetMethodID(sColorClass, "setRed", "(F)V");
	sMethod__SetGreen__F = JNI_ENV()->GetMethodID(sColorClass, "setGreen", "(F)V");
	sMethod__SetBlue__F = JNI_ENV()->GetMethodID(sColorClass, "setBlue", "(F)V");
	sMethod__SetAlpha__F = JNI_ENV()->GetMethodID(sColorClass, "setAlpha", "(F)V");
}

Color::Color(jobject pColorProxy) {
	this->mUnwrapped = pColorProxy;
}
jobject Color::unwrap() {
	return this->mUnwrapped;
}
Color::Color(Color* pColor) {
	this->mUnwrapped = JNI_ENV()->NewObject(sColorClass, sConstructor____org_andengine_util_color_Color__, (jlong)this, pColor->unwrap());
}
Color::Color(jfloat pRed, jfloat pGreen, jfloat pBlue) {
	this->mUnwrapped = JNI_ENV()->NewObject(sColorClass, sConstructor__FFF, (jlong)this, pRed, pGreen, pBlue);
}
Color::Color(jfloat pRed, jfloat pGreen, jfloat pBlue, jfloat pAlpha) {
	this->mUnwrapped = JNI_ENV()->NewObject(sColorClass, sConstructor__FFFF, (jlong)this, pRed, pGreen, pBlue, pAlpha);
}
Color::Color() {

}
jfloat Color::getRed() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetRed);
}
jfloat Color::getGreen() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetGreen);
}
jfloat Color::getBlue() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetBlue);
}
jfloat Color::getAlpha() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetAlpha);
}
void Color::setRed(jfloat pRed) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetRed__F, pRed);
}
void Color::setGreen(jfloat pGreen) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetGreen__F, pGreen);
}
void Color::setBlue(jfloat pBlue) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetBlue__F, pBlue);
}
void Color::setAlpha(jfloat pAlpha) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetAlpha__F, pAlpha);
}

