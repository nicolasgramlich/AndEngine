#include <cstdlib>
#include "src/org/andengine/entity/Entity.h"

static jclass sEntityClass;
static jmethodID sConstructor;
static jmethodID sConstructor__FF;
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

JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_entity_EntityProxy_nativeInitClass(JNIEnv* pJNIEnv, jclass pJClass) {
	sEntityClass = (jclass)JNI_ENV()->NewGlobalRef(pJClass);
	sConstructor = JNI_ENV()->GetMethodID(sEntityClass, "<init>", "(J)V");
	sConstructor__FF = JNI_ENV()->GetMethodID(sEntityClass, "<init>", "(JFF)V");
	sMethod__GetY = JNI_ENV()->GetMethodID(sEntityClass, "getY", "()F");
	sMethod__GetX = JNI_ENV()->GetMethodID(sEntityClass, "getX", "()F");
	sMethod__SetColor__FFF = JNI_ENV()->GetMethodID(sEntityClass, "setColor", "(FFF)V");
	sMethod__SetColor____org_andengine_util_color_Color__ = JNI_ENV()->GetMethodID(sEntityClass, "setColor", "(Lorg/andengine/util/color/Color;)V");
	sMethod__SetColor__FFFF = JNI_ENV()->GetMethodID(sEntityClass, "setColor", "(FFFF)V");
	sMethod__SetScale__FF = JNI_ENV()->GetMethodID(sEntityClass, "setScale", "(FF)V");
	sMethod__SetScale__F = JNI_ENV()->GetMethodID(sEntityClass, "setScale", "(F)V");
	sMethod__GetRotation = JNI_ENV()->GetMethodID(sEntityClass, "getRotation", "()F");
	sMethod__SetRotation__F = JNI_ENV()->GetMethodID(sEntityClass, "setRotation", "(F)V");
	sMethod__AttachChild____org_andengine_entity_IEntity__ = JNI_ENV()->GetMethodID(sEntityClass, "attachChild", "(Lorg/andengine/entity/IEntity;)V");
	sMethod__AttachChild____org_andengine_entity_IEntity__I = JNI_ENV()->GetMethodID(sEntityClass, "attachChild", "(Lorg/andengine/entity/IEntity;I)Z");
	sMethod__SetX__F = JNI_ENV()->GetMethodID(sEntityClass, "setX", "(F)V");
	sMethod__SetY__F = JNI_ENV()->GetMethodID(sEntityClass, "setY", "(F)V");
	sMethod__GetScaleX = JNI_ENV()->GetMethodID(sEntityClass, "getScaleX", "()F");
	sMethod__GetScaleY = JNI_ENV()->GetMethodID(sEntityClass, "getScaleY", "()F");
	sMethod__GetSkewX = JNI_ENV()->GetMethodID(sEntityClass, "getSkewX", "()F");
	sMethod__GetSkewY = JNI_ENV()->GetMethodID(sEntityClass, "getSkewY", "()F");
	sMethod__SetSkew__FF = JNI_ENV()->GetMethodID(sEntityClass, "setSkew", "(FF)V");
	sMethod__SetSkew__F = JNI_ENV()->GetMethodID(sEntityClass, "setSkew", "(F)V");
	sMethod__GetRed = JNI_ENV()->GetMethodID(sEntityClass, "getRed", "()F");
	sMethod__GetGreen = JNI_ENV()->GetMethodID(sEntityClass, "getGreen", "()F");
	sMethod__GetBlue = JNI_ENV()->GetMethodID(sEntityClass, "getBlue", "()F");
	sMethod__GetAlpha = JNI_ENV()->GetMethodID(sEntityClass, "getAlpha", "()F");
	sMethod__GetColor = JNI_ENV()->GetMethodID(sEntityClass, "getColor", "()Lorg/andengine/util/color/Color;");
	sMethod__SetRed__F = JNI_ENV()->GetMethodID(sEntityClass, "setRed", "(F)V");
	sMethod__SetGreen__F = JNI_ENV()->GetMethodID(sEntityClass, "setGreen", "(F)V");
	sMethod__SetBlue__F = JNI_ENV()->GetMethodID(sEntityClass, "setBlue", "(F)V");
	sMethod__SetAlpha__F = JNI_ENV()->GetMethodID(sEntityClass, "setAlpha", "(F)V");
	sMethod__GetChildCount = JNI_ENV()->GetMethodID(sEntityClass, "getChildCount", "()I");
	sMethod__GetChild__I = JNI_ENV()->GetMethodID(sEntityClass, "getChild", "(I)Lorg/andengine/entity/IEntity;");
	sMethod__GetChild____org_andengine_entity_IEntityMatcher__ = JNI_ENV()->GetMethodID(sEntityClass, "getChild", "(Lorg/andengine/entity/IEntityMatcher;)Lorg/andengine/entity/IEntity;");
	sMethod__DetachSelf = JNI_ENV()->GetMethodID(sEntityClass, "detachSelf", "()Z");
	sMethod__DetachChild____org_andengine_entity_IEntityMatcher__ = JNI_ENV()->GetMethodID(sEntityClass, "detachChild", "(Lorg/andengine/entity/IEntityMatcher;)Lorg/andengine/entity/IEntity;");
	sMethod__DetachChild____org_andengine_entity_IEntity__ = JNI_ENV()->GetMethodID(sEntityClass, "detachChild", "(Lorg/andengine/entity/IEntity;)Z");
	sMethod__SwapChildren__II = JNI_ENV()->GetMethodID(sEntityClass, "swapChildren", "(II)Z");
	sMethod__SwapChildren____org_andengine_entity_IEntity____org_andengine_entity_IEntity__ = JNI_ENV()->GetMethodID(sEntityClass, "swapChildren", "(Lorg/andengine/entity/IEntity;Lorg/andengine/entity/IEntity;)Z");
}

	JNIEXPORT jboolean JNICALL Java_org_andengine_extension_scripting_entity_EntityProxy_nativeOnAttached(JNIEnv* pJNIEnv, jobject pJObject, jlong pAddress) {Entity* entity = (Entity*)pAddress;
return entity->onAttached();}
	JNIEXPORT jboolean JNICALL Java_org_andengine_extension_scripting_entity_EntityProxy_nativeOnDetached(JNIEnv* pJNIEnv, jobject pJObject, jlong pAddress) {Entity* entity = (Entity*)pAddress;
return entity->onDetached();}

