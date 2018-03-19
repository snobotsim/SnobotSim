
#include "HAL/HAL.h"
#include "HAL/handles/HandlesInternal.h"
#include "SnobotSimJava/Logging/SnobotCoutLogger.h"
#include "SnobotSimJava/Logging/SnobotLogger.h"
#include "com_snobot_simulator_jni_RegisterCallbacksJni.h"

static SnobotLogging::ISnobotLogger* sSnobotLogger = NULL;
static bool sINITIALIZED = false;

/*
 * Class:     com_snobot_simulator_jni_RegisterCallbacksJni
 * Method:    resetWpiHal
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_RegisterCallbacksJni_resetWpiHal
  (JNIEnv *, jclass)
{

    if(sSnobotLogger)
    {
        delete sSnobotLogger;
    }

    sSnobotLogger = new SnobotLogging::SnobotCoutLogger();
    sSnobotLogger->SetLogLevel(SnobotLogging::INFO);
    SnobotLogging::SetLogger(sSnobotLogger);

    if(!sINITIALIZED)
    {
        sINITIALIZED = true;
        SNOBOT_LOG(SnobotLogging::INFO, "Initializing the simulator");

        if(!HAL_Initialize(0, 0))
        {
            SNOBOT_LOG(SnobotLogging::CRITICAL, "Couldn't initialize!!!");
        }
    }

    hal::HandleBase::ResetGlobalHandles();
}
