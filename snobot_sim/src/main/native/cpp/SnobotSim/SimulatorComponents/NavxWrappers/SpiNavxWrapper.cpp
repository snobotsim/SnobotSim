/*
 * SpiNavxWrapper.cpp
 *
 *  Created on: Nov 23, 2017
 *      Author: PJ
 */

#include "SnobotSim/SimulatorComponents/NavxWrappers/SpiNavxWrapper.h"

SpiNavxWrapper::SpiNavxWrapper(int aPort) :
        BaseNavxWrapper(200 + aPort * 3)
{
}

SpiNavxWrapper::~SpiNavxWrapper()
{
}
