/*
 * BaseDcMotorSimulator.hpp
 *
 *  Created on: May 9, 2017
 *      Author: preiniger
 */

#ifndef SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_MOTORSIM_BASEDCMOTORSIMULATOR_H_
#define SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_MOTORSIM_BASEDCMOTORSIMULATOR_H_

#include "SnobotSim/MotorSim/DcMotorModel.h"
#include "SnobotSim/MotorSim/IMotorSimulator.h"

class EXPORT_ BaseDcMotorSimulator: public IMotorSimulator
{
public:
    BaseDcMotorSimulator(
            const std::string& aSimulatorType,
            const DcMotorModel& aMotorModel,
            double aConversionFactor = 1);
    virtual ~BaseDcMotorSimulator();

    virtual const std::string& GetSimulatorType() override;

    virtual void SetVoltagePercentage(double aSpeed) override;

    virtual double GetVoltagePercentage() override;

    virtual double GetAcceleration() override;

    virtual double GetVelocity() override;

    virtual double GetPosition() override;

    virtual double GetCurrent() override;

    virtual void Reset() override;

    virtual void Reset(double aPosition, double aVelocity, double aCurrent) override;

    virtual const DcMotorModel& GetMotorModel();

protected:

    std::string mSimulatorType;
    DcMotorModel mMotorModel;
    const double mConversionFactor;
    double mVoltagePercentage;
};



#endif  // SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_MOTORSIM_BASEDCMOTORSIMULATOR_H_
