/*
 * RotationalLoadDcMotorSim.h
 *
 *  Created on: May 9, 2017
 *      Author: preiniger
 */

#pragma once
#include <memory>
#include <string>

#include "SnobotSim/ModuleWrapper/Interfaces/ISpeedControllerWrapper.h"
#include "SnobotSim/MotorSim/BaseDcMotorSimulator.h"

class EXPORT_ RotationalLoadDcMotorSim : public BaseDcMotorSimulator
{
public:
    struct RotationalLoadMotorSimulationConfig
    {
        double mArmCenterOfMass{ 1.0 };
        double mArmMass{ 1.0 };
        double mConstantAssistTorque{ 1.0 };
        double mOverCenterAssistTorque{ 1.0 };
    };
    static std::string GetType()
    {
        return "com.snobot.simulator.motor_sim.RotationalLoadMotorSimulationConfig";
    }
    std::string GetDisplaySimulatorType() override
    {
        return "Rotational Load";
    }

    RotationalLoadDcMotorSim(const DcMotorModel& aMotorModel, const std::shared_ptr<ISpeedControllerWrapper>& aSpeedController,
            const RotationalLoadMotorSimulationConfig& config);
    RotationalLoadDcMotorSim(const DcMotorModel& aMotorModel, const std::shared_ptr<ISpeedControllerWrapper>& aSpeedController, double aArmCenterOfMass,
            double aArmMass,
            double aConstantAssistTorque, double aOverCenterAssistTorque);
    virtual ~RotationalLoadDcMotorSim();

    void Update(double cycleTime) override;

    double GetArmCenterOfMass();
    double GetArmMass();

protected:
    static const double sGRAVITY;

    const std::shared_ptr<ISpeedControllerWrapper> mSpeedController;
    const double mArmInertia;
    const double mGravityBasedTorqueFactor;

    // Helper springs
    const double mConstantAssistTorque;
    const double mOverCenterAssistTorque;

    const double mArmCenterOfMass;
    const double mArmMass;
};
