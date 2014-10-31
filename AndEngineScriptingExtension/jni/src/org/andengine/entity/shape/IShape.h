#ifndef IShape_H
#define IShape_H

#include <memory>
#include <jni.h>
#include "src/org/andengine/entity/IEntity.h"
#include "src/org/andengine/entity/scene/ITouchArea.h"
#include "src/org/andengine/opengl/vbo/VertexBufferObjectManager.h"
#include "src/org/andengine/util/color/Color.h"
#include "src/org/andengine/entity/IEntity.h"
#include "src/org/andengine/entity/IEntity.h"
#include "src/org/andengine/util/color/Color.h"
#include "src/org/andengine/entity/IEntityMatcher.h"
#include "src/org/andengine/entity/IEntity.h"
#include "src/org/andengine/entity/IEntity.h"
#include "src/org/andengine/entity/IEntityMatcher.h"
#include "src/org/andengine/entity/IEntity.h"
#include "src/org/andengine/entity/IEntity.h"
#include "src/org/andengine/entity/IEntity.h"
#include "src/org/andengine/entity/IEntity.h"
#include "src/org/andengine/input/touch/TouchEvent.h"

class IShape : public IEntity, public ITouchArea {

	public:
		virtual ~IShape() { };
		virtual jobject unwrap() = 0;
		virtual VertexBufferObjectManager* getVertexBufferObjectManager() = 0;
		virtual jfloat getY() = 0;
		virtual jfloat getX() = 0;
		virtual void setColor(Color*) = 0;
		virtual void setColor(jfloat, jfloat, jfloat, jfloat) = 0;
		virtual void setColor(jfloat, jfloat, jfloat) = 0;
		virtual void setScale(jfloat, jfloat) = 0;
		virtual void setScale(jfloat) = 0;
		virtual jfloat getRotation() = 0;
		virtual void setRotation(jfloat) = 0;
		virtual void attachChild(IEntity*) = 0;
		virtual jboolean attachChild(IEntity*, jint) = 0;
		virtual void setX(jfloat) = 0;
		virtual void setY(jfloat) = 0;
		virtual jfloat getScaleX() = 0;
		virtual jfloat getScaleY() = 0;
		virtual jfloat getSkewX() = 0;
		virtual jfloat getSkewY() = 0;
		virtual void setSkew(jfloat, jfloat) = 0;
		virtual void setSkew(jfloat) = 0;
		virtual jfloat getRed() = 0;
		virtual jfloat getGreen() = 0;
		virtual jfloat getBlue() = 0;
		virtual jfloat getAlpha() = 0;
		virtual Color* getColor() = 0;
		virtual void setRed(jfloat) = 0;
		virtual void setGreen(jfloat) = 0;
		virtual void setBlue(jfloat) = 0;
		virtual void setAlpha(jfloat) = 0;
		virtual jint getChildCount() = 0;
		virtual IEntity* getChild(IEntityMatcher*) = 0;
		virtual IEntity* getChild(jint) = 0;
		virtual jboolean detachSelf() = 0;
		virtual IEntity* detachChild(IEntityMatcher*) = 0;
		virtual jboolean detachChild(IEntity*) = 0;
		virtual jboolean onAttached() = 0;virtual jboolean swapChildren(jint, jint) = 0;
		virtual jboolean swapChildren(IEntity*, IEntity*) = 0;
		virtual jboolean onDetached() = 0;virtual jboolean onAreaTouched(TouchEvent*, jfloat, jfloat) = 0;
};
#endif

