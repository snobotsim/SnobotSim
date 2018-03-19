/*
 * SpeedControllerWrapper.cpp
 *
 *  Created on: May 3, 2017
 *      Author: PJ
 */

#include "SnobotSim/ModuleWrapper/SpeedControllerWrapper.h"

#include "SnobotSim/PortUnwrapper.h"

SpeedControllerWrapper::SpeedControllerWrapper(int aPort) :
        AModuleWrapper("Speed Controller " + std::to_string(UnwrapPort(aPort))),
        mId(aPort),
        mMotorSimulator(new NullMotorSimulator),
        mFeedbackSensor(new NullFeedbackSensor)
{
}

SpeedControllerWrapper::~SpeedControllerWrapper()
{
}

int SpeedControllerWrapper::GetId()
{
    return mId;
}

void SpeedControllerWrapper::SetMotorSimulator(
        const std::shared_ptr<IMotorSimulator>& aSimulator)
{
    mMotorSimulator = aSimulator;
}

const std::shared_ptr<IMotorSimulator>& SpeedControllerWrapper::GetMotorSimulator()
{
    return mMotorSimulator;
}

void SpeedControllerWrapper::SetFeedbackSensor(const std::shared_ptr<IFeedbackSensor>& aFeedbackSensor)
{
    mFeedbackSensor = aFeedbackSensor;
}

const std::shared_ptr<IFeedbackSensor>& SpeedControllerWrapper::GetFeedbackSensor()
{
    return mFeedbackSensor;
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
    mFeedbackSensor->SetPosition(GetPosition());
}

double SpeedControllerWrapper::GetPosition()
{
    return mMotorSimulator->GetPosition();
}

double SpeedControllerWrapper::GetVelocity()
{
    return mMotorSimulator->GetVelocity();
}

double SpeedControllerWrapper::GetCurrent()
{
    return mMotorSimulator->GetCurrent();
}

double SpeedControllerWrapper::GetAcceleration()
{
    return mMotorSimulator->GetAcceleration();
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
