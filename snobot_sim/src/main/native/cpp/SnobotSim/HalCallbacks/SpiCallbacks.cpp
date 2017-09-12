/*
 * SpiCallbacks.cpp
 *
 *  Created on: Sep 11, 2017
 *      Author: preiniger
 */

#include "SnobotSim/HalCallbacks/SpiCallbacks.h"
#include "SnobotSim/SimulatorComponents/Spi/ISpiWrapper.h"
#include "SnobotSim/SimulatorComponents/Spi/SpiWrapperFactory.h"
#include "MockData/SPIData.h"

#include "SnobotSim/SensorActuatorRegistry.h"
#include "SnobotSim/GetSensorActuatorHelper.h"
#include "SnobotSim/Logging/SnobotLogger.h"


void SpiCallback(const char* name, void* param, const struct HAL_Value* value)
{
    static SpiWrapperFactory gSpiWrapperFactory;

    std::string nameStr = name;
    int port = *((int*) param);

    if ("Initialized" == nameStr)
    {
        std::shared_ptr<ISpiWrapper> spiWrapper = gSpiWrapperFactory.GetSpiWrapper(port);
        SensorActuatorRegistry::Get().Register(port, spiWrapper);
    }
    else if ("Read" == nameStr)
    {
        std::shared_ptr<ISpiWrapper> spiWrapper = gSpiWrapperFactory.GetSpiWrapper(port);
        spiWrapper->HandleRead();
    }
    else
    {
        SNOBOT_LOG(SnobotLogging::WARN, "Unknown name " << nameStr);
    }
}

int gSpiInArrayIndices[5];

void SnobotSim::InitializeSpiCallbacks()
{
    for(int i = 0; i < 5; ++i)
    {
        gSpiInArrayIndices[i] = i;
        HALSIM_RegisterSPIInitializedCallback(i, &SpiCallback, &gSpiInArrayIndices[i], false);
        HALSIM_RegisterSPIReadCallback(i, &SpiCallback, &gSpiInArrayIndices[i], false);
    }
}

void SnobotSim::ResetSpiCallbacks()
{
    for(int i = 0; i < 5; ++i)
    {
        HALSIM_ResetSPIData(i);
    }
}
