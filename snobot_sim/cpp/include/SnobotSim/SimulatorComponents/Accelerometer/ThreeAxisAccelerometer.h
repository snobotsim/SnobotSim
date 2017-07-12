/*
 * ThreeAxisAccelerometer.h
 *
 *  Created on: Jul 12, 2017
 *      Author: preiniger
 */

#ifndef SRC_THREEAXISACCELEROMETER_H_
#define SRC_THREEAXISACCELEROMETER_H_

#include "SnobotSim/SimulatorComponents/Accelerometer/AccelerometerWrapper.h"
#include <memory>

class EXPORT_ ThreeAxisAccelerometer
{
public:
    ThreeAxisAccelerometer(int aPort, const std::string& aBaseName);
    virtual ~ThreeAxisAccelerometer();

    double GetX();
    double GetY();
    double GetZ();

    const std::shared_ptr<AccelerometerWrapper>& GetXWrapper() const;
    const std::shared_ptr<AccelerometerWrapper>& GetYWrapper() const;
    const std::shared_ptr<AccelerometerWrapper>& GetZWrapper() const;

    std::shared_ptr<AccelerometerWrapper>& GetXWrapper();
    std::shared_ptr<AccelerometerWrapper>& GetYWrapper();
    std::shared_ptr<AccelerometerWrapper>& GetZWrapper();

protected:

    std::shared_ptr<AccelerometerWrapper> mXWrapper;
    std::shared_ptr<AccelerometerWrapper> mYWrapper;
    std::shared_ptr<AccelerometerWrapper> mZWrapper;
};

#endif /* SRC_THREEAXISACCELEROMETER_H_ */
