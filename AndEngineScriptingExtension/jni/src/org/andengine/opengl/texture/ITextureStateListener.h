#ifndef ITextureStateListener_H
#define ITextureStateListener_H

#include <memory>
#include <jni.h>

class ITextureStateListener {

	public:
		virtual ~ITextureStateListener() { };
		virtual jobject unwrap() = 0;

};
#endif

