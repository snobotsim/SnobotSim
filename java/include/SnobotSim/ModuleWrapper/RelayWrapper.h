/*
 * RelayWrapper.h
 *
 *  Created on: May 3, 2017
 *      Author: PJ
 */

#ifndef SRC_SNOBOTSIM_MODULEWRAPPER_RELAYWRAPPER_H_
#define SRC_SNOBOTSIM_MODULEWRAPPER_RELAYWRAPPER_H_

#include "SnobotSim/ModuleWrapper/AModuleWrapper.h"

class RelayWrapper: public AModuleWrapper
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

#endif /* SRC_SNOBOTSIM_MODULEWRAPPER_RELAYWRAPPER_H_ */
