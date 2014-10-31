#ifndef Transformation_H
#define Transformation_H

#include <memory>
#include <jni.h>
#include "src/AndEngineScriptingExtension.h"
#include "src/Wrapper.h"

extern "C" {
	JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_util_adt_transformation_TransformationProxy_nativeInitClass(JNIEnv*, jclass);
}

class Transformation : public Wrapper {

	public:
		Transformation(jobject);
		virtual jobject unwrap();
		Transformation();

	protected:

	private:

};
#endif

