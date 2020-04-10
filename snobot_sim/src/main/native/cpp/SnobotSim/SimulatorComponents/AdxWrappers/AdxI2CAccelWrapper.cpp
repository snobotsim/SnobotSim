/*
 * AdxI2CAccelWrapper.cpp
 *
 *  Created on: Nov 23, 2017
 *      Author: PJ
 */

#include "SnobotSim/SimulatorComponents/AdxWrappers/AdxI2CAccelWrapper.h"

AdxI2CAccelWrapper::AdxI2CAccelWrapper(const std::string& aBaseName, const std::string& aDeviceName, int aPort) :
        BaseAdxAccelWrapper(aBaseName, aDeviceName, 50 + aPort * 3)
{
}

AdxI2CAccelWrapper::~AdxI2CAccelWrapper()
{
}
