#ifndef ITextureRegion_H
#define ITextureRegion_H

#include <memory>
#include <jni.h>

class ITextureRegion {

	public:
		virtual ~ITextureRegion() { };
		virtual jobject unwrap() = 0;
		virtual jfloat getWidth() = 0;
		virtual jfloat getHeight() = 0;

};
#endif

