#ifndef EngineOptions_H
#define EngineOptions_H

#include <memory>
#include <jni.h>
#include "src/AndEngineScriptingExtension.h"
#include "src/Wrapper.h"
#include "src/org/andengine/engine/options/ScreenOrientation.h"
#include "src/org/andengine/engine/options/resolutionpolicy/IResolutionPolicy.h"
#include "src/org/andengine/engine/camera/Camera.h"

extern "C" {
	JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_engine_options_EngineOptionsProxy_nativeInitClass(JNIEnv*, jclass);
}

class EngineOptions : public Wrapper {

	public:
		EngineOptions(jobject);
		virtual jobject unwrap();
		EngineOptions(jboolean, ScreenOrientation*, IResolutionPolicy*, Camera*);
		EngineOptions();

	protected:

	private:

};
#endif

