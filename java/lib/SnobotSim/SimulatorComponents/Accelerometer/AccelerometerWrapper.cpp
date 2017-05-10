/*
 * AccelerometerWrapper.cpp
 *
 *  Created on: May 7, 2017
 *      Author: PJ
 */

#include "SnobotSim/SimulatorComponents/Accelerometer/AccelerometerWrapper.h"

AccelerometerWrapper::AccelerometerWrapper(const std::string& aName):
    AModuleWrapper(aName),
    mAcceleration(0)
{

}

AccelerometerWrapper::~AccelerometerWrapper()
{

}

void AccelerometerWrapper::SetAcceleration(double aAcceleration)
{
    mAcceleration = aAcceleration;
}

double AccelerometerWrapper::GetAcceleration()
{
    return mAcceleration;
}
