#include "src/AndEngineScriptingExtension.h"

// ===========================================================
// org.andengine.extension.scripting.AndEngineScriptingExtension
// ===========================================================

static Context* sContext;
static JavaVM* sJavaVM;
static JNIEnv* sJNIEnv;
static Engine* sEngine;
static ScriptingCore* sScriptingCore;

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* pJavaVM, void* pReserved) {
	sJavaVM = pJavaVM;
 
	return JNI_VERSION_1_4;
}

JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_AndEngineScriptingExtension_nativeInitClass(JNIEnv* pJNIEnv, jclass pJClass) {
	sJNIEnv = pJNIEnv;
}

JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_AndEngineScriptingExtension_nativeInit(JNIEnv* pJNIEnv, jclass pJClass, jobject pContext, jobject pEngine) {
	sContext = new Context(pContext);
	sEngine = new Engine(pEngine);
   	sScriptingCore = new ScriptingCore();
}

JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_AndEngineScriptingExtension_nativeAttachCurrentThread(JNIEnv* pJNIEnv, jclass pJClass) {
	// TODO
}

JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_AndEngineScriptingExtension_nativeDetachCurrentThread(JNIEnv* pJNIEnv, jclass pJClass) {
	// TODO
}

JNIEXPORT jstring JNICALL Java_org_andengine_extension_scripting_AndEngineScriptingExtension_getJavaScriptVMVersion(JNIEnv* pJNIEnv, jclass pJClass) {
	const char* version = sScriptingCore->getJavaScriptVMVersion();

	return pJNIEnv->NewStringUTF(version);
}

JNIEXPORT jint JNICALL Java_org_andengine_extension_scripting_AndEngineScriptingExtension_runScript(JNIEnv* pJNIEnv, jclass pJClass, jstring pScript) {
	const char* script = JNI_ENV()->GetStringUTFChars(pScript, 0);

	sScriptingCore->runScript(script);
}

JavaVM* JAVA_VM() {
	return sJavaVM;
}

JNIEnv* JNI_ENV() {
	return sJNIEnv;
}

Context* getContext() {
	return sContext;
}

Engine* getEngine() {
	return sEngine;
}

ScriptingCore* getScriptingCore() {
	return sScriptingCore;
}

JSContext* getJSContext() {
	return sScriptingCore->getJSContext();
}
