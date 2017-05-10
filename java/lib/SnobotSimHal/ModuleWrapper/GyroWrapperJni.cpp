
#include <assert.h>
#include <jni.h>
#include "HAL/cpp/Log.h"
#include "support/jni_util.h"

#include "com_snobot_simulator_module_wrapper_GyroWrapperJni.h"
#include "SnobotSim/SensorActuatorRegistry.h"

using namespace wpi::java;

extern "C"
{

/*
 * Class:     com_snobot_simulator_module_wrapper_GyroWrapperJni
 * Method:    register
 * Signature: (ILjava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_module_1wrapper_GyroWrapperJni_register
  (JNIEnv * env, jclass, jint aHandle, jstring aName)
{
    std::shared_ptr<GyroWrapper> gyroWrapper(new GyroWrapper(env->GetStringUTFChars(aName, NULL)));
    SensorActuatorRegistry::Get().Register(aHandle, gyroWrapper);
}

/*
 * Class:     com_snobot_simulator_module_wrapper_GyroWrapperJni
 * Method:    setName
 * Signature: (ILjava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_module_1wrapper_GyroWrapperJni_setName
  (JNIEnv * env, jclass, jint aPortHandle, jstring aName)
{
    SensorActuatorRegistry::Get().GetGyroWrapper(aPortHandle)->SetName(env->GetStringUTFChars(aName, NULL));
}

/*
 * Class:     com_snobot_simulator_module_wrapper_GyroWrapperJni
 * Method:    getName
 * Signature: (I)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_snobot_simulator_module_1wrapper_GyroWrapperJni_getName
  (JNIEnv * env, jclass, jint aPortHandle)
{
    jstring output = MakeJString(env, SensorActuatorRegistry::Get().GetGyroWrapper(aPortHandle)->GetName());
    return output;
}

/*
 * Class:     com_snobot_simulator_module_wrapper_GyroWrapperJni
 * Method:    getWantsHidden
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_com_snobot_simulator_module_1wrapper_GyroWrapperJni_getWantsHidden
  (JNIEnv *, jclass, jint aPortHandle)
{
    return SensorActuatorRegistry::Get().GetGyroWrapper(aPortHandle)->WantsHidden();
}

/*
 * Class:     com_snobot_simulator_module_wrapper_GyroWrapperJni
 * Method:    getVoltagePercentage
 * Signature: (I)D
 */
JNIEXPORT jdouble JNICALL Java_com_snobot_simulator_module_1wrapper_GyroWrapperJni_getAngle
  (JNIEnv *, jclass, jint aPortHandle)
{
    return SensorActuatorRegistry::Get().GetGyroWrapper(aPortHandle)->GetAngle();
}

/*
 * Class:     com_snobot_simulator_module_wrapper_GyroWrapperJni
 * Method:    reset
 * Signature: (I)D
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_module_1wrapper_GyroWrapperJni_reset
  (JNIEnv *, jclass, jint aPortHandle)
{
    SensorActuatorRegistry::Get().GetGyroWrapper(aPortHandle)->SetAngle(0);
}

/*
 * Class:     com_snobot_simulator_module_wrapper_GyroWrapperJni
 * Method:    getPortList
 * Signature: ()[I
 */
JNIEXPORT jintArray JNICALL Java_com_snobot_simulator_module_1wrapper_GyroWrapperJni_getPortList
  (JNIEnv * env, jclass)
{
    const std::map<int, std::shared_ptr<GyroWrapper>>& gyroWrappers =
            SensorActuatorRegistry::Get().GetGyroWrapperMap();

    jintArray output = env->NewIntArray(gyroWrappers.size());

    jint values[30];

    std::map<int, std::shared_ptr<GyroWrapper>>::const_iterator iter =
            gyroWrappers.begin();

    int ctr = 0;
    for (; iter != gyroWrappers.end(); ++iter)
    {
        values[ctr++] = iter->first;
    }

    env->SetIntArrayRegion(output, 0, gyroWrappers.size(), values);

    return output;
}

}