Entity::Entity(jobject pEntityProxy) {
	this->mUnwrapped = pEntityProxy;
}
jobject Entity::unwrap() {
	return this->mUnwrapped;
}
Entity::Entity() {
	this->mUnwrapped = JNI_ENV()->NewObject(sEntityClass, sConstructor, (jlong)this);
}
Entity::Entity(jfloat pX, jfloat pY) {
	this->mUnwrapped = JNI_ENV()->NewObject(sEntityClass, sConstructor__FF, (jlong)this, pX, pY);
}
jfloat Entity::getY() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetY);
}
jfloat Entity::getX() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetX);
}
void Entity::setColor(jfloat pRed, jfloat pGreen, jfloat pBlue) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetColor__FFF, pRed, pGreen, pBlue);
}
void Entity::setColor(Color* pColor) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetColor____org_andengine_util_color_Color__, pColor->unwrap());
}
void Entity::setColor(jfloat pRed, jfloat pGreen, jfloat pBlue, jfloat pAlpha) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetColor__FFFF, pRed, pGreen, pBlue, pAlpha);
}
void Entity::setScale(jfloat pScaleX, jfloat pScaleY) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetScale__FF, pScaleX, pScaleY);
}
void Entity::setScale(jfloat pScale) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetScale__F, pScale);
}
jfloat Entity::getRotation() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetRotation);
}
void Entity::setRotation(jfloat pRotation) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetRotation__F, pRotation);
}
void Entity::attachChild(IEntity* pEntity) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__AttachChild____org_andengine_entity_IEntity__, pEntity->unwrap());
}
jboolean Entity::attachChild(IEntity* pEntity, jint pIndex) {
	return JNI_ENV()->CallBooleanMethod(this->mUnwrapped, sMethod__AttachChild____org_andengine_entity_IEntity__I, pEntity->unwrap(), pIndex);
}
void Entity::setX(jfloat pX) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetX__F, pX);
}
void Entity::setY(jfloat pY) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetY__F, pY);
}
jfloat Entity::getScaleX() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetScaleX);
}
jfloat Entity::getScaleY() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetScaleY);
}
jfloat Entity::getSkewX() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetSkewX);
}
jfloat Entity::getSkewY() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetSkewY);
}
void Entity::setSkew(jfloat pSkewX, jfloat pSkewY) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetSkew__FF, pSkewX, pSkewY);
}
void Entity::setSkew(jfloat pSkew) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetSkew__F, pSkew);
}
jfloat Entity::getRed() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetRed);
}
jfloat Entity::getGreen() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetGreen);
}
jfloat Entity::getBlue() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetBlue);
}
jfloat Entity::getAlpha() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetAlpha);
}
Color* Entity::getColor() {
	return new Color(JNI_ENV()->CallObjectMethod(this->mUnwrapped, sMethod__GetColor));
}
void Entity::setRed(jfloat pRed) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetRed__F, pRed);
}
void Entity::setGreen(jfloat pGreen) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetGreen__F, pGreen);
}
void Entity::setBlue(jfloat pBlue) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetBlue__F, pBlue);
}
void Entity::setAlpha(jfloat pAlpha) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetAlpha__F, pAlpha);
}
jint Entity::getChildCount() {
	return JNI_ENV()->CallIntMethod(this->mUnwrapped, sMethod__GetChildCount);
}
IEntity* Entity::getChild(jint pIndex) {
	return new Entity(JNI_ENV()->CallObjectMethod(this->mUnwrapped, sMethod__GetChild__I, pIndex));
}
IEntity* Entity::getChild(IEntityMatcher* pEntityMatcher) {
	return new Entity(JNI_ENV()->CallObjectMethod(this->mUnwrapped, sMethod__GetChild____org_andengine_entity_IEntityMatcher__, pEntityMatcher->unwrap()));
}
jboolean Entity::detachSelf() {
	return JNI_ENV()->CallBooleanMethod(this->mUnwrapped, sMethod__DetachSelf);
}
IEntity* Entity::detachChild(IEntityMatcher* pEntityMatcher) {
	return new Entity(JNI_ENV()->CallObjectMethod(this->mUnwrapped, sMethod__DetachChild____org_andengine_entity_IEntityMatcher__, pEntityMatcher->unwrap()));
}
jboolean Entity::detachChild(IEntity* pEntity) {
	return JNI_ENV()->CallBooleanMethod(this->mUnwrapped, sMethod__DetachChild____org_andengine_entity_IEntity__, pEntity->unwrap());
}
jboolean Entity::onAttached() {
	return false;
}
jboolean Entity::swapChildren(jint pIndexA, jint pIndexB) {
	return JNI_ENV()->CallBooleanMethod(this->mUnwrapped, sMethod__SwapChildren__II, pIndexA, pIndexB);
}
jboolean Entity::swapChildren(IEntity* pEntityA, IEntity* pEntityB) {
	return JNI_ENV()->CallBooleanMethod(this->mUnwrapped, sMethod__SwapChildren____org_andengine_entity_IEntity____org_andengine_entity_IEntity__, pEntityA->unwrap(), pEntityB->unwrap());
}
jboolean Entity::onDetached() {
	return false;
}

