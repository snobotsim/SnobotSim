/*
 * I2CNavxSimulator.h
 *
 *  Created on: Jul 9, 2017
 *      Author: PJ
 */

#ifndef I2CNAVXSIMULATOR_H_
#define I2CNAVXSIMULATOR_H_

#include "SnobotSim/SimulatorComponents/I2C/II2CWrapper.h"
#include "SnobotSim/SimulatorComponents/navx/NavxSimulator.h"

class EXPORT_ I2CNavxSimulator : public II2CWrapper, public NavxSimulator
{
public:
    I2CNavxSimulator(int aI2cPort);
    virtual ~I2CNavxSimulator();

    virtual void HandleRead() override;

protected:

    uint8_t mLastWriteAddress;
};

#endif /* I2CNAVXSIMULATOR_H_ */
