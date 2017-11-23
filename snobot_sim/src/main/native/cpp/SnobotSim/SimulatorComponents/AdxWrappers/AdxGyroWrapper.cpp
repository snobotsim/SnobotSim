/*
 * AdxGyroWrapper.cpp
 *
 *  Created on: Nov 23, 2017
 *      Author: PJ
 */

#include "SnobotSim/SimulatorComponents/AdxWrappers/AdxGyroWrapper.h"

AdxGyroWrapper::AdxGyroWrapper(int aPort) :
	mGyro(new hal::ADXRS450_SpiGyroWrapper(aPort))
{

}

AdxGyroWrapper::~AdxGyroWrapper() {

}

