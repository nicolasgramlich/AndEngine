#ifndef Color_H
#define Color_H

#include <memory>
#include <jni.h>
#include "src/AndEngineScriptingExtension.h"
#include "src/Wrapper.h"
#include "src/org/andengine/util/color/Color.h"

extern "C" {
	JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_util_color_ColorProxy_nativeInitClass(JNIEnv*, jclass);
}

class Color : public Wrapper {

	public:
		Color(jobject);
		virtual jobject unwrap();
		Color(Color*);
		Color(jfloat, jfloat, jfloat);
		Color(jfloat, jfloat, jfloat, jfloat);
		Color();
		virtual jfloat getRed();
		virtual jfloat getGreen();
		virtual jfloat getBlue();
		virtual jfloat getAlpha();
		virtual void setRed(jfloat);
		virtual void setGreen(jfloat);
		virtual void setBlue(jfloat);
		virtual void setAlpha(jfloat);

	protected:

	private:

};
#endif

