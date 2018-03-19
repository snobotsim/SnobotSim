
#include <jni.h>

#include <cassert>

#include "com_ctre_phoenix_CTRLoggerJNI.h"

/*
 * Class:     com_ctre_phoenix_CTRLoggerJNI
 * Method:    JNI_Logger_Log
 * Signature: (ILjava/lang/String;Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_ctre_phoenix_CTRLoggerJNI_JNI_1Logger_1Log(JNIEnv*, jclass, jint, jstring, jstring)
{
    return 0;
}
