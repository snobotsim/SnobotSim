/*
 * EncoderWrapper.cpp
 *
 *  Created on: May 4, 2017
 *      Author: PJ
 */

#include "SnobotSim/ModuleWrapper/EncoderWrapper.h"

EncoderWrapper::EncoderWrapper(int aPortA, int aPortB) :
        AModuleWrapper("Encoder (" + std::to_string(aPortA) + ", " + std::to_string(aPortB) + ")"),
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
