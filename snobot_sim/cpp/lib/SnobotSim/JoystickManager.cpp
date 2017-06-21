/*
 * JoystickManager.cpp
 *
 *  Created on: May 9, 2017
 *      Author: preiniger
 */

#include "SnobotSim/JoystickManager.h"

JoystickManager JoystickManager::sINSTANCE = JoystickManager();
JoystickInformation JoystickManager::sNULL_INSTANCE = JoystickInformation();

JoystickInformation::JoystickInformation()
{
    mAxes.count = 0;
    mPovs.count = 0;
    mButtons.count = 0;

    for (int i = 0; i < kMaxJoystickAxes; ++i)
    {
        mAxes.axes[i] = 0;
    }
    for (int i = 0; i < kMaxJoystickPOVs; ++i)
    {
        mPovs.povs[i] = 0;
    }

    mButtons.buttons = 0;
}

JoystickManager::JoystickManager()
{
    for (int i = 0; i < 6; ++i)
    {
        mJoystickInformation[i] = JoystickInformation();
    }
}

JoystickManager::~JoystickManager()
{

}

JoystickManager& JoystickManager::Get()
{
    return sINSTANCE;
}

bool JoystickManager::HasJoystick(int aHandle)
{
    std::map<int, JoystickInformation>::iterator findIter = mJoystickInformation.find(aHandle);

    return findIter != mJoystickInformation.end();
}

JoystickInformation& JoystickManager::GetJoystick(int aHandle)
{
    std::map<int, JoystickInformation>::iterator findIter = mJoystickInformation.find(aHandle);
    if (findIter != mJoystickInformation.end())
    {
        return findIter->second;
    }

    return sNULL_INSTANCE;
}
