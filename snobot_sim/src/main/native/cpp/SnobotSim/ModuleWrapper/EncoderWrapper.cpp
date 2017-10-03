/*
 * EncoderWrapper.cpp
 *
 *  Created on: May 4, 2017
 *      Author: PJ
 */

#include "SnobotSim/ModuleWrapper/EncoderWrapper.h"
#include "SnobotSim/PortUnwrapper.h"
#include "MockData/EncoderData.h"

EncoderWrapper::EncoderWrapper(int aPortA, int aPortB) :
        EncoderWrapper(aPortA, "Encoder " + std::to_string(UnwrapPort(aPortA)))
{

}

EncoderWrapper::EncoderWrapper(int aHandle, const std::string& aName) :
        AModuleWrapper(aName),
        mEncodingFactor(4),
        mDistancePerTick(1),
        mHandle(aHandle)
{

}

EncoderWrapper::~EncoderWrapper()
{

}


void EncoderWrapper::Reset()
{
    if (mMotorWrapper)
    {
        mMotorWrapper->Reset();
    }
}

int EncoderWrapper::GetRaw()
{
    return (int) (GetDistance() / (mEncodingFactor * mDistancePerTick));
}

double EncoderWrapper::GetDistance()
{
    if (mMotorWrapper)
    {
        return mMotorWrapper->GetPosition();
    }
    else
    {
        return 0;
    }
}


double EncoderWrapper::GetPosition()
{
    return GetDistance();
}

void EncoderWrapper::SetPosition(double aPosition)
{
    HALSIM_SetEncoderCount(mHandle, (int) aPosition);
}


double EncoderWrapper::GetVelocity()
{
    if (mMotorWrapper)
    {
        return mMotorWrapper->GetVelocity();
    }
    else
    {
        return 0;
    }
}

bool EncoderWrapper::IsHookedUp()
{
    if (mMotorWrapper)
    {
        return true;
    }
    return false;
}

void EncoderWrapper::SetSpeedController(const std::shared_ptr<SpeedControllerWrapper>& aMotorWrapper)
{
    mMotorWrapper = aMotorWrapper;
    mMotorWrapper->SetFeedbackSensor(shared_from_this());
}


const std::shared_ptr<SpeedControllerWrapper>& EncoderWrapper::GetSpeedController()
{
    return mMotorWrapper;
}

void EncoderWrapper::SetDistancePerTick(double aDPT)
{
    mDistancePerTick = aDPT;
}

double EncoderWrapper::GetDistancePerTick()
{
    return mDistancePerTick;
}
