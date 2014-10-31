#ifndef ITouchArea_H
#define ITouchArea_H

#include <memory>
#include <jni.h>
#include "src/org/andengine/input/touch/TouchEvent.h"

class ITouchArea {

	public:
		virtual ~ITouchArea() { };
		virtual jobject unwrap() = 0;
		virtual jboolean onAreaTouched(TouchEvent*, jfloat, jfloat) = 0;
};
#endif

