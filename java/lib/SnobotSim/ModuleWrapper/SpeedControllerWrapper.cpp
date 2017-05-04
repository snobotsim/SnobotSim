/*
 * SpeedControllerWrapper.cpp
 *
 *  Created on: May 3, 2017
 *      Author: PJ
 */

#include "SnobotSim/ModuleWrapper/SpeedControllerWrapper.h"

SpeedControllerWrapper::SpeedControllerWrapper(int aPort) :
        AModuleWrapper("SpeedController" + std::to_string(aPort)),
        mMotorSimulator(new NullMotorSimulator)
{

}

SpeedControllerWrapper::~SpeedControllerWrapper()
{

}

void SpeedControllerWrapper::SetMotorSimulator(
        const std::shared_ptr<IMotorSimulator>& aSimulator)
{
    mMotorSimulator = aSimulator;
}

double SpeedControllerWrapper::GetVoltagePercentage()
{
    return mMotorSimulator->getVoltagePercentage();
}

void SpeedControllerWrapper::SetVoltagePercentage(double aSpeed)
{
    mMotorSimulator->setVoltagePercentage(aSpeed);
}

void SpeedControllerWrapper::Update(double aWaitTime)
{
    mMotorSimulator->update(aWaitTime);
}

double SpeedControllerWrapper::GetPosition()
{
    return mMotorSimulator->getPosition();
}

double SpeedControllerWrapper::GetVelocity()
{
    return mMotorSimulator->getVelocity();
}

void SpeedControllerWrapper::Reset()
{
    mMotorSimulator->reset();
}

void SpeedControllerWrapper::Reset(double aPosition, double aVelocity,
        double aCurrent)
{
    mMotorSimulator->reset(aPosition, aVelocity, aCurrent);
}
