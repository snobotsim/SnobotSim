
#include <assert.h>
#include <jni.h>
#include "support/jni_util.h"

#include "com_snobot_simulator_module_wrapper_SpeedControllerWrapperJni.h"
#include "SnobotSim/SensorActuatorRegistry.h"
#include "SnobotSim/GetSensorActuatorHelper.h"

using namespace wpi::java;

extern "C"
{

/*
 * Class:     com_snobot_simulator_module_wrapper_SpeedControllerWrapperJni
 * Method:    setName
 * Signature: (ILjava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_module_1wrapper_SpeedControllerWrapperJni_setName
  (JNIEnv * env, jclass, jint aPortHandle, jstring aName)
{
    std::shared_ptr<SpeedControllerWrapper> wrapper = GetSensorActuatorHelper::GetSpeedControllerWrapper(aPortHandle);
    if(wrapper)
    {
        wrapper->SetName(env->GetStringUTFChars(aName, NULL));
    }
}

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
 * Method:    getWantsHidden
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_com_snobot_simulator_module_1wrapper_SpeedControllerWrapperJni_getWantsHidden(JNIEnv *, jclass, jint portHandle)
{
    return SensorActuatorRegistry::Get().GetSpeedControllerWrapper(portHandle)->WantsHidden();
}

/*
 * Class:     com_snobot_simulator_module_wrapper_SpeedControllerWrapperJni
 * Method:    getVoltagePercentage
 * Signature: ()D
 */
JNIEXPORT jdouble JNICALL Java_com_snobot_simulator_module_1wrapper_SpeedControllerWrapperJni_getVoltagePercentage(JNIEnv *, jclass, jint portHandle)
{
    return SensorActuatorRegistry::Get().GetSpeedControllerWrapper(portHandle)->GetVoltagePercentage();
}

/*
 * Class:     com_snobot_simulator_module_wrapper_SpeedControllerWrapperJni
 * Method:    updateAllSpeedControllers
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_module_1wrapper_SpeedControllerWrapperJni_updateAllSpeedControllers(JNIEnv *, jclass, jdouble aUpdatePeriod)
{
    std::map<int, std::shared_ptr<SpeedControllerWrapper>>& speedControllers = SensorActuatorRegistry::Get().GetSpeedControllerWrapperMap();

    std::map<int, std::shared_ptr<SpeedControllerWrapper>>::iterator iter;

    for (iter = speedControllers.begin(); iter != speedControllers.end(); ++iter)
    {
        iter->second->Update(aUpdatePeriod);
    }
}

/*
 * Class:     com_snobot_simulator_module_wrapper_SpeedControllerWrapperJni
 * Method:    getPortList
 * Signature: ()[I
 */
JNIEXPORT jintArray JNICALL Java_com_snobot_simulator_module_1wrapper_SpeedControllerWrapperJni_getPortList(JNIEnv * env, jclass)
{
    const std::map<int, std::shared_ptr<SpeedControllerWrapper>>& speedControllers = SensorActuatorRegistry::Get().GetSpeedControllerWrapperMap();

    jintArray output = env->NewIntArray(speedControllers.size());

    jint values[30];

    std::map<int, std::shared_ptr<SpeedControllerWrapper>>::const_iterator iter = speedControllers.begin();

    int ctr = 0;
    for (; iter != speedControllers.end(); ++iter)
    {
        values[ctr++] = iter->first;
    }

    env->SetIntArrayRegion(output, 0, speedControllers.size(), values);

    return output;
}


}
