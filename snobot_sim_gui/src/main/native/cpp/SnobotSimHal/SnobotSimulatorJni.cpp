
#include <assert.h>
#include <jni.h>
#include "support/jni_util.h"

#include "com_snobot_simulator_jni_SnobotSimulatorJni.h"
#include "SnobotSim/HalCallbacks/CallbackSetup.h"
#include "SnobotSim/SensorActuatorRegistry.h"
#include "SnobotSim/SensorActuatorRegistry.h"
#include "SnobotSim/RobotStateSingleton.h"
#include "SnobotSim/SnobotSimHalVersion.h"
#include "SnobotSim/Logging/SnobotLogger.h"
#include "SnobotSim/Logging/SnobotCoutLogger.h"

using namespace wpi::java;

static SnobotLogging::ISnobotLogger* sSnobotLogger = NULL;

extern "C"
{

/*
 * Class:     com_snobot_simulator_jni_SnobotSimulatorJni
 * Method:    initializeSimulator
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_SnobotSimulatorJni_initializeSimulator
  (JNIEnv *, jclass)
{
	SnobotSim::InitializeSnobotCallbacks();
}

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
    RobotStateSingleton::Get().Reset();
    SensorActuatorRegistry::Get().Reset();

    if(sSnobotLogger)
    {
        delete sSnobotLogger;
        sSnobotLogger = NULL;
    }
    SnobotLogging::SetLogger(NULL);
}

/*
 * Class:     com_snobot_simulator_jni_SnobotSimulatorJni
 * Method:    getVersion
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_snobot_simulator_jni_SnobotSimulatorJni_getVersion
  (JNIEnv * env, jclass)
{
    jstring output = MakeJString(env, SnobotSim::GetSnobotSimVersion());

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
    if(!sSnobotLogger)
    {
        sSnobotLogger = new SnobotLogging::SnobotCoutLogger();
    }

    SnobotLogging::LogLevel logLevel = (SnobotLogging::LogLevel) aLogLevel;
    sSnobotLogger->SetLogLevel(logLevel);

    SnobotLogging::SetLogger(sSnobotLogger);
}

}
