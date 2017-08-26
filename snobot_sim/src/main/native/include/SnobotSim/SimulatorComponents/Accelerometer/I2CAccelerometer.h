/*
 * I2CAccelerometer.h
 *
 *  Created on: Jul 12, 2017
 *      Author: preiniger
 */

#ifndef SRC_I2CAccelerometer_H_
#define SRC_I2CAccelerometer_H_

#include "SnobotSim/SimulatorComponents/Accelerometer/ThreeAxisAccelerometer.h"
#include "SnobotSim/SimulatorComponents/II2CWrapper.h"

class EXPORT_ I2CAccelerometer : public II2CWrapper
{
public:
    I2CAccelerometer(int aPort, const std::string& aBaseName);
    virtual ~I2CAccelerometer();

    virtual int Transaction(
            uint8_t* dataToSend, int32_t sendSize,
            uint8_t* dataReceived, int32_t receiveSize) override;

    virtual int32_t Read(
            int32_t deviceAddress, uint8_t* buffer, int32_t count) override;

    virtual int32_t Write(
            int32_t deviceAddress, uint8_t* dataToSend, int32_t sendSize) override;

protected:

    ThreeAxisAccelerometer mThreeAxisAccelerometer;
};

#endif /* SRC_I2CAccelerometer_H_ */
