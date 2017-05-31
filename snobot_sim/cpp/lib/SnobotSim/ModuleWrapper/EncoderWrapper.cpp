/*
 * EncoderWrapper.cpp
 *
 *  Created on: May 4, 2017
 *      Author: PJ
 */

#include "SnobotSim/ModuleWrapper/EncoderWrapper.h"
#include "SnobotSim/PortUnwrapper.h"

EncoderWrapper::EncoderWrapper(int aPortA, int aPortB) :
        EncoderWrapper("Encoder (" + std::to_string(UnwrapPort(aPortA)) + ", " + std::to_string(UnwrapPort(aPortB)) + ")")
{

}

EncoderWrapper::EncoderWrapper(const std::string& aName) :
        AModuleWrapper(aName),
        mEncodingFactor(4),
        mDistancePerTick(1)
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
}
