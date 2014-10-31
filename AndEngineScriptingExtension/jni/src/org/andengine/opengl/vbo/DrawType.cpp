#include <cstdlib>
#include "src/org/andengine/opengl/vbo/DrawType.h"

static jclass sDrawTypeClass;
DrawType* DrawType::STATIC = NULL;
DrawType* DrawType::DYNAMIC = NULL;
DrawType* DrawType::STREAM = NULL;

JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_opengl_vbo_DrawTypeProxy_nativeInitClass(JNIEnv* pJNIEnv, jclass pJClass) {
	sDrawTypeClass = (jclass)JNI_ENV()->NewGlobalRef(JNI_ENV()->FindClass("org/andengine/opengl/vbo/DrawType"));
	jfieldID DrawType_STATIC_ID = JNI_ENV()->GetStaticFieldID(sDrawTypeClass, "STATIC", "Lorg/andengine/opengl/vbo/DrawType;");
	DrawType::STATIC = new DrawType(JNI_ENV()->GetStaticObjectField(sDrawTypeClass, DrawType_STATIC_ID));
	jfieldID DrawType_DYNAMIC_ID = JNI_ENV()->GetStaticFieldID(sDrawTypeClass, "DYNAMIC", "Lorg/andengine/opengl/vbo/DrawType;");
	DrawType::DYNAMIC = new DrawType(JNI_ENV()->GetStaticObjectField(sDrawTypeClass, DrawType_DYNAMIC_ID));
	jfieldID DrawType_STREAM_ID = JNI_ENV()->GetStaticFieldID(sDrawTypeClass, "STREAM", "Lorg/andengine/opengl/vbo/DrawType;");
	DrawType::STREAM = new DrawType(JNI_ENV()->GetStaticObjectField(sDrawTypeClass, DrawType_STREAM_ID));
}

DrawType::DrawType(jobject pDrawTypeProxy) {
	this->mUnwrapped = pDrawTypeProxy;
}

