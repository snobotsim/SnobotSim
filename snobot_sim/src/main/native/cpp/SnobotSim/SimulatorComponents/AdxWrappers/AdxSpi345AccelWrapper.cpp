/*
 * AdxSpi345AccelWrapper.cpp
 *
 *  Created on: Nov 23, 2017
 *      Author: PJ
 */

#include "SnobotSim/SimulatorComponents/AdxWrappers/AdxSpi345AccelWrapper.h"

#include "ADXL345_SpiAccelerometerData.h"

AdxSpi345AccelWrapper::AdxSpi345AccelWrapper(int aPort)  :
	BaseAdxAccelWrapper(100 + aPort * 3, std::shared_ptr<hal::ThreeAxisAccelerometerData>(new hal::ADXL345_SpiAccelerometer(aPort)))
{

}

AdxSpi345AccelWrapper::~AdxSpi345AccelWrapper() {

}

