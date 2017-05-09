/*
 * SimulationConnector.cpp
 *
 *  Created on: May 5, 2017
 *      Author: PJ
 */

#include "SnobotSim/SimulationConnector.h"
#include "SnobotSim/SensorActuatorRegistry.h"
#include "SnobotSim/RobotStateSingleton.h"
#include "SnobotSim/MotorSim/SimpleMotorSimulator.h"
#include "SnobotSim/SimulatorComponents/TankDriveSimulator.h"
#include "SnobotSim/SimulatorComponents/Gyro/GyroWrapper.h"

SimulationConnector::SimulationConnector() {


}

SimulationConnector::~SimulationConnector() {

}

void SimulationConnector::SetSpeedControllerSimpleModel(int aSpeedControllerHandle, double aMaxSpeed)
{
    std::shared_ptr<SpeedControllerWrapper> speedController = SensorActuatorRegistry::Get().GetSpeedControllerWrapper(aSpeedControllerHandle);
    speedController->SetMotorSimulator(std::shared_ptr<SimpleMotorSimulator>(new SimpleMotorSimulator(aMaxSpeed)));
}

void SimulationConnector::SetSpeedControllerModel(int aSpeedControllerHandle, MotorModelType aModelType, const DcMotorModelConfig& aConfig)
{
    std::cout << "UNSUPPORTED" << std::endl;
}

void SimulationConnector::ConnectEncoderAndSpeedController(int aEncoderHandle, int aSpeedControllerHandle)
{
    std::shared_ptr<EncoderWrapper> encoder = SensorActuatorRegistry::Get().GetEncoderWrapper(aEncoderHandle);
    std::shared_ptr<SpeedControllerWrapper> speedController = SensorActuatorRegistry::Get().GetSpeedControllerWrapper(aSpeedControllerHandle);

    encoder->SetSpeedController(speedController);
}

void SimulationConnector::ConnectTankDriveSimulator(int aLeftEncoderHandle, int aRightEncoderHandle, int aGyroHandle, double aTurnKp)
{
    std::shared_ptr<EncoderWrapper> leftEncoder = SensorActuatorRegistry::Get().GetEncoderWrapper(aLeftEncoderHandle);
    std::shared_ptr<EncoderWrapper> rightEncoder = SensorActuatorRegistry::Get().GetEncoderWrapper(aRightEncoderHandle);
    std::shared_ptr<GyroWrapper> gyro = SensorActuatorRegistry::Get().GetGyroWrapper(aGyroHandle);

    std::shared_ptr<TankDriveSimulator> simulator(new TankDriveSimulator(leftEncoder, rightEncoder, gyro, aTurnKp));

    SensorActuatorRegistry::Get().AddSimulatorComponent(simulator);
}
