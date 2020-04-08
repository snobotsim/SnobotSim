
#include "SnobotSimGui/Joysticks/WpiJoystick.h"

#include <GLFW/glfw3.h>
#include <imgui.h>
#include <imgui_internal.h>

#include <algorithm>

namespace
{
    
int HatToAngle(unsigned char hat) {
  switch (hat) {
    case GLFW_HAT_UP:
      return 0;
    case GLFW_HAT_RIGHT:
      return 90;
    case GLFW_HAT_DOWN:
      return 180;
    case GLFW_HAT_LEFT:
      return 270;
    case GLFW_HAT_RIGHT_UP:
      return 45;
    case GLFW_HAT_RIGHT_DOWN:
      return 135;
    case GLFW_HAT_LEFT_UP:
      return 315;
    case GLFW_HAT_LEFT_DOWN:
      return 225;
    default:
      return -1;
  }
}

}
void WpiJoystick::Update()
{
    std::memset(&desc, 0, sizeof(desc));
    desc.type = -1;
    std::memset(&axes, 0, sizeof(axes));
    std::memset(&buttons, 0, sizeof(buttons));
    std::memset(&povs, 0, sizeof(povs));

    if (!sys || !sys->present) return;

    // use gamepad mappings if present and enabled
    const float* sysAxes;
    const unsigned char* sysButtons;
    if (sys->isGamepad && useGamepad) {
        // sysAxes = sys->gamepadState.axes;
        // don't remap on windows
    #ifdef _WIN32
        sysButtons = sys->buttons;
    #else
        sysButtons = sys->gamepadState.buttons;
    #endif
    } else {
        sysAxes = sys->axes;
        sysButtons = sys->buttons;
    }

    // copy into HAL structures
    desc.isXbox = sys->isGamepad ? 1 : 0;
    desc.type = sys->isGamepad ? 21 : 20;
    std::strncpy(desc.name, sys->name, 256);
    desc.axisCount = (std::min)(sys->axisCount, HAL_kMaxJoystickAxes);
    // desc.axisTypes ???
    desc.buttonCount = (std::min)(sys->buttonCount, 32);
    desc.povCount = (std::min)(sys->hatCount, HAL_kMaxJoystickPOVs);

    buttons.count = desc.buttonCount;
    for (int j = 0; j < buttons.count; ++j)
    {
        buttons.buttons |= (sysButtons[j] ? 1u : 0u) << j;
    }

    axes.count = desc.axisCount;
    if (sys->isGamepad && useGamepad) 
    {
        // the FRC DriverStation maps gamepad (XInput) trigger values to 0-1 range
        // on axis 2 and 3.
        axes.axes[0] = sysAxes[0];
        axes.axes[1] = sysAxes[1];
        axes.axes[2] = 0.5 + sysAxes[4] / 2.0;
        axes.axes[3] = 0.5 + sysAxes[5] / 2.0;
        axes.axes[4] = sysAxes[2];
        axes.axes[5] = sysAxes[3];

        // the start button for gamepads is not mapped on the FRC DriverStation
        // platforms, so remove it if present
        if (buttons.count == 11)
        {
            --desc.buttonCount;
            --buttons.count;
            buttons.buttons = (buttons.buttons & 0xff) | ((buttons.buttons >> 1) & 0x300);
        }
    } else {
        std::memcpy(axes.axes, sysAxes, axes.count * sizeof(&axes.axes[0]));
    }

    povs.count = desc.povCount;
    for (int j = 0; j < povs.count; ++j) 
    {
        povs.povs[j] = HatToAngle(sys->hats[j]);
    }
}

void WpiJoystick::SetHAL(int i)
{
  HALSIM_SetJoystickDescriptor(i, &desc);
  HALSIM_SetJoystickAxes(i, &axes);
  HALSIM_SetJoystickButtons(i, &buttons);
  HALSIM_SetJoystickPOVs(i, &povs);
}