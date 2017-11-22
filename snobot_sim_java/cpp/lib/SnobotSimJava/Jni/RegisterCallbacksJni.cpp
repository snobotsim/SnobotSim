
#include "com_snobot_simulator_jni_RegisterCallbacksJni.h"

#include "HAL/handles/HandlesInternal.h"

#include "SnobotSimJava/Logging/SnobotLogger.h"
#include "SnobotSimJava/Logging/SnobotCoutLogger.h"

static SnobotLogging::ISnobotLogger* sSnobotLogger = NULL;

/*
 * Class:     com_snobot_simulator_jni_RegisterCallbacksJni
 * Method:    resetWpiHal
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_RegisterCallbacksJni_resetWpiHal
  (JNIEnv *, jclass)
{
    hal::HandleBase::ResetGlobalHandles();

    if(sSnobotLogger)
    {
        delete sSnobotLogger;
    }

    sSnobotLogger = new SnobotLogging::SnobotCoutLogger();
    sSnobotLogger->SetLogLevel(SnobotLogging::DEBUG);
    SnobotLogging::SetLogger(sSnobotLogger);
}
