
#include <assert.h>
#include <jni.h>
#include "support/jni_util.h"

#include "com_snobot_simulator_jni_module_wrapper_SpeedControllerWrapperJni.h"
#include "SnobotSim/ModuleWrapper/SpeedControllerWrapper.h"
#include "SnobotSim/SensorActuatorRegistry.h"
#include "SnobotSim/GetSensorActuatorHelper.h"
#include "SnobotSim/MotorSim/StaticLoadDcMotorSim.h"
#include "SnobotSim/Logging/SnobotLogger.h"
#include "../ConversionUtils.h"

using namespace wpi::java;

extern "C"
{

/*
 * Class:     com_snobot_simulator_jni_module_wrapper_SpeedControllerWrapperJni
 * Method:    setName
 * Signature: (ILjava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_module_1wrapper_SpeedControllerWrapperJni_setName
  (JNIEnv * env, jclass, jint aPortHandle, jstring aName)
{
    std::shared_ptr<SpeedControllerWrapper> wrapper = GetSensorActuatorHelper::GetSpeedControllerWrapper(aPortHandle);
    if(wrapper)
    {
        wrapper->SetName(env->GetStringUTFChars(aName, NULL));
    }
}

/*
 * Class:     com_snobot_simulator_jni_module_wrapper_SpeedControllerWrapperJni
 * Method:    getName
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_snobot_simulator_jni_module_1wrapper_SpeedControllerWrapperJni_getName(JNIEnv * env, jclass, jint portHandle)
{

    jstring output = MakeJString(env, SensorActuatorRegistry::Get().GetSpeedControllerWrapper(portHandle)->GetName());

    return output;
}

/*
 * Class:     com_snobot_simulator_jni_module_wrapper_SpeedControllerWrapperJni
 * Method:    getWantsHidden
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_com_snobot_simulator_jni_module_1wrapper_SpeedControllerWrapperJni_getWantsHidden(JNIEnv *, jclass, jint portHandle)
{
    return SensorActuatorRegistry::Get().GetSpeedControllerWrapper(portHandle)->WantsHidden();
}

/*
 * Class:     com_snobot_simulator_jni_module_wrapper_SpeedControllerWrapperJni
 * Method:    getVoltagePercentage
 * Signature: ()D
 */
JNIEXPORT jdouble JNICALL Java_com_snobot_simulator_jni_module_1wrapper_SpeedControllerWrapperJni_getVoltagePercentage(JNIEnv *, jclass, jint portHandle)
{
    return SensorActuatorRegistry::Get().GetSpeedControllerWrapper(portHandle)->GetVoltagePercentage();
}

/*
 * Class:     com_snobot_simulator_jni_module_wrapper_SpeedControllerWrapperJni
 * Method:    updateAllSpeedControllers
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_module_1wrapper_SpeedControllerWrapperJni_updateAllSpeedControllers(JNIEnv *, jclass, jdouble aUpdatePeriod)
{
    std::map<int, std::shared_ptr<SpeedControllerWrapper>>& speedControllers = SensorActuatorRegistry::Get().GetSpeedControllerWrapperMap();

    std::map<int, std::shared_ptr<SpeedControllerWrapper>>::iterator iter;

    for (iter = speedControllers.begin(); iter != speedControllers.end(); ++iter)
    {
        iter->second->Update(aUpdatePeriod);
    }
}

/*
 * Class:     com_snobot_simulator_jni_module_wrapper_SpeedControllerWrapperJni
 * Method:    getMotorConfig
 * Signature: (I)Lcom/snobot/simulator/DcMotorModelConfig;
 */
JNIEXPORT jobject JNICALL Java_com_snobot_simulator_jni_module_1wrapper_SpeedControllerWrapperJni_getMotorConfig
  (JNIEnv * env, jclass, jint aPortHandle)
{
    const std::shared_ptr<IMotorSimulator>& motorSim =
            SensorActuatorRegistry::Get().GetSpeedControllerWrapper(aPortHandle)->GetMotorSimulator();
    std::string type = motorSim->GetSimulatorType();

    jobject output = NULL;

    if(type == "Static Load")
    {
        const std::shared_ptr<StaticLoadDcMotorSim>& castMotorSim = std::dynamic_pointer_cast<StaticLoadDcMotorSim>(motorSim);
        output = ConversionUtils::ConvertDcMotorModelConfig(env, castMotorSim->GetMotorModel().GetModelConfig());
    }
    else if(type == "Null" || type == "Simple")
    {
        SNOBOT_LOG(SnobotLogging::WARN, "The type " << type << " does not have a DC Motor config...");
    }
    else
    {
        SNOBOT_LOG(SnobotLogging::CRITICAL, "Unknown motor sim type " << type);
    }

    return output;
}

/*
 * Class:     com_snobot_simulator_jni_module_wrapper_SpeedControllerWrapperJni
 * Method:    getMotorSimTypeNative
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_snobot_simulator_jni_module_1wrapper_SpeedControllerWrapperJni_getMotorSimTypeNative
  (JNIEnv *, jclass, jint aPortHandle)
{
    const std::shared_ptr<IMotorSimulator>& motorSim =
            SensorActuatorRegistry::Get().GetSpeedControllerWrapper(aPortHandle)->GetMotorSimulator();
    std::string type = motorSim->GetSimulatorType();

    if(type == "Null")
    {
        return 0;
    }
    else if(type == "Simple")
    {
        return 1;
    }
    else if(type == "Static Load")
    {
        return 2;
    }
    else
    {
        SNOBOT_LOG(SnobotLogging::CRITICAL, "Unknown motor sim type " << type);
    }

    return -1;
}

/*
 * Class:     com_snobot_simulator_jni_module_wrapper_SpeedControllerWrapperJni
 * Method:    getPortList
 * Signature: ()[I
 */
JNIEXPORT jintArray JNICALL Java_com_snobot_simulator_jni_module_1wrapper_SpeedControllerWrapperJni_getPortList(JNIEnv * env, jclass)
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
