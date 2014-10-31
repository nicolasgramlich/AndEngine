#include <cstdlib>
#include "src/org/andengine/entity/scene/CameraScene.h"

static jclass sCameraSceneClass;
static jmethodID sConstructor____org_andengine_engine_camera_Camera__;
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

JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_entity_scene_CameraSceneProxy_nativeInitClass(JNIEnv* pJNIEnv, jclass pJClass) {
	sCameraSceneClass = (jclass)JNI_ENV()->NewGlobalRef(pJClass);
	sConstructor____org_andengine_engine_camera_Camera__ = JNI_ENV()->GetMethodID(sCameraSceneClass, "<init>", "(JLorg/andengine/engine/camera/Camera;)V");
	sConstructor = JNI_ENV()->GetMethodID(sCameraSceneClass, "<init>", "(J)V");
	sMethod__GetY = JNI_ENV()->GetMethodID(sCameraSceneClass, "getY", "()F");
	sMethod__GetX = JNI_ENV()->GetMethodID(sCameraSceneClass, "getX", "()F");
	sMethod__SetColor__FFF = JNI_ENV()->GetMethodID(sCameraSceneClass, "setColor", "(FFF)V");
	sMethod__SetColor____org_andengine_util_color_Color__ = JNI_ENV()->GetMethodID(sCameraSceneClass, "setColor", "(Lorg/andengine/util/color/Color;)V");
	sMethod__SetColor__FFFF = JNI_ENV()->GetMethodID(sCameraSceneClass, "setColor", "(FFFF)V");
	sMethod__SetScale__FF = JNI_ENV()->GetMethodID(sCameraSceneClass, "setScale", "(FF)V");
	sMethod__SetScale__F = JNI_ENV()->GetMethodID(sCameraSceneClass, "setScale", "(F)V");
	sMethod__GetRotation = JNI_ENV()->GetMethodID(sCameraSceneClass, "getRotation", "()F");
	sMethod__SetRotation__F = JNI_ENV()->GetMethodID(sCameraSceneClass, "setRotation", "(F)V");
	sMethod__AttachChild____org_andengine_entity_IEntity__ = JNI_ENV()->GetMethodID(sCameraSceneClass, "attachChild", "(Lorg/andengine/entity/IEntity;)V");
	sMethod__AttachChild____org_andengine_entity_IEntity__I = JNI_ENV()->GetMethodID(sCameraSceneClass, "attachChild", "(Lorg/andengine/entity/IEntity;I)Z");
	sMethod__SetX__F = JNI_ENV()->GetMethodID(sCameraSceneClass, "setX", "(F)V");
	sMethod__SetY__F = JNI_ENV()->GetMethodID(sCameraSceneClass, "setY", "(F)V");
	sMethod__GetScaleX = JNI_ENV()->GetMethodID(sCameraSceneClass, "getScaleX", "()F");
	sMethod__GetScaleY = JNI_ENV()->GetMethodID(sCameraSceneClass, "getScaleY", "()F");
	sMethod__GetSkewX = JNI_ENV()->GetMethodID(sCameraSceneClass, "getSkewX", "()F");
	sMethod__GetSkewY = JNI_ENV()->GetMethodID(sCameraSceneClass, "getSkewY", "()F");
	sMethod__SetSkew__FF = JNI_ENV()->GetMethodID(sCameraSceneClass, "setSkew", "(FF)V");
	sMethod__SetSkew__F = JNI_ENV()->GetMethodID(sCameraSceneClass, "setSkew", "(F)V");
	sMethod__GetRed = JNI_ENV()->GetMethodID(sCameraSceneClass, "getRed", "()F");
	sMethod__GetGreen = JNI_ENV()->GetMethodID(sCameraSceneClass, "getGreen", "()F");
	sMethod__GetBlue = JNI_ENV()->GetMethodID(sCameraSceneClass, "getBlue", "()F");
	sMethod__GetAlpha = JNI_ENV()->GetMethodID(sCameraSceneClass, "getAlpha", "()F");
	sMethod__GetColor = JNI_ENV()->GetMethodID(sCameraSceneClass, "getColor", "()Lorg/andengine/util/color/Color;");
	sMethod__SetRed__F = JNI_ENV()->GetMethodID(sCameraSceneClass, "setRed", "(F)V");
	sMethod__SetGreen__F = JNI_ENV()->GetMethodID(sCameraSceneClass, "setGreen", "(F)V");
	sMethod__SetBlue__F = JNI_ENV()->GetMethodID(sCameraSceneClass, "setBlue", "(F)V");
	sMethod__SetAlpha__F = JNI_ENV()->GetMethodID(sCameraSceneClass, "setAlpha", "(F)V");
	sMethod__GetChildCount = JNI_ENV()->GetMethodID(sCameraSceneClass, "getChildCount", "()I");
	sMethod__GetChild__I = JNI_ENV()->GetMethodID(sCameraSceneClass, "getChild", "(I)Lorg/andengine/entity/IEntity;");
	sMethod__GetChild____org_andengine_entity_IEntityMatcher__ = JNI_ENV()->GetMethodID(sCameraSceneClass, "getChild", "(Lorg/andengine/entity/IEntityMatcher;)Lorg/andengine/entity/IEntity;");
	sMethod__DetachSelf = JNI_ENV()->GetMethodID(sCameraSceneClass, "detachSelf", "()Z");
	sMethod__DetachChild____org_andengine_entity_IEntityMatcher__ = JNI_ENV()->GetMethodID(sCameraSceneClass, "detachChild", "(Lorg/andengine/entity/IEntityMatcher;)Lorg/andengine/entity/IEntity;");
	sMethod__DetachChild____org_andengine_entity_IEntity__ = JNI_ENV()->GetMethodID(sCameraSceneClass, "detachChild", "(Lorg/andengine/entity/IEntity;)Z");
	sMethod__SwapChildren__II = JNI_ENV()->GetMethodID(sCameraSceneClass, "swapChildren", "(II)Z");
	sMethod__SwapChildren____org_andengine_entity_IEntity____org_andengine_entity_IEntity__ = JNI_ENV()->GetMethodID(sCameraSceneClass, "swapChildren", "(Lorg/andengine/entity/IEntity;Lorg/andengine/entity/IEntity;)Z");
}

	JNIEXPORT jboolean JNICALL Java_org_andengine_extension_scripting_entity_scene_CameraSceneProxy_nativeOnAttached(JNIEnv* pJNIEnv, jobject pJObject, jlong pAddress) {CameraScene* cameraScene = (CameraScene*)pAddress;
return cameraScene->onAttached();}
	JNIEXPORT jboolean JNICALL Java_org_andengine_extension_scripting_entity_scene_CameraSceneProxy_nativeOnDetached(JNIEnv* pJNIEnv, jobject pJObject, jlong pAddress) {CameraScene* cameraScene = (CameraScene*)pAddress;
return cameraScene->onDetached();}

