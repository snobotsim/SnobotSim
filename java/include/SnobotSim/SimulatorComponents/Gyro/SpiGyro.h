/*
 * SpiGyro.h
 *
 *  Created on: May 7, 2017
 *      Author: PJ
 */

#ifndef SPIGYRO_H_
#define SPIGYRO_H_

#include "SnobotSim/SimulatorComponents/Gyro/GyroWrapper.h"
#include "SnobotSim/SimulatorComponents/ISpiWrapper.h"
#include <memory>

class SpiGyro: public GyroWrapper, public ISpiWrapper
{
public:
    SpiGyro();
    virtual ~SpiGyro();

    double GetAccumulatorValue();
    void ResetAccumulatorValue();

protected:

    std::shared_ptr<ISpiWrapper> mSpiWrapper;
};

#endif /* SPIGYRO_H_ */
