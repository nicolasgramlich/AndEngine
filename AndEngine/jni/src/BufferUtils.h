#include <jni.h>

extern "C" {
	// ===========================================================
	// org.andengine.opengl.util.BufferUtils
	// ===========================================================
	
	JNIEXPORT void JNICALL Java_org_andengine_opengl_util_BufferUtils_jniPut(JNIEnv *, jclass, jobject, jfloatArray, jint, jint);
	JNIEXPORT jobject JNICALL Java_org_andengine_opengl_util_BufferUtils_jniAllocateDirect(JNIEnv *, jclass, jint);
	JNIEXPORT void JNICALL Java_org_andengine_opengl_util_BufferUtils_jniFreeDirect(JNIEnv *, jclass, jobject);
}