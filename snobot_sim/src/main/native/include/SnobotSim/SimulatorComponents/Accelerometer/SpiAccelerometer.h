/*
 * SpiAccelerometer.h
 *
 *  Created on: Jul 12, 2017
 *      Author: preiniger
 */

#ifndef SRC_SPIACCELEROMETER_H_
#define SRC_SPIACCELEROMETER_H_

#include "SnobotSim/SimulatorComponents/Accelerometer/ThreeAxisAccelerometer.h"
#include "SnobotSim/SimulatorComponents/Spi/ISpiWrapper.h"

class EXPORT_ SpiAccelerometer: public ISpiWrapper
{
public:
    SpiAccelerometer(int aPort, const std::string& aBaseName);
    virtual ~SpiAccelerometer();

    virtual void HandleRead() override;

protected:

    ThreeAxisAccelerometer mThreeAxisAccelerometer;

    const int mSpiPort;
    int mLastRegisterRequest;
};

#endif /* SRC_SPIACCELEROMETER_H_ */
