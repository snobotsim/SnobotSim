/*
 * SpeedControllerWrapper.cpp
 *
 *  Created on: May 3, 2017
 *      Author: PJ
 */

#include "SnobotSim/ModuleWrapper/SpeedControllerWrapper.h"
#include "SnobotSim/PortUnwrapper.h"

SpeedControllerWrapper::SpeedControllerWrapper(int aPort) :
        AModuleWrapper("SpeedController" + std::to_string(UnwrapPort(aPort))),
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
    return mMotorSimulator->GetVoltagePercentage();
}

void SpeedControllerWrapper::SetVoltagePercentage(double aSpeed)
{
    mMotorSimulator->SetVoltagePercentage(aSpeed);
}

void SpeedControllerWrapper::Update(double aWaitTime)
{
    mMotorSimulator->Update(aWaitTime);
}

double SpeedControllerWrapper::GetPosition()
{
    return mMotorSimulator->GetPosition();
}

double SpeedControllerWrapper::GetVelocity()
{
    return mMotorSimulator->GetVelocity();
}

void SpeedControllerWrapper::Reset()
{
    mMotorSimulator->Reset();
}

void SpeedControllerWrapper::Reset(double aPosition, double aVelocity,
        double aCurrent)
{
    mMotorSimulator->Reset(aPosition, aVelocity, aCurrent);
}
