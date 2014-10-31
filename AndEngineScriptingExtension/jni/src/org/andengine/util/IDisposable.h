#ifndef IDisposable_H
#define IDisposable_H

#include <memory>
#include <jni.h>

class IDisposable {

	public:
		virtual ~IDisposable() { };
		virtual jobject unwrap() = 0;

};
#endif

