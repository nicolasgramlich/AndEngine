#include <cstdlib>
#include "src/org/andengine/entity/sprite/Sprite.h"

static jclass sSpriteClass;
static jmethodID sConstructor__FF__org_andengine_opengl_texture_region_ITextureRegion____org_andengine_opengl_vbo_VertexBufferObjectManager__;
static jmethodID sConstructor__FF__org_andengine_opengl_texture_region_ITextureRegion____org_andengine_entity_sprite_vbo_ISpriteVertexBufferObject____org_andengine_opengl_shader_ShaderProgram__;
static jmethodID sConstructor__FFFF__org_andengine_opengl_texture_region_ITextureRegion____org_andengine_opengl_vbo_VertexBufferObjectManager__;
static jmethodID sConstructor__FFFF__org_andengine_opengl_texture_region_ITextureRegion____org_andengine_opengl_vbo_VertexBufferObjectManager____org_andengine_opengl_shader_ShaderProgram__;
static jmethodID sConstructor__FFFF__org_andengine_opengl_texture_region_ITextureRegion____org_andengine_opengl_vbo_VertexBufferObjectManager____org_andengine_opengl_vbo_DrawType__;
static jmethodID sConstructor__FFFF__org_andengine_opengl_texture_region_ITextureRegion____org_andengine_opengl_vbo_VertexBufferObjectManager____org_andengine_opengl_vbo_DrawType____org_andengine_opengl_shader_ShaderProgram__;
static jmethodID sConstructor__FFFF__org_andengine_opengl_texture_region_ITextureRegion____org_andengine_entity_sprite_vbo_ISpriteVertexBufferObject__;
static jmethodID sConstructor__FFFF__org_andengine_opengl_texture_region_ITextureRegion____org_andengine_entity_sprite_vbo_ISpriteVertexBufferObject____org_andengine_opengl_shader_ShaderProgram__;
static jmethodID sConstructor__FF__org_andengine_opengl_texture_region_ITextureRegion____org_andengine_opengl_vbo_VertexBufferObjectManager____org_andengine_opengl_shader_ShaderProgram__;
static jmethodID sConstructor__FF__org_andengine_opengl_texture_region_ITextureRegion____org_andengine_opengl_vbo_VertexBufferObjectManager____org_andengine_opengl_vbo_DrawType__;
static jmethodID sConstructor__FF__org_andengine_opengl_texture_region_ITextureRegion____org_andengine_opengl_vbo_VertexBufferObjectManager____org_andengine_opengl_vbo_DrawType____org_andengine_opengl_shader_ShaderProgram__;
static jmethodID sConstructor__FF__org_andengine_opengl_texture_region_ITextureRegion____org_andengine_entity_sprite_vbo_ISpriteVertexBufferObject__;
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

JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_entity_sprite_SpriteProxy_nativeInitClass(JNIEnv* pJNIEnv, jclass pJClass) {
	sSpriteClass = (jclass)JNI_ENV()->NewGlobalRef(pJClass);
	sConstructor__FF__org_andengine_opengl_texture_region_ITextureRegion____org_andengine_opengl_vbo_VertexBufferObjectManager__ = JNI_ENV()->GetMethodID(sSpriteClass, "<init>", "(JFFLorg/andengine/opengl/texture/region/ITextureRegion;Lorg/andengine/opengl/vbo/VertexBufferObjectManager;)V");
	sConstructor__FF__org_andengine_opengl_texture_region_ITextureRegion____org_andengine_entity_sprite_vbo_ISpriteVertexBufferObject____org_andengine_opengl_shader_ShaderProgram__ = JNI_ENV()->GetMethodID(sSpriteClass, "<init>", "(JFFLorg/andengine/opengl/texture/region/ITextureRegion;Lorg/andengine/entity/sprite/vbo/ISpriteVertexBufferObject;Lorg/andengine/opengl/shader/ShaderProgram;)V");
	sConstructor__FFFF__org_andengine_opengl_texture_region_ITextureRegion____org_andengine_opengl_vbo_VertexBufferObjectManager__ = JNI_ENV()->GetMethodID(sSpriteClass, "<init>", "(JFFFFLorg/andengine/opengl/texture/region/ITextureRegion;Lorg/andengine/opengl/vbo/VertexBufferObjectManager;)V");
	sConstructor__FFFF__org_andengine_opengl_texture_region_ITextureRegion____org_andengine_opengl_vbo_VertexBufferObjectManager____org_andengine_opengl_shader_ShaderProgram__ = JNI_ENV()->GetMethodID(sSpriteClass, "<init>", "(JFFFFLorg/andengine/opengl/texture/region/ITextureRegion;Lorg/andengine/opengl/vbo/VertexBufferObjectManager;Lorg/andengine/opengl/shader/ShaderProgram;)V");
	sConstructor__FFFF__org_andengine_opengl_texture_region_ITextureRegion____org_andengine_opengl_vbo_VertexBufferObjectManager____org_andengine_opengl_vbo_DrawType__ = JNI_ENV()->GetMethodID(sSpriteClass, "<init>", "(JFFFFLorg/andengine/opengl/texture/region/ITextureRegion;Lorg/andengine/opengl/vbo/VertexBufferObjectManager;Lorg/andengine/opengl/vbo/DrawType;)V");
	sConstructor__FFFF__org_andengine_opengl_texture_region_ITextureRegion____org_andengine_opengl_vbo_VertexBufferObjectManager____org_andengine_opengl_vbo_DrawType____org_andengine_opengl_shader_ShaderProgram__ = JNI_ENV()->GetMethodID(sSpriteClass, "<init>", "(JFFFFLorg/andengine/opengl/texture/region/ITextureRegion;Lorg/andengine/opengl/vbo/VertexBufferObjectManager;Lorg/andengine/opengl/vbo/DrawType;Lorg/andengine/opengl/shader/ShaderProgram;)V");
	sConstructor__FFFF__org_andengine_opengl_texture_region_ITextureRegion____org_andengine_entity_sprite_vbo_ISpriteVertexBufferObject__ = JNI_ENV()->GetMethodID(sSpriteClass, "<init>", "(JFFFFLorg/andengine/opengl/texture/region/ITextureRegion;Lorg/andengine/entity/sprite/vbo/ISpriteVertexBufferObject;)V");
	sConstructor__FFFF__org_andengine_opengl_texture_region_ITextureRegion____org_andengine_entity_sprite_vbo_ISpriteVertexBufferObject____org_andengine_opengl_shader_ShaderProgram__ = JNI_ENV()->GetMethodID(sSpriteClass, "<init>", "(JFFFFLorg/andengine/opengl/texture/region/ITextureRegion;Lorg/andengine/entity/sprite/vbo/ISpriteVertexBufferObject;Lorg/andengine/opengl/shader/ShaderProgram;)V");
	sConstructor__FF__org_andengine_opengl_texture_region_ITextureRegion____org_andengine_opengl_vbo_VertexBufferObjectManager____org_andengine_opengl_shader_ShaderProgram__ = JNI_ENV()->GetMethodID(sSpriteClass, "<init>", "(JFFLorg/andengine/opengl/texture/region/ITextureRegion;Lorg/andengine/opengl/vbo/VertexBufferObjectManager;Lorg/andengine/opengl/shader/ShaderProgram;)V");
	sConstructor__FF__org_andengine_opengl_texture_region_ITextureRegion____org_andengine_opengl_vbo_VertexBufferObjectManager____org_andengine_opengl_vbo_DrawType__ = JNI_ENV()->GetMethodID(sSpriteClass, "<init>", "(JFFLorg/andengine/opengl/texture/region/ITextureRegion;Lorg/andengine/opengl/vbo/VertexBufferObjectManager;Lorg/andengine/opengl/vbo/DrawType;)V");
	sConstructor__FF__org_andengine_opengl_texture_region_ITextureRegion____org_andengine_opengl_vbo_VertexBufferObjectManager____org_andengine_opengl_vbo_DrawType____org_andengine_opengl_shader_ShaderProgram__ = JNI_ENV()->GetMethodID(sSpriteClass, "<init>", "(JFFLorg/andengine/opengl/texture/region/ITextureRegion;Lorg/andengine/opengl/vbo/VertexBufferObjectManager;Lorg/andengine/opengl/vbo/DrawType;Lorg/andengine/opengl/shader/ShaderProgram;)V");
	sConstructor__FF__org_andengine_opengl_texture_region_ITextureRegion____org_andengine_entity_sprite_vbo_ISpriteVertexBufferObject__ = JNI_ENV()->GetMethodID(sSpriteClass, "<init>", "(JFFLorg/andengine/opengl/texture/region/ITextureRegion;Lorg/andengine/entity/sprite/vbo/ISpriteVertexBufferObject;)V");
	sMethod__GetWidth = JNI_ENV()->GetMethodID(sSpriteClass, "getWidth", "()F");
	sMethod__GetHeight = JNI_ENV()->GetMethodID(sSpriteClass, "getHeight", "()F");
	sMethod__GetVertexBufferObjectManager = JNI_ENV()->GetMethodID(sSpriteClass, "getVertexBufferObjectManager", "()Lorg/andengine/opengl/vbo/VertexBufferObjectManager;");
	sMethod__GetY = JNI_ENV()->GetMethodID(sSpriteClass, "getY", "()F");
	sMethod__GetX = JNI_ENV()->GetMethodID(sSpriteClass, "getX", "()F");
	sMethod__SetColor__FFF = JNI_ENV()->GetMethodID(sSpriteClass, "setColor", "(FFF)V");
	sMethod__SetColor____org_andengine_util_color_Color__ = JNI_ENV()->GetMethodID(sSpriteClass, "setColor", "(Lorg/andengine/util/color/Color;)V");
	sMethod__SetColor__FFFF = JNI_ENV()->GetMethodID(sSpriteClass, "setColor", "(FFFF)V");
	sMethod__SetScale__FF = JNI_ENV()->GetMethodID(sSpriteClass, "setScale", "(FF)V");
	sMethod__SetScale__F = JNI_ENV()->GetMethodID(sSpriteClass, "setScale", "(F)V");
	sMethod__GetRotation = JNI_ENV()->GetMethodID(sSpriteClass, "getRotation", "()F");
	sMethod__SetRotation__F = JNI_ENV()->GetMethodID(sSpriteClass, "setRotation", "(F)V");
	sMethod__AttachChild____org_andengine_entity_IEntity__ = JNI_ENV()->GetMethodID(sSpriteClass, "attachChild", "(Lorg/andengine/entity/IEntity;)V");
	sMethod__AttachChild____org_andengine_entity_IEntity__I = JNI_ENV()->GetMethodID(sSpriteClass, "attachChild", "(Lorg/andengine/entity/IEntity;I)Z");
	sMethod__SetX__F = JNI_ENV()->GetMethodID(sSpriteClass, "setX", "(F)V");
	sMethod__SetY__F = JNI_ENV()->GetMethodID(sSpriteClass, "setY", "(F)V");
	sMethod__GetScaleX = JNI_ENV()->GetMethodID(sSpriteClass, "getScaleX", "()F");
	sMethod__GetScaleY = JNI_ENV()->GetMethodID(sSpriteClass, "getScaleY", "()F");
	sMethod__GetSkewX = JNI_ENV()->GetMethodID(sSpriteClass, "getSkewX", "()F");
	sMethod__GetSkewY = JNI_ENV()->GetMethodID(sSpriteClass, "getSkewY", "()F");
	sMethod__SetSkew__FF = JNI_ENV()->GetMethodID(sSpriteClass, "setSkew", "(FF)V");
	sMethod__SetSkew__F = JNI_ENV()->GetMethodID(sSpriteClass, "setSkew", "(F)V");
	sMethod__GetRed = JNI_ENV()->GetMethodID(sSpriteClass, "getRed", "()F");
	sMethod__GetGreen = JNI_ENV()->GetMethodID(sSpriteClass, "getGreen", "()F");
	sMethod__GetBlue = JNI_ENV()->GetMethodID(sSpriteClass, "getBlue", "()F");
	sMethod__GetAlpha = JNI_ENV()->GetMethodID(sSpriteClass, "getAlpha", "()F");
	sMethod__GetColor = JNI_ENV()->GetMethodID(sSpriteClass, "getColor", "()Lorg/andengine/util/color/Color;");
	sMethod__SetRed__F = JNI_ENV()->GetMethodID(sSpriteClass, "setRed", "(F)V");
	sMethod__SetGreen__F = JNI_ENV()->GetMethodID(sSpriteClass, "setGreen", "(F)V");
	sMethod__SetBlue__F = JNI_ENV()->GetMethodID(sSpriteClass, "setBlue", "(F)V");
	sMethod__SetAlpha__F = JNI_ENV()->GetMethodID(sSpriteClass, "setAlpha", "(F)V");
	sMethod__GetChildCount = JNI_ENV()->GetMethodID(sSpriteClass, "getChildCount", "()I");
	sMethod__GetChild__I = JNI_ENV()->GetMethodID(sSpriteClass, "getChild", "(I)Lorg/andengine/entity/IEntity;");
	sMethod__GetChild____org_andengine_entity_IEntityMatcher__ = JNI_ENV()->GetMethodID(sSpriteClass, "getChild", "(Lorg/andengine/entity/IEntityMatcher;)Lorg/andengine/entity/IEntity;");
	sMethod__DetachSelf = JNI_ENV()->GetMethodID(sSpriteClass, "detachSelf", "()Z");
	sMethod__DetachChild____org_andengine_entity_IEntityMatcher__ = JNI_ENV()->GetMethodID(sSpriteClass, "detachChild", "(Lorg/andengine/entity/IEntityMatcher;)Lorg/andengine/entity/IEntity;");
	sMethod__DetachChild____org_andengine_entity_IEntity__ = JNI_ENV()->GetMethodID(sSpriteClass, "detachChild", "(Lorg/andengine/entity/IEntity;)Z");
	sMethod__SwapChildren__II = JNI_ENV()->GetMethodID(sSpriteClass, "swapChildren", "(II)Z");
	sMethod__SwapChildren____org_andengine_entity_IEntity____org_andengine_entity_IEntity__ = JNI_ENV()->GetMethodID(sSpriteClass, "swapChildren", "(Lorg/andengine/entity/IEntity;Lorg/andengine/entity/IEntity;)Z");
}

	JNIEXPORT jboolean JNICALL Java_org_andengine_extension_scripting_entity_sprite_SpriteProxy_nativeOnAreaTouched(JNIEnv* pJNIEnv, jobject pJObject, jlong pAddress, jobject pSceneTouchEvent, jfloat pTouchAreaLocalX, jfloat pTouchAreaLocalY) {Sprite* sprite = (Sprite*)pAddress;
	TouchEvent sceneTouchEvent(pSceneTouchEvent);
return sprite->onAreaTouched(&sceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);}
	JNIEXPORT jboolean JNICALL Java_org_andengine_extension_scripting_entity_sprite_SpriteProxy_nativeOnAttached(JNIEnv* pJNIEnv, jobject pJObject, jlong pAddress) {Sprite* sprite = (Sprite*)pAddress;
return sprite->onAttached();}
	JNIEXPORT jboolean JNICALL Java_org_andengine_extension_scripting_entity_sprite_SpriteProxy_nativeOnDetached(JNIEnv* pJNIEnv, jobject pJObject, jlong pAddress) {Sprite* sprite = (Sprite*)pAddress;
return sprite->onDetached();}

