/*
 * AModuleWrapper.h
 *
 *  Created on: May 3, 2017
 *      Author: PJ
 */

#ifndef SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_MODULEWRAPPER_AMODULEWRAPPER_H_
#define SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_MODULEWRAPPER_AMODULEWRAPPER_H_

#include <string>

#include "SnobotSim/ExportHelper.h"

class AModuleWrapper
{
public:
    AModuleWrapper(const std::string& aName) :
            mName(aName), mWantsHidden(false)
    {

    }
    virtual ~AModuleWrapper()
    {

    }

    const std::string& GetName()
    {
        return mName;
    }

    void SetName(const std::string& aName)
    {
        mName = aName;
    }

    bool WantsHidden()
    {
        return mWantsHidden;
    }

    void SetWantsHidden(bool aVisible)
    {
        mWantsHidden = aVisible;
    }

protected:

    std::string mName;
    bool mWantsHidden;
};

#endif  // SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_MODULEWRAPPER_AMODULEWRAPPER_H_
