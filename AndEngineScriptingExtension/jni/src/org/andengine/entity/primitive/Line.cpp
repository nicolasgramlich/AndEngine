#include <cstdlib>
#include "src/org/andengine/entity/primitive/Line.h"

static jclass sLineClass;
static jmethodID sConstructor__FFFF__org_andengine_opengl_vbo_VertexBufferObjectManager__;
static jmethodID sConstructor__FFFF__org_andengine_opengl_vbo_VertexBufferObjectManager____org_andengine_opengl_vbo_DrawType__;
static jmethodID sConstructor__FFFFF__org_andengine_opengl_vbo_VertexBufferObjectManager__;
static jmethodID sConstructor__FFFFF__org_andengine_opengl_vbo_VertexBufferObjectManager____org_andengine_opengl_vbo_DrawType__;
static jmethodID sConstructor__FFFFF__org_andengine_entity_primitive_vbo_I__ineVertexBufferObject__;
static jmethodID sMethod__GetY;
static jmethodID sMethod__GetX;
static jmethodID sMethod__SetX__F;
static jmethodID sMethod__SetY__F;
static jmethodID sMethod__GetVertexBufferObjectManager;
static jmethodID sMethod__SetColor__FFF;
static jmethodID sMethod__SetColor____org_andengine_util_color_Color__;
static jmethodID sMethod__SetColor__FFFF;
static jmethodID sMethod__SetScale__FF;
static jmethodID sMethod__SetScale__F;
static jmethodID sMethod__GetRotation;
static jmethodID sMethod__SetRotation__F;
static jmethodID sMethod__AttachChild____org_andengine_entity_IEntity__;
static jmethodID sMethod__AttachChild____org_andengine_entity_IEntity__I;
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

JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_entity_primitive_LineProxy_nativeInitClass(JNIEnv* pJNIEnv, jclass pJClass) {
	sLineClass = (jclass)JNI_ENV()->NewGlobalRef(pJClass);
	sConstructor__FFFF__org_andengine_opengl_vbo_VertexBufferObjectManager__ = JNI_ENV()->GetMethodID(sLineClass, "<init>", "(JFFFFLorg/andengine/opengl/vbo/VertexBufferObjectManager;)V");
	sConstructor__FFFF__org_andengine_opengl_vbo_VertexBufferObjectManager____org_andengine_opengl_vbo_DrawType__ = JNI_ENV()->GetMethodID(sLineClass, "<init>", "(JFFFFLorg/andengine/opengl/vbo/VertexBufferObjectManager;Lorg/andengine/opengl/vbo/DrawType;)V");
	sConstructor__FFFFF__org_andengine_opengl_vbo_VertexBufferObjectManager__ = JNI_ENV()->GetMethodID(sLineClass, "<init>", "(JFFFFFLorg/andengine/opengl/vbo/VertexBufferObjectManager;)V");
	sConstructor__FFFFF__org_andengine_opengl_vbo_VertexBufferObjectManager____org_andengine_opengl_vbo_DrawType__ = JNI_ENV()->GetMethodID(sLineClass, "<init>", "(JFFFFFLorg/andengine/opengl/vbo/VertexBufferObjectManager;Lorg/andengine/opengl/vbo/DrawType;)V");
	sConstructor__FFFFF__org_andengine_entity_primitive_vbo_I__ineVertexBufferObject__ = JNI_ENV()->GetMethodID(sLineClass, "<init>", "(JFFFFFLorg/andengine/entity/primitive/vbo/ILineVertexBufferObject;)V");
	sMethod__GetY = JNI_ENV()->GetMethodID(sLineClass, "getY", "()F");
	sMethod__GetX = JNI_ENV()->GetMethodID(sLineClass, "getX", "()F");
	sMethod__SetX__F = JNI_ENV()->GetMethodID(sLineClass, "setX", "(F)V");
	sMethod__SetY__F = JNI_ENV()->GetMethodID(sLineClass, "setY", "(F)V");
	sMethod__GetVertexBufferObjectManager = JNI_ENV()->GetMethodID(sLineClass, "getVertexBufferObjectManager", "()Lorg/andengine/opengl/vbo/VertexBufferObjectManager;");
	sMethod__SetColor__FFF = JNI_ENV()->GetMethodID(sLineClass, "setColor", "(FFF)V");
	sMethod__SetColor____org_andengine_util_color_Color__ = JNI_ENV()->GetMethodID(sLineClass, "setColor", "(Lorg/andengine/util/color/Color;)V");
	sMethod__SetColor__FFFF = JNI_ENV()->GetMethodID(sLineClass, "setColor", "(FFFF)V");
	sMethod__SetScale__FF = JNI_ENV()->GetMethodID(sLineClass, "setScale", "(FF)V");
	sMethod__SetScale__F = JNI_ENV()->GetMethodID(sLineClass, "setScale", "(F)V");
	sMethod__GetRotation = JNI_ENV()->GetMethodID(sLineClass, "getRotation", "()F");
	sMethod__SetRotation__F = JNI_ENV()->GetMethodID(sLineClass, "setRotation", "(F)V");
	sMethod__AttachChild____org_andengine_entity_IEntity__ = JNI_ENV()->GetMethodID(sLineClass, "attachChild", "(Lorg/andengine/entity/IEntity;)V");
	sMethod__AttachChild____org_andengine_entity_IEntity__I = JNI_ENV()->GetMethodID(sLineClass, "attachChild", "(Lorg/andengine/entity/IEntity;I)Z");
	sMethod__GetScaleX = JNI_ENV()->GetMethodID(sLineClass, "getScaleX", "()F");
	sMethod__GetScaleY = JNI_ENV()->GetMethodID(sLineClass, "getScaleY", "()F");
	sMethod__GetSkewX = JNI_ENV()->GetMethodID(sLineClass, "getSkewX", "()F");
	sMethod__GetSkewY = JNI_ENV()->GetMethodID(sLineClass, "getSkewY", "()F");
	sMethod__SetSkew__FF = JNI_ENV()->GetMethodID(sLineClass, "setSkew", "(FF)V");
	sMethod__SetSkew__F = JNI_ENV()->GetMethodID(sLineClass, "setSkew", "(F)V");
	sMethod__GetRed = JNI_ENV()->GetMethodID(sLineClass, "getRed", "()F");
	sMethod__GetGreen = JNI_ENV()->GetMethodID(sLineClass, "getGreen", "()F");
	sMethod__GetBlue = JNI_ENV()->GetMethodID(sLineClass, "getBlue", "()F");
	sMethod__GetAlpha = JNI_ENV()->GetMethodID(sLineClass, "getAlpha", "()F");
	sMethod__GetColor = JNI_ENV()->GetMethodID(sLineClass, "getColor", "()Lorg/andengine/util/color/Color;");
	sMethod__SetRed__F = JNI_ENV()->GetMethodID(sLineClass, "setRed", "(F)V");
	sMethod__SetGreen__F = JNI_ENV()->GetMethodID(sLineClass, "setGreen", "(F)V");
	sMethod__SetBlue__F = JNI_ENV()->GetMethodID(sLineClass, "setBlue", "(F)V");
	sMethod__SetAlpha__F = JNI_ENV()->GetMethodID(sLineClass, "setAlpha", "(F)V");
	sMethod__GetChildCount = JNI_ENV()->GetMethodID(sLineClass, "getChildCount", "()I");
	sMethod__GetChild__I = JNI_ENV()->GetMethodID(sLineClass, "getChild", "(I)Lorg/andengine/entity/IEntity;");
	sMethod__GetChild____org_andengine_entity_IEntityMatcher__ = JNI_ENV()->GetMethodID(sLineClass, "getChild", "(Lorg/andengine/entity/IEntityMatcher;)Lorg/andengine/entity/IEntity;");
	sMethod__DetachSelf = JNI_ENV()->GetMethodID(sLineClass, "detachSelf", "()Z");
	sMethod__DetachChild____org_andengine_entity_IEntityMatcher__ = JNI_ENV()->GetMethodID(sLineClass, "detachChild", "(Lorg/andengine/entity/IEntityMatcher;)Lorg/andengine/entity/IEntity;");
	sMethod__DetachChild____org_andengine_entity_IEntity__ = JNI_ENV()->GetMethodID(sLineClass, "detachChild", "(Lorg/andengine/entity/IEntity;)Z");
	sMethod__SwapChildren__II = JNI_ENV()->GetMethodID(sLineClass, "swapChildren", "(II)Z");
	sMethod__SwapChildren____org_andengine_entity_IEntity____org_andengine_entity_IEntity__ = JNI_ENV()->GetMethodID(sLineClass, "swapChildren", "(Lorg/andengine/entity/IEntity;Lorg/andengine/entity/IEntity;)Z");
}

	JNIEXPORT jboolean JNICALL Java_org_andengine_extension_scripting_entity_primitive_LineProxy_nativeOnAreaTouched(JNIEnv* pJNIEnv, jobject pJObject, jlong pAddress, jobject pSceneTouchEvent, jfloat pTouchAreaLocalX, jfloat pTouchAreaLocalY) {Line* line = (Line*)pAddress;
	TouchEvent sceneTouchEvent(pSceneTouchEvent);
return line->onAreaTouched(&sceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);}
	JNIEXPORT jboolean JNICALL Java_org_andengine_extension_scripting_entity_primitive_LineProxy_nativeOnAttached(JNIEnv* pJNIEnv, jobject pJObject, jlong pAddress) {Line* line = (Line*)pAddress;
return line->onAttached();}
	JNIEXPORT jboolean JNICALL Java_org_andengine_extension_scripting_entity_primitive_LineProxy_nativeOnDetached(JNIEnv* pJNIEnv, jobject pJObject, jlong pAddress) {Line* line = (Line*)pAddress;
return line->onDetached();}

