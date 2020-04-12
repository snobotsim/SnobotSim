/*
 * TankDriveSimulator.h
 *
 *  Created on: May 7, 2017
 *      Author: PJ
 */

#pragma once
#include <memory>
#include <string>

#include "SnobotSim/ModuleWrapper/Interfaces/IEncoderWrapper.h"
#include "SnobotSim/ModuleWrapper/Interfaces/IGyroWrapper.h"
#include "SnobotSim/SimulatorComponents/ISimulatorUpdater.h"

class EXPORT_ TankDriveSimulator : public ISimulatorUpdater
{
public:
    TankDriveSimulator(
            const std::shared_ptr<ISpeedControllerWrapper>& aLeftMotor,
            const std::shared_ptr<ISpeedControllerWrapper>& aRightMotor,
            const std::shared_ptr<IGyroWrapper>& aGyroWrapper,
            double aTurnKp);

    virtual ~TankDriveSimulator();

    void Update();

    static std::string GetType()
    {
        return "com.snobot.simulator.simulator_components.config.TankDriveConfig";
    }

    std::string GetSimulatorType() override
    {
        return GetType();
    }

    bool IsSetup() const;
    const std::shared_ptr<ISpeedControllerWrapper>& GetLeftMotor() const;
    const std::shared_ptr<ISpeedControllerWrapper>& GetRightMotor() const;
    const std::shared_ptr<IGyroWrapper>& GetGyro() const;
    double GetTurnKp() const;

protected:
    std::shared_ptr<ISpeedControllerWrapper> mLeftMotor;
    std::shared_ptr<ISpeedControllerWrapper> mRightMotor;
    std::shared_ptr<IGyroWrapper> mGyroWrapper;

    double mTurnKp;
    bool mIsSetup;
};
