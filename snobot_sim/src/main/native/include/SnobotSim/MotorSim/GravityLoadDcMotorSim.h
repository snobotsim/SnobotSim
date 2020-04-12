/*
 * GravityLoadDcMotorSim.h
 *
 *  Created on: May 9, 2017
 *      Author: preiniger
 */

#pragma once

#include <string>

#include "SnobotSim/MotorSim/BaseDcMotorSimulator.h"

class EXPORT_ GravityLoadDcMotorSim : public BaseDcMotorSimulator
{
public:
    struct GravityLoadMotorSimulationConfig
    {
        double mLoad{ 1.0 };
    };

    static std::string GetType()
    {
        return "com.snobot.simulator.motor_sim.GravityLoadMotorSimulationConfig";
    }
    std::string GetDisplaySimulatorType() override
    {
        return "Gravitational Load";
    }

    GravityLoadDcMotorSim(const DcMotorModel& aMotorModel, const GravityLoadMotorSimulationConfig& config);
    GravityLoadDcMotorSim(const DcMotorModel& aMotorModel, double aLoad);
    virtual ~GravityLoadDcMotorSim();

    void Update(double aCycleTime) override;

    double GetLoad();

protected:
    const double mLoad;

    static const double sGRAVITY;
};
