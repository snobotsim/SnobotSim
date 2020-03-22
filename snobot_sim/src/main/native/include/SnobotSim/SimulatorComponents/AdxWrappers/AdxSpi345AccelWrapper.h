/*
 * AdxSpi345AccelWrapper.h
 *
 *  Created on: Nov 23, 2017
 *      Author: PJ
 */

#pragma once
#include <string>

#include "SnobotSim/ModuleWrapper/Interfaces/ISpiWrapper.h"
#include "SnobotSim/SimulatorComponents/AdxWrappers/BaseAdxAccelWrapper.h"

class AdxSpi345AccelWrapper : public ISpiWrapper, public BaseAdxAccelWrapper
{
public:
    explicit AdxSpi345AccelWrapper(const std::string& aBaseName, const std::string& aDeviceName, int aPort);
    virtual ~AdxSpi345AccelWrapper();
};
