#include <cstdlib>
#include "src/org/andengine/engine/Engine.h"

static jclass sEngineClass;
static jmethodID sConstructor____org_andengine_engine_options_EngineOptions__;
static jmethodID sMethod__GetVertexBufferObjectManager;
static jmethodID sMethod__GetTextureManager;
static jmethodID sMethod__GetFontManager;

JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_engine_EngineProxy_nativeInitClass(JNIEnv* pJNIEnv, jclass pJClass) {
	sEngineClass = (jclass)JNI_ENV()->NewGlobalRef(pJClass);
	sConstructor____org_andengine_engine_options_EngineOptions__ = JNI_ENV()->GetMethodID(sEngineClass, "<init>", "(JLorg/andengine/engine/options/EngineOptions;)V");
	sMethod__GetVertexBufferObjectManager = JNI_ENV()->GetMethodID(sEngineClass, "getVertexBufferObjectManager", "()Lorg/andengine/opengl/vbo/VertexBufferObjectManager;");
	sMethod__GetTextureManager = JNI_ENV()->GetMethodID(sEngineClass, "getTextureManager", "()Lorg/andengine/opengl/texture/TextureManager;");
	sMethod__GetFontManager = JNI_ENV()->GetMethodID(sEngineClass, "getFontManager", "()Lorg/andengine/opengl/font/FontManager;");
}

Engine::Engine(jobject pEngineProxy) {
	this->mUnwrapped = pEngineProxy;
}
jobject Engine::unwrap() {
	return this->mUnwrapped;
}
Engine::Engine(EngineOptions* pEngineOptions) {
	this->mUnwrapped = JNI_ENV()->NewObject(sEngineClass, sConstructor____org_andengine_engine_options_EngineOptions__, (jlong)this, pEngineOptions->unwrap());
}
Engine::Engine() {

}
VertexBufferObjectManager* Engine::getVertexBufferObjectManager() {
	return new VertexBufferObjectManager(JNI_ENV()->CallObjectMethod(this->mUnwrapped, sMethod__GetVertexBufferObjectManager));
}
TextureManager* Engine::getTextureManager() {
	return new TextureManager(JNI_ENV()->CallObjectMethod(this->mUnwrapped, sMethod__GetTextureManager));
}
FontManager* Engine::getFontManager() {
	return new FontManager(JNI_ENV()->CallObjectMethod(this->mUnwrapped, sMethod__GetFontManager));
}

