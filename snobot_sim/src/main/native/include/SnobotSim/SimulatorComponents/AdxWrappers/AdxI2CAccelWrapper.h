/*
 * AdxI2CAccelWrapper.h
 *
 *  Created on: Nov 23, 2017
 *      Author: PJ
 */

#ifndef ADXI2CACCELWRAPPER_H_
#define ADXI2CACCELWRAPPER_H_

#include <memory>

#include "SnobotSim/SimulatorComponents/I2C/II2CWrapper.h"
#include "SnobotSim/SimulatorComponents/AdxWrappers/BaseAdxAccelWrapper.h"

class AdxI2CAccelWrapper : public II2CWrapper, public BaseAdxAccelWrapper
{
public:
	AdxI2CAccelWrapper(int aPort);
	virtual ~AdxI2CAccelWrapper();
};

#endif /* ADXI2CACCELWRAPPER_H_ */
