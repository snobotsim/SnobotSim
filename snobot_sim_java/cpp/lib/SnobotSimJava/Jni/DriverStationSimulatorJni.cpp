
#include <assert.h>
#include <jni.h>
#include "support/jni_util.h"

#include <chrono>

#include "com_snobot_simulator_jni_DriverStationSimulatorJni.h"

#include "MockData/DriverStationData.h"
#include "MockData/MockHooks.h"

#include "SnobotSimJava/Logging/SnobotLogger.h"

extern "C"
{

/*
 * Class:     com_snobot_simulator_jni_SensorFeedbackJni
 * Method:    setEnabled
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_DriverStationSimulatorJni_setEnabled
  (JNIEnv *, jclass, jboolean aEnabled)
{
    HALSIM_SetDriverStationDsAttached(aEnabled);
    HALSIM_SetDriverStationEnabled(aEnabled);
}

/*
 * Class:     com_snobot_simulator_jni_SensorFeedbackJni
 * Method:    setAutonomous
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_DriverStationSimulatorJni_setAutonomous
  (JNIEnv *, jclass, jboolean aAuton)
{
    HALSIM_SetDriverStationAutonomous(aAuton);
}

/*
 * Class:     com_snobot_simulator_jni_SensorFeedbackJni
 * Method:    waitForProgramToStart
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_DriverStationSimulatorJni_waitForProgramToStart
  (JNIEnv *, jclass)
{
    HALSIM_WaitForProgramStart();
}

/*
 * Class:     com_snobot_simulator_jni_SensorFeedbackJni
 * Method:    delayForNextUpdateLoop
 * Signature: (D)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_DriverStationSimulatorJni_delayForNextUpdateLoop
  (JNIEnv *, jclass, jdouble aDelayPeriod)
{
    if(aDelayPeriod > 0)
    {
        std::this_thread::sleep_for(std::chrono::milliseconds((int) (aDelayPeriod * 1000)));
        HALSIM_SetDriverStationMatchTime(HALSIM_GetDriverStationMatchTime() + aDelayPeriod);
        HALSIM_NotifyDriverStationNewData();
    }
    else
    {
        SNOBOT_LOG(SnobotLogging::CRITICAL, "Unsupported... Cannot delay 0 at the moment");
    }
}

/*
 * Class:     com_snobot_simulator_jni_SensorFeedbackJni
 * Method:    getMatchTime
 * Signature: ()D
 */
JNIEXPORT jdouble JNICALL Java_com_snobot_simulator_jni_DriverStationSimulatorJni_getMatchTime
  (JNIEnv *, jclass)
{
    return HALSIM_GetDriverStationMatchTime();
}


/*
 * Class:     com_snobot_simulator_jni_SensorFeedbackJni
 * Method:    setJoystickInformation
 * Signature: (I[F[SII)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_DriverStationSimulatorJni_setJoystickInformation
  (JNIEnv * env, jclass,
          jint aHandle, jfloatArray aAxes, jshortArray aPovs, jint aButtonCount, jint aButtonMask)
{

    HAL_JoystickAxes newAxes;
    HAL_JoystickPOVs newPov;
    HAL_JoystickButtons newButtons;

    newButtons.count = aButtonCount;
    newButtons.buttons = aButtonMask;

    float* axes = env->GetFloatArrayElements(aAxes, NULL);
    newAxes.count = env->GetArrayLength(aAxes);
    for (int i = 0; i < newAxes.count; ++i)
    {
        newAxes.axes[i] = axes[i];
    }
    env->ReleaseFloatArrayElements(aAxes, axes, 0);

    short* povs = env->GetShortArrayElements(aPovs, NULL);
    newPov.count = env->GetArrayLength(aPovs);
    for (int i = 0; i < newPov.count; ++i)
    {
        newPov.povs[i] = povs[i];
    }
    env->ReleaseShortArrayElements(aPovs, povs, 0);

    HALSIM_SetJoystickAxes(aHandle, &newAxes);
    HALSIM_SetJoystickPOVs(aHandle, &newPov);
    HALSIM_SetJoystickButtons(aHandle, &newButtons);
}

/*
 * Class:     com_snobot_simulator_jni_DriverStationSimulatorJni
 * Method:    setMatchInfo
 * Signature: (Ljava/lang/String;IIILjava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_DriverStationSimulatorJni_setMatchInfo
  (JNIEnv * env, jclass, jstring aEventName, jint aMatchTypeVal, jint aMatchNumber, jint aReplayNumber, jstring aGameSpecificMessage)
{
    std::string eventName = env->GetStringUTFChars(aEventName, NULL);
    std::string gameSpecificMessage = env->GetStringUTFChars(aGameSpecificMessage, NULL);

    HAL_MatchInfo matchInfo;
    matchInfo.eventName = const_cast<char*>(eventName.c_str());
    matchInfo.gameSpecificMessage = const_cast<char*>(gameSpecificMessage.c_str());
    matchInfo.matchType = (HAL_MatchType) aMatchTypeVal;
    matchInfo.replayNumber = aReplayNumber;
    matchInfo.matchNumber  = aMatchNumber;

    HALSIM_SetMatchInfo(&matchInfo);
}

} // extern c
