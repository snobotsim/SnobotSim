/*
 * SpiNavxWrapper.h
 *
 *  Created on: Nov 23, 2017
 *      Author: PJ
 */

#ifndef SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_SIMULATORCOMPONENTS_NAVXWRAPPERS_SPINAVXWRAPPER_H_
#define SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_SIMULATORCOMPONENTS_NAVXWRAPPERS_SPINAVXWRAPPER_H_

#include "SnobotSim/SimulatorComponents/NavxWrappers/BaseNavxWrapper.h"
#include "SnobotSim/SimulatorComponents/Spi/ISpiWrapper.h"

class SpiNavxWrapper : public ISpiWrapper, public BaseNavxWrapper
{
public:
    explicit SpiNavxWrapper(int aPort);
    virtual ~SpiNavxWrapper();
};

#endif // SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_SIMULATORCOMPONENTS_NAVXWRAPPERS_SPINAVXWRAPPER_H_
