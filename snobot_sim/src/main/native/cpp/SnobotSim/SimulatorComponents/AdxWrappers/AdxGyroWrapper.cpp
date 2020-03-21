/*
 * AdxGyroWrapper.cpp
 *
 *  Created on: Nov 23, 2017
 *      Author: PJ
 */

#include "SnobotSim/SimulatorComponents/AdxWrappers/AdxGyroWrapper.h"

AdxGyroWrapper::AdxGyroWrapper(int aPort) :
        AModuleWrapper("Gyro")
{
}

AdxGyroWrapper::~AdxGyroWrapper()
{
}

void AdxGyroWrapper::SetAngle(double aAngle)
{
    // mGyro->SetAngle(aAngle);
}

double AdxGyroWrapper::GetAngle()
{
    return 0;
    // return mGyro->GetAngle();
}
