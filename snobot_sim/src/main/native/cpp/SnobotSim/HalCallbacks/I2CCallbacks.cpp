/*
 * I2CCallbacks.cpp
 *
 *  Created on: Sep 11, 2017
 *      Author: preiniger
 */

#include "SnobotSim/HalCallbacks/I2CCallbacks.h"
#include "SnobotSim/SimulatorComponents/II2CWrapper.h"
#include "SnobotSim/SimulatorComponents/navx/I2CNavxSimulator.h"
#include "MockData/I2CData.h"

#include "SnobotSim/SensorActuatorRegistry.h"
#include "SnobotSim/GetSensorActuatorHelper.h"
#include "SnobotSim/Logging/SnobotLogger.h"


void I2CCallback(const char* name, void* param, const struct HAL_Value* value)
{
    std::string nameStr = name;
    int port = *((int*) param);

    if ("Initialized" == nameStr)
    {
//        std::shared_ptr<II2CWrapper> i2cWrapper(new NullI2CWrapper);
        std::shared_ptr<II2CWrapper> i2cWrapper(new I2CNavxSimulator(port));
        SensorActuatorRegistry::Get().Register(port, i2cWrapper);
    }
    else if ("Read" == nameStr)
    {
        std::shared_ptr<II2CWrapper> i2cWrapper = GetSensorActuatorHelper::GetII2CWrapper(port);
        if(i2cWrapper)
        {
            i2cWrapper->HandleRead();
        }
        else
        {
//            SNOBOT_LOG(SnobotLogging::WARN, "I2C not set up on " << port);
        }
    }
    else
    {
        SNOBOT_LOG(SnobotLogging::WARN, "Unknown name " << nameStr);
    }
}

int gI2CInArrayIndices[2];

void SnobotSim::InitializeI2CCallbacks()
{
    for(int i = 0; i < 2; ++i)
    {
        gI2CInArrayIndices[i] = i;
        HALSIM_RegisterI2CInitializedCallback(i, &I2CCallback, &gI2CInArrayIndices[i], false);
        HALSIM_RegisterI2CReadCallback(i, &I2CCallback, &gI2CInArrayIndices[i], false);
    }
}

void SnobotSim::ResetI2CCallbacks()
{
    for(int i = 0; i < 2; ++i)
    {
        HALSIM_ResetI2CData(i);
    }
}
