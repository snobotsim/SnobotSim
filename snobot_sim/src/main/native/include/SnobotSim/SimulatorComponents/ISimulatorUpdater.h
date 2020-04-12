/*
 * ISimulatorUpdater.hpp
 *
 *  Created on: May 7, 2017
 *      Author: PJ
 */

#pragma once

#include <string>

class ISimulatorUpdater
{
public:
    virtual void Update() = 0;
    virtual std::string GetSimulatorType() = 0;
};
