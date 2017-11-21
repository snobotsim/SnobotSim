/*
 * SpiNavxSimulator.h
 *
 *  Created on: Jul 9, 2017
 *      Author: PJ
 */

#ifndef SPINAVXSIMULATOR_H_
#define SPINAVXSIMULATOR_H_

#include "NavxSim/NavxSimulator.h"

class SpiNavxSimulator : public NavxSimulator
{
public:
    SpiNavxSimulator(int aPort);
    virtual ~SpiNavxSimulator();

    void HandleRead(uint8_t* buffer, uint32_t count);
    void HandleWrite(const uint8_t* buffer, uint32_t count);

protected:

    uint8_t GetCRC(uint8_t* buffer, int length);

    uint8_t mLastWriteAddress;
};

#endif /* SPINAVXSIMULATOR_H_ */
