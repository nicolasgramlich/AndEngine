#ifndef AndEngineScriptingExtension_H
#define AndEngineScriptingExtension_H

#include <jni.h>
#include <string>
#include <jsapi.h>
#include "src/Util.h"
#include "src/ScriptingCore.h"
#include "src/android/content/Context.h"
#include "src/android/content/res/AssetManager.h"
#include "src/org/andengine/engine/Engine.h"

extern "C" {
	// ===========================================================
	// org.andengine.extension.scripting.AndEngineScriptingExtension
	// ===========================================================

	JNIEXPORT jstring JNICALL Java_org_andengine_extension_scripting_AndEngineScriptingExtension_getJavaScriptVMVersion(JNIEnv*, jclass);
	JNIEXPORT jint JNICALL Java_org_andengine_extension_scripting_AndEngineScriptingExtension_runScript(JNIEnv*, jclass, jstring);

	JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_AndEngineScriptingExtension_nativeInitClass(JNIEnv*, jclass);
	JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_AndEngineScriptingExtension_nativeInit(JNIEnv*, jclass, jobject, jobject);

	JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_AndEngineScriptingExtension_nativeAttachCurrentThread(JNIEnv*, jclass);
	JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_AndEngineScriptingExtension_nativeDetachCurrentThread(JNIEnv*, jclass);
}

class Engine;

JavaVM* JAVA_VM();
JNIEnv* JNI_ENV();
Context* getContext();
Engine* getEngine();
ScriptingCore* getScriptingCore();
JSContext* getJSContext();

/*
std::string convertJStringToStdString(jstring pJString) {
	const char* chars = JNI_ENV()->GetStringUTFChars(pJString, NULL);
	std::string result(chars);
	JNI_ENV()->ReleaseStringUTFChars(pJString, chars); 
	return result;
}

jstring convertStdStringToJString(std::string pString) {
	const char* chars = 
	JNI_ENV()->NewStringUTF(chars);
	
}*/

#endif
