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
#include "SnobotSim/SimulatorComponents/Gyro/IGyroWrapper.h"
#include "ADXRS450_SpiGyroWrapperData.h"

class AdxGyroWrapper : public ISpiWrapper, public IGyroWrapper
{
public:
    AdxGyroWrapper(int aPort);
    virtual ~AdxGyroWrapper();


    void SetAngle(double aAngle) override;

    double GetAngle() override;

protected:

    std::shared_ptr<hal::ADXRS450_SpiGyroWrapper> mGyro;
};

#endif /* ADXGYROWRAPPER_H_ */
