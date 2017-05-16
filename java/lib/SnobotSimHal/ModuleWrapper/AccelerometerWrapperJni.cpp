
#include <assert.h>
#include <jni.h>
#include "support/jni_util.h"

#include "com_snobot_simulator_module_wrapper_GyroWrapperJni.h"
#include "SnobotSim/SensorActuatorRegistry.h"

using namespace wpi::java;

extern "C"
{

/*
 * Class:     com_snobot_simulator_module_wrapper_AccelerometerWrapperJni
 * Method:    register
 * Signature: (ILjava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_module_1wrapper_AccelerometerWrapperJni_register
  (JNIEnv * env, jclass, jint aPortHandle, jstring aName)
{
    std::shared_ptr<AccelerometerWrapper> accelerometerWrapper(new AccelerometerWrapper(env->GetStringUTFChars(aName, NULL)));
    SensorActuatorRegistry::Get().Register(aPortHandle, accelerometerWrapper);
}

/*
 * Class:     com_snobot_simulator_module_wrapper_AccelerometerWrapperJni
 * Method:    setName
 * Signature: (ILjava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_module_1wrapper_AccelerometerWrapperJni_setName
  (JNIEnv * env, jclass, jint aPortHandle, jstring aName)
{
    SensorActuatorRegistry::Get().GetAccelerometerWrapper(aPortHandle)->SetName(env->GetStringUTFChars(aName, NULL));
}

/*
 * Class:     com_snobot_simulator_module_wrapper_AccelerometerWrapperJni
 * Method:    getName
 * Signature: (I)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_snobot_simulator_module_1wrapper_AccelerometerWrapperJni_getName
  (JNIEnv * env, jclass, jint aPortHandle)
{
    jstring output = MakeJString(env, SensorActuatorRegistry::Get().GetAccelerometerWrapper(aPortHandle)->GetName());
    return output;
}

/*
 * Class:     com_snobot_simulator_module_wrapper_AccelerometerWrapperJni
 * Method:    getWantsHidden
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_com_snobot_simulator_module_1wrapper_AccelerometerWrapperJni_getWantsHidden
  (JNIEnv * env, jclass, jint aPortHandle)
{
    return SensorActuatorRegistry::Get().GetAccelerometerWrapper(aPortHandle)->WantsHidden();
}

/*
 * Class:     com_snobot_simulator_module_wrapper_AccelerometerWrapperJni
 * Method:    getAcceleration
 * Signature: (I)D
 */
JNIEXPORT jdouble JNICALL Java_com_snobot_simulator_module_1wrapper_AccelerometerWrapperJni_getAcceleration
  (JNIEnv * env, jclass, jint aPortHandle)
{
    return SensorActuatorRegistry::Get().GetAccelerometerWrapper(aPortHandle)->GetAcceleration();
}

/*
 * Class:     com_snobot_simulator_module_wrapper_AccelerometerWrapperJni
 * Method:    getPortList
 * Signature: ()[I
 */
JNIEXPORT jintArray JNICALL Java_com_snobot_simulator_module_1wrapper_AccelerometerWrapperJni_getPortList
  (JNIEnv * env, jclass)
{
    const std::map<int, std::shared_ptr<AccelerometerWrapper>>& accelerometerWrappers =
            SensorActuatorRegistry::Get().GetAccelerometerWrapperMap();

    jintArray output = env->NewIntArray(accelerometerWrappers.size());

    jint values[30];

    std::map<int, std::shared_ptr<AccelerometerWrapper>>::const_iterator iter =
            accelerometerWrappers.begin();

    int ctr = 0;
    for (; iter != accelerometerWrappers.end(); ++iter)
    {
        values[ctr++] = iter->first;
    }

    env->SetIntArrayRegion(output, 0, accelerometerWrappers.size(), values);

    return output;
}


}
