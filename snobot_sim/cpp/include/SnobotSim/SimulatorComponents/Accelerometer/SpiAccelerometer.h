/*
 * SpiAccelerometer.h
 *
 *  Created on: Jul 12, 2017
 *      Author: preiniger
 */

#ifndef SRC_SPIACCELEROMETER_H_
#define SRC_SPIACCELEROMETER_H_

#include "SnobotSim/SimulatorComponents/Accelerometer/ThreeAxisAccelerometer.h"
#include "SnobotSim/SimulatorComponents/ISpiWrapper.h"

class EXPORT_ SpiAccelerometer: public ISpiWrapper
{
public:
    SpiAccelerometer(int aPort, const std::string& aBaseName);
    virtual ~SpiAccelerometer();

    double GetAccumulatorValue() override;
    void ResetAccumulatorValue() override;

    int32_t Read(uint8_t* buffer, int32_t count) override;
    void Write(uint8_t* dataToSend, int32_t sendSize) override;

protected:

    ThreeAxisAccelerometer mThreeAxisAccelerometer;

    int mLastRegisterRequest;
};

#endif /* SRC_SPIACCELEROMETER_H_ */
