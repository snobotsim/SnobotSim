
#include <assert.h>
#include <jni.h>
#include "HAL/cpp/Log.h"
#include "support/jni_util.h"

#include "com_snobot_simulator_module_wrapper_EncoderWrapperJni.h"
#include "SnobotSim/SensorActuatorRegistry.h"

using namespace wpi::java;

extern "C"
{

/*
 * Class:     com_snobot_simulator_module_wrapper_EncoderWrapperJni
 * Method:    getName
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_snobot_simulator_module_1wrapper_EncoderWrapperJni_getName(JNIEnv * env, jclass, jint portHandle)
{

//    jstring output = MakeJString(env, SensorActuatorRegistry::Get().GetEncoderWrapper(portHandle)->GetName());
    jstring output = MakeJString(env, "");

    return output;
}

/*
 * Class:     com_snobot_simulator_module_wrapper_EncoderWrapperJni
 * Method:    getVoltagePercentage
 * Signature: ()D
 */
JNIEXPORT jdouble JNICALL Java_com_snobot_simulator_module_1wrapper_EncoderWrapperJni_getDistance(JNIEnv *, jclass, jint portHandle)
{
//    return SensorActuatorRegistry::Get().GetEncoderWrapper(portHandle)->GetVoltagePercentage();
    return 0;
}

/*
 * Class:     com_snobot_simulator_module_wrapper_EncoderWrapperJni
 * Method:    getPortList
 * Signature: ()[I
 */
JNIEXPORT jintArray JNICALL Java_com_snobot_simulator_module_1wrapper_EncoderWrapperJni_getPortList(JNIEnv * env, jclass)
{
//    const std::map<int, std::shared_ptr<EncoderWrapper>>& encoders = SensorActuatorRegistry::Get().GetEncoderWrapperMap();
//
//    jintArray output = env->NewIntArray(speedControllers.size());
//
//    jint values[30];
//
//    std::map<int, std::shared_ptr<EncoderWrapper>>::const_iterator iter = speedControllers.begin();
//
//    int ctr = 0;
//    for (; iter != speedControllers.end(); ++iter)
//    {
//        values[ctr++] = iter->first;
//    }
//
//    env->SetIntArrayRegion(output, 0, speedControllers.size(), values);
//
//    return output;

    jintArray output = env->NewIntArray(0);
    return output;
}


}
