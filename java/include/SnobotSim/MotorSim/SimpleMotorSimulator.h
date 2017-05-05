/*
 * SimpleMotorSimulator.h
 *
 *  Created on: May 5, 2017
 *      Author: PJ
 */

#ifndef INCLUDE_SIMPLEMOTORSIMULATOR_H_
#define INCLUDE_SIMPLEMOTORSIMULATOR_H_

#include "SnobotSim/MotorSim/IMotorSimulator.hpp"

class SimpleMotorSimulator: public IMotorSimulator
{
public:
    SimpleMotorSimulator(double aMaxSpeed);
    virtual ~SimpleMotorSimulator();

    void setVoltagePercentage(double aSpeed) override;

    virtual double getVoltagePercentage() override;

    virtual double getAcceleration() override;

    virtual double getVelocity() override;

    virtual double getPosition() override;

    virtual void reset() override;

    virtual void reset(double aPosition, double aVelocity, double aCurrent) override;

    virtual void update(double aCycleTime) override;

protected:
    double mMaxSpeed;

    double mVoltagePercent;
    double mVelocity;
    double mPosition;
};

#endif /* INCLUDE_SIMPLEMOTORSIMULATOR_H_ */
