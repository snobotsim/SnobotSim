/*
 * SolenoidWrapper.h
 *
 *  Created on: May 3, 2017
 *      Author: PJ
 */

#ifndef SRC_SNOBOTSIM_MODULEWRAPPER_SOLENOIDWRAPPER_H_
#define SRC_SNOBOTSIM_MODULEWRAPPER_SOLENOIDWRAPPER_H_

#include "SnobotSim/ModuleWrapper/AModuleWrapper.h"

class SolenoidWrapper: public AModuleWrapper
{
public:
    SolenoidWrapper(int aPort);
    virtual ~SolenoidWrapper();

    void SetState(bool aOn);

    bool GetState();

protected:
    bool mState;
};

#endif /* SRC_SNOBOTSIM_MODULEWRAPPER_SOLENOIDWRAPPER_H_ */
