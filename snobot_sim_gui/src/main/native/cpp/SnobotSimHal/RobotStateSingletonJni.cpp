
#include <assert.h>
#include <jni.h>
#include "com_snobot_simulator_jni_RobotStateSingletonJni.h"
#include "SnobotSim/RobotStateSingleton.h"

extern "C"
{

/*
 * Class:     com_snobot_simulator_jni_RobotStateSingletonJni
 * Method:    setDisabled
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_RobotStateSingletonJni_setDisabled(JNIEnv *, jclass, jboolean aBool)
{
    RobotStateSingleton::Get().SetDisabled(aBool);
}

/*
 * Class:     com_snobot_simulator_jni_RobotStateSingletonJni
 * Method:    setAutonomous
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_RobotStateSingletonJni_setAutonomous(JNIEnv *, jclass, jboolean aBool)
{
    RobotStateSingleton::Get().SetAutonomous(aBool);
}

/*
 * Class:     com_snobot_simulator_jni_RobotStateSingletonJni
 * Method:    setTest
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_RobotStateSingletonJni_setTest(JNIEnv *, jclass, jboolean aBool)
{
    RobotStateSingleton::Get().SetTest(aBool);
}

/*
 * Class:     com_snobot_simulator_jni_RobotStateSingletonJni
 * Method:    getMatchTime
 * Signature: ()D
 */
JNIEXPORT jdouble JNICALL Java_com_snobot_simulator_jni_RobotStateSingletonJni_getMatchTime(JNIEnv *, jclass)
{
    return RobotStateSingleton::Get().GetMatchTime();
}

/*
 * Class:     com_snobot_simulator_jni_RobotStateSingletonJni
 * Method:    waitForProgramToStart
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_RobotStateSingletonJni_waitForProgramToStart
  (JNIEnv *, jclass)
{
	RobotStateSingleton::Get().WaitForProgramToStart();
}

/*
 * Class:     com_snobot_simulator_jni_RobotStateSingletonJni
 * Method:    waitForNextUpdateLoop
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_RobotStateSingletonJni_waitForNextUpdateLoop
  (JNIEnv *, jclass)
{
	RobotStateSingleton::Get().WaitForNextControlLoop();
}

/*
 * Class:     com_snobot_simulator_jni_RobotStateSingletonJni
 * Method:    getCycleTime
 * Signature: ()D
 */
JNIEXPORT jdouble JNICALL Java_com_snobot_simulator_jni_RobotStateSingletonJni_getCycleTime
  (JNIEnv *, jclass)
{
	return .02;
}

}
