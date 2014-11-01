#include <cstdlib>
#include "src/org/andengine/engine/options/ScreenOrientation.h"

static jclass sScreenOrientationClass;
ScreenOrientation* ScreenOrientation::LANDSCAPE_FIXED = NULL;
ScreenOrientation* ScreenOrientation::LANDSCAPE_SENSOR = NULL;
ScreenOrientation* ScreenOrientation::PORTRAIT_FIXED = NULL;
ScreenOrientation* ScreenOrientation::PORTRAIT_SENSOR = NULL;

JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_engine_options_ScreenOrientationProxy_nativeInitClass(JNIEnv* pJNIEnv, jclass pJClass) {
	sScreenOrientationClass = (jclass)JNI_ENV()->NewGlobalRef(JNI_ENV()->FindClass("org/andengine/engine/options/ScreenOrientation"));
	jfieldID ScreenOrientation_LANDSCAPE_FIXED_ID = JNI_ENV()->GetStaticFieldID(sScreenOrientationClass, "LANDSCAPE_FIXED", "Lorg/andengine/engine/options/ScreenOrientation;");
	ScreenOrientation::LANDSCAPE_FIXED = new ScreenOrientation(JNI_ENV()->GetStaticObjectField(sScreenOrientationClass, ScreenOrientation_LANDSCAPE_FIXED_ID));
	jfieldID ScreenOrientation_LANDSCAPE_SENSOR_ID = JNI_ENV()->GetStaticFieldID(sScreenOrientationClass, "LANDSCAPE_SENSOR", "Lorg/andengine/engine/options/ScreenOrientation;");
	ScreenOrientation::LANDSCAPE_SENSOR = new ScreenOrientation(JNI_ENV()->GetStaticObjectField(sScreenOrientationClass, ScreenOrientation_LANDSCAPE_SENSOR_ID));
	jfieldID ScreenOrientation_PORTRAIT_FIXED_ID = JNI_ENV()->GetStaticFieldID(sScreenOrientationClass, "PORTRAIT_FIXED", "Lorg/andengine/engine/options/ScreenOrientation;");
	ScreenOrientation::PORTRAIT_FIXED = new ScreenOrientation(JNI_ENV()->GetStaticObjectField(sScreenOrientationClass, ScreenOrientation_PORTRAIT_FIXED_ID));
	jfieldID ScreenOrientation_PORTRAIT_SENSOR_ID = JNI_ENV()->GetStaticFieldID(sScreenOrientationClass, "PORTRAIT_SENSOR", "Lorg/andengine/engine/options/ScreenOrientation;");
	ScreenOrientation::PORTRAIT_SENSOR = new ScreenOrientation(JNI_ENV()->GetStaticObjectField(sScreenOrientationClass, ScreenOrientation_PORTRAIT_SENSOR_ID));
}

ScreenOrientation::ScreenOrientation(jobject pScreenOrientationProxy) {
	this->mUnwrapped = pScreenOrientationProxy;
}

