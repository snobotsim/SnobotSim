/*
 * SimpleMotorSimulator.h
 *
 *  Created on: May 5, 2017
 *      Author: PJ
 */

#ifndef INCLUDE_SIMPLEMOTORSIMULATOR_H_
#define INCLUDE_SIMPLEMOTORSIMULATOR_H_

#include "SnobotSim/MotorSim/IMotorSimulator.h"

class SimpleMotorSimulator: public IMotorSimulator
{
public:
    SimpleMotorSimulator(double aMaxSpeed);
    virtual ~SimpleMotorSimulator();

    const std::string& GetSimulatorType() override;

    void SetVoltagePercentage(double aSpeed) override;

    virtual double GetVoltagePercentage() override;

    virtual double GetAcceleration() override;

    virtual double GetVelocity() override;

    virtual double GetPosition() override;

    virtual void Reset() override;

    virtual void Reset(double aPosition, double aVelocity, double aCurrent) override;

    virtual void Update(double aCycleTime) override;

protected:
    double mMaxSpeed;

    double mVoltagePercent;
    double mVelocity;
    double mPosition;
};

#endif /* INCLUDE_SIMPLEMOTORSIMULATOR_H_ */
