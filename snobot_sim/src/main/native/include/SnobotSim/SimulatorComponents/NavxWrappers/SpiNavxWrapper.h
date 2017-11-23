/*
 * SpiNavxWrapper.h
 *
 *  Created on: Nov 23, 2017
 *      Author: PJ
 */

#ifndef SPINAVXWRAPPER_H_
#define SPINAVXWRAPPER_H_

#include "SnobotSim/SimulatorComponents/Spi/ISpiWrapper.h"

class SpiNavxWrapper : public ISpiWrapper
{
public:
	SpiNavxWrapper();
	virtual ~SpiNavxWrapper();
};

#endif /* SPINAVXWRAPPER_H_ */
