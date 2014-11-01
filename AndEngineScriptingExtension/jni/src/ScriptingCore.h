#ifndef ScriptingCore_H
#define ScriptingCore_H


#include <jni.h>
#include <jsapi.h>
#include "src/Util.h"



typedef struct {
	uint32_t flags;
	void* data;
} pointerShell_t;

typedef enum {
	kPointerTemporary = 1
} pointerShellFlags;

#define JSGET_PTRSHELL(type, cobj, jsobj) do { \
	pointerShell_t* pt = (pointerShell_t*)JS_GetPrivate(jsobj); \
	if (pt) { \
		cobj = (type*)pt->data; \
	} else { \
		cobj = NULL; \
	} \
} while (0)



class ScriptingCore {
	JSRuntime* mJSRuntime;
	JSContext* mJSContext;
	JSObject* mGlobal;

	public:
		ScriptingCore();
		~ScriptingCore();

		JSContext* getJSContext();

		bool runScript(const char*);
		const char* getJavaScriptVMVersion();		
};

#endif
