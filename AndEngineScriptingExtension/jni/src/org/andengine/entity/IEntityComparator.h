#ifndef IEntityComparator_H
#define IEntityComparator_H

#include <memory>
#include <jni.h>
#include "src/java/util/Comparator.h"

class IEntityComparator : public Comparator {

	public:
		virtual ~IEntityComparator() { };
		virtual jobject unwrap() = 0;

};
#endif

