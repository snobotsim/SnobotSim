/*
 * IMotorSimulator.hpp
 *
 *  Created on: May 3, 2017
 *      Author: PJ
 */

#ifndef INCLUDE_SNOBOTSIM_MOTORSIM_IMOTORSIMULATOR_HPP_
#define INCLUDE_SNOBOTSIM_MOTORSIM_IMOTORSIMULATOR_HPP_

#include <string>

class IMotorSimulator
{
public:

    virtual ~IMotorSimulator()
    {
    }

    virtual const std::string& GetSimulatorType() = 0;

    virtual void SetVoltagePercentage(double aSpeed) = 0;

    virtual double GetVoltagePercentage() = 0;

    virtual double GetAcceleration() = 0;

    virtual double GetVelocity() = 0;

    virtual double GetPosition() = 0;

    virtual double GetCurrent() = 0;

    virtual void Reset() = 0;

    virtual void Reset(double aPosition, double aVelocity, double aCurrent) = 0;

    virtual void Update(double aCycleTime) = 0;
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

    const std::string& GetSimulatorType() override
    {
        static std::string sName = "Null";
        return sName;
    }

    void SetVoltagePercentage(double aSpeed) override
    {
        mSpeed = aSpeed;
    }

    virtual double GetVoltagePercentage() override
    {
        return mSpeed;
    }

    virtual double GetAcceleration() override
    {
        return 0;
    }

    virtual double GetVelocity() override
    {
        return 0;
    }

    virtual double GetPosition() override
    {
        return 0;
    }

    virtual double GetCurrent() override
    {
    	return 0;
    }

    virtual void Reset() override
    {

    }

    virtual void Reset(double aPosition, double aVelocity, double aCurrent) override
    {

    }

    virtual void Update(double aCycleTime) override
    {

    }

protected:

    double mSpeed;
};

#endif /* INCLUDE_SNOBOTSIM_MOTORSIM_IMOTORSIMULATOR_HPP_ */
