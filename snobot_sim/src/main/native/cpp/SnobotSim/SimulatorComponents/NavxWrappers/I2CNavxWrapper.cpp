/*
 * I2CNavxWrapper.cpp
 *
 *  Created on: Nov 23, 2017
 *      Author: PJ
 */

#include "SnobotSim/SimulatorComponents/NavxWrappers/I2CNavxWrapper.h"

#include "NavxSim/I2CNavxSimulator.h"

I2CNavxWrapper::I2CNavxWrapper(int aPort) :
        BaseNavxWrapper(250 + aPort * 3, std::shared_ptr<NavxSimulator>(new I2CNavxSimulator(aPort)))
{
}

I2CNavxWrapper::~I2CNavxWrapper()
{
}
