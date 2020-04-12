/*
 * StaticLoadDcMotorSim.h
 *
 *  Created on: May 9, 2017
 *      Author: preiniger
 */

#pragma once

#include <string>

#include "SnobotSim/MotorSim/BaseDcMotorSimulator.h"

class EXPORT_ StaticLoadDcMotorSim : public BaseDcMotorSimulator
{
public:

struct StaticLoadMotorSimulationConfig
{
    double mLoad{1.0};
    double mConversionFactor{1.0};
};

    static std::string GetType()
    {
        return "com.snobot.simulator.motor_sim.StaticLoadMotorSimulationConfig";
    }
    std::string GetDisplaySimulatorType() override
    {
        return "Static Load";
    }

    StaticLoadDcMotorSim(const DcMotorModel& aMotorModel, const StaticLoadMotorSimulationConfig& config);
    StaticLoadDcMotorSim(const DcMotorModel& aMotorModel, double aLoad, double aConversionFactor);
    virtual ~StaticLoadDcMotorSim();

    void Update(double aCycleTime) override;

    double GetLoad();

protected:
    const double mLoad;
};
