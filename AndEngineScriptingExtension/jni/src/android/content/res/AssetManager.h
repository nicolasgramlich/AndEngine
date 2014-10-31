#ifndef AssetManager_H
#define AssetManager_H

#include <jni.h>
#include "src/Wrapper.h"

extern "C" {
	// ===========================================================
	// org.andengine.extension.scripting.AssetManagerProxy
	// ===========================================================

	JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_AssetManagerProxy_nativeInitClass(JNIEnv*, jclass);
}

class AssetManager : public Wrapper {
	public:
		/* Constructors */
		AssetManager(jobject);
};

#endif
