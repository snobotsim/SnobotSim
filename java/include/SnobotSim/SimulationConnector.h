/*
 * SimulationConnector.h
 *
 *  Created on: May 5, 2017
 *      Author: PJ
 */

#ifndef SIMULATIONCONNECTOR_H_
#define SIMULATIONCONNECTOR_H_

#include "SnobotSim/MotorSim/DcMotorModelConfig.h"
#include "SnobotSim/SimulatorComponents/ISimulatorUpdater.h"
#include <vector>
#include <memory>

class SimulationConnector {
public:
    SimulationConnector();
    virtual ~SimulationConnector();

    enum MotorModelType
    {
        MotorModelType_GravityLoad,
        MotorModelType_RotationalLoad,
        MotorModelType_StaticLoad,
    };

    void SetSpeedControllerSimpleModel(int aSpeedControllerHandle, double aMaxSpeed);

    void SetSpeedControllerModel(int aSpeedControllerHandle, MotorModelType aModelType, const DcMotorModelConfig& aConfig);

    void ConnectEncoderAndSpeedController(int aEncoderHandle, int aSpeedControllerHandle);

    void ConnectTankDriveSimulator(int aLeftEncoderHandle, int aRightEncoderHandle, int aGyroHandle, double aTurnKp);
};

#endif /* SIMULATIONCONNECTOR_H_ */
