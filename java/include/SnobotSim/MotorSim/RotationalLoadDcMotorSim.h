/*
 * RotationalLoadDcMotorSim.h
 *
 *  Created on: May 9, 2017
 *      Author: preiniger
 */

#ifndef ROTATIONALLOADDCMOTORSIM_H_
#define ROTATIONALLOADDCMOTORSIM_H_

#include <memory>
#include "SnobotSim/MotorSim/BaseDcMotorSimulator.h"
#include "SnobotSim/ModuleWrapper/SpeedControllerWrapper.h"

class RotationalLoadDcMotorSim: public BaseDcMotorSimulator
{
public:
    RotationalLoadDcMotorSim(const DcMotorModel& aMotorModel, const std::shared_ptr<SpeedControllerWrapper>& aSpeedController, double aArmCenterOfMass,
            double aArmMass,
            double aConstantAssistTorque, double aOverCenterAssistTorque);
    virtual ~RotationalLoadDcMotorSim();

    void Update(double cycleTime) override;

protected:
    static const double sGRAVITY;

    const std::shared_ptr<SpeedControllerWrapper> mSpeedController;
    const double mArmInertia;
    const double mGravityBasedTorqueFactor;

    // Helper springs
    const double mConstantAssistTorque;
    const double mOverCenterAssistTorque;
};

#endif /* ROTATIONALLOADDCMOTORSIM_H_ */
