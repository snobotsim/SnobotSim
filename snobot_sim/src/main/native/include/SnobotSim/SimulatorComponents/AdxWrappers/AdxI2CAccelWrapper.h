/*
 * AdxI2CAccelWrapper.h
 *
 *  Created on: Nov 23, 2017
 *      Author: PJ
 */

#pragma once
#include <memory>
#include <string>

#include "SnobotSim/ModuleWrapper/Interfaces/II2CWrapper.h"
#include "SnobotSim/SimulatorComponents/AdxWrappers/BaseAdxAccelWrapper.h"

class AdxI2CAccelWrapper : public II2CWrapper, public BaseAdxAccelWrapper
{
public:
    explicit AdxI2CAccelWrapper(const std::string& aBaseName, const std::string& aDeviceName, int aPort);
    virtual ~AdxI2CAccelWrapper();
};
