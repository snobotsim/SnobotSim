/*
 * AdxSpi362AccelWrapper.cpp
 *
 *  Created on: Nov 23, 2017
 *      Author: PJ
 */

#include "SnobotSim/SimulatorComponents/AdxWrappers/AdxSpi362AccelWrapper.h"

AdxSpi362AccelWrapper::AdxSpi362AccelWrapper(const std::string& aBaseName, const std::string& aDeviceName, int aPort) :
        BaseAdxAccelWrapper(aBaseName, aDeviceName, 150 + aPort * 3)
{
}

AdxSpi362AccelWrapper::~AdxSpi362AccelWrapper()
{
}
