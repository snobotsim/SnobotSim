/*
 * I2CAccelerometer.h
 *
 *  Created on: Jul 12, 2017
 *      Author: preiniger
 */

#ifndef SRC_I2CAccelerometer_H_
#define SRC_I2CAccelerometer_H_

#include "SnobotSim/SimulatorComponents/Accelerometer/ThreeAxisAccelerometer.h"
#include "SnobotSim/SimulatorComponents/I2C/II2CWrapper.h"

class EXPORT_ I2CAccelerometer : public II2CWrapper
{
public:
    I2CAccelerometer(int aPort, const std::string& aBaseName);
    virtual ~I2CAccelerometer();

    virtual void HandleRead() override;

protected:

    ThreeAxisAccelerometer mThreeAxisAccelerometer;
};

#endif /* SRC_I2CAccelerometer_H_ */
