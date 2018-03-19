/*
 * II2CWrapper.h
 *
 *  Created on: Jun 22, 2017
 *      Author: preiniger
 */

#ifndef SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_SIMULATORCOMPONENTS_I2C_II2CWRAPPER_H_
#define SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_SIMULATORCOMPONENTS_I2C_II2CWRAPPER_H_

#include <stdint.h>

#include "SnobotSim/ExportHelper.h"

class II2CWrapper
{
public:
    virtual ~II2CWrapper()
    {
    }
};

class EXPORT_ NullI2CWrapper : public II2CWrapper
{
};

#endif // SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_SIMULATORCOMPONENTS_I2C_II2CWRAPPER_H_
