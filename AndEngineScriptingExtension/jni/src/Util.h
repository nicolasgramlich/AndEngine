#ifndef Util_H
#define Util_H

#include <android/log.h>

#define  LOG_TAG	"AndEngineScriptingExtension"
#define  LOG_D(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#define  LOG_W(...)  __android_log_print(ANDROID_LOG_WARNING,LOG_TAG,__VA_ARGS__)
#define  LOG_E(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

#endif
