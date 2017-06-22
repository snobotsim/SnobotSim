
#include <assert.h>
#include <jni.h>
#include "SnobotSim/RobotStateSingleton.h"
#include "SnobotSim/SensorActuatorRegistry.h"
#include "SnobotSim/MotorSim/DcMotorModelConfig.h"
#include "SnobotSim/MotorSim/GravityLoadDcMotorSim.h"
#include "SnobotSim/MotorSim/RotationalLoadDcMotorSim.h"
#include "SnobotSim/MotorSim/StaticLoadDcMotorSim.h"
#include "SnobotSim/MotorSim/SimpleMotorSimulator.h"
#include "SnobotSim/SimulatorComponents/ISimulatorUpdater.h"
#include "SnobotSim/SimulatorComponents/TankDriveSimulator.h"
#include "SnobotSim/SimulatorComponents/Gyro/GyroWrapper.h"
#include "ConversionUtils.h"
#include <vector>
#include <memory>


#include "SnobotSim/GetSensorActuatorHelper.h"

using namespace GetSensorActuatorHelper;


extern "C"
{
/*
 * Class:     com_snobot_simulator_jni_SimulationConnectorJni
 * Method:    updateLoop
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_SimulationConnectorJni_updateLoop
  (JNIEnv *, jclass)
{
    RobotStateSingleton::Get().UpdateLoop();
}

/*
 * Class:     com_snobot_simulator_jni_SimulationConnectorJni
 * Method:    setSpeedControllerModel_Simple
 * Signature: (ID)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_SimulationConnectorJni_setSpeedControllerModel_1Simple
  (JNIEnv *, jclass, jint aHandle, jdouble aMaxSpeed)
{
	std::shared_ptr<SpeedControllerWrapper> speedController = GetSpeedControllerWrapper(aHandle);
	if(speedController)
	{
    	speedController->SetMotorSimulator(std::shared_ptr < IMotorSimulator > (new SimpleMotorSimulator(aMaxSpeed)));
	}
}

/*
 * Class:     com_snobot_simulator_jni_SimulationConnectorJni
 * Method:    setSpeedControllerModel_Static
 * Signature: (ILcom/snobot/simulator/DcMotorModelConfigJni;DD)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_SimulationConnectorJni_setSpeedControllerModel_1Static
  (JNIEnv * env, jclass, jint aSpeedControllerHandle,
        jobject aConfig, jdouble aLoad, jdouble aConversionFactor)
{
    DcMotorModel motorModel(ConversionUtils::ConvertDcMotorModelConfig(env, aConfig));

	std::shared_ptr<SpeedControllerWrapper> speedController = GetSpeedControllerWrapper(aSpeedControllerHandle);
	if(speedController)
	{
	    speedController->SetMotorSimulator(std::shared_ptr < IMotorSimulator > (new StaticLoadDcMotorSim(motorModel, aLoad, aConversionFactor)));
	}
}

/*
 * Class:     com_snobot_simulator_jni_SimulationConnectorJni
 * Method:    setSpeedControllerModel_Gravitational
 * Signature: (ILcom/snobot/simulator/DcMotorModelConfigJni;D)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_SimulationConnectorJni_setSpeedControllerModel_1Gravitational
  (JNIEnv * env, jclass,
        jint aSpeedControllerHandle, jobject aConfig, jdouble aLoad)
{
    DcMotorModel motorModel(ConversionUtils::ConvertDcMotorModelConfig(env, aConfig));

	std::shared_ptr<SpeedControllerWrapper> speedController = GetSpeedControllerWrapper(aSpeedControllerHandle);
	if(speedController)
	{
	    speedController->SetMotorSimulator(std::shared_ptr < IMotorSimulator > (new GravityLoadDcMotorSim(motorModel, aLoad)));
	}
}

/*
 * Class:     com_snobot_simulator_jni_SimulationConnectorJni
 * Method:    setSpeedControllerModel_Rotational
 * Signature: (ILcom/snobot/simulator/DcMotorModelConfigJni;DDDD)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_SimulationConnectorJni_setSpeedControllerModel_1Rotational
  (JNIEnv * env, jclass, jint aSpeedControllerHandle,
        jobject aConfig, jdouble aArmCenterOfMass, jdouble aArmMass, jdouble aConstantAssistTorque, jdouble aOverCenterAssistTorque)
{
    SNOBOT_LOG(SnobotLogging::CRITICAL, "Unsupported");
//    std::cerr << "Unsupported!" << std::endl;
//    DcMotorModel motorModel = ConvertDcMotorModel(aConfig);
//    std::shared_ptr<SpeedControllerWrapper> speedController = SensorActuatorRegistry::Get().GetSpeedControllerWrapper(aSpeedControllerHandle);
//    speedController->SetMotorSimulator(std::shared_ptr < IMotorSimulator > (new StaticLoadDcMotorSimulator(aLoad, aConversionFactor)));
}

/*
 * Class:     com_snobot_simulator_jni_SimulationConnectorJni
 * Method:    connectTankDriveSimulator
 * Signature: (IIID)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_SimulationConnectorJni_connectTankDriveSimulator
  (JNIEnv *, jclass,
          jint aLeftEncHandle,
          jint aRightEncHandle,
          jint aGyroHandle, jdouble aTurnKp)
{    
    std::shared_ptr<EncoderWrapper> leftEncoder = SensorActuatorRegistry::Get().GetEncoderWrapper(aLeftEncHandle);
    std::shared_ptr<EncoderWrapper> rightEncoder = SensorActuatorRegistry::Get().GetEncoderWrapper(aRightEncHandle);
    std::shared_ptr<GyroWrapper> gyro = GetGyroWrapper(aGyroHandle);

    std::shared_ptr<TankDriveSimulator> simulator(new TankDriveSimulator(leftEncoder, rightEncoder, gyro, aTurnKp));

    SensorActuatorRegistry::Get().AddSimulatorComponent(simulator);
}

}
