#include "src/Wrapper.h"

Wrapper::Wrapper() {
	/* Intentionally empty. */
}

jobject Wrapper::unwrap() {
	return this->mUnwrapped;
}
