
#include <assert.h>
#include <jni.h>
#include "SnobotSim/SimulationConnector.h"
#include "SnobotSim/RobotStateSingleton.h"

static SimulationConnector simulationConnector;

extern "C"
{
/*
 * Class:     com_snobot_simulator_SimulationConnectorJni
 * Method:    updateLoop
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_SimulationConnectorJni_updateLoop
  (JNIEnv *, jclass)
{
    RobotStateSingleton::Get().UpdateLoop();
}

/*
 * Class:     com_snobot_simulator_SimulationConnectorJni
 * Method:    setSpeedControllerSimpleModel
 * Signature: (ID)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_SimulationConnectorJni_setSpeedControllerSimpleModel
  (JNIEnv *, jclass, jint aHandle, jdouble aMaxSpeed)
{
    simulationConnector.SetSpeedControllerSimpleModel(aHandle, aMaxSpeed);
}

/*
 * Class:     com_snobot_simulator_SimulationConnectorJni
 * Method:    setSpeedControllerModel
 * Signature: (ILjava/lang/String;Lcom/snobot/simulator/SimulationConnectorJni/DcMotorModelConfig;)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_SimulationConnectorJni_setSpeedControllerModel
  (JNIEnv *, jclass, jint aHandle, jstring, jobject)
{
//     simulationConnector.SetSpeedControllerSimpleModel(aHandle, aMaxSpeed);
}

/*
 * Class:     com_snobot_simulator_SimulationConnectorJni
 * Method:    connectEncoderAndSpeedController
 * Signature: (III)V
 */
JNIEXPORT jint JNICALL Java_com_snobot_simulator_SimulationConnectorJni_connectEncoderAndSpeedController__III
  (JNIEnv *, jclass, jint aPortA, jint aPortB, jint aSpeedController)
{
    int encoderHandle = (aPortA << 8) + aPortB;

    simulationConnector.ConnectEncoderAndSpeedController(encoderHandle, aSpeedController);

    return encoderHandle;
}

/*
 * Class:     com_snobot_simulator_SimulationConnectorJni
 * Method:    connectEncoderAndSpeedController
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_SimulationConnectorJni_connectEncoderAndSpeedController__II
  (JNIEnv *, jclass, jint aEncoderhandle, jint aScHandle)
{
    simulationConnector.ConnectEncoderAndSpeedController(aEncoderhandle, aScHandle);
}

/*
 * Class:     com_snobot_simulator_SimulationConnectorJni
 * Method:    connectTankDriveSimulator
 * Signature: (IIIII)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_SimulationConnectorJni_connectTankDriveSimulator__IIIIID
  (JNIEnv *, jclass,
          jint aLeftEncA, jint aLeftEncB,
          jint aRightEncA, jint aRightEncB,
          jint aGyroHandle, jdouble aTurnKp)
{
    int leftEncHandle = (aLeftEncA << 8) + aLeftEncB;
    int rightEncHandle = (aRightEncA << 8) + aRightEncB;


    simulationConnector.ConnectTankDriveSimulator(leftEncHandle, rightEncHandle, aGyroHandle, aTurnKp);
}

/*
 * Class:     com_snobot_simulator_SimulationConnectorJni
 * Method:    cConnectTankDriveSimulator
 * Signature: (III)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_SimulationConnectorJni_connectTankDriveSimulator__IIID
  (JNIEnv *, jclass,
          jint aLeftEncHandle,
          jint aRightEncHandle,
          jint aGyroHandle, jdouble aTurnKp)
{
    simulationConnector.ConnectTankDriveSimulator(aLeftEncHandle, aRightEncHandle, aGyroHandle, aTurnKp);
}

}
