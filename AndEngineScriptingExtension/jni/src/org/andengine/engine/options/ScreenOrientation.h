#ifndef ScreenOrientation_H
#define ScreenOrientation_H

#include <jni.h>
#include "src/AndEngineScriptingExtension.h"
#include "src/Wrapper.h"

extern "C" {
	JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_engine_options_ScreenOrientationProxy_nativeInitClass(JNIEnv*, jclass);
}

class ScreenOrientation : public Wrapper {

	public:
		ScreenOrientation(jobject);
		static ScreenOrientation* LANDSCAPE_FIXED;
		static ScreenOrientation* LANDSCAPE_SENSOR;
		static ScreenOrientation* PORTRAIT_FIXED;
		static ScreenOrientation* PORTRAIT_SENSOR;

};
#endif