CameraScene::CameraScene(jobject pCameraSceneProxy) {
	this->mUnwrapped = pCameraSceneProxy;
}
jobject CameraScene::unwrap() {
	return this->mUnwrapped;
}
CameraScene::CameraScene(Camera* pCamera) {
	this->mUnwrapped = JNI_ENV()->NewObject(sCameraSceneClass, sConstructor____org_andengine_engine_camera_Camera__, (jlong)this, pCamera->unwrap());
}
CameraScene::CameraScene() {
	this->mUnwrapped = JNI_ENV()->NewObject(sCameraSceneClass, sConstructor, (jlong)this);
}
jfloat CameraScene::getY() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetY);
}
jfloat CameraScene::getX() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetX);
}
void CameraScene::setColor(jfloat pRed, jfloat pGreen, jfloat pBlue) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetColor__FFF, pRed, pGreen, pBlue);
}
void CameraScene::setColor(Color* pColor) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetColor____org_andengine_util_color_Color__, pColor->unwrap());
}
void CameraScene::setColor(jfloat pRed, jfloat pGreen, jfloat pBlue, jfloat pAlpha) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetColor__FFFF, pRed, pGreen, pBlue, pAlpha);
}
void CameraScene::setScale(jfloat pScaleX, jfloat pScaleY) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetScale__FF, pScaleX, pScaleY);
}
void CameraScene::setScale(jfloat pScale) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetScale__F, pScale);
}
jfloat CameraScene::getRotation() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetRotation);
}
void CameraScene::setRotation(jfloat pRotation) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetRotation__F, pRotation);
}
void CameraScene::attachChild(IEntity* pEntity) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__AttachChild____org_andengine_entity_IEntity__, pEntity->unwrap());
}
jboolean CameraScene::attachChild(IEntity* pEntity, jint pIndex) {
	return JNI_ENV()->CallBooleanMethod(this->mUnwrapped, sMethod__AttachChild____org_andengine_entity_IEntity__I, pEntity->unwrap(), pIndex);
}
void CameraScene::setX(jfloat pX) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetX__F, pX);
}
void CameraScene::setY(jfloat pY) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetY__F, pY);
}
jfloat CameraScene::getScaleX() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetScaleX);
}
jfloat CameraScene::getScaleY() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetScaleY);
}
jfloat CameraScene::getSkewX() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetSkewX);
}
jfloat CameraScene::getSkewY() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetSkewY);
}
void CameraScene::setSkew(jfloat pSkewX, jfloat pSkewY) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetSkew__FF, pSkewX, pSkewY);
}
void CameraScene::setSkew(jfloat pSkew) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetSkew__F, pSkew);
}
jfloat CameraScene::getRed() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetRed);
}
jfloat CameraScene::getGreen() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetGreen);
}
jfloat CameraScene::getBlue() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetBlue);
}
jfloat CameraScene::getAlpha() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetAlpha);
}
Color* CameraScene::getColor() {
	return new Color(JNI_ENV()->CallObjectMethod(this->mUnwrapped, sMethod__GetColor));
}
void CameraScene::setRed(jfloat pRed) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetRed__F, pRed);
}
void CameraScene::setGreen(jfloat pGreen) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetGreen__F, pGreen);
}
void CameraScene::setBlue(jfloat pBlue) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetBlue__F, pBlue);
}
void CameraScene::setAlpha(jfloat pAlpha) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetAlpha__F, pAlpha);
}
jint CameraScene::getChildCount() {
	return JNI_ENV()->CallIntMethod(this->mUnwrapped, sMethod__GetChildCount);
}
IEntity* CameraScene::getChild(jint pIndex) {
	return new Entity(JNI_ENV()->CallObjectMethod(this->mUnwrapped, sMethod__GetChild__I, pIndex));
}
IEntity* CameraScene::getChild(IEntityMatcher* pEntityMatcher) {
	return new Entity(JNI_ENV()->CallObjectMethod(this->mUnwrapped, sMethod__GetChild____org_andengine_entity_IEntityMatcher__, pEntityMatcher->unwrap()));
}
jboolean CameraScene::detachSelf() {
	return JNI_ENV()->CallBooleanMethod(this->mUnwrapped, sMethod__DetachSelf);
}
IEntity* CameraScene::detachChild(IEntityMatcher* pEntityMatcher) {
	return new Entity(JNI_ENV()->CallObjectMethod(this->mUnwrapped, sMethod__DetachChild____org_andengine_entity_IEntityMatcher__, pEntityMatcher->unwrap()));
}
jboolean CameraScene::detachChild(IEntity* pEntity) {
	return JNI_ENV()->CallBooleanMethod(this->mUnwrapped, sMethod__DetachChild____org_andengine_entity_IEntity__, pEntity->unwrap());
}
jboolean CameraScene::onAttached() {
	return false;
}
jboolean CameraScene::swapChildren(jint pIndexA, jint pIndexB) {
	return JNI_ENV()->CallBooleanMethod(this->mUnwrapped, sMethod__SwapChildren__II, pIndexA, pIndexB);
}
jboolean CameraScene::swapChildren(IEntity* pEntityA, IEntity* pEntityB) {
	return JNI_ENV()->CallBooleanMethod(this->mUnwrapped, sMethod__SwapChildren____org_andengine_entity_IEntity____org_andengine_entity_IEntity__, pEntityA->unwrap(), pEntityB->unwrap());
}
jboolean CameraScene::onDetached() {
	return false;
}

