#include <jni.h>
#include <GLES2/gl2.h>

// ===========================================================
// org.andengine.opengl.GLES20Fix
// ===========================================================

void Java_org_andengine_opengl_GLES20Fix_glVertexAttribPointer (JNIEnv *env, jclass c, jint index, jint size, jint type, jboolean normalized, jint stride, jint offset) {
	glVertexAttribPointer(index, size, type, normalized, stride, (void*) offset);
}

void Java_org_andengine_opengl_GLES20Fix_glDrawElements (JNIEnv *env, jclass c, jint mode, jint count, jint type, jint offset) {
	glDrawElements(mode, count, type, (void*) offset);
}