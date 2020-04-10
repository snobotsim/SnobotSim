/*
 * AdxSpi345AccelWrapper.cpp
 *
 *  Created on: Nov 23, 2017
 *      Author: PJ
 */

#include "SnobotSim/SimulatorComponents/AdxWrappers/AdxSpi345AccelWrapper.h"

AdxSpi345AccelWrapper::AdxSpi345AccelWrapper(const std::string& aBaseName, const std::string& aDeviceName, int aPort) :
        BaseAdxAccelWrapper(aBaseName, aDeviceName, 100 + aPort * 3)
{
}

AdxSpi345AccelWrapper::~AdxSpi345AccelWrapper()
{
}
