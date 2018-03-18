/*
 * I2CNavxWrapper.h
 *
 *  Created on: Nov 23, 2017
 *      Author: PJ
 */

#ifndef I2CNAVXWRAPPER_H_
#define I2CNAVXWRAPPER_H_

#include "SnobotSim/SimulatorComponents/NavxWrappers/BaseNavxWrapper.h"
#include "SnobotSim/SimulatorComponents/I2C/II2CWrapper.h"

class I2CNavxWrapper : public II2CWrapper, public BaseNavxWrapper
{
public:
    I2CNavxWrapper(int aPort);
    virtual ~I2CNavxWrapper();
};

#endif /* I2CNAVXWRAPPER_H_ */
