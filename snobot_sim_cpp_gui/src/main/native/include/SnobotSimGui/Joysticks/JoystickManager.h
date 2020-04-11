
#pragma once

#include <vector>

#include "SnobotSimGui/Joysticks/WpiJoystick.h"

class JoystickManager
{
public:
    JoystickManager();

    void Update();

protected:
    void UpdateJoysticks();
    void RenderSystemJoysticks();
    void RenderWpiJoysticks();

    std::vector<SystemJoystick> mSystemJoysticks;
    std::vector<WpiJoystick> mWpiJoysticks;
};
