/*
 * I2CNavxWrapper.cpp
 *
 *  Created on: Nov 23, 2017
 *      Author: PJ
 */

#include "SnobotSim/SimulatorComponents/NavxWrappers/I2CNavxWrapper.h"

I2CNavxWrapper::I2CNavxWrapper(int aPort) :
        BaseNavxWrapper(250 + aPort * 3)
{
}

I2CNavxWrapper::~I2CNavxWrapper()
{
}
