/*
 * I2CNavxSimulator.h
 *
 *  Created on: Jul 9, 2017
 *      Author: PJ
 */

#ifndef I2CNAVXSIMULATOR_H_
#define I2CNAVXSIMULATOR_H_

#include "NavxSim/NavxSimulator.h"

class I2CNavxSimulator : public NavxSimulator
{
public:
    I2CNavxSimulator(int port);
    virtual ~I2CNavxSimulator();

    virtual void HandleRead(uint8_t* buffer, uint32_t count);
    virtual void HandleWrite(const uint8_t* buffer, uint32_t count);

protected:

    uint8_t mLastWriteAddress;
};

#endif /* I2CNAVXSIMULATOR_H_ */
