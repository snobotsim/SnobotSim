/*
 * AdxSpi363AccelWrapper.h
 *
 *  Created on: Nov 23, 2017
 *      Author: PJ
 */

#pragma once
#include <string>

#include "SnobotSim/ModuleWrapper/Interfaces/ISpiWrapper.h"
#include "SnobotSim/SimulatorComponents/AdxWrappers/BaseAdxAccelWrapper.h"

class AdxSpi362AccelWrapper : public ISpiWrapper, public BaseAdxAccelWrapper
{
public:
    explicit AdxSpi362AccelWrapper(const std::string& aBaseName, const std::string& aDeviceName, int aPort);
    virtual ~AdxSpi362AccelWrapper();
};
