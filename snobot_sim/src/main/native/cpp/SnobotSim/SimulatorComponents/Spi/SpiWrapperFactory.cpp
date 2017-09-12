/*
 * SpiWrapperFactory.cpp
 *
 *  Created on: Sep 11, 2017
 *      Author: PJ
 */

#include "SnobotSim/SimulatorComponents/Spi/SpiWrapperFactory.h"

#include "SnobotSim/SimulatorComponents/Gyro/ADXRS450_SpiGyro.h"
#include "SnobotSim/SimulatorComponents/Accelerometer/ADXL345_SpiAccelerometer.h"
#include "SnobotSim/SimulatorComponents/Accelerometer/ADXL362_SpiAccelerometer.h"
#include "SnobotSim/SimulatorComponents/navx/SpiNavxSimulator.h"
#include "SnobotSim/GetSensorActuatorHelper.h"

const std::string SpiWrapperFactory::AUTO_DISCOVER_NAME = "AutoDiscover";
const std::string SpiWrapperFactory::ADXRS450_GYRO_NAME = "ADXRS450";
const std::string SpiWrapperFactory::ADXL345_ACCELEROMETER_NAME = "ADXL345";
const std::string SpiWrapperFactory::ADXL362_ACCELEROMETER_NAME = "ADXL362";
const std::string SpiWrapperFactory::NAVX = "NavX";


SpiWrapperFactory SpiWrapperFactory::sINSTANCE;

SpiWrapperFactory::SpiWrapperFactory()
{

}

SpiWrapperFactory::~SpiWrapperFactory()
{

}

SpiWrapperFactory& SpiWrapperFactory::Get()
{
    return sINSTANCE;
}

void SpiWrapperFactory::RegisterDefaultWrapperType(int aPort, const std::string& aWrapperType)
{
    SNOBOT_LOG(SnobotLogging::DEBUG, "Setting default for port " << aPort << " to '" << aWrapperType);
    mDefaultsMap[aPort] = aWrapperType;
}

void SpiWrapperFactory::ResetDefaults()
{
    mDefaultsMap.clear();
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
            SNOBOT_LOG(SnobotLogging::CRITICAL, "No default specified for " << aPort << ", using null wrapper");
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

    if(aType == ADXRS450_GYRO_NAME)
    {
        std::shared_ptr<ADXRS450_SpiGyro> spiGyro(new ADXRS450_SpiGyro(aPort));
        SensorActuatorRegistry::Get().Register(aPort + 100, std::shared_ptr<GyroWrapper>(spiGyro));

        return spiGyro;
    }
    else if(aType == ADXL345_ACCELEROMETER_NAME)
    {
        return std::shared_ptr<ISpiWrapper>(new ADXL345_SpiAccelerometer(aPort, ""));
    }
    else if(aType == ADXL362_ACCELEROMETER_NAME)
    {
        return std::shared_ptr<ISpiWrapper>(new ADXL362_SpiAccelerometer(aPort, ""));
    }


    SNOBOT_LOG(SnobotLogging::CRITICAL, "Unknown simulator type '" << aType << "', defaulting to null");
    return std::shared_ptr<ISpiWrapper>(new NullSpiWrapper);


    //        std::shared_ptr<ISpiWrapper> spiWrapper(new NullSpiWrapper);
    //        std::shared_ptr<ISpiWrapper> spiWrapper(new SpiNavxSimulator(port));
    //        SensorActuatorRegistry::Get().Register(port, spiWrapper);

}



