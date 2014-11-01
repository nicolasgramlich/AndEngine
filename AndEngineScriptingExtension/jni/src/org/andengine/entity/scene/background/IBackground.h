#ifndef IBackground_H
#define IBackground_H

#include <memory>
#include <jni.h>
#include "src/org/andengine/engine/handler/IDrawHandler.h"
#include "src/org/andengine/engine/handler/IUpdateHandler.h"
#include "src/org/andengine/util/color/Color.h"

class IBackground : public IDrawHandler, public IUpdateHandler {

	public:
		virtual ~IBackground() { };
		virtual jobject unwrap() = 0;
		virtual void setColor(Color*) = 0;
		virtual void setColor(jfloat, jfloat, jfloat, jfloat) = 0;
		virtual void setColor(jfloat, jfloat, jfloat) = 0;

};
#endif

