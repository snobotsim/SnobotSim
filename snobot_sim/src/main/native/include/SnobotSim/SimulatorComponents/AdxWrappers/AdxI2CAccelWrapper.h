/*
 * AdxI2CAccelWrapper.h
 *
 *  Created on: Nov 23, 2017
 *      Author: PJ
 */

#ifndef ADXI2CACCELWRAPPER_H_
#define ADXI2CACCELWRAPPER_H_

#include <memory>

#include "SnobotSim/SimulatorComponents/I2C/II2CWrapper.h"
#include "SnobotSim/SimulatorComponents/Accelerometer/IAccelerometerWrapper.h"

#include "ADXL345_I2CAccelerometerData.h"

class AdxI2CAccelWrapper : public II2CWrapper
{
public:
	AdxI2CAccelWrapper(int aPort);
	virtual ~AdxI2CAccelWrapper();

protected:

	class AccelerometerWrapper : public IAccelerometerWrapper
	{
	public:

		enum AxisType
		{
			AXIS_X,
			AXIS_Y,
			AXIS_Z
		};

		AccelerometerWrapper(AxisType aAxisType, const std::shared_ptr<hal::ADXL345_I2CData>& aAccel);

	    void SetAcceleration(double aAcceleration) override;

	    double GetAcceleration() override;

	    AxisType mAxisType;
	    std::shared_ptr<hal::ADXL345_I2CData> mAccel;
	};

    std::shared_ptr<hal::ADXL345_I2CData> mAccel;

    std::shared_ptr<AccelerometerWrapper> mXWrapper;
    std::shared_ptr<AccelerometerWrapper> mYWrapper;
    std::shared_ptr<AccelerometerWrapper> mZWrapper;
};

#endif /* ADXI2CACCELWRAPPER_H_ */
