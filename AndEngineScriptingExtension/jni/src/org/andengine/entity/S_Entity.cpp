#include <cstdlib>
#include "src/org/andengine/entity/S_Entity.h"

JSClass* S_Entity::sJSClass = NULL;
JSObject* S_Entity::sJSObject = NULL;

S_Entity::S_Entity(JSObject* pJSObject) : Entity() {
	this->mJSObject = pJSObject;
}
S_Entity::S_Entity(JSObject* pJSObject, float pX, float pY) : Entity(pX, pY) {
	this->mJSObject = pJSObject;
}

jboolean S_Entity::onAttached() {
	JSContext* jsContext = getJSContext();
	JSBool found;

	/* Check if JS function exists. */
	JS_HasProperty(jsContext, this->mJSObject, "onAttached", &found);
	if(JS_TRUE == found) {
		/* Get JS function. */
		jsval fval;
		JS_GetProperty(jsContext, this->mJSObject, "onAttached", &fval);

		/* Call JS function and get return value. */
		jsval rval;
		JS_CallFunctionValue(jsContext, this->mJSObject, fval, 0, 0, &rval);

		if(JSVAL_IS_BOOLEAN(rval)) {
			bool result = JS_TRUE == JSVAL_TO_BOOLEAN(rval);
			return result;
		} else {
			// TODO throw Exception?
			return false;
		}
	} else {
		return false;
	}
}

jboolean S_Entity::onDetached() {
	JSContext* jsContext = getJSContext();
	JSBool found;

	/* Check if JS function exists. */
	JS_HasProperty(jsContext, this->mJSObject, "onDetached", &found);
	if(JS_TRUE == found) {
		/* Get JS function. */
		jsval fval;
		JS_GetProperty(jsContext, this->mJSObject, "onDetached", &fval);

		/* Call JS function and get return value. */
		jsval rval;
		JS_CallFunctionValue(jsContext, this->mJSObject, fval, 0, 0, &rval);

		if(JSVAL_IS_BOOLEAN(rval)) {
			bool result = JS_TRUE == JSVAL_TO_BOOLEAN(rval);
			return result;
		} else {
			// TODO throw Exception?
			return false;
		}
	} else {
		return false;
	}
}
