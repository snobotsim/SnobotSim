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
            mName(aName), mVisible(true)
    {

    }
    virtual ~AModuleWrapper()
    {

    }

    const std::string& GetName()
    {
        return mName;
    }

    bool isVisible()
    {
        return mVisible;
    }

    void setVisible(bool aVisible)
    {
        mVisible = aVisible;
    }

protected:

    std::string mName;
    bool mVisible;
};

#endif /* SRC_SNOBOTSIM_MODULEWRAPPER_AMODULEWRAPPER_H_ */
