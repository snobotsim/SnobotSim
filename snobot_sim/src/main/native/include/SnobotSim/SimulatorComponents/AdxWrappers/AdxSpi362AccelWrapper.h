/*
 * AdxSpi363AccelWrapper.h
 *
 *  Created on: Nov 23, 2017
 *      Author: PJ
 */

#ifndef SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_SIMULATORCOMPONENTS_ADXWRAPPERS_ADXSPI362ACCELWRAPPER_H_
#define SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_SIMULATORCOMPONENTS_ADXWRAPPERS_ADXSPI362ACCELWRAPPER_H_

#include "SnobotSim/SimulatorComponents/AdxWrappers/BaseAdxAccelWrapper.h"
#include "SnobotSim/SimulatorComponents/Spi/ISpiWrapper.h"

class AdxSpi362AccelWrapper : public ISpiWrapper, public BaseAdxAccelWrapper
{
public:
    AdxSpi362AccelWrapper(int aPort);
    virtual ~AdxSpi362AccelWrapper();
};

#endif  // SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_SIMULATORCOMPONENTS_ADXWRAPPERS_ADXSPI362ACCELWRAPPER_H_
