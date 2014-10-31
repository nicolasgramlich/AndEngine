#ifndef IEntityMatcher_H
#define IEntityMatcher_H

#include <memory>
#include <jni.h>
#include "src/org/andengine/util/IMatcher.h"

class IEntityMatcher : public IMatcher {

	public:
		virtual ~IEntityMatcher() { };
		virtual jobject unwrap() = 0;

};
#endif

