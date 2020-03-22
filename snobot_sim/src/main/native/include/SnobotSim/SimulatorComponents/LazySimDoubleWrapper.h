/*
 * TankDriveSimulator.h
 *
 *  Created on: May 7, 2017
 *      Author: PJ
 */

#pragma once
#include <memory>
#include <string>

#include "SnobotSim/ExportHelper.h"
#include "simulation/SimDeviceSim.h"

class EXPORT_ LazySimDoubleWrapper
{
public:
    LazySimDoubleWrapper(
            const std::string& aDeviceName,
            const std::string& aValueName);

    virtual ~LazySimDoubleWrapper() = default;

    double get();

    void set(double aValue);

protected:
    void setupDeviceSim();
    void setupSimValue();

    std::string mDeviceName;
    std::string mValueName;

    HAL_SimDeviceHandle mDeviceSimHandle{ 0 };
    hal::SimDouble mSimDouble;
};
