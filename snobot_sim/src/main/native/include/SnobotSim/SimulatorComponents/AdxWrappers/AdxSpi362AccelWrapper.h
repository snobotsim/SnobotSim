/*
 * AdxSpi363AccelWrapper.h
 *
 *  Created on: Nov 23, 2017
 *      Author: PJ
 */

#ifndef ADXSPI363ACCELWRAPPER_H_
#define ADXSPI363ACCELWRAPPER_H_

#include "SnobotSim/SimulatorComponents/Spi/ISpiWrapper.h"
#include "SnobotSim/SimulatorComponents/AdxWrappers/BaseAdxAccelWrapper.h"

class AdxSpi362AccelWrapper : public ISpiWrapper, public BaseAdxAccelWrapper
{
public:
    AdxSpi362AccelWrapper(int aPort);
    virtual ~AdxSpi362AccelWrapper();
};

#endif /* ADXSPI363ACCELWRAPPER_H_ */
