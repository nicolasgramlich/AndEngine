#ifndef Camera_H
#define Camera_H

#include <memory>
#include <jni.h>
#include "src/AndEngineScriptingExtension.h"
#include "src/Wrapper.h"
#include "src/org/andengine/engine/handler/IUpdateHandler.h"

extern "C" {
	JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_engine_camera_CameraProxy_nativeInitClass(JNIEnv*, jclass);
}

class Camera : public Wrapper, public IUpdateHandler {

	public:
		Camera(jobject);
		virtual jobject unwrap();
		Camera(jfloat, jfloat, jfloat, jfloat);
		Camera();
		virtual jfloat getWidth();
		virtual jfloat getHeight();
		virtual jfloat getRotation();
		virtual void setRotation(jfloat);

	protected:

	private:

};
#endif

