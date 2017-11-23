/*
 * AdxGyroWrapper.h
 *
 *  Created on: Nov 23, 2017
 *      Author: PJ
 */

#ifndef ADXGYROWRAPPER_H_
#define ADXGYROWRAPPER_H_

#include <memory>

#include "SnobotSim/SimulatorComponents/Spi/ISpiWrapper.h"
#include "ADXRS450_SpiGyroWrapperData.h"

class AdxGyroWrapper : public ISpiWrapper
{
public:
	AdxGyroWrapper(int aPort);
	virtual ~AdxGyroWrapper();

protected:

    std::shared_ptr<hal::ADXRS450_SpiGyroWrapper> mGyro;
};

#endif /* ADXGYROWRAPPER_H_ */
