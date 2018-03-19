/*
 * GravityLoadDcMotorSim.h
 *
 *  Created on: May 9, 2017
 *      Author: preiniger
 */

#ifndef SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_MOTORSIM_GRAVITYLOADDCMOTORSIM_H_
#define SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_MOTORSIM_GRAVITYLOADDCMOTORSIM_H_

#include "SnobotSim/MotorSim/BaseDcMotorSimulator.h"

class EXPORT_ GravityLoadDcMotorSim: public BaseDcMotorSimulator
{
public:

    GravityLoadDcMotorSim(const DcMotorModel& aMotorModel, double aLoad);
    virtual ~GravityLoadDcMotorSim();

    void Update(double aCycleTime) override;

    double GetLoad();

protected:
    const double mLoad;

    static const double sGRAVITY;
};

#endif  // SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_MOTORSIM_GRAVITYLOADDCMOTORSIM_H_
