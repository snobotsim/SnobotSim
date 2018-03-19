/*
 * TankDriveSimulator.h
 *
 *  Created on: May 7, 2017
 *      Author: PJ
 */

#ifndef SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_SIMULATORCOMPONENTS_TANKDRIVESIMULATOR_H_
#define SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_SIMULATORCOMPONENTS_TANKDRIVESIMULATOR_H_

#include "SnobotSim/ModuleWrapper/EncoderWrapper.h"
#include "SnobotSim/SimulatorComponents/Gyro/IGyroWrapper.h"
#include "SnobotSim/SimulatorComponents/ISimulatorUpdater.h"

class EXPORT_ TankDriveSimulator: public ISimulatorUpdater
{
public:
    TankDriveSimulator(
        const std::shared_ptr<EncoderWrapper>& aLeftEncoder,
        const std::shared_ptr<EncoderWrapper>& aRightEncoder,
        const std::shared_ptr<IGyroWrapper>& aGyroWrapper,
        double aTurnKp);

    virtual ~TankDriveSimulator();

    void Update();

protected:

    std::shared_ptr<EncoderWrapper> mLeftEncoder;
    std::shared_ptr<EncoderWrapper> mRightEncoder;
    std::shared_ptr<IGyroWrapper> mGyroWrapper;

    double mTurnKp;
    bool mIsSetup;

};

#endif  // SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_SIMULATORCOMPONENTS_TANKDRIVESIMULATOR_H_
