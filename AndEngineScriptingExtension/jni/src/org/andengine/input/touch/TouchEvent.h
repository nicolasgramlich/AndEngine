#ifndef TouchEvent_H
#define TouchEvent_H

#include <memory>
#include <jni.h>
#include "src/AndEngineScriptingExtension.h"
#include "src/Wrapper.h"

extern "C" {
	JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_input_touch_TouchEventProxy_nativeInitClass(JNIEnv*, jclass);
}

class TouchEvent : public Wrapper {

	public:
		TouchEvent(jobject);
		virtual jobject unwrap();
		TouchEvent();
		virtual jfloat getY();
		virtual jfloat getX();

	protected:

	private:

};
#endif

