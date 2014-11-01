#include <cstdlib>
#include "src/org/andengine/entity/primitive/Rectangle.h"

static jclass sRectangleClass;
static jmethodID sConstructor__FFFF__org_andengine_opengl_vbo_VertexBufferObjectManager__;
static jmethodID sConstructor__FFFF__org_andengine_opengl_vbo_VertexBufferObjectManager____org_andengine_opengl_vbo_DrawType__;
static jmethodID sConstructor__FFFF__org_andengine_entity_primitive_vbo_IRectangleVertexBufferObject__;
static jmethodID sMethod__GetWidth;
static jmethodID sMethod__GetHeight;
static jmethodID sMethod__GetVertexBufferObjectManager;
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

JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_entity_primitive_RectangleProxy_nativeInitClass(JNIEnv* pJNIEnv, jclass pJClass) {
	sRectangleClass = (jclass)JNI_ENV()->NewGlobalRef(pJClass);
	sConstructor__FFFF__org_andengine_opengl_vbo_VertexBufferObjectManager__ = JNI_ENV()->GetMethodID(sRectangleClass, "<init>", "(JFFFFLorg/andengine/opengl/vbo/VertexBufferObjectManager;)V");
	sConstructor__FFFF__org_andengine_opengl_vbo_VertexBufferObjectManager____org_andengine_opengl_vbo_DrawType__ = JNI_ENV()->GetMethodID(sRectangleClass, "<init>", "(JFFFFLorg/andengine/opengl/vbo/VertexBufferObjectManager;Lorg/andengine/opengl/vbo/DrawType;)V");
	sConstructor__FFFF__org_andengine_entity_primitive_vbo_IRectangleVertexBufferObject__ = JNI_ENV()->GetMethodID(sRectangleClass, "<init>", "(JFFFFLorg/andengine/entity/primitive/vbo/IRectangleVertexBufferObject;)V");
	sMethod__GetWidth = JNI_ENV()->GetMethodID(sRectangleClass, "getWidth", "()F");
	sMethod__GetHeight = JNI_ENV()->GetMethodID(sRectangleClass, "getHeight", "()F");
	sMethod__GetVertexBufferObjectManager = JNI_ENV()->GetMethodID(sRectangleClass, "getVertexBufferObjectManager", "()Lorg/andengine/opengl/vbo/VertexBufferObjectManager;");
	sMethod__GetY = JNI_ENV()->GetMethodID(sRectangleClass, "getY", "()F");
	sMethod__GetX = JNI_ENV()->GetMethodID(sRectangleClass, "getX", "()F");
	sMethod__SetColor__FFF = JNI_ENV()->GetMethodID(sRectangleClass, "setColor", "(FFF)V");
	sMethod__SetColor____org_andengine_util_color_Color__ = JNI_ENV()->GetMethodID(sRectangleClass, "setColor", "(Lorg/andengine/util/color/Color;)V");
	sMethod__SetColor__FFFF = JNI_ENV()->GetMethodID(sRectangleClass, "setColor", "(FFFF)V");
	sMethod__SetScale__FF = JNI_ENV()->GetMethodID(sRectangleClass, "setScale", "(FF)V");
	sMethod__SetScale__F = JNI_ENV()->GetMethodID(sRectangleClass, "setScale", "(F)V");
	sMethod__GetRotation = JNI_ENV()->GetMethodID(sRectangleClass, "getRotation", "()F");
	sMethod__SetRotation__F = JNI_ENV()->GetMethodID(sRectangleClass, "setRotation", "(F)V");
	sMethod__AttachChild____org_andengine_entity_IEntity__ = JNI_ENV()->GetMethodID(sRectangleClass, "attachChild", "(Lorg/andengine/entity/IEntity;)V");
	sMethod__AttachChild____org_andengine_entity_IEntity__I = JNI_ENV()->GetMethodID(sRectangleClass, "attachChild", "(Lorg/andengine/entity/IEntity;I)Z");
	sMethod__SetX__F = JNI_ENV()->GetMethodID(sRectangleClass, "setX", "(F)V");
	sMethod__SetY__F = JNI_ENV()->GetMethodID(sRectangleClass, "setY", "(F)V");
	sMethod__GetScaleX = JNI_ENV()->GetMethodID(sRectangleClass, "getScaleX", "()F");
	sMethod__GetScaleY = JNI_ENV()->GetMethodID(sRectangleClass, "getScaleY", "()F");
	sMethod__GetSkewX = JNI_ENV()->GetMethodID(sRectangleClass, "getSkewX", "()F");
	sMethod__GetSkewY = JNI_ENV()->GetMethodID(sRectangleClass, "getSkewY", "()F");
	sMethod__SetSkew__FF = JNI_ENV()->GetMethodID(sRectangleClass, "setSkew", "(FF)V");
	sMethod__SetSkew__F = JNI_ENV()->GetMethodID(sRectangleClass, "setSkew", "(F)V");
	sMethod__GetRed = JNI_ENV()->GetMethodID(sRectangleClass, "getRed", "()F");
	sMethod__GetGreen = JNI_ENV()->GetMethodID(sRectangleClass, "getGreen", "()F");
	sMethod__GetBlue = JNI_ENV()->GetMethodID(sRectangleClass, "getBlue", "()F");
	sMethod__GetAlpha = JNI_ENV()->GetMethodID(sRectangleClass, "getAlpha", "()F");
	sMethod__GetColor = JNI_ENV()->GetMethodID(sRectangleClass, "getColor", "()Lorg/andengine/util/color/Color;");
	sMethod__SetRed__F = JNI_ENV()->GetMethodID(sRectangleClass, "setRed", "(F)V");
	sMethod__SetGreen__F = JNI_ENV()->GetMethodID(sRectangleClass, "setGreen", "(F)V");
	sMethod__SetBlue__F = JNI_ENV()->GetMethodID(sRectangleClass, "setBlue", "(F)V");
	sMethod__SetAlpha__F = JNI_ENV()->GetMethodID(sRectangleClass, "setAlpha", "(F)V");
	sMethod__GetChildCount = JNI_ENV()->GetMethodID(sRectangleClass, "getChildCount", "()I");
	sMethod__GetChild__I = JNI_ENV()->GetMethodID(sRectangleClass, "getChild", "(I)Lorg/andengine/entity/IEntity;");
	sMethod__GetChild____org_andengine_entity_IEntityMatcher__ = JNI_ENV()->GetMethodID(sRectangleClass, "getChild", "(Lorg/andengine/entity/IEntityMatcher;)Lorg/andengine/entity/IEntity;");
	sMethod__DetachSelf = JNI_ENV()->GetMethodID(sRectangleClass, "detachSelf", "()Z");
	sMethod__DetachChild____org_andengine_entity_IEntityMatcher__ = JNI_ENV()->GetMethodID(sRectangleClass, "detachChild", "(Lorg/andengine/entity/IEntityMatcher;)Lorg/andengine/entity/IEntity;");
	sMethod__DetachChild____org_andengine_entity_IEntity__ = JNI_ENV()->GetMethodID(sRectangleClass, "detachChild", "(Lorg/andengine/entity/IEntity;)Z");
	sMethod__SwapChildren__II = JNI_ENV()->GetMethodID(sRectangleClass, "swapChildren", "(II)Z");
	sMethod__SwapChildren____org_andengine_entity_IEntity____org_andengine_entity_IEntity__ = JNI_ENV()->GetMethodID(sRectangleClass, "swapChildren", "(Lorg/andengine/entity/IEntity;Lorg/andengine/entity/IEntity;)Z");
}

	JNIEXPORT jboolean JNICALL Java_org_andengine_extension_scripting_entity_primitive_RectangleProxy_nativeOnAreaTouched(JNIEnv* pJNIEnv, jobject pJObject, jlong pAddress, jobject pSceneTouchEvent, jfloat pTouchAreaLocalX, jfloat pTouchAreaLocalY) {Rectangle* rectangle = (Rectangle*)pAddress;
	TouchEvent sceneTouchEvent(pSceneTouchEvent);
return rectangle->onAreaTouched(&sceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);}
	JNIEXPORT jboolean JNICALL Java_org_andengine_extension_scripting_entity_primitive_RectangleProxy_nativeOnAttached(JNIEnv* pJNIEnv, jobject pJObject, jlong pAddress) {Rectangle* rectangle = (Rectangle*)pAddress;
return rectangle->onAttached();}
	JNIEXPORT jboolean JNICALL Java_org_andengine_extension_scripting_entity_primitive_RectangleProxy_nativeOnDetached(JNIEnv* pJNIEnv, jobject pJObject, jlong pAddress) {Rectangle* rectangle = (Rectangle*)pAddress;
return rectangle->onDetached();}

