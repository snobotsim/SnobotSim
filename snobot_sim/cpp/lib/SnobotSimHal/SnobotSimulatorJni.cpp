
#include <assert.h>
#include <jni.h>
#include "com_snobot_simulator_jni_SnobotSimulatorJni.h"
#include "SnobotSim/SensorActuatorRegistry.h"

extern "C"
{

/*
 * Class:     com_snobot_simulator_jni_SnobotSimulatorJni
 * Method:    reset
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_SnobotSimulatorJni_reset
  (JNIEnv *, jclass)
{
    SensorActuatorRegistry::Get().Reset();
}

/*
 * Class:     com_snobot_simulator_jni_SnobotSimulatorJni
 * Method:    shutdown
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_SnobotSimulatorJni_shutdown
  (JNIEnv *, jclass)
{
    std::cout << "Shutting down the simulator..." << std::endl;
}

}
