/*
 * AccelerometerWrapper.cpp
 *
 *  Created on: May 7, 2017
 *      Author: PJ
 */

#include "SnobotSim/SimulatorComponents/Gyro/GyroWrapper.h"

GyroWrapper::GyroWrapper(const std::string& aName):
    AModuleWrapper(aName),
    mAngle(0)
{

}

GyroWrapper::~GyroWrapper()
{

}

void GyroWrapper::SetAngle(double aAngle)
{
    mAngle = aAngle;
}

double GyroWrapper::GetAngle()
{
    return mAngle;
}
