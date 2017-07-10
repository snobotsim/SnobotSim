/*
 * I2CNavxSimulator.h
 *
 *  Created on: Jul 9, 2017
 *      Author: PJ
 */

#ifndef I2CNAVXSIMULATOR_H_
#define I2CNAVXSIMULATOR_H_

#include "SnobotSim/SimulatorComponents/II2CWrapper.h"
#include "SnobotSim/SimulatorComponents/navx/NavxSimulator.h"

class EXPORT_ I2CNavxSimulator : public II2CWrapper, public NavxSimulator
{
public:
	I2CNavxSimulator(int aPort);
	virtual ~I2CNavxSimulator();

    virtual int Transaction(
            uint8_t* dataToSend, int32_t sendSize,
            uint8_t* dataReceived, int32_t receiveSize) override;

    virtual int32_t Read(
    		int32_t deviceAddress, uint8_t* buffer, int32_t count) override;

    virtual int32_t Write(
    		int32_t deviceAddress, uint8_t* dataToSend, int32_t sendSize) override;

protected:

	uint8_t mLastWriteAddress;
};

#endif /* I2CNAVXSIMULATOR_H_ */
