/*
 * AdxSpiAccelWrapper.h
 *
 *  Created on: Nov 23, 2017
 *      Author: PJ
 */

#ifndef ADXSPIACCELWRAPPER_H_
#define ADXSPIACCELWRAPPER_H_

#include <memory>

#include "SnobotSim/SimulatorComponents/Spi/ISpiWrapper.h"
#include "ADXL345_SpiAccelerometerData.h"
#include "ADXL362_SpiAccelerometerData.h"

class AdxSpiAccelWrapper : public ISpiWrapper
{
public:
	AdxSpiAccelWrapper(const std::string& aType, int aPort);
	virtual ~AdxSpiAccelWrapper();

protected:

    std::shared_ptr<hal::ADXL345_SpiAccelerometer> m345Accel;
    std::shared_ptr<hal::ADXL362_SpiAccelerometer> m362Accel;
};

#endif /* ADXSPIACCELWRAPPER_H_ */
