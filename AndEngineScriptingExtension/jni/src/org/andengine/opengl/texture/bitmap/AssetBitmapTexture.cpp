#include <cstdlib>
#include "src/org/andengine/opengl/texture/bitmap/AssetBitmapTexture.h"

static jclass sAssetBitmapTextureClass;
static jmethodID sConstructor____org_andengine_opengl_texture_TextureManager____android_content_res_AssetManager____java_lang_String__;
static jmethodID sConstructor____org_andengine_opengl_texture_TextureManager____android_content_res_AssetManager____java_lang_String____org_andengine_opengl_texture_bitmap_BitmapTextureFormat__;
static jmethodID sConstructor____org_andengine_opengl_texture_TextureManager____android_content_res_AssetManager____java_lang_String____org_andengine_opengl_texture_TextureOptions__;
static jmethodID sConstructor____org_andengine_opengl_texture_TextureManager____android_content_res_AssetManager____java_lang_String____org_andengine_opengl_texture_bitmap_BitmapTextureFormat____org_andengine_opengl_texture_TextureOptions__;
static jmethodID sConstructor____org_andengine_opengl_texture_TextureManager____android_content_res_AssetManager____java_lang_String____org_andengine_opengl_texture_bitmap_BitmapTextureFormat____org_andengine_opengl_texture_TextureOptions____org_andengine_opengl_texture_ITextureState__istener__;
static jmethodID sMethod__GetWidth;
static jmethodID sMethod__GetHeight;
static jmethodID sMethod__Load;
static jmethodID sMethod__Load____org_andengine_opengl_util_G__State__;
static jmethodID sMethod__Unload____org_andengine_opengl_util_G__State__;
static jmethodID sMethod__Unload;

JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_opengl_texture_bitmap_AssetBitmapTextureProxy_nativeInitClass(JNIEnv* pJNIEnv, jclass pJClass) {
	sAssetBitmapTextureClass = (jclass)JNI_ENV()->NewGlobalRef(pJClass);
	sConstructor____org_andengine_opengl_texture_TextureManager____android_content_res_AssetManager____java_lang_String__ = JNI_ENV()->GetMethodID(sAssetBitmapTextureClass, "<init>", "(JLorg/andengine/opengl/texture/TextureManager;Landroid/content/res/AssetManager;Ljava/lang/String;)V");
	sConstructor____org_andengine_opengl_texture_TextureManager____android_content_res_AssetManager____java_lang_String____org_andengine_opengl_texture_bitmap_BitmapTextureFormat__ = JNI_ENV()->GetMethodID(sAssetBitmapTextureClass, "<init>", "(JLorg/andengine/opengl/texture/TextureManager;Landroid/content/res/AssetManager;Ljava/lang/String;Lorg/andengine/opengl/texture/bitmap/BitmapTextureFormat;)V");
	sConstructor____org_andengine_opengl_texture_TextureManager____android_content_res_AssetManager____java_lang_String____org_andengine_opengl_texture_TextureOptions__ = JNI_ENV()->GetMethodID(sAssetBitmapTextureClass, "<init>", "(JLorg/andengine/opengl/texture/TextureManager;Landroid/content/res/AssetManager;Ljava/lang/String;Lorg/andengine/opengl/texture/TextureOptions;)V");
	sConstructor____org_andengine_opengl_texture_TextureManager____android_content_res_AssetManager____java_lang_String____org_andengine_opengl_texture_bitmap_BitmapTextureFormat____org_andengine_opengl_texture_TextureOptions__ = JNI_ENV()->GetMethodID(sAssetBitmapTextureClass, "<init>", "(JLorg/andengine/opengl/texture/TextureManager;Landroid/content/res/AssetManager;Ljava/lang/String;Lorg/andengine/opengl/texture/bitmap/BitmapTextureFormat;Lorg/andengine/opengl/texture/TextureOptions;)V");
	sConstructor____org_andengine_opengl_texture_TextureManager____android_content_res_AssetManager____java_lang_String____org_andengine_opengl_texture_bitmap_BitmapTextureFormat____org_andengine_opengl_texture_TextureOptions____org_andengine_opengl_texture_ITextureState__istener__ = JNI_ENV()->GetMethodID(sAssetBitmapTextureClass, "<init>", "(JLorg/andengine/opengl/texture/TextureManager;Landroid/content/res/AssetManager;Ljava/lang/String;Lorg/andengine/opengl/texture/bitmap/BitmapTextureFormat;Lorg/andengine/opengl/texture/TextureOptions;Lorg/andengine/opengl/texture/ITextureStateListener;)V");
	sMethod__GetWidth = JNI_ENV()->GetMethodID(sAssetBitmapTextureClass, "getWidth", "()I");
	sMethod__GetHeight = JNI_ENV()->GetMethodID(sAssetBitmapTextureClass, "getHeight", "()I");
	sMethod__Load = JNI_ENV()->GetMethodID(sAssetBitmapTextureClass, "load", "()V");
	sMethod__Load____org_andengine_opengl_util_G__State__ = JNI_ENV()->GetMethodID(sAssetBitmapTextureClass, "load", "(Lorg/andengine/opengl/util/GLState;)V");
	sMethod__Unload____org_andengine_opengl_util_G__State__ = JNI_ENV()->GetMethodID(sAssetBitmapTextureClass, "unload", "(Lorg/andengine/opengl/util/GLState;)V");
	sMethod__Unload = JNI_ENV()->GetMethodID(sAssetBitmapTextureClass, "unload", "()V");
}

