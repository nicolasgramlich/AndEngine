#ifndef Line_H
#define Line_H

#include <memory>
#include <jni.h>
#include "src/AndEngineScriptingExtension.h"
#include "src/org/andengine/entity/shape/Shape.h"
#include "src/org/andengine/opengl/vbo/VertexBufferObjectManager.h"
#include "src/org/andengine/opengl/vbo/VertexBufferObjectManager.h"
#include "src/org/andengine/opengl/vbo/DrawType.h"
#include "src/org/andengine/opengl/vbo/VertexBufferObjectManager.h"
#include "src/org/andengine/opengl/vbo/VertexBufferObjectManager.h"
#include "src/org/andengine/opengl/vbo/DrawType.h"
#include "src/org/andengine/entity/primitive/vbo/ILineVertexBufferObject.h"
#include "src/org/andengine/opengl/vbo/VertexBufferObjectManager.h"
#include "src/org/andengine/input/touch/TouchEvent.h"
#include "src/org/andengine/input/touch/TouchEvent.h"
#include "src/org/andengine/util/color/Color.h"
#include "src/org/andengine/entity/IEntity.h"
#include "src/org/andengine/entity/IEntity.h"
#include "src/org/andengine/util/color/Color.h"
#include "src/org/andengine/entity/IEntity.h"
#include "src/org/andengine/entity/IEntityMatcher.h"
#include "src/org/andengine/entity/IEntity.h"
#include "src/org/andengine/entity/IEntityMatcher.h"
#include "src/org/andengine/entity/IEntity.h"
#include "src/org/andengine/entity/IEntity.h"
#include "src/org/andengine/entity/IEntity.h"
#include "src/org/andengine/entity/IEntity.h"

extern "C" {
	JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_entity_primitive_LineProxy_nativeInitClass(JNIEnv*, jclass);
	JNIEXPORT jboolean JNICALL Java_org_andengine_extension_scripting_entity_primitive_LineProxy_nativeOnAreaTouched(JNIEnv*, jobject, jlong, jobject, jfloat, jfloat);
	JNIEXPORT jboolean JNICALL Java_org_andengine_extension_scripting_entity_primitive_LineProxy_nativeOnAttached(JNIEnv*, jobject, jlong);
	JNIEXPORT jboolean JNICALL Java_org_andengine_extension_scripting_entity_primitive_LineProxy_nativeOnDetached(JNIEnv*, jobject, jlong);
}

class Line : public Shape {

	public:
		Line(jobject);
		virtual jobject unwrap();
		Line(jfloat, jfloat, jfloat, jfloat, VertexBufferObjectManager*);
		Line(jfloat, jfloat, jfloat, jfloat, VertexBufferObjectManager*, DrawType*);
		Line(jfloat, jfloat, jfloat, jfloat, jfloat, VertexBufferObjectManager*);
		Line(jfloat, jfloat, jfloat, jfloat, jfloat, VertexBufferObjectManager*, DrawType*);
		Line(jfloat, jfloat, jfloat, jfloat, jfloat, ILineVertexBufferObject*);
		Line();
		virtual jfloat getY();
		virtual jfloat getX();
		virtual void setX(jfloat);
		virtual void setY(jfloat);
		virtual VertexBufferObjectManager* getVertexBufferObjectManager();
		virtual jboolean onAreaTouched(TouchEvent*, jfloat, jfloat);
		virtual void setColor(jfloat, jfloat, jfloat);
		virtual void setColor(Color*);
		virtual void setColor(jfloat, jfloat, jfloat, jfloat);
		virtual void setScale(jfloat, jfloat);
		virtual void setScale(jfloat);
		virtual jfloat getRotation();
		virtual void setRotation(jfloat);
		virtual void attachChild(IEntity*);
		virtual jboolean attachChild(IEntity*, jint);
		virtual jfloat getScaleX();
		virtual jfloat getScaleY();
		virtual jfloat getSkewX();
		virtual jfloat getSkewY();
		virtual void setSkew(jfloat, jfloat);
		virtual void setSkew(jfloat);
		virtual jfloat getRed();
		virtual jfloat getGreen();
		virtual jfloat getBlue();
		virtual jfloat getAlpha();
		virtual Color* getColor();
		virtual void setRed(jfloat);
		virtual void setGreen(jfloat);
		virtual void setBlue(jfloat);
		virtual void setAlpha(jfloat);
		virtual jint getChildCount();
		virtual IEntity* getChild(jint);
		virtual IEntity* getChild(IEntityMatcher*);
		virtual jboolean detachSelf();
		virtual IEntity* detachChild(IEntityMatcher*);
		virtual jboolean detachChild(IEntity*);
		virtual jboolean onAttached();
		virtual jboolean swapChildren(jint, jint);
		virtual jboolean swapChildren(IEntity*, IEntity*);
		virtual jboolean onDetached();

	protected:

	private:

};
#endif

