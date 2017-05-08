/*
 * SimpleMotorSimulator.cpp
 *
 *  Created on: May 5, 2017
 *      Author: PJ
 */

#include "SnobotSim/MotorSim/SimpleMotorSimulator.h"

SimpleMotorSimulator::SimpleMotorSimulator(double aMaxSpeed) :
        mMaxSpeed(aMaxSpeed),
        mVoltagePercent(0),
        mVelocity(0),
        mPosition(0)
{

}

SimpleMotorSimulator::~SimpleMotorSimulator()
{

}

void SimpleMotorSimulator::setVoltagePercentage(double aSpeed)
{
    mVoltagePercent = aSpeed;
}

double SimpleMotorSimulator::getVoltagePercentage()
{
    return mVoltagePercent;
}

double SimpleMotorSimulator::getAcceleration()
{
    return 0;
}

double SimpleMotorSimulator::getVelocity()
{
    return mVelocity;
}

double SimpleMotorSimulator::getPosition()
{
    return mPosition;
}

void SimpleMotorSimulator::reset()
{
    reset(0, 0, 0);
}

void SimpleMotorSimulator::reset(double aPosition, double aVelocity, double aCurrent)
{
    mPosition = aPosition;
    mVelocity = aVelocity;
}

void SimpleMotorSimulator::update(double aUpdateTime)
{
    mVelocity = mMaxSpeed * mVoltagePercent;
    mPosition += mVelocity * aUpdateTime;
}