AssetBitmapTexture::AssetBitmapTexture(jobject pAssetBitmapTextureProxy) {
	this->mUnwrapped = pAssetBitmapTextureProxy;
}
jobject AssetBitmapTexture::unwrap() {
	return this->mUnwrapped;
}
AssetBitmapTexture::AssetBitmapTexture(TextureManager* pTextureManager, AssetManager* pAssetManager, jstring pAssetPath) {
	this->mUnwrapped = JNI_ENV()->NewObject(sAssetBitmapTextureClass, sConstructor____org_andengine_opengl_texture_TextureManager____android_content_res_AssetManager____java_lang_String__, (jlong)this, pTextureManager->unwrap(), pAssetManager->unwrap(), pAssetPath);
}
AssetBitmapTexture::AssetBitmapTexture(TextureManager* pTextureManager, AssetManager* pAssetManager, jstring pAssetPath, BitmapTextureFormat* pBitmapTextureFormat) {
	this->mUnwrapped = JNI_ENV()->NewObject(sAssetBitmapTextureClass, sConstructor____org_andengine_opengl_texture_TextureManager____android_content_res_AssetManager____java_lang_String____org_andengine_opengl_texture_bitmap_BitmapTextureFormat__, (jlong)this, pTextureManager->unwrap(), pAssetManager->unwrap(), pAssetPath, pBitmapTextureFormat->unwrap());
}
AssetBitmapTexture::AssetBitmapTexture(TextureManager* pTextureManager, AssetManager* pAssetManager, jstring pAssetPath, TextureOptions* pTextureOptions) {
	this->mUnwrapped = JNI_ENV()->NewObject(sAssetBitmapTextureClass, sConstructor____org_andengine_opengl_texture_TextureManager____android_content_res_AssetManager____java_lang_String____org_andengine_opengl_texture_TextureOptions__, (jlong)this, pTextureManager->unwrap(), pAssetManager->unwrap(), pAssetPath, pTextureOptions->unwrap());
}
AssetBitmapTexture::AssetBitmapTexture(TextureManager* pTextureManager, AssetManager* pAssetManager, jstring pAssetPath, BitmapTextureFormat* pBitmapTextureFormat, TextureOptions* pTextureOptions) {
	this->mUnwrapped = JNI_ENV()->NewObject(sAssetBitmapTextureClass, sConstructor____org_andengine_opengl_texture_TextureManager____android_content_res_AssetManager____java_lang_String____org_andengine_opengl_texture_bitmap_BitmapTextureFormat____org_andengine_opengl_texture_TextureOptions__, (jlong)this, pTextureManager->unwrap(), pAssetManager->unwrap(), pAssetPath, pBitmapTextureFormat->unwrap(), pTextureOptions->unwrap());
}
AssetBitmapTexture::AssetBitmapTexture(TextureManager* pTextureManager, AssetManager* pAssetManager, jstring pAssetPath, BitmapTextureFormat* pBitmapTextureFormat, TextureOptions* pTextureOptions, ITextureStateListener* pTextureStateListener) {
	this->mUnwrapped = JNI_ENV()->NewObject(sAssetBitmapTextureClass, sConstructor____org_andengine_opengl_texture_TextureManager____android_content_res_AssetManager____java_lang_String____org_andengine_opengl_texture_bitmap_BitmapTextureFormat____org_andengine_opengl_texture_TextureOptions____org_andengine_opengl_texture_ITextureState__istener__, (jlong)this, pTextureManager->unwrap(), pAssetManager->unwrap(), pAssetPath, pBitmapTextureFormat->unwrap(), pTextureOptions->unwrap(), pTextureStateListener->unwrap());
}
AssetBitmapTexture::AssetBitmapTexture() {

}
jint AssetBitmapTexture::getWidth() {
	return JNI_ENV()->CallIntMethod(this->mUnwrapped, sMethod__GetWidth);
}
jint AssetBitmapTexture::getHeight() {
	return JNI_ENV()->CallIntMethod(this->mUnwrapped, sMethod__GetHeight);
}
void AssetBitmapTexture::load() {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__Load);
}
void AssetBitmapTexture::load(GLState* pGLState) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__Load____org_andengine_opengl_util_G__State__, pGLState->unwrap());
}
void AssetBitmapTexture::unload(GLState* pGLState) {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__Unload____org_andengine_opengl_util_G__State__, pGLState->unwrap());
}
void AssetBitmapTexture::unload() {
	JNI_ENV()->CallVoidMethod(this->mUnwrapped, sMethod__Unload);
}

