/*
 * AnalogSourceWrapper.h
 *
 *  Created on: May 3, 2017
 *      Author: PJ
 */

#ifndef SRC_SNOBOTSIM_MODULEWRAPPER_ANALOGSOURCEWRAPPER_H_
#define SRC_SNOBOTSIM_MODULEWRAPPER_ANALOGSOURCEWRAPPER_H_

#include "SnobotSim/ModuleWrapper/AModuleWrapper.h"

class EXPORT_ AnalogSourceWrapper: public AModuleWrapper
{
public:
    AnalogSourceWrapper(int aPort);
    virtual ~AnalogSourceWrapper();

    void SetVoltage(double aVoltage);
    double GetVoltage();

    int GetHandle();

protected:

    int mPort;
    double mVoltage;
};

#endif /* SRC_SNOBOTSIM_MODULEWRAPPER_ANALOGSOURCEWRAPPER_H_ */
