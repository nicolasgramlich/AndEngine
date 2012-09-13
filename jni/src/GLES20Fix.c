#include <stdlib.h>
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

JNIEXPORT jstring JNICALL Java_org_andengine_opengl_GLES20Fix_glGetShaderInfoLog(JNIEnv *env, jclass c, jint shader) {
	int charBufferLength;

	glGetShaderiv(shader , GL_INFO_LOG_LENGTH, &charBufferLength);
	//some user reported that sometimes glGetShaderiv returns the correct value
	//if it is the case, use that instead of the default 4096
	if(charBufferLength == 0)
		charBufferLength = 4096;

	char* charBuffer = (char *) malloc(charBufferLength);
	int logLenth;

	glGetShaderInfoLog(shader, charBufferLength, &logLenth, charBuffer);

	char* infoLog = (char *) malloc(logLenth - 1);
	memcpy(infoLog, charBuffer, logLenth - 1);
	jstring logString = (*env) -> NewStringUTF(env, infoLog);

	free(charBuffer);
	free(infoLog);

	return logString;
}
