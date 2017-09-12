/*
 * SerialNavxSimulator.cpp
 *
 *  Created on: Jul 9, 2017
 *      Author: PJ
 */

#include "SnobotSim/SimulatorComponents/navx/SerialNavxSimulator.h"

SerialNavxSimulator::SerialNavxSimulator(int aPort)  :
    NavxSimulator(aPort, 270)
{

}

SerialNavxSimulator::~SerialNavxSimulator()
{

}

