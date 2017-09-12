/*
 * ADXL345_SpiAccelerometer.h
 *
 *  Created on: Jul 12, 2017
 *      Author: preiniger
 */

#ifndef SRC_ADXL345_SpiAccelerometer_H_
#define SRC_ADXL345_SpiAccelerometer_H_

#include "SnobotSim/SimulatorComponents/Accelerometer/ThreeAxisAccelerometer.h"
#include "SnobotSim/SimulatorComponents/Spi/ISpiWrapper.h"

class EXPORT_ ADXL345_SpiAccelerometer: public ISpiWrapper
{
public:
    ADXL345_SpiAccelerometer(int aPort, const std::string& aBaseName);
    virtual ~ADXL345_SpiAccelerometer();

    virtual void HandleRead() override;

protected:

    ThreeAxisAccelerometer mThreeAxisAccelerometer;

    const int mSpiPort;
};

#endif /* SRC_ADXL345_SpiAccelerometer_H_ */
