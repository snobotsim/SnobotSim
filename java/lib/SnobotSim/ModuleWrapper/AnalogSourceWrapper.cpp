/*
 * AnalogSourceWrapper.cpp
 *
 *  Created on: May 3, 2017
 *      Author: PJ
 */

#include "SnobotSim/ModuleWrapper/AnalogSourceWrapper.h"

AnalogSourceWrapper::AnalogSourceWrapper(int aPort) :
        AModuleWrapper("Analog Source" + std::to_string(aPort)), mVoltage(0)
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