Sprite::Sprite(jobject pSpriteProxy) {
	this->mUnwrapped = pSpriteProxy;
}
jobject Sprite::unwrap() {
	return this->mUnwrapped;
}
Sprite::Sprite(jfloat pX, jfloat pY, ITextureRegion* pTextureRegion, VertexBufferObjectManager* pVertexBufferObjectManager) {
	this->mUnwrapped = JNI_ENV()->NewObject(sSpriteClass, sConstructor__FF__org_andengine_opengl_texture_region_ITextureRegion____org_andengine_opengl_vbo_VertexBufferObjectManager__, (jlong)this, pX, pY, pTextureRegion->unwrap(), pVertexBufferObjectManager->unwrap());
}
Sprite::Sprite(jfloat pX, jfloat pY, ITextureRegion* pTextureRegion, ISpriteVertexBufferObject* pVertexBufferObject, ShaderProgram* pShaderProgram) {
	this->mUnwrapped = JNI_ENV()->NewObject(sSpriteClass, sConstructor__FF__org_andengine_opengl_texture_region_ITextureRegion____org_andengine_entity_sprite_vbo_ISpriteVertexBufferObject____org_andengine_opengl_shader_ShaderProgram__, (jlong)this, pX, pY, pTextureRegion->unwrap(), pVertexBufferObject->unwrap(), pShaderProgram->unwrap());
}
Sprite::Sprite(jfloat pX, jfloat pY, jfloat pWidth, jfloat pHeight, ITextureRegion* pTextureRegion, VertexBufferObjectManager* pVertexBufferObjectManager) {
	this->mUnwrapped = JNI_ENV()->NewObject(sSpriteClass, sConstructor__FFFF__org_andengine_opengl_texture_region_ITextureRegion____org_andengine_opengl_vbo_VertexBufferObjectManager__, (jlong)this, pX, pY, pWidth, pHeight, pTextureRegion->unwrap(), pVertexBufferObjectManager->unwrap());
}
Sprite::Sprite(jfloat pX, jfloat pY, jfloat pWidth, jfloat pHeight, ITextureRegion* pTextureRegion, VertexBufferObjectManager* pVertexBufferObjectManager, ShaderProgram* pShaderProgram) {
	this->mUnwrapped = JNI_ENV()->NewObject(sSpriteClass, sConstructor__FFFF__org_andengine_opengl_texture_region_ITextureRegion____org_andengine_opengl_vbo_VertexBufferObjectManager____org_andengine_opengl_shader_ShaderProgram__, (jlong)this, pX, pY, pWidth, pHeight, pTextureRegion->unwrap(), pVertexBufferObjectManager->unwrap(), pShaderProgram->unwrap());
}
Sprite::Sprite(jfloat pX, jfloat pY, jfloat pWidth, jfloat pHeight, ITextureRegion* pTextureRegion, VertexBufferObjectManager* pVertexBufferObjectManager, DrawType* pDrawType) {
	this->mUnwrapped = JNI_ENV()->NewObject(sSpriteClass, sConstructor__FFFF__org_andengine_opengl_texture_region_ITextureRegion____org_andengine_opengl_vbo_VertexBufferObjectManager____org_andengine_opengl_vbo_DrawType__, (jlong)this, pX, pY, pWidth, pHeight, pTextureRegion->unwrap(), pVertexBufferObjectManager->unwrap(), pDrawType->unwrap());
}
Sprite::Sprite(jfloat pX, jfloat pY, jfloat pWidth, jfloat pHeight, ITextureRegion* pTextureRegion, VertexBufferObjectManager* pVertexBufferObjectManager, DrawType* pDrawType, ShaderProgram* pShaderProgram) {
	this->mUnwrapped = JNI_ENV()->NewObject(sSpriteClass, sConstructor__FFFF__org_andengine_opengl_texture_region_ITextureRegion____org_andengine_opengl_vbo_VertexBufferObjectManager____org_andengine_opengl_vbo_DrawType____org_andengine_opengl_shader_ShaderProgram__, (jlong)this, pX, pY, pWidth, pHeight, pTextureRegion->unwrap(), pVertexBufferObjectManager->unwrap(), pDrawType->unwrap(), pShaderProgram->unwrap());
}
Sprite::Sprite(jfloat pX, jfloat pY, jfloat pWidth, jfloat pHeight, ITextureRegion* pTextureRegion, ISpriteVertexBufferObject* pSpriteVertexBufferObject) {
	this->mUnwrapped = JNI_ENV()->NewObject(sSpriteClass, sConstructor__FFFF__org_andengine_opengl_texture_region_ITextureRegion____org_andengine_entity_sprite_vbo_ISpriteVertexBufferObject__, (jlong)this, pX, pY, pWidth, pHeight, pTextureRegion->unwrap(), pSpriteVertexBufferObject->unwrap());
}
Sprite::Sprite(jfloat pX, jfloat pY, jfloat pWidth, jfloat pHeight, ITextureRegion* pTextureRegion, ISpriteVertexBufferObject* pSpriteVertexBufferObject, ShaderProgram* pShaderProgram) {
	this->mUnwrapped = JNI_ENV()->NewObject(sSpriteClass, sConstructor__FFFF__org_andengine_opengl_texture_region_ITextureRegion____org_andengine_entity_sprite_vbo_ISpriteVertexBufferObject____org_andengine_opengl_shader_ShaderProgram__, (jlong)this, pX, pY, pWidth, pHeight, pTextureRegion->unwrap(), pSpriteVertexBufferObject->unwrap(), pShaderProgram->unwrap());
}
Sprite::Sprite(jfloat pX, jfloat pY, ITextureRegion* pTextureRegion, VertexBufferObjectManager* pVertexBufferObjectManager, ShaderProgram* pShaderProgram) {
	this->mUnwrapped = JNI_ENV()->NewObject(sSpriteClass, sConstructor__FF__org_andengine_opengl_texture_region_ITextureRegion____org_andengine_opengl_vbo_VertexBufferObjectManager____org_andengine_opengl_shader_ShaderProgram__, (jlong)this, pX, pY, pTextureRegion->unwrap(), pVertexBufferObjectManager->unwrap(), pShaderProgram->unwrap());
}
Sprite::Sprite(jfloat pX, jfloat pY, ITextureRegion* pTextureRegion, VertexBufferObjectManager* pVertexBufferObjectManager, DrawType* pDrawType) {
	this->mUnwrapped = JNI_ENV()->NewObject(sSpriteClass, sConstructor__FF__org_andengine_opengl_texture_region_ITextureRegion____org_andengine_opengl_vbo_VertexBufferObjectManager____org_andengine_opengl_vbo_DrawType__, (jlong)this, pX, pY, pTextureRegion->unwrap(), pVertexBufferObjectManager->unwrap(), pDrawType->unwrap());
}
Sprite::Sprite(jfloat pX, jfloat pY, ITextureRegion* pTextureRegion, VertexBufferObjectManager* pVertexBufferObjectManager, DrawType* pDrawType, ShaderProgram* pShaderProgram) {
	this->mUnwrapped = JNI_ENV()->NewObject(sSpriteClass, sConstructor__FF__org_andengine_opengl_texture_region_ITextureRegion____org_andengine_opengl_vbo_VertexBufferObjectManager____org_andengine_opengl_vbo_DrawType____org_andengine_opengl_shader_ShaderProgram__, (jlong)this, pX, pY, pTextureRegion->unwrap(), pVertexBufferObjectManager->unwrap(), pDrawType->unwrap(), pShaderProgram->unwrap());
}
Sprite::Sprite(jfloat pX, jfloat pY, ITextureRegion* pTextureRegion, ISpriteVertexBufferObject* pVertexBufferObject) {
	this->mUnwrapped = JNI_ENV()->NewObject(sSpriteClass, sConstructor__FF__org_andengine_opengl_texture_region_ITextureRegion____org_andengine_entity_sprite_vbo_ISpriteVertexBufferObject__, (jlong)this, pX, pY, pTextureRegion->unwrap(), pVertexBufferObject->unwrap());
}
Sprite::Sprite() {

}
jfloat Sprite::getWidth() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetWidth);
}
jfloat Sprite::getHeight() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetHeight);
}
VertexBufferObjectManager* Sprite::getVertexBufferObjectManager() {
	return new VertexBufferObjectManager(JNI_ENV()->CallObjectMethod(this->mUnwrapped, sMethod__GetVertexBufferObjectManager));
}
jboolean Sprite::onAreaTouched(TouchEvent* pSceneTouchEvent, jfloat pTouchAreaLocalX, jfloat pTouchAreaLocalY) {
	return false;
}
jfloat Sprite::getY() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetY);
}
jfloat Sprite::getX() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetX);
}
void Sprite::setColor(jfloat pRed, jfloat pGreen, jfloat pBlue) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetColor__FFF, pRed, pGreen, pBlue);
}
void Sprite::setColor(Color* pColor) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetColor____org_andengine_util_color_Color__, pColor->unwrap());
}
void Sprite::setColor(jfloat pRed, jfloat pGreen, jfloat pBlue, jfloat pAlpha) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetColor__FFFF, pRed, pGreen, pBlue, pAlpha);
}
void Sprite::setScale(jfloat pScaleX, jfloat pScaleY) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetScale__FF, pScaleX, pScaleY);
}
void Sprite::setScale(jfloat pScale) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetScale__F, pScale);
}
jfloat Sprite::getRotation() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetRotation);
}
void Sprite::setRotation(jfloat pRotation) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetRotation__F, pRotation);
}
void Sprite::attachChild(IEntity* pEntity) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__AttachChild____org_andengine_entity_IEntity__, pEntity->unwrap());
}
jboolean Sprite::attachChild(IEntity* pEntity, jint pIndex) {
	return JNI_ENV()->CallBooleanMethod(this->mUnwrapped, sMethod__AttachChild____org_andengine_entity_IEntity__I, pEntity->unwrap(), pIndex);
}
void Sprite::setX(jfloat pX) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetX__F, pX);
}
void Sprite::setY(jfloat pY) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetY__F, pY);
}
jfloat Sprite::getScaleX() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetScaleX);
}
jfloat Sprite::getScaleY() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetScaleY);
}
jfloat Sprite::getSkewX() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetSkewX);
}
jfloat Sprite::getSkewY() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetSkewY);
}
void Sprite::setSkew(jfloat pSkewX, jfloat pSkewY) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetSkew__FF, pSkewX, pSkewY);
}
void Sprite::setSkew(jfloat pSkew) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetSkew__F, pSkew);
}
jfloat Sprite::getRed() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetRed);
}
jfloat Sprite::getGreen() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetGreen);
}
jfloat Sprite::getBlue() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetBlue);
}
jfloat Sprite::getAlpha() {
	return JNI_ENV()->CallFloatMethod(this->mUnwrapped, sMethod__GetAlpha);
}
Color* Sprite::getColor() {
	return new Color(JNI_ENV()->CallObjectMethod(this->mUnwrapped, sMethod__GetColor));
}
void Sprite::setRed(jfloat pRed) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetRed__F, pRed);
}
void Sprite::setGreen(jfloat pGreen) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetGreen__F, pGreen);
}
void Sprite::setBlue(jfloat pBlue) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetBlue__F, pBlue);
}
void Sprite::setAlpha(jfloat pAlpha) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__SetAlpha__F, pAlpha);
}
jint Sprite::getChildCount() {
	return JNI_ENV()->CallIntMethod(this->mUnwrapped, sMethod__GetChildCount);
}
IEntity* Sprite::getChild(jint pIndex) {
	return new Entity(JNI_ENV()->CallObjectMethod(this->mUnwrapped, sMethod__GetChild__I, pIndex));
}
IEntity* Sprite::getChild(IEntityMatcher* pEntityMatcher) {
	return new Entity(JNI_ENV()->CallObjectMethod(this->mUnwrapped, sMethod__GetChild____org_andengine_entity_IEntityMatcher__, pEntityMatcher->unwrap()));
}
jboolean Sprite::detachSelf() {
	return JNI_ENV()->CallBooleanMethod(this->mUnwrapped, sMethod__DetachSelf);
}
IEntity* Sprite::detachChild(IEntityMatcher* pEntityMatcher) {
	return new Entity(JNI_ENV()->CallObjectMethod(this->mUnwrapped, sMethod__DetachChild____org_andengine_entity_IEntityMatcher__, pEntityMatcher->unwrap()));
}
jboolean Sprite::detachChild(IEntity* pEntity) {
	return JNI_ENV()->CallBooleanMethod(this->mUnwrapped, sMethod__DetachChild____org_andengine_entity_IEntity__, pEntity->unwrap());
}
jboolean Sprite::onAttached() {
	return false;
}
jboolean Sprite::swapChildren(jint pIndexA, jint pIndexB) {
	return JNI_ENV()->CallBooleanMethod(this->mUnwrapped, sMethod__SwapChildren__II, pIndexA, pIndexB);
}
jboolean Sprite::swapChildren(IEntity* pEntityA, IEntity* pEntityB) {
	return JNI_ENV()->CallBooleanMethod(this->mUnwrapped, sMethod__SwapChildren____org_andengine_entity_IEntity____org_andengine_entity_IEntity__, pEntityA->unwrap(), pEntityB->unwrap());
}
jboolean Sprite::onDetached() {
	return false;
}

