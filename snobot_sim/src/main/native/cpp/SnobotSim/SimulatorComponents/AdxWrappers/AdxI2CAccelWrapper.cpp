/*
 * AdxI2CAccelWrapper.cpp
 *
 *  Created on: Nov 23, 2017
 *      Author: PJ
 */

#include "SnobotSim/SimulatorComponents/AdxWrappers/AdxI2CAccelWrapper.h"

#include "ADXL345_I2CAccelerometerData.h"

AdxI2CAccelWrapper::AdxI2CAccelWrapper(int aPort) :
        BaseAdxAccelWrapper(50 + aPort * 3/*, std::shared_ptr<hal::ThreeAxisAccelerometerData>(new hal::ADXL345_I2CData(aPort))*/)
{
}

AdxI2CAccelWrapper::~AdxI2CAccelWrapper()
{
}
