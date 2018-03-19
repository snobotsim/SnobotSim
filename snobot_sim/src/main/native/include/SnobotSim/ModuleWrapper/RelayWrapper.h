/*
 * RelayWrapper.h
 *
 *  Created on: May 3, 2017
 *      Author: PJ
 */

#ifndef SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_MODULEWRAPPER_RELAYWRAPPER_H_
#define SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_MODULEWRAPPER_RELAYWRAPPER_H_

#include "SnobotSim/ModuleWrapper/AModuleWrapper.h"

class EXPORT_ RelayWrapper: public AModuleWrapper
{
public:
    RelayWrapper(int aPort);
    virtual ~RelayWrapper();

    void SetRelayForwards(bool aOn);
    void SetRelayReverse(bool aOn);

    bool GetRelayForwards();
    bool GetRelayReverse();

protected:
    bool mForwards;
    bool mReverse;
};

#endif  // SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_MODULEWRAPPER_RELAYWRAPPER_H_
