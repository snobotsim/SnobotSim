/*
 * AdxSpiAccelWrapper.cpp
 *
 *  Created on: Nov 23, 2017
 *      Author: PJ
 */

#include "SnobotSim/SimulatorComponents/AdxWrappers/AdxSpiAccelWrapper.h"

AdxSpiAccelWrapper::AdxSpiAccelWrapper(const std::string& aType, int aPort)
{

    if(aType == "345")
    {
    	m345Accel = std::shared_ptr<hal::ADXL345_SpiAccelerometer>(new hal::ADXL345_SpiAccelerometer(aPort));
    }
    else
    {
    	m362Accel = std::shared_ptr<hal::ADXL362_SpiAccelerometer>(new hal::ADXL362_SpiAccelerometer(aPort));
    }
}

AdxSpiAccelWrapper::~AdxSpiAccelWrapper() {

}

