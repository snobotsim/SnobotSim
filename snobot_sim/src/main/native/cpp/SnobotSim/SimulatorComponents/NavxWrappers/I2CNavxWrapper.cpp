/*
 * I2CNavxWrapper.cpp
 *
 *  Created on: Nov 23, 2017
 *      Author: PJ
 */

#include "SnobotSim/SimulatorComponents/NavxWrappers/I2CNavxWrapper.h"

I2CNavxWrapper::I2CNavxWrapper(const std::string& aBaseName, const std::string& aDeviceName, int aPort) :
        BaseNavxWrapper(aBaseName, aDeviceName, 250 + aPort * 3)
{
}

I2CNavxWrapper::~I2CNavxWrapper()
{
}