Line::Line(jobject pLineProxy) {
	this->mUnwrapped = pLineProxy;
}
jobject Line::unwrap() {
	return this->mUnwrapped;
}
Line::Line(jfloat pX1, jfloat pY1, jfloat pX2, jfloat pY2, VertexBufferObjectManager* pVertexBufferObjectManager) {
	this->mUnwrapped = JNI_ENV()->NewObject(sLineClass, sConstructor__FFFF__org_andengine_opengl_vbo_VertexBufferObjectManager__, (jlong)this, pX1, pY1, pX2, pY2, pVertexBufferObjectManager->unwrap());
}
Line::Line(jfloat pX1, jfloat pY1, jfloat pX2, jfloat pY2, VertexBufferObjectManager* pVertexBufferObjectManager, DrawType* pDrawType) {
	this->mUnwrapped = JNI_ENV()->NewObject(sLineClass, sConstructor__FFFF__org_andengine_opengl_vbo_VertexBufferObjectManager____org_andengine_opengl_vbo_DrawType__, (jlong)this, pX1, pY1, pX2, pY2, pVertexBufferObjectManager->unwrap(), pDrawType->unwrap());
}
Line::Line(jfloat pX1, jfloat pY1, jfloat pX2, jfloat pY2, jfloat pLineWidth, VertexBufferObjectManager* pVertexBufferObjectManager) {
	this->mUnwrapped = JNI_ENV()->NewObject(sLineClass, sConstructor__FFFFF__org_andengine_opengl_vbo_VertexBufferObjectManager__, (jlong)this, pX1, pY1, pX2, pY2, pLineWidth, pVertexBufferObjectManager->unwrap());
}
Line::Line(jfloat pX1, jfloat pY1, jfloat pX2, jfloat pY2, jfloat pLineWidth, VertexBufferObjectManager* pVertexBufferObjectManager, DrawType* pDrawType) {
	this->mUnwrapped = JNI_ENV()->NewObject(sLineClass, sConstructor__FFFFF__org_andengine_opengl_vbo_VertexBufferObjectManager____org_andengine_opengl_vbo_DrawType__, (jlong)this, pX1, pY1, pX2, pY2, pLineWidth, pVertexBufferObjectManager->unwrap(), pDrawType->unwrap());
}
Line::Line(jfloat pX1, jfloat pY1, jfloat pX2, jfloat pY2, jfloat pLineWidth, ILineVertexBufferObject* pLineVertexBufferObject) {
	this->mUnwrapped = JNI_ENV()->NewObject(sLineClass, sConstructor__FFFFF__org_andengine_entity_primitive_vbo_I__ineVertexBufferObject__, (jlong)this, pX1, pY1, pX2, pY2, pLineWidth, pLineVertexBufferObject->unwrap());
}
Line::Line() {

}
jfloat Line::getY() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetY);
}
jfloat Line::getX() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetX);
}
void Line::setX(jfloat pX) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetX__F, pX);
}
void Line::setY(jfloat pY) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetY__F, pY);
}
VertexBufferObjectManager* Line::getVertexBufferObjectManager() {
	return new VertexBufferObjectManager(JNI_ENV()->CallObjectMethod(this->mUnwrapped, sMethod__GetVertexBufferObjectManager));
}
jboolean Line::onAreaTouched(TouchEvent* pSceneTouchEvent, jfloat pTouchAreaLocalX, jfloat pTouchAreaLocalY) {
	return false;
}
void Line::setColor(jfloat pRed, jfloat pGreen, jfloat pBlue) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetColor__FFF, pRed, pGreen, pBlue);
}
void Line::setColor(Color* pColor) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetColor____org_andengine_util_color_Color__, pColor->unwrap());
}
void Line::setColor(jfloat pRed, jfloat pGreen, jfloat pBlue, jfloat pAlpha) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetColor__FFFF, pRed, pGreen, pBlue, pAlpha);
}
void Line::setScale(jfloat pScaleX, jfloat pScaleY) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetScale__FF, pScaleX, pScaleY);
}
void Line::setScale(jfloat pScale) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetScale__F, pScale);
}
jfloat Line::getRotation() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetRotation);
}
void Line::setRotation(jfloat pRotation) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetRotation__F, pRotation);
}
void Line::attachChild(IEntity* pEntity) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__AttachChild____org_andengine_entity_IEntity__, pEntity->unwrap());
}
jboolean Line::attachChild(IEntity* pEntity, jint pIndex) {
	return JNI_ENV()->CallBooleanMethod(this->mUnwrapped, sMethod__AttachChild____org_andengine_entity_IEntity__I, pEntity->unwrap(), pIndex);
}
jfloat Line::getScaleX() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetScaleX);
}
jfloat Line::getScaleY() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetScaleY);
}
jfloat Line::getSkewX() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetSkewX);
}
jfloat Line::getSkewY() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetSkewY);
}
void Line::setSkew(jfloat pSkewX, jfloat pSkewY) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetSkew__FF, pSkewX, pSkewY);
}
void Line::setSkew(jfloat pSkew) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetSkew__F, pSkew);
}
jfloat Line::getRed() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetRed);
}
jfloat Line::getGreen() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetGreen);
}
jfloat Line::getBlue() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetBlue);
}
jfloat Line::getAlpha() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetAlpha);
}
Color* Line::getColor() {
	return new Color(JNI_ENV()->CallObjectMethod(this->mUnwrapped, sMethod__GetColor));
}
void Line::setRed(jfloat pRed) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetRed__F, pRed);
}
void Line::setGreen(jfloat pGreen) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetGreen__F, pGreen);
}
void Line::setBlue(jfloat pBlue) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetBlue__F, pBlue);
}
void Line::setAlpha(jfloat pAlpha) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetAlpha__F, pAlpha);
}
jint Line::getChildCount() {
	return JNI_ENV()->CallIntMethod(this->mUnwrapped, sMethod__GetChildCount);
}
IEntity* Line::getChild(jint pIndex) {
	return new Entity(JNI_ENV()->CallObjectMethod(this->mUnwrapped, sMethod__GetChild__I, pIndex));
}
IEntity* Line::getChild(IEntityMatcher* pEntityMatcher) {
	return new Entity(JNI_ENV()->CallObjectMethod(this->mUnwrapped, sMethod__GetChild____org_andengine_entity_IEntityMatcher__, pEntityMatcher->unwrap()));
}
jboolean Line::detachSelf() {
	return JNI_ENV()->CallBooleanMethod(this->mUnwrapped, sMethod__DetachSelf);
}
IEntity* Line::detachChild(IEntityMatcher* pEntityMatcher) {
	return new Entity(JNI_ENV()->CallObjectMethod(this->mUnwrapped, sMethod__DetachChild____org_andengine_entity_IEntityMatcher__, pEntityMatcher->unwrap()));
}
jboolean Line::detachChild(IEntity* pEntity) {
	return JNI_ENV()->CallBooleanMethod(this->mUnwrapped, sMethod__DetachChild____org_andengine_entity_IEntity__, pEntity->unwrap());
}
jboolean Line::onAttached() {
	return false;
}
jboolean Line::swapChildren(jint pIndexA, jint pIndexB) {
	return JNI_ENV()->CallBooleanMethod(this->mUnwrapped, sMethod__SwapChildren__II, pIndexA, pIndexB);
}
jboolean Line::swapChildren(IEntity* pEntityA, IEntity* pEntityB) {
	return JNI_ENV()->CallBooleanMethod(this->mUnwrapped, sMethod__SwapChildren____org_andengine_entity_IEntity____org_andengine_entity_IEntity__, pEntityA->unwrap(), pEntityB->unwrap());
}
jboolean Line::onDetached() {
	return false;
}

