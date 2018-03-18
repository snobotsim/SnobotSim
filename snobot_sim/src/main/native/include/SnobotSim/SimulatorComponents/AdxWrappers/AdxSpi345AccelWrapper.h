/*
 * AdxSpi345AccelWrapper.h
 *
 *  Created on: Nov 23, 2017
 *      Author: PJ
 */

#ifndef ADXSPI345ACCELWRAPPER_H_
#define ADXSPI345ACCELWRAPPER_H_

#include "SnobotSim/SimulatorComponents/Spi/ISpiWrapper.h"
#include "SnobotSim/SimulatorComponents/AdxWrappers/BaseAdxAccelWrapper.h"

class AdxSpi345AccelWrapper : public ISpiWrapper, public BaseAdxAccelWrapper
{
public:
    AdxSpi345AccelWrapper(int aPort);
    virtual ~AdxSpi345AccelWrapper();
};

#endif /* ADXSPI345ACCELWRAPPER_H_ */
