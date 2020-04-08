
#include "SnobotSimGui/Joysticks/SystemJoystick.h"

#include <GLFW/glfw3.h>
#include <imgui.h>
#include <imgui_internal.h>

void SystemJoystick::Update(int i) {
  bool wasPresent = present;
  present = glfwJoystickPresent(i);

  if (!present) return;
  axes = glfwGetJoystickAxes(i, &axisCount);
  buttons = glfwGetJoystickButtons(i, &buttonCount);
  hats = glfwGetJoystickHats(i, &hatCount);
//   isGamepad = glfwGetGamepadState(i, &gamepadState);

  anyButtonPressed = false;
  for (int j = 0; j < buttonCount; ++j) {
    if (buttons[j]) {
      anyButtonPressed = true;
      break;
    }
  }
  for (int j = 0; j < hatCount; ++j) {
    if (hats[j] != GLFW_HAT_CENTERED) {
      anyButtonPressed = true;
      break;
    }
  }

  if (!present || wasPresent) return;
  name = glfwGetJoystickName(i);

  // try to find matching GUID
  if (const char* guid = glfwGetJoystickGUID(i)) {
    // for (auto&& joy : mWpiJoysticks) {
    //   if (guid == joy.guid) {
    //     joy.sys = &mSystemJoysticks[i];
    //     joy.guid.clear();
    //     break;
    //   }
    // }
  }
}