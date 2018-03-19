/*
 * SolenoidWrapper.cpp
 *
 *  Created on: May 3, 2017
 *      Author: PJ
 */

#include "SnobotSim/ModuleWrapper/SolenoidWrapper.h"

#include "SnobotSim/PortUnwrapper.h"

SolenoidWrapper::SolenoidWrapper(int aPort) :
        AModuleWrapper("Solenoid " + std::to_string(UnwrapPort(aPort))),
        mState(false)
{
}

SolenoidWrapper::~SolenoidWrapper()
{
}

void SolenoidWrapper::SetState(bool aOn)
{
    mState = aOn;
}

bool SolenoidWrapper::GetState()
{
    return mState;
}
