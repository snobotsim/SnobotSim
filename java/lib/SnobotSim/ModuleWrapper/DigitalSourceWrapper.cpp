/*
 * DigitalSourceWrapper.cpp
 *
 *  Created on: May 3, 2017
 *      Author: PJ
 */

#include "SnobotSim/ModuleWrapper/DigitalSourceWrapper.h"

DigitalSourceWrapper::DigitalSourceWrapper(int aPort) :
        AModuleWrapper("Digital Source" + std::to_string(aPort))
{

}

DigitalSourceWrapper::~DigitalSourceWrapper()
{

}

