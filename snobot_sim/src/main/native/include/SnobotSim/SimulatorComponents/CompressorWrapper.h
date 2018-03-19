/*
 * CompressorWrapper.h
 *
 *  Created on: Jun 22, 2017
 *      Author: preiniger
 */

#ifndef SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_SIMULATORCOMPONENTS_COMPRESSORWRAPPER_H_
#define SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_SIMULATORCOMPONENTS_COMPRESSORWRAPPER_H_

#include "SnobotSim/ExportHelper.h"

class EXPORT_ CompressorWrapper
{
public:
    CompressorWrapper();
    virtual ~CompressorWrapper();

    bool IsPressureSwitchFull();
};

#endif  // SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_SIMULATORCOMPONENTS_COMPRESSORWRAPPER_H_
