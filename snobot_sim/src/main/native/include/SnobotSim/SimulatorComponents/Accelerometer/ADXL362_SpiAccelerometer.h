/*
 * ADXL362_SpiAccelerometer.h
 *
 *  Created on: Jul 12, 2017
 *      Author: preiniger
 */

#ifndef SRC_ADXL362_SPIACCELEROMETER_H_
#define SRC_ADXL362_SPIACCELEROMETER_H_

#include "SnobotSim/SimulatorComponents/Accelerometer/ThreeAxisAccelerometer.h"
#include "SnobotSim/SimulatorComponents/Spi/ISpiWrapper.h"

class EXPORT_ ADXL362_SpiAccelerometer: public ISpiWrapper
{
public:
    ADXL362_SpiAccelerometer(int aPort, const std::string& aBaseName);
    virtual ~ADXL362_SpiAccelerometer();

    virtual void HandleRead() override;

protected:

    ThreeAxisAccelerometer mThreeAxisAccelerometer;

    const int mSpiPort;
};

#endif /* SRC_ADXL362_SpiAccelerometer_H_ */
