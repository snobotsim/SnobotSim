/*
 * RelayWrapper.cpp
 *
 *  Created on: May 3, 2017
 *      Author: PJ
 */

#include "SnobotSim/ModuleWrapper/RelayWrapper.h"

#include "SnobotSim/PortUnwrapper.h"

RelayWrapper::RelayWrapper(int aPort) :
        AModuleWrapper("Relay " + std::to_string(UnwrapPort(aPort))),
        mForwards(false),
        mReverse(false)
{
}

RelayWrapper::~RelayWrapper()
{
}

void RelayWrapper::SetRelayForwards(bool aOn)
{
    mForwards = aOn;
}

void RelayWrapper::SetRelayReverse(bool aOn)
{
    mReverse = aOn;
}

bool RelayWrapper::GetRelayForwards()
{
    return mForwards;
}

bool RelayWrapper::GetRelayReverse()
{
    return mReverse;
}
