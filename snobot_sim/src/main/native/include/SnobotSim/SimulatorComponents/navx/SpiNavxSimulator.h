/*
 * SpiNavxSimulator.h
 *
 *  Created on: Jul 9, 2017
 *      Author: PJ
 */

#ifndef SPINAVXSIMULATOR_H_
#define SPINAVXSIMULATOR_H_

#include "SnobotSim/SimulatorComponents/ISpiWrapper.h"
#include "SnobotSim/SimulatorComponents/navx/NavxSimulator.h"

class EXPORT_ SpiNavxSimulator : public ISpiWrapper, public NavxSimulator
{
public:
    SpiNavxSimulator(int aPort);
    virtual ~SpiNavxSimulator();

    double GetAccumulatorValue() override;
    void ResetAccumulatorValue() override;

    int32_t Read(uint8_t* buffer, int32_t count) override;
    virtual void Write(uint8_t* dataToSend, int32_t sendSize) override;

protected:

    uint8_t GetCRC(uint8_t* buffer, int length);

    uint8_t mLastWriteAddress;
};

#endif /* SPINAVXSIMULATOR_H_ */
