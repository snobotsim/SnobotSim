
#include <assert.h>
#include <jni.h>
#include "HAL/cpp/Log.h"
#include "support/jni_util.h"

#include "com_snobot_simulator_module_wrapper_SpeedControllerWrapperJni.h"
#include "SnobotSim/SensorActuatorRegistry.h"

using namespace wpi::java;

extern "C"
{

/*
 * Class:     com_snobot_simulator_module_wrapper_SpeedControllerWrapperJni
 * Method:    getName
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_snobot_simulator_module_1wrapper_SpeedControllerWrapperJni_getName(JNIEnv * env, jclass, jint portHandle)
{

    jstring output = MakeJString(env, SensorActuatorRegistry::Get().GetSpeedControllerWrapper(portHandle)->GetName());

    return output;
}

/*
 * Class:     com_snobot_simulator_module_wrapper_SpeedControllerWrapperJni
 * Method:    getVoltagePercentage
 * Signature: ()D
 */
JNIEXPORT jdouble JNICALL Java_com_snobot_simulator_module_1wrapper_SpeedControllerWrapperJni_getVoltagePercentage(JNIEnv *, jclass, jint portHandle)
{
    return 0;
}

}
