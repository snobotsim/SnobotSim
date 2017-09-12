/*
 * AnalogSourceWrapper.cpp
 *
 *  Created on: May 3, 2017
 *      Author: PJ
 */

#include "SnobotSim/ModuleWrapper/AnalogSourceWrapper.h"
#include "SnobotSim/PortUnwrapper.h"

AnalogSourceWrapper::AnalogSourceWrapper(int aPort) :
        AModuleWrapper("Analog " + std::to_string(UnwrapPort(aPort))), mPort(aPort), mVoltage(0)
{

}

AnalogSourceWrapper::~AnalogSourceWrapper()
{

}

void AnalogSourceWrapper::SetVoltage(double aVoltage)
{
    mVoltage = aVoltage;
}

double AnalogSourceWrapper::GetVoltage()
{
    return mVoltage;
}


int AnalogSourceWrapper::GetHandle()
{
    return mPort;
}
