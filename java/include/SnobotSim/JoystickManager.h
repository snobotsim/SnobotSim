/*
 * JoystickManager.h
 *
 *  Created on: May 9, 2017
 *      Author: preiniger
 */

#ifndef JOYSTICKMANAGER_H_
#define JOYSTICKMANAGER_H_

#include "HAL/DriverStation.h"
#include <map>

struct JoystickInformation
{
    HAL_JoystickAxes mAxes;
    HAL_JoystickPOVs mPovs;
    HAL_JoystickButtons mButtons;

    JoystickInformation();
};

class JoystickManager
{
private:
    JoystickManager();

    static JoystickManager sINSTANCE;

public:

    virtual ~JoystickManager();

    static JoystickManager& Get();
    bool HasJoystick(int aHandle);
    JoystickInformation& GetJoystick(int aHandle);

protected:

    std::map<int, JoystickInformation> mJoystickInformation;

    static JoystickInformation sNULL_INSTANCE;

};

#endif /* JOYSTICKMANAGER_H_ */
