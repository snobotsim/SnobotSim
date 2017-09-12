/*
 * ADXL345_I2CAccelerometer.h
 *
 *  Created on: Jul 12, 2017
 *      Author: preiniger
 */

#ifndef SRC_ADXL345_I2CAccelerometer_H_
#define SRC_ADXL345_I2CAccelerometer_H_

#include "SnobotSim/SimulatorComponents/Accelerometer/ThreeAxisAccelerometer.h"
#include "SnobotSim/SimulatorComponents/I2C/II2CWrapper.h"

class EXPORT_ ADXL345_I2CAccelerometer : public II2CWrapper
{
public:
    ADXL345_I2CAccelerometer(int aPort, const std::string& aBaseName);
    virtual ~ADXL345_I2CAccelerometer();

    virtual void HandleRead() override;

protected:

    const int mI2CPort;
    ThreeAxisAccelerometer mThreeAxisAccelerometer;
};

#endif /* SRC_ADXL345_I2CAccelerometer_H_ */
