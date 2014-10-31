#include "BufferUtils.h"
#include <stdio.h>
#include <stdlib.h>

// ===========================================================
// org.andengine.opengl.util.BufferUtils
// ===========================================================
	
JNIEXPORT void JNICALL Java_org_andengine_opengl_util_BufferUtils_jniPut(JNIEnv *env, jclass, jobject pBuffer, jfloatArray pData, jint pLength, jint pOffset) {
	unsigned char* bufferAddress = (unsigned char*)env->GetDirectBufferAddress(pBuffer);
	float* dataAddress = (float*)env->GetPrimitiveArrayCritical(pData, 0);
	
	memcpy(bufferAddress, dataAddress + pOffset, pLength << 2);
	env->ReleasePrimitiveArrayCritical(pData, dataAddress, 0);
}

JNIEXPORT jobject JNICALL Java_org_andengine_opengl_util_BufferUtils_jniAllocateDirect(JNIEnv *env, jclass, jint pCapacity) {
	jbyte* bytebuffer = (jbyte*) malloc(sizeof(jbyte) * pCapacity);
	return env->NewDirectByteBuffer(bytebuffer, pCapacity);
}

JNIEXPORT void JNICALL Java_org_andengine_opengl_util_BufferUtils_jniFreeDirect(JNIEnv* env, jclass, jobject pBuffer) {
	unsigned char* bufferAddress = (unsigned char*)env->GetDirectBufferAddress(pBuffer);
	free(bufferAddress);
}
