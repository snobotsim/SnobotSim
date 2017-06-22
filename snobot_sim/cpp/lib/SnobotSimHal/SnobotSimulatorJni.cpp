
#include <assert.h>
#include <jni.h>
#include "support/jni_util.h"

#include "com_snobot_simulator_jni_SnobotSimulatorJni.h"
#include "SnobotSim/SensorActuatorRegistry.h"
#include "SnobotSim/SnobotSimHalVersion.h"
#include "SnobotSim/Logging/SnobotLogger.h"
#include "SnobotSim/Logging/SnobotCoutLogger.h"

using namespace wpi::java;

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
    SensorActuatorRegistry::Get().Reset();
}

/*
 * Class:     com_snobot_simulator_jni_SnobotSimulatorJni
 * Method:    getVersion
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_snobot_simulator_jni_SnobotSimulatorJni_getVersion
  (JNIEnv * env, jclass)
{
    jstring output = MakeJString(env, SnobotSimHal::Version);

    return output;
}

/*
 * Class:     com_snobot_simulator_jni_SnobotSimulatorJni
 * Method:    initializeLogging
 * Signature: (I)Ljava/lang/String;
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_SnobotSimulatorJni_initializeLogging
  (JNIEnv *, jclass, jint aLogLevel)
{
    static SnobotLogging::SnobotCoutLogger coutLogger;

    SnobotLogging::LogLevel logLevel = (SnobotLogging::LogLevel) aLogLevel;
    coutLogger.SetLogLevel(logLevel);

    SnobotLogging::LoggerWrapper::SetLogger(&coutLogger);
}

}
