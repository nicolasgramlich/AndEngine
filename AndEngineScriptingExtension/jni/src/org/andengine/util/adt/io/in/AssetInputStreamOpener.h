#ifndef AssetInputStreamOpener_H
#define AssetInputStreamOpener_H

#include <memory>
#include <jni.h>
#include "src/AndEngineScriptingExtension.h"
#include "src/Wrapper.h"
#include "src/org/andengine/util/adt/io/in/IInputStreamOpener.h"
#include "src/android/content/res/AssetManager.h"

extern "C" {
	JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_util_adt_io_in_AssetInputStreamOpenerProxy_nativeInitClass(JNIEnv*, jclass);
}

class AssetInputStreamOpener : public Wrapper, public IInputStreamOpener {

	public:
		AssetInputStreamOpener(jobject);
		virtual jobject unwrap();
		AssetInputStreamOpener(AssetManager*, jstring);
		AssetInputStreamOpener();

	protected:

	private:

};
#endif

