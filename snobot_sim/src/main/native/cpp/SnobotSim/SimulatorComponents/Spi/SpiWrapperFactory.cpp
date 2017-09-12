/*
 * SpiWrapperFactory.cpp
 *
 *  Created on: Sep 11, 2017
 *      Author: PJ
 */

#include "SnobotSim/SimulatorComponents/Spi/SpiWrapperFactory.h"

#include "SnobotSim/SimulatorComponents/Gyro/SpiGyro.h"
#include "SnobotSim/SimulatorComponents/Accelerometer/SpiAccelerometer.h"
#include "SnobotSim/SimulatorComponents/navx/SpiNavxSimulator.h"
#include "SnobotSim/GetSensorActuatorHelper.h"

const std::string SpiWrapperFactory::AUTO_DISCOVER_NAME = "AutoDiscover";
const std::string SpiWrapperFactory::SPI_GYRO_NAME = "SpiGyro";
const std::string SpiWrapperFactory::SPI_ACCELEROMETER_NAME = "SpiAccel";
const std::string SpiWrapperFactory::NAVX = "SpiNavx";

SpiWrapperFactory::SpiWrapperFactory()
{
    mDefaultsMap[0] = SPI_GYRO_NAME;
    mDefaultsMap[1] = NAVX;
    mDefaultsMap[2] = SPI_ACCELEROMETER_NAME;
}

SpiWrapperFactory::~SpiWrapperFactory()
{

}

std::shared_ptr<ISpiWrapper> SpiWrapperFactory::GetSpiWrapper(int aPort)
{
    std::shared_ptr<ISpiWrapper> spiWrapper = GetSensorActuatorHelper::GetISpiWrapper(aPort);
    if(spiWrapper)
    {
        // Already exists, and there is no auto-discovery so just return that, even if it is null
    }
    // This must be an "initialize" call, it will be registered outside of this
    else
    {
        std::map<int, std::string>::iterator iter = mDefaultsMap.find(aPort);
        if(iter != mDefaultsMap.end())
        {
            SNOBOT_LOG(SnobotLogging::INFO, "Using specified default '" << iter->second << "' on port " << aPort);
            spiWrapper = CreateWrapper(aPort, iter->second);
        }
        else
        {
            SNOBOT_LOG(SnobotLogging::CRITICAL, "No default specified, using null wrapper");
            spiWrapper = std::shared_ptr<ISpiWrapper>(new NullSpiWrapper);
        }
    }

    return spiWrapper;
}

std::shared_ptr<ISpiWrapper> SpiWrapperFactory::CreateWrapper(int aPort, const std::string& aType)
{
    if(aType == NAVX)
    {
        return std::shared_ptr<ISpiWrapper>(new SpiNavxSimulator(aPort));
    }

    if(aType == SPI_GYRO_NAME)
    {
        std::shared_ptr<SpiGyro> spiGyro(new SpiGyro(aPort));
        SensorActuatorRegistry::Get().Register(aPort + 100, std::shared_ptr<GyroWrapper>(spiGyro));

        return spiGyro;
    }
    else if(aType == SPI_ACCELEROMETER_NAME)
    {
        return std::shared_ptr<ISpiWrapper>(new SpiAccelerometer(aPort, ""));
    }

    SNOBOT_LOG(SnobotLogging::CRITICAL, "Unknown simulator type '" << aType << "', defaulting to null");
    return std::shared_ptr<ISpiWrapper>(new NullSpiWrapper);


    //        std::shared_ptr<ISpiWrapper> spiWrapper(new NullSpiWrapper);
    //        std::shared_ptr<ISpiWrapper> spiWrapper(new SpiNavxSimulator(port));
    //        SensorActuatorRegistry::Get().Register(port, spiWrapper);

}



