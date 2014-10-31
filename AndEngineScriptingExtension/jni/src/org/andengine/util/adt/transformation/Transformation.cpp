#include <cstdlib>
#include "src/org/andengine/util/adt/transformation/Transformation.h"

static jclass sTransformationClass;
static jmethodID sConstructor;

JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_util_adt_transformation_TransformationProxy_nativeInitClass(JNIEnv* pJNIEnv, jclass pJClass) {
	sTransformationClass = (jclass)JNI_ENV()->NewGlobalRef(pJClass);
	sConstructor = JNI_ENV()->GetMethodID(sTransformationClass, "<init>", "(J)V");
}

Transformation::Transformation(jobject pTransformationProxy) {
	this->mUnwrapped = pTransformationProxy;
}
jobject Transformation::unwrap() {
	return this->mUnwrapped;
}
Transformation::Transformation() {
	this->mUnwrapped = JNI_ENV()->NewObject(sTransformationClass, sConstructor, (jlong)this);
}

