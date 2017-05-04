/*
 * IMotorSimulator.hpp
 *
 *  Created on: May 3, 2017
 *      Author: PJ
 */

#ifndef INCLUDE_SNOBOTSIM_MOTORSIM_IMOTORSIMULATOR_HPP_
#define INCLUDE_SNOBOTSIM_MOTORSIM_IMOTORSIMULATOR_HPP_


class IMotorSimulator
{
public:

    virtual ~IMotorSimulator()
    {
    }

    virtual void setVoltagePercentage(double aSpeed) = 0;

    virtual double getVoltagePercentage() = 0;

    virtual double getAcceleration() = 0;

    virtual double getVelocity() = 0;

    virtual double getPosition() = 0;

    virtual void reset() = 0;

    virtual void reset(double aPosition, double aVelocity, double aCurrent) = 0;

    virtual void update(double aCycleTime) = 0;
};



class NullMotorSimulator: public IMotorSimulator
{
public:

    NullMotorSimulator() :
            mSpeed(0)
    {

    }

    virtual ~NullMotorSimulator()
    {
    }

    void setVoltagePercentage(double aSpeed) override
    {
        mSpeed = aSpeed;
    }

    virtual double getVoltagePercentage() override
    {
        return mSpeed;
    }

    virtual double getAcceleration() override
    {
        return 0;
    }

    virtual double getVelocity() override
    {
        return 0;
    }

    virtual double getPosition() override
    {
        return 0;
    }

    virtual void reset() override
    {

    }

    virtual void reset(double aPosition, double aVelocity, double aCurrent) override
    {

    }

    virtual void update(double aCycleTime) override
    {

    }

protected:

    double mSpeed;
};

#endif /* INCLUDE_SNOBOTSIM_MOTORSIM_IMOTORSIMULATOR_HPP_ */
