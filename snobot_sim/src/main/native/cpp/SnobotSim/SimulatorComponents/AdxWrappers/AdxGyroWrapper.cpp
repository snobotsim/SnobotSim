/*
 * AdxGyroWrapper.cpp
 *
 *  Created on: Nov 23, 2017
 *      Author: PJ
 */

#include "SnobotSim/SimulatorComponents/AdxWrappers/AdxGyroWrapper.h"

AdxGyroWrapper::AdxGyroWrapper(const std::string& aBaseName, int aPort) :
        AModuleWrapper("Gyro"),
        mSimWrapper(aBaseName, "Angle")
{
}

AdxGyroWrapper::~AdxGyroWrapper()
{
}

void AdxGyroWrapper::SetAngle(double aAngle)
{
    mSimWrapper.set(aAngle);
}

double AdxGyroWrapper::GetAngle()
{
    return mSimWrapper.get();
}
