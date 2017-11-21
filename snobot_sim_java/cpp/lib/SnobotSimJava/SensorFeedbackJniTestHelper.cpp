//
//#include <assert.h>
//#include <jni.h>
//#include "support/jni_util.h"
//
//#include <chrono>
//
//#include "com_snobot_simulator_jni_SensorFeedbackJni.h"
//#include "MockData/CANData.h"
//
//
//extern "C"
//{
//
//
///*
// * Class:     com_snobot_simulator_jni_SensorFeedbackJniTestHelper
// * Method:    commandCan
// * Signature: (Ljava/nio/ByteBuffer;I)V
// */
//JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_SensorFeedbackJniTestHelper_commandCan
//  (JNIEnv * env, jclass, jint messageId, jobject data, jint size)
//{
//    uint8_t *dataPtr = nullptr;
//    if (data != 0) {
//        dataPtr = (uint8_t *)env->GetDirectBufferAddress(data);
//    }
//
//    int32_t status = 0;
//    HAL_CAN_SendMessage(messageId, dataPtr, size, 0, &status);
//}
//
//
//} // extern c
