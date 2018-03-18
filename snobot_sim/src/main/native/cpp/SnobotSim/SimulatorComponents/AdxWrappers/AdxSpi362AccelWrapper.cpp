/*
 * AdxSpi362AccelWrapper.cpp
 *
 *  Created on: Nov 23, 2017
 *      Author: PJ
 */

#include "SnobotSim/SimulatorComponents/AdxWrappers/AdxSpi362AccelWrapper.h"

#include "ADXL362_SpiAccelerometerData.h"

AdxSpi362AccelWrapper::AdxSpi362AccelWrapper(int aPort)  :
    BaseAdxAccelWrapper(150 + aPort * 3, std::shared_ptr<hal::ThreeAxisAccelerometerData>(new hal::ADXL362_SpiAccelerometer(aPort)))
{

}

AdxSpi362AccelWrapper::~AdxSpi362AccelWrapper()
{

}