Rectangle::Rectangle(jobject pRectangleProxy) {
	this->mUnwrapped = pRectangleProxy;
}
jobject Rectangle::unwrap() {
	return this->mUnwrapped;
}
Rectangle::Rectangle(jfloat pX, jfloat pY, jfloat pWidth, jfloat pHeight, VertexBufferObjectManager* pVertexBufferObjectManager) {
	this->mUnwrapped = JNI_ENV()->NewObject(sRectangleClass, sConstructor__FFFF__org_andengine_opengl_vbo_VertexBufferObjectManager__, (jlong)this, pX, pY, pWidth, pHeight, pVertexBufferObjectManager->unwrap());
}
Rectangle::Rectangle(jfloat pX, jfloat pY, jfloat pWidth, jfloat pHeight, VertexBufferObjectManager* pVertexBufferObjectManager, DrawType* pDrawType) {
	this->mUnwrapped = JNI_ENV()->NewObject(sRectangleClass, sConstructor__FFFF__org_andengine_opengl_vbo_VertexBufferObjectManager____org_andengine_opengl_vbo_DrawType__, (jlong)this, pX, pY, pWidth, pHeight, pVertexBufferObjectManager->unwrap(), pDrawType->unwrap());
}
Rectangle::Rectangle(jfloat pX, jfloat pY, jfloat pWidth, jfloat pHeight, IRectangleVertexBufferObject* pRectangleVertexBufferObject) {
	this->mUnwrapped = JNI_ENV()->NewObject(sRectangleClass, sConstructor__FFFF__org_andengine_entity_primitive_vbo_IRectangleVertexBufferObject__, (jlong)this, pX, pY, pWidth, pHeight, pRectangleVertexBufferObject->unwrap());
}
Rectangle::Rectangle() {

}
jfloat Rectangle::getWidth() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetWidth);
}
jfloat Rectangle::getHeight() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetHeight);
}
VertexBufferObjectManager* Rectangle::getVertexBufferObjectManager() {
	return new VertexBufferObjectManager(JNI_ENV()->CallObjectMethod(this->mUnwrapped, sMethod__GetVertexBufferObjectManager));
}
jboolean Rectangle::onAreaTouched(TouchEvent* pSceneTouchEvent, jfloat pTouchAreaLocalX, jfloat pTouchAreaLocalY) {
	return false;
}
jfloat Rectangle::getY() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetY);
}
jfloat Rectangle::getX() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetX);
}
void Rectangle::setColor(jfloat pRed, jfloat pGreen, jfloat pBlue) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetColor__FFF, pRed, pGreen, pBlue);
}
void Rectangle::setColor(Color* pColor) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetColor____org_andengine_util_color_Color__, pColor->unwrap());
}
void Rectangle::setColor(jfloat pRed, jfloat pGreen, jfloat pBlue, jfloat pAlpha) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetColor__FFFF, pRed, pGreen, pBlue, pAlpha);
}
void Rectangle::setScale(jfloat pScaleX, jfloat pScaleY) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetScale__FF, pScaleX, pScaleY);
}
void Rectangle::setScale(jfloat pScale) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetScale__F, pScale);
}
jfloat Rectangle::getRotation() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetRotation);
}
void Rectangle::setRotation(jfloat pRotation) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetRotation__F, pRotation);
}
void Rectangle::attachChild(IEntity* pEntity) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__AttachChild____org_andengine_entity_IEntity__, pEntity->unwrap());
}
jboolean Rectangle::attachChild(IEntity* pEntity, jint pIndex) {
	return JNI_ENV()->CallBooleanMethod(this->mUnwrapped, sMethod__AttachChild____org_andengine_entity_IEntity__I, pEntity->unwrap(), pIndex);
}
void Rectangle::setX(jfloat pX) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetX__F, pX);
}
void Rectangle::setY(jfloat pY) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetY__F, pY);
}
jfloat Rectangle::getScaleX() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetScaleX);
}
jfloat Rectangle::getScaleY() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetScaleY);
}
jfloat Rectangle::getSkewX() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetSkewX);
}
jfloat Rectangle::getSkewY() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetSkewY);
}
void Rectangle::setSkew(jfloat pSkewX, jfloat pSkewY) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetSkew__FF, pSkewX, pSkewY);
}
void Rectangle::setSkew(jfloat pSkew) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetSkew__F, pSkew);
}
jfloat Rectangle::getRed() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetRed);
}
jfloat Rectangle::getGreen() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetGreen);
}
jfloat Rectangle::getBlue() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetBlue);
}
jfloat Rectangle::getAlpha() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetAlpha);
}
Color* Rectangle::getColor() {
	return new Color(JNI_ENV()->CallObjectMethod(this->mUnwrapped, sMethod__GetColor));
}
void Rectangle::setRed(jfloat pRed) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetRed__F, pRed);
}
void Rectangle::setGreen(jfloat pGreen) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetGreen__F, pGreen);
}
void Rectangle::setBlue(jfloat pBlue) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetBlue__F, pBlue);
}
void Rectangle::setAlpha(jfloat pAlpha) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetAlpha__F, pAlpha);
}
jint Rectangle::getChildCount() {
	return JNI_ENV()->CallIntMethod(this->mUnwrapped, sMethod__GetChildCount);
}
IEntity* Rectangle::getChild(jint pIndex) {
	return new Entity(JNI_ENV()->CallObjectMethod(this->mUnwrapped, sMethod__GetChild__I, pIndex));
}
IEntity* Rectangle::getChild(IEntityMatcher* pEntityMatcher) {
	return new Entity(JNI_ENV()->CallObjectMethod(this->mUnwrapped, sMethod__GetChild____org_andengine_entity_IEntityMatcher__, pEntityMatcher->unwrap()));
}
jboolean Rectangle::detachSelf() {
	return JNI_ENV()->CallBooleanMethod(this->mUnwrapped, sMethod__DetachSelf);
}
IEntity* Rectangle::detachChild(IEntityMatcher* pEntityMatcher) {
	return new Entity(JNI_ENV()->CallObjectMethod(this->mUnwrapped, sMethod__DetachChild____org_andengine_entity_IEntityMatcher__, pEntityMatcher->unwrap()));
}
jboolean Rectangle::detachChild(IEntity* pEntity) {
	return JNI_ENV()->CallBooleanMethod(this->mUnwrapped, sMethod__DetachChild____org_andengine_entity_IEntity__, pEntity->unwrap());
}
jboolean Rectangle::onAttached() {
	return false;
}
jboolean Rectangle::swapChildren(jint pIndexA, jint pIndexB) {
	return JNI_ENV()->CallBooleanMethod(this->mUnwrapped, sMethod__SwapChildren__II, pIndexA, pIndexB);
}
jboolean Rectangle::swapChildren(IEntity* pEntityA, IEntity* pEntityB) {
	return JNI_ENV()->CallBooleanMethod(this->mUnwrapped, sMethod__SwapChildren____org_andengine_entity_IEntity____org_andengine_entity_IEntity__, pEntityA->unwrap(), pEntityB->unwrap());
}
jboolean Rectangle::onDetached() {
	return false;
}

