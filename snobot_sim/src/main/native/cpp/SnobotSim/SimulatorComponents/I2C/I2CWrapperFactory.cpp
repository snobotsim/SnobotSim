/*
 * I2CWrapperFactory.cpp
 *
 *  Created on: Sep 11, 2017
 *      Author: PJ
 */

#include "SnobotSim/SimulatorComponents/I2C/I2CWrapperFactory.h"
#include "SnobotSim/SimulatorComponents/navx/I2CNavxSimulator.h"
#include "SnobotSim/SimulatorComponents/Accelerometer/I2CAccelerometer.h"
#include "SnobotSim/GetSensorActuatorHelper.h"

#include <iostream>

const std::string I2CWrapperFactory::I2C_ACCELEROMETER_NAME = "I2C Accelerometer";
const std::string I2CWrapperFactory::NAVX = "I2C NavX";

I2CWrapperFactory::I2CWrapperFactory()
{
    mDefaultsMap[0] = NAVX;
    mDefaultsMap[1] = I2C_ACCELEROMETER_NAME;
}

I2CWrapperFactory::~I2CWrapperFactory()
{

}

std::shared_ptr<II2CWrapper> I2CWrapperFactory::GetI2CWrapper(int aPort)
{
    std::shared_ptr<II2CWrapper> i2cWrapper = GetSensorActuatorHelper::GetII2CWrapper(aPort);
    if(i2cWrapper)
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
            i2cWrapper = CreateWrapper(aPort, iter->second);
        }
        else
        {
            SNOBOT_LOG(SnobotLogging::CRITICAL, "No default specified, using null wrapper");
            i2cWrapper = std::shared_ptr<II2CWrapper>(new NullI2CWrapper);
        }
    }

    return i2cWrapper;
}

std::shared_ptr<II2CWrapper> I2CWrapperFactory::CreateWrapper(int aPort, const std::string& aType)
{
    if(aType == NAVX)
    {
        return std::shared_ptr<II2CWrapper>(new I2CNavxSimulator(aPort));
    }

    if(aType == I2C_ACCELEROMETER_NAME)
    {
        return std::shared_ptr<II2CWrapper>(new I2CAccelerometer(aPort, ""));
    }

    SNOBOT_LOG(SnobotLogging::CRITICAL, "Unknown simulator type '" << aType << "', defaulting to null");
    return std::shared_ptr<II2CWrapper>(new NullI2CWrapper);
}

