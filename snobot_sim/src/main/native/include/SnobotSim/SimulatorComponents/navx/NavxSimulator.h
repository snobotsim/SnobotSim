/*
 * NavxSimulator.h
 *
 *  Created on: Jul 8, 2017
 *      Author: PJ
 */

#ifndef NAVXSIMULATOR_H_
#define NAVXSIMULATOR_H_

#include <memory>
#include <cstring>
#include "SnobotSim/ExportHelper.h"
#include "SnobotSim/SimulatorComponents/Gyro/GyroWrapper.h"
#include "SnobotSim/SimulatorComponents/Accelerometer/ThreeAxisAccelerometer.h"

class EXPORT_ NavxSimulator
{
public:
    NavxSimulator(int aPort, int aPortOffset);
    virtual ~NavxSimulator();

protected:

    template <typename Value>
    void PutTheValue(uint8_t* aBuffer, int aPosition, const Value& value, int aSize)
    {
        std::memcpy(&aBuffer[aPosition], &value, aSize);
    }

    template <typename Type>
    Type BoundBetweenNeg180Pos180(Type input)
    {
        Type output = input;
        while (output > 180)
        {
            output -= 360;
        }
        while (output < -180)
        {
            output += 360;
        }

        return output;
    }

    void GetCurrentData(uint8_t* aBuffer, int aFirstAddress);
    void GetWriteConfig(uint8_t* aBuffer);

    std::shared_ptr<GyroWrapper> mYawGyro;
    std::shared_ptr<GyroWrapper> mPitchGyro;
    std::shared_ptr<GyroWrapper> mRollGyro;

    ThreeAxisAccelerometer mThreeAxisAccelerometer;

    int mNativePort;
};

#endif /* NAVXSIMULATOR_H_ */
