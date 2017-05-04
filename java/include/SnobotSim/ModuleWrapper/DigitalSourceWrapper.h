/*
 * DigitalSourceWrapper.h
 *
 *  Created on: May 3, 2017
 *      Author: PJ
 */

#ifndef SRC_SNOBOTSIM_MODULEWRAPPER_DIGITALSOURCEWRAPPER_H_
#define SRC_SNOBOTSIM_MODULEWRAPPER_DIGITALSOURCEWRAPPER_H_

#include "SnobotSim/ModuleWrapper/AModuleWrapper.h"

class DigitalSourceWrapper: public AModuleWrapper
{
public:
    DigitalSourceWrapper(int aPort);
    virtual ~DigitalSourceWrapper();
};

#endif /* SRC_SNOBOTSIM_MODULEWRAPPER_DIGITALSOURCEWRAPPER_H_ */
