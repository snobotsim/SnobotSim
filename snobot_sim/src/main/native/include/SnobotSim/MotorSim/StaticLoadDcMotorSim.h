/*
 * StaticLoadDcMotorSim.h
 *
 *  Created on: May 9, 2017
 *      Author: preiniger
 */

#pragma once
#include "SnobotSim/MotorSim/BaseDcMotorSimulator.h"

class EXPORT_ StaticLoadDcMotorSim : public BaseDcMotorSimulator
{
public:

    static std::string GetType() { return "com.snobot.simulator.motor_sim.StaticLoadMotorSimulationConfig"; }
    std::string GetDisplaySimulatorType() override { return "Static Load"; }
    
    StaticLoadDcMotorSim(const DcMotorModel& aMotorModel, double aLoad, double aConversionFactor = 1);
    virtual ~StaticLoadDcMotorSim();

    void Update(double aCycleTime) override;

    double GetLoad();

protected:
    const double mLoad;
};
