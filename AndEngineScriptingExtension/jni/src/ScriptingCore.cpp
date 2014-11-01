#include "src/ScriptingCore.h"
#include "src/org/andengine/entity/S_Entity.h"

/* The class of the global object. */
static JSClass global_class = { "global", JSCLASS_GLOBAL_FLAGS, JS_PropertyStub, JS_PropertyStub, JS_PropertyStub, JS_StrictPropertyStub, JS_EnumerateStub, JS_ResolveStub, JS_ConvertStub, JS_FinalizeStub, JSCLASS_NO_OPTIONAL_MEMBERS };

/* The error reporter callback. */
void reportError(JSContext* pJSContext, const char* pMessage, JSErrorReport* pJSErrorReport) {
	LOG_D("%s:%u:%s\n", pJSErrorReport->filename ? pJSErrorReport->filename : "<no filename>", (unsigned int) pJSErrorReport->lineno, pMessage);
}

ScriptingCore::ScriptingCore() {
	/* Create the JSRuntime. */
	this->mJSRuntime = JS_NewRuntime(8L * 1024L * 1024L);
	if (this->mJSRuntime == NULL) {
		LOG_D("Could not create JSRuntime!");
	}

	/* Create the JSContext. */
	this->mJSContext = JS_NewContext(this->mJSRuntime, 8192);
	if (this->mJSContext == NULL) {
		LOG_D("Could not create JSContext!");
	}

	/* Set flags. */
	JS_SetOptions(this->mJSContext, JSOPTION_VAROBJFIX);
	JS_SetVersion(this->mJSContext, JSVERSION_LATEST);
	JS_SetErrorReporter(this->mJSContext, reportError);

	/* Create the global object in a new compartment. */
	this->mGlobal = JS_NewCompartmentAndGlobalObject(this->mJSContext, &global_class, NULL);
	if (this->mGlobal == NULL) {
		LOG_D("Could not create global!");
	}

	if (!JS_InitStandardClasses(this->mJSContext, this->mGlobal)) {
		LOG_D("Could not initialize standard classes!");
	}

	/* Create the AndEngine namespace. */
	JSObject* andengineNamespace = JS_NewObject(this->mJSContext, NULL, NULL, NULL);
	jsval andengineNamespaceVal = OBJECT_TO_JSVAL(andengineNamespace);
	JS_SetProperty(this->mJSContext, this->mGlobal, "andengine", &andengineNamespaceVal);

	/* Register AndEngine classes. */
	S_Entity::jsCreateClass(this->mJSContext, andengineNamespace, "Entity");
//	S_CCSize::jsCreateClass(this->mJSContext, andengineNamespace, "Size");
//	S_CCRect::jsCreateClass(this->mJSContext, andengineNamespace, "Rect");

	/* Register some global functions. */
//	JS_DefineFunction(this->mJSContext, andengineNamespace, "log", ScriptingCore::log, 0, JSPROP_READONLY | JSPROP_PERMANENT);
//	JS_DefineFunction(this->mJSContext, andengineNamespace, "forceGC", ScriptingCore::forceGC, 0, JSPROP_READONLY | JSPROP_PERMANENT);
}

ScriptingCore::~ScriptingCore() {
	/* Cleanup. */
	JS_DestroyContext(this->mJSContext);
	JS_DestroyRuntime(this->mJSRuntime);
	JS_ShutDown();
}

JSContext* ScriptingCore::getJSContext() {
	return this->mJSContext;
}

const char* ScriptingCore::getJavaScriptVMVersion() {
	return JS_GetImplementationVersion();
}

bool ScriptingCore::runScript(const char* pScript) {
	LOG_D("##############################");
	LOG_D("runScript");
	LOG_D("##############################");
	LOG_D(pScript);
	LOG_D("##############################");

	const char* filename = NULL;
	int lineno = 0;  

	jsval rval;
	JSBool success = JS_EvaluateScript(this->mJSContext, this->mGlobal, pScript, strlen(pScript), filename, lineno, &rval);  

	if (JS_TRUE == success) {
		LOG_D("Success!");
	} else {
		LOG_D("Fail!");
	}

	LOG_D("##############################");
	LOG_D("runScript done.");
	LOG_D("##############################");

	return JS_TRUE == success;
}
