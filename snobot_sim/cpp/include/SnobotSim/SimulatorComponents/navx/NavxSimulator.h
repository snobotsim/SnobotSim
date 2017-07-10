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

class EXPORT_ NavxSimulator
{
public:
	NavxSimulator(int aPort);
	virtual ~NavxSimulator();

protected:

    template <typename Value>
    void PutTheValue(uint8_t* aBuffer, int aPosition, const Value& value, int aSize)
    {
        std::memcpy(&aBuffer[aPosition], &value, aSize);
    }

    void GetCurrentData(uint8_t* aBuffer, int aFirstAddress);
    void GetWriteConfig(uint8_t* aBuffer);

	std::shared_ptr<GyroWrapper> mYawGyro;
	std::shared_ptr<GyroWrapper> mPitchGyro;
	std::shared_ptr<GyroWrapper> mRollGyro;
};

#endif /* NAVXSIMULATOR_H_ */
