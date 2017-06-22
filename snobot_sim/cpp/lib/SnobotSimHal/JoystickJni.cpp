
#include <assert.h>
#include <jni.h>
#include "com_snobot_simulator_jni_JoystickJni.h"
#include "SnobotSim/JoystickManager.h"
#include "SnobotSim/Logging/SnobotLogger.h"


std::ostream& operator<<(std::ostream& aStream, const JoystickInformation& aJoystickInfo)
{
    aStream << "Joystick: \n";

    aStream << "  Buttons: " << aJoystickInfo.mButtons.count << "- " << aJoystickInfo.mButtons.buttons << "\n";

    aStream << "  Axis: " << aJoystickInfo.mAxes.count << "\n";
    for (int i = 0; i < aJoystickInfo.mAxes.count; ++i)
    {
        aStream << "    Axis: " << aJoystickInfo.mAxes.axes[i] << "\n";
    }

    aStream << "  POV: " << aJoystickInfo.mPovs.count << "\n";
    for (int i = 0; i < aJoystickInfo.mPovs.count; ++i)
    {
        aStream << "    POV: " << aJoystickInfo.mPovs.povs[i] << "\n";
    }

    return aStream;
}

extern "C"
{
/*
 * Class:     com_snobot_simulator_jni_JoystickJni
 * Method:    setJoystickInformation
 * Signature: (I[F[S[Z)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_JoystickJni_setJoystickInformation
  (JNIEnv * env, jclass, 
   jint aHandle, jfloatArray aAxes, jshortArray aPovs, jint aButtonCount, jint aButtonMask)
{
    if (!JoystickManager::Get().HasJoystick(aHandle))
    {
        SNOBOT_LOG(SnobotLogging::ERROR, "Unregistered joystick " << aHandle);
        return;
    }

    JoystickInformation& info = JoystickManager::Get().GetJoystick(aHandle);
    

    info.mButtons.count = aButtonCount;
    info.mButtons.buttons = aButtonMask;

    float* axes = env->GetFloatArrayElements(aAxes, NULL);
    info.mAxes.count = env->GetArrayLength(aAxes);
    for (int i = 0; i < info.mAxes.count; ++i)
    {
        info.mAxes.axes[i] = axes[i];
    }
    env->ReleaseFloatArrayElements(aAxes, axes, 0);

    short* povs = env->GetShortArrayElements(aPovs, NULL);
    info.mPovs.count = env->GetArrayLength(aPovs);
    for (int i = 0; i < info.mPovs.count; ++i)
    {
        info.mPovs.povs[i] = povs[i];
    }
    env->ReleaseShortArrayElements(aPovs, povs, 0);
}
}
