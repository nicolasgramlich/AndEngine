#include <cstdlib>
#include "src/org/andengine/engine/camera/hud/HUD.h"

static jclass sHUDClass;
static jmethodID sConstructor;
static jmethodID sMethod__GetY;
static jmethodID sMethod__GetX;
static jmethodID sMethod__SetColor__FFF;
static jmethodID sMethod__SetColor____org_andengine_util_color_Color__;
static jmethodID sMethod__SetColor__FFFF;
static jmethodID sMethod__SetScale__FF;
static jmethodID sMethod__SetScale__F;
static jmethodID sMethod__GetRotation;
static jmethodID sMethod__SetRotation__F;
static jmethodID sMethod__AttachChild____org_andengine_entity_IEntity__;
static jmethodID sMethod__AttachChild____org_andengine_entity_IEntity__I;
static jmethodID sMethod__SetX__F;
static jmethodID sMethod__SetY__F;
static jmethodID sMethod__GetScaleX;
static jmethodID sMethod__GetScaleY;
static jmethodID sMethod__GetSkewX;
static jmethodID sMethod__GetSkewY;
static jmethodID sMethod__SetSkew__FF;
static jmethodID sMethod__SetSkew__F;
static jmethodID sMethod__GetRed;
static jmethodID sMethod__GetGreen;
static jmethodID sMethod__GetBlue;
static jmethodID sMethod__GetAlpha;
static jmethodID sMethod__GetColor;
static jmethodID sMethod__SetRed__F;
static jmethodID sMethod__SetGreen__F;
static jmethodID sMethod__SetBlue__F;
static jmethodID sMethod__SetAlpha__F;
static jmethodID sMethod__GetChildCount;
static jmethodID sMethod__GetChild__I;
static jmethodID sMethod__GetChild____org_andengine_entity_IEntityMatcher__;
static jmethodID sMethod__DetachSelf;
static jmethodID sMethod__DetachChild____org_andengine_entity_IEntityMatcher__;
static jmethodID sMethod__DetachChild____org_andengine_entity_IEntity__;
static jmethodID sMethod__SwapChildren__II;
static jmethodID sMethod__SwapChildren____org_andengine_entity_IEntity____org_andengine_entity_IEntity__;

JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_engine_camera_hud_HUDProxy_nativeInitClass(JNIEnv* pJNIEnv, jclass pJClass) {
	sHUDClass = (jclass)JNI_ENV()->NewGlobalRef(pJClass);
	sConstructor = JNI_ENV()->GetMethodID(sHUDClass, "<init>", "(J)V");
	sMethod__GetY = JNI_ENV()->GetMethodID(sHUDClass, "getY", "()F");
	sMethod__GetX = JNI_ENV()->GetMethodID(sHUDClass, "getX", "()F");
	sMethod__SetColor__FFF = JNI_ENV()->GetMethodID(sHUDClass, "setColor", "(FFF)V");
	sMethod__SetColor____org_andengine_util_color_Color__ = JNI_ENV()->GetMethodID(sHUDClass, "setColor", "(Lorg/andengine/util/color/Color;)V");
	sMethod__SetColor__FFFF = JNI_ENV()->GetMethodID(sHUDClass, "setColor", "(FFFF)V");
	sMethod__SetScale__FF = JNI_ENV()->GetMethodID(sHUDClass, "setScale", "(FF)V");
	sMethod__SetScale__F = JNI_ENV()->GetMethodID(sHUDClass, "setScale", "(F)V");
	sMethod__GetRotation = JNI_ENV()->GetMethodID(sHUDClass, "getRotation", "()F");
	sMethod__SetRotation__F = JNI_ENV()->GetMethodID(sHUDClass, "setRotation", "(F)V");
	sMethod__AttachChild____org_andengine_entity_IEntity__ = JNI_ENV()->GetMethodID(sHUDClass, "attachChild", "(Lorg/andengine/entity/IEntity;)V");
	sMethod__AttachChild____org_andengine_entity_IEntity__I = JNI_ENV()->GetMethodID(sHUDClass, "attachChild", "(Lorg/andengine/entity/IEntity;I)Z");
	sMethod__SetX__F = JNI_ENV()->GetMethodID(sHUDClass, "setX", "(F)V");
	sMethod__SetY__F = JNI_ENV()->GetMethodID(sHUDClass, "setY", "(F)V");
	sMethod__GetScaleX = JNI_ENV()->GetMethodID(sHUDClass, "getScaleX", "()F");
	sMethod__GetScaleY = JNI_ENV()->GetMethodID(sHUDClass, "getScaleY", "()F");
	sMethod__GetSkewX = JNI_ENV()->GetMethodID(sHUDClass, "getSkewX", "()F");
	sMethod__GetSkewY = JNI_ENV()->GetMethodID(sHUDClass, "getSkewY", "()F");
	sMethod__SetSkew__FF = JNI_ENV()->GetMethodID(sHUDClass, "setSkew", "(FF)V");
	sMethod__SetSkew__F = JNI_ENV()->GetMethodID(sHUDClass, "setSkew", "(F)V");
	sMethod__GetRed = JNI_ENV()->GetMethodID(sHUDClass, "getRed", "()F");
	sMethod__GetGreen = JNI_ENV()->GetMethodID(sHUDClass, "getGreen", "()F");
	sMethod__GetBlue = JNI_ENV()->GetMethodID(sHUDClass, "getBlue", "()F");
	sMethod__GetAlpha = JNI_ENV()->GetMethodID(sHUDClass, "getAlpha", "()F");
	sMethod__GetColor = JNI_ENV()->GetMethodID(sHUDClass, "getColor", "()Lorg/andengine/util/color/Color;");
	sMethod__SetRed__F = JNI_ENV()->GetMethodID(sHUDClass, "setRed", "(F)V");
	sMethod__SetGreen__F = JNI_ENV()->GetMethodID(sHUDClass, "setGreen", "(F)V");
	sMethod__SetBlue__F = JNI_ENV()->GetMethodID(sHUDClass, "setBlue", "(F)V");
	sMethod__SetAlpha__F = JNI_ENV()->GetMethodID(sHUDClass, "setAlpha", "(F)V");
	sMethod__GetChildCount = JNI_ENV()->GetMethodID(sHUDClass, "getChildCount", "()I");
	sMethod__GetChild__I = JNI_ENV()->GetMethodID(sHUDClass, "getChild", "(I)Lorg/andengine/entity/IEntity;");
	sMethod__GetChild____org_andengine_entity_IEntityMatcher__ = JNI_ENV()->GetMethodID(sHUDClass, "getChild", "(Lorg/andengine/entity/IEntityMatcher;)Lorg/andengine/entity/IEntity;");
	sMethod__DetachSelf = JNI_ENV()->GetMethodID(sHUDClass, "detachSelf", "()Z");
	sMethod__DetachChild____org_andengine_entity_IEntityMatcher__ = JNI_ENV()->GetMethodID(sHUDClass, "detachChild", "(Lorg/andengine/entity/IEntityMatcher;)Lorg/andengine/entity/IEntity;");
	sMethod__DetachChild____org_andengine_entity_IEntity__ = JNI_ENV()->GetMethodID(sHUDClass, "detachChild", "(Lorg/andengine/entity/IEntity;)Z");
	sMethod__SwapChildren__II = JNI_ENV()->GetMethodID(sHUDClass, "swapChildren", "(II)Z");
	sMethod__SwapChildren____org_andengine_entity_IEntity____org_andengine_entity_IEntity__ = JNI_ENV()->GetMethodID(sHUDClass, "swapChildren", "(Lorg/andengine/entity/IEntity;Lorg/andengine/entity/IEntity;)Z");
}

	JNIEXPORT jboolean JNICALL Java_org_andengine_extension_scripting_engine_camera_hud_HUDProxy_nativeOnAttached(JNIEnv* pJNIEnv, jobject pJObject, jlong pAddress) {HUD* hUD = (HUD*)pAddress;
return hUD->onAttached();}
	JNIEXPORT jboolean JNICALL Java_org_andengine_extension_scripting_engine_camera_hud_HUDProxy_nativeOnDetached(JNIEnv* pJNIEnv, jobject pJObject, jlong pAddress) {HUD* hUD = (HUD*)pAddress;
return hUD->onDetached();}

