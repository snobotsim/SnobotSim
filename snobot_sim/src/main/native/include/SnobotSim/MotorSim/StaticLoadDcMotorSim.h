/*
 * StaticLoadDcMotorSim.h
 *
 *  Created on: May 9, 2017
 *      Author: preiniger
 */

#ifndef SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_MOTORSIM_STATICLOADDCMOTORSIM_H_
#define SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_MOTORSIM_STATICLOADDCMOTORSIM_H_

#include "SnobotSim/MotorSim/BaseDcMotorSimulator.h"

class EXPORT_ StaticLoadDcMotorSim : public BaseDcMotorSimulator
{
public:
    StaticLoadDcMotorSim(const DcMotorModel& aMotorModel, double aLoad, double aConversionFactor = 1);
    virtual ~StaticLoadDcMotorSim();

    void Update(double aCycleTime) override;

    double GetLoad();

protected:
    const double mLoad;
};

#endif // SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_MOTORSIM_STATICLOADDCMOTORSIM_H_
