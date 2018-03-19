/*
 * I2CCallbacks.h
 *
 *  Created on: Sep 11, 2017
 *      Author: preiniger
 */

#ifndef SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_HALCALLBACKS_I2CCALLBACKS_H_
#define SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_HALCALLBACKS_I2CCALLBACKS_H_

#include "SnobotSim/ExportHelper.h"

namespace SnobotSim
{
    EXPORT_ void InitializeI2CCallbacks();
    EXPORT_ void ResetI2CCallbacks();
}  // namespace SnobotSim

#endif  // SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_HALCALLBACKS_I2CCALLBACKS_H_
