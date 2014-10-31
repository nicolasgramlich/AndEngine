#ifndef IOnSceneTouchListener_H
#define IOnSceneTouchListener_H

#include <memory>
#include <jni.h>

class IOnSceneTouchListener {

	public:
		virtual ~IOnSceneTouchListener() { };
		virtual jobject unwrap() = 0;

};
#endif

