/*
 * SerialNavxSimulator.h
 *
 *  Created on: Jul 9, 2017
 *      Author: PJ
 */

#ifndef SERIALNAVXSIMULATOR_H_
#define SERIALNAVXSIMULATOR_H_

#include "SnobotSim/SimulatorComponents/navx/NavxSimulator.h"

class EXPORT_ SerialNavxSimulator : public NavxSimulator
{
public:
	SerialNavxSimulator(int aPort);
	virtual ~SerialNavxSimulator();
};

#endif /* SERIALNAVXSIMULATOR_H_ */
