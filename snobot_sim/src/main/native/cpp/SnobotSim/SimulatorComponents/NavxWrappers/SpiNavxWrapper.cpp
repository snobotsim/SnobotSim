/*
 * SpiNavxWrapper.cpp
 *
 *  Created on: Nov 23, 2017
 *      Author: PJ
 */

#include "SnobotSim/SimulatorComponents/NavxWrappers/SpiNavxWrapper.h"

SpiNavxWrapper::SpiNavxWrapper(const std::string& aBaseName, const std::string& aDeviceName, int aPort) :
        BaseNavxWrapper(aBaseName, aDeviceName, 200 + aPort * 3)
{
}

SpiNavxWrapper::~SpiNavxWrapper()
{
}
