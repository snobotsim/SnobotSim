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
};

#endif /* SRC_SNOBOTSIM_MODULEWRAPPER_RELAYWRAPPER_H_ */
