#ifndef IOnAreaTouchListener_H
#define IOnAreaTouchListener_H

#include <memory>
#include <jni.h>
#include "src/org/andengine/input/touch/TouchEvent.h"
#include "src/org/andengine/entity/scene/ITouchArea.h"

class IOnAreaTouchListener {

	public:
		virtual ~IOnAreaTouchListener() { };
		virtual jobject unwrap() = 0;
		virtual jboolean onAreaTouched(TouchEvent*, ITouchArea*, jfloat, jfloat) = 0;
};
#endif

