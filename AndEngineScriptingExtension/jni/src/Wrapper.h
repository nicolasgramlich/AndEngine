#ifndef Wrapper_H
#define Wrapper_H

#include <jni.h>

class Wrapper {
	protected:
		jobject mUnwrapped;

		/* Constructors */ 
		Wrapper();

	public:
		/* Unwrapper. */
		virtual jobject unwrap();
};

#endif
