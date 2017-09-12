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
#include "SnobotSim/SimulatorComponents/Spi/ISpiWrapper.h"
#include <memory>

class EXPORT_ ADXRS450_SpiGyro: public GyroWrapper, public ISpiWrapper
{
public:
    ADXRS450_SpiGyro(int aSpiPort);
    virtual ~ADXRS450_SpiGyro();

    void HandleRead() override;

    void SetAngle(double aAngle) override;

protected:

    const int mSpiPort;
    std::shared_ptr<ISpiWrapper> mSpiWrapper;
};

#endif /* SPIGYRO_H_ */
