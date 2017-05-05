/*
 * AModuleWrapper.h
 *
 *  Created on: May 3, 2017
 *      Author: PJ
 */

#ifndef SRC_SNOBOTSIM_MODULEWRAPPER_AMODULEWRAPPER_H_
#define SRC_SNOBOTSIM_MODULEWRAPPER_AMODULEWRAPPER_H_

#include <string>

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

#endif /* SRC_SNOBOTSIM_MODULEWRAPPER_AMODULEWRAPPER_H_ */
