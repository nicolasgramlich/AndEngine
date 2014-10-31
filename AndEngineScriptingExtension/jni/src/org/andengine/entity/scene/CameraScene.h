#ifndef CameraScene_H
#define CameraScene_H

#include <memory>
#include <jni.h>
#include "src/AndEngineScriptingExtension.h"
#include "src/org/andengine/entity/scene/Scene.h"
#include "src/org/andengine/engine/camera/Camera.h"
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
	JNIEXPORT void JNICALL Java_org_andengine_extension_scripting_entity_scene_CameraSceneProxy_nativeInitClass(JNIEnv*, jclass);
	JNIEXPORT jboolean JNICALL Java_org_andengine_extension_scripting_entity_scene_CameraSceneProxy_nativeOnAttached(JNIEnv*, jobject, jlong);
	JNIEXPORT jboolean JNICALL Java_org_andengine_extension_scripting_entity_scene_CameraSceneProxy_nativeOnDetached(JNIEnv*, jobject, jlong);
}

class CameraScene : public Scene {

	public:
		CameraScene(jobject);
		virtual jobject unwrap();
		CameraScene(Camera*);
		CameraScene();
		virtual jfloat getY();
		virtual jfloat getX();
		virtual void setColor(jfloat, jfloat, jfloat);
		virtual void setColor(Color*);
		virtual void setColor(jfloat, jfloat, jfloat, jfloat);
		virtual void setScale(jfloat, jfloat);
		virtual void setScale(jfloat);
		virtual jfloat getRotation();
		virtual void setRotation(jfloat);
		virtual void attachChild(IEntity*);
		virtual jboolean attachChild(IEntity*, jint);
		virtual void setX(jfloat);
		virtual void setY(jfloat);
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

