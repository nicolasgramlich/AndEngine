#include <jni.h>

extern "C" {

	// ===========================================================
	// org.anddev.andengine.opengl.util.BufferUtils
	// ===========================================================
	
	JNIEXPORT void JNICALL Java_org_anddev_andengine_opengl_util_BufferUtils_jniPut(JNIEnv *, jclass, jobject, jfloatArray, jint, jint);
}