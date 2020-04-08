
#include "SnobotSimGui/Joysticks/WpiJoystick.h"
#include <vector>

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
