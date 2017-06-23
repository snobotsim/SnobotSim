/*
 * SpiGyro.h
 *
 *  Created on: May 7, 2017
 *      Author: PJ
 */

#ifndef SPIGYRO_H_
#define SPIGYRO_H_

#include "SnobotSim/ExportHelper.h"
#include "SnobotSim/SimulatorComponents/Gyro/GyroWrapper.h"
#include "SnobotSim/SimulatorComponents/ISpiWrapper.h"
#include <memory>

class EXPORT_ SpiGyro: public GyroWrapper, public ISpiWrapper
{
public:
    SpiGyro();
    virtual ~SpiGyro();

    double GetAccumulatorValue() override;
    void ResetAccumulatorValue() override;

    int32_t Read(uint8_t* buffer, int32_t count) override;

protected:

    std::shared_ptr<ISpiWrapper> mSpiWrapper;
};

#endif /* SPIGYRO_H_ */
