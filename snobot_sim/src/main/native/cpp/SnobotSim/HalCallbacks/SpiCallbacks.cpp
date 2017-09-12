/*
 * SpiCallbacks.cpp
 *
 *  Created on: Sep 11, 2017
 *      Author: preiniger
 */

#include "SnobotSim/HalCallbacks/SpiCallbacks.h"
#include "SnobotSim/SimulatorComponents/ISpiWrapper.h"
#include "SnobotSim/SimulatorComponents/Gyro/SpiGyro.h"
#include "SnobotSim/SimulatorComponents/Accelerometer/SpiAccelerometer.h"
#include "SnobotSim/SimulatorComponents/navx/SpiNavxSimulator.h"
#include "MockData/SPIData.h"

#include "SnobotSim/SensorActuatorRegistry.h"
#include "SnobotSim/GetSensorActuatorHelper.h"
#include "SnobotSim/Logging/SnobotLogger.h"


void SpiCallback(const char* name, void* param, const struct HAL_Value* value)
{
    std::string nameStr = name;
    int port = *((int*) param);

    if ("Initialized" == nameStr)
    {
        SNOBOT_LOG(SnobotLogging::WARN, "Initializing name " << nameStr);
//        std::shared_ptr<ISpiWrapper> spiWrapper(new NullSpiWrapper);
//        std::shared_ptr<ISpiWrapper> spiWrapper(new SpiNavxSimulator(port));
//        SensorActuatorRegistry::Get().Register(port, spiWrapper);

        std::shared_ptr<SpiGyro> spiGyro(new SpiGyro(port));
        SensorActuatorRegistry::Get().Register(port + 100, std::shared_ptr<GyroWrapper>(spiGyro));
        SensorActuatorRegistry::Get().Register(port, std::shared_ptr<ISpiWrapper>(spiGyro));
    }
    else if ("Read" == nameStr)
    {
        std::shared_ptr<ISpiWrapper> spiWrapper = GetSensorActuatorHelper::GetISpiWrapper(port);
        if(spiWrapper)
        {
            spiWrapper->HandleRead();
        }
        else
        {
//            SNOBOT_LOG(SnobotLogging::WARN, "SPI not set up on " << port);
        }
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
