/*
 * BaseDcMotorSimulator.hpp
 *
 *  Created on: May 9, 2017
 *      Author: preiniger
 */

#ifndef SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_MOTORSIM_BASEDCMOTORSIMULATOR_H_
#define SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_MOTORSIM_BASEDCMOTORSIMULATOR_H_

#include <string>

#include "SnobotSim/MotorSim/DcMotorModel.h"
#include "SnobotSim/MotorSim/IMotorSimulator.h"

class EXPORT_ BaseDcMotorSimulator : public IMotorSimulator
{
public:
    BaseDcMotorSimulator(
            const std::string& aSimulatorType,
            const DcMotorModel& aMotorModel,
            double aConversionFactor = 1);
    virtual ~BaseDcMotorSimulator();

    const std::string& GetSimulatorType() override;

    void SetVoltagePercentage(double aSpeed) override;

    double GetVoltagePercentage() override;

    double GetAcceleration() override;

    double GetVelocity() override;

    double GetPosition() override;

    double GetCurrent() override;

    void Reset() override;

    void Reset(double aPosition, double aVelocity, double aCurrent) override;

    virtual const DcMotorModel& GetMotorModel();

protected:
    std::string mSimulatorType;
    DcMotorModel mMotorModel;
    const double mConversionFactor;
    double mVoltagePercentage;
};

#endif // SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_MOTORSIM_BASEDCMOTORSIMULATOR_H_
