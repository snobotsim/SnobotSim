/*
 * BaseAdxAccelWrapper.h
 *
 *  Created on: Nov 23, 2017
 *      Author: PJ
 */

#ifndef BASEADXACCELWRAPPER_H_
#define BASEADXACCELWRAPPER_H_

#include <memory>

#include "ThreeAxisAccelerometerData.h"

#include "SnobotSim/SimulatorComponents/Accelerometer/IAccelerometerWrapper.h"

class BaseAdxAccelWrapper {
public:
	BaseAdxAccelWrapper(int aBasePort, const std::shared_ptr<hal::ThreeAxisAccelerometerData>& aAccel);
	virtual ~BaseAdxAccelWrapper();

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

		AccelerometerWrapper(AxisType aAxisType, const std::shared_ptr<hal::ThreeAxisAccelerometerData>& aAccel);

	    void SetAcceleration(double aAcceleration) override;

	    double GetAcceleration() override;

	    AxisType mAxisType;
	    std::shared_ptr<hal::ThreeAxisAccelerometerData> mAccel;
	};


    std::shared_ptr<AccelerometerWrapper> mXWrapper;
    std::shared_ptr<AccelerometerWrapper> mYWrapper;
    std::shared_ptr<AccelerometerWrapper> mZWrapper;

};

#endif /* BASEADXACCELWRAPPER_H_ */