HUD::HUD(jobject pHUDProxy) {
	this->mUnwrapped = pHUDProxy;
}
jobject HUD::unwrap() {
	return this->mUnwrapped;
}
HUD::HUD() {
	this->mUnwrapped = JNI_ENV()->NewObject(sHUDClass, sConstructor, (jlong)this);
}
jfloat HUD::getY() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetY);
}
jfloat HUD::getX() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetX);
}
void HUD::setColor(jfloat pRed, jfloat pGreen, jfloat pBlue) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetColor__FFF, pRed, pGreen, pBlue);
}
void HUD::setColor(Color* pColor) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetColor____org_andengine_util_color_Color__, pColor->unwrap());
}
void HUD::setColor(jfloat pRed, jfloat pGreen, jfloat pBlue, jfloat pAlpha) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetColor__FFFF, pRed, pGreen, pBlue, pAlpha);
}
void HUD::setScale(jfloat pScaleX, jfloat pScaleY) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetScale__FF, pScaleX, pScaleY);
}
void HUD::setScale(jfloat pScale) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetScale__F, pScale);
}
jfloat HUD::getRotation() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetRotation);
}
void HUD::setRotation(jfloat pRotation) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetRotation__F, pRotation);
}
void HUD::attachChild(IEntity* pEntity) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__AttachChild____org_andengine_entity_IEntity__, pEntity->unwrap());
}
jboolean HUD::attachChild(IEntity* pEntity, jint pIndex) {
	return JNI_ENV()->CallBooleanMethod(this->mUnwrapped, sMethod__AttachChild____org_andengine_entity_IEntity__I, pEntity->unwrap(), pIndex);
}
void HUD::setX(jfloat pX) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetX__F, pX);
}
void HUD::setY(jfloat pY) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetY__F, pY);
}
jfloat HUD::getScaleX() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetScaleX);
}
jfloat HUD::getScaleY() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetScaleY);
}
jfloat HUD::getSkewX() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetSkewX);
}
jfloat HUD::getSkewY() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetSkewY);
}
void HUD::setSkew(jfloat pSkewX, jfloat pSkewY) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetSkew__FF, pSkewX, pSkewY);
}
void HUD::setSkew(jfloat pSkew) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetSkew__F, pSkew);
}
jfloat HUD::getRed() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetRed);
}
jfloat HUD::getGreen() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetGreen);
}
jfloat HUD::getBlue() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetBlue);
}
jfloat HUD::getAlpha() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetAlpha);
}
Color* HUD::getColor() {
	return new Color(JNI_ENV()->CallObjectMethod(this->mUnwrapped, sMethod__GetColor));
}
void HUD::setRed(jfloat pRed) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetRed__F, pRed);
}
void HUD::setGreen(jfloat pGreen) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetGreen__F, pGreen);
}
void HUD::setBlue(jfloat pBlue) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetBlue__F, pBlue);
}
void HUD::setAlpha(jfloat pAlpha) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetAlpha__F, pAlpha);
}
jint HUD::getChildCount() {
	return JNI_ENV()->CallIntMethod(this->mUnwrapped, sMethod__GetChildCount);
}
IEntity* HUD::getChild(jint pIndex) {
	return new Entity(JNI_ENV()->CallObjectMethod(this->mUnwrapped, sMethod__GetChild__I, pIndex));
}
IEntity* HUD::getChild(IEntityMatcher* pEntityMatcher) {
	return new Entity(JNI_ENV()->CallObjectMethod(this->mUnwrapped, sMethod__GetChild____org_andengine_entity_IEntityMatcher__, pEntityMatcher->unwrap()));
}
jboolean HUD::detachSelf() {
	return JNI_ENV()->CallBooleanMethod(this->mUnwrapped, sMethod__DetachSelf);
}
IEntity* HUD::detachChild(IEntityMatcher* pEntityMatcher) {
	return new Entity(JNI_ENV()->CallObjectMethod(this->mUnwrapped, sMethod__DetachChild____org_andengine_entity_IEntityMatcher__, pEntityMatcher->unwrap()));
}
jboolean HUD::detachChild(IEntity* pEntity) {
	return JNI_ENV()->CallBooleanMethod(this->mUnwrapped, sMethod__DetachChild____org_andengine_entity_IEntity__, pEntity->unwrap());
}
jboolean HUD::onAttached() {
	return false;
}
jboolean HUD::swapChildren(jint pIndexA, jint pIndexB) {
	return JNI_ENV()->CallBooleanMethod(this->mUnwrapped, sMethod__SwapChildren__II, pIndexA, pIndexB);
}
jboolean HUD::swapChildren(IEntity* pEntityA, IEntity* pEntityB) {
	return JNI_ENV()->CallBooleanMethod(this->mUnwrapped, sMethod__SwapChildren____org_andengine_entity_IEntity____org_andengine_entity_IEntity__, pEntityA->unwrap(), pEntityB->unwrap());
}
jboolean HUD::onDetached() {
	return false;
}

