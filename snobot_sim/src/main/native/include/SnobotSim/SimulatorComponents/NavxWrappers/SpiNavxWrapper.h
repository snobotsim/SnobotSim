/*
 * SpiNavxWrapper.h
 *
 *  Created on: Nov 23, 2017
 *      Author: PJ
 */

#ifndef SPINAVXWRAPPER_H_
#define SPINAVXWRAPPER_H_

#include "SnobotSim/SimulatorComponents/NavxWrappers/BaseNavxWrapper.h"
#include "SnobotSim/SimulatorComponents/Spi/ISpiWrapper.h"

class SpiNavxWrapper : public ISpiWrapper, public BaseNavxWrapper
{
public:
    SpiNavxWrapper(int aPort);
    virtual ~SpiNavxWrapper();
};

#endif /* SPINAVXWRAPPER_H_ */
