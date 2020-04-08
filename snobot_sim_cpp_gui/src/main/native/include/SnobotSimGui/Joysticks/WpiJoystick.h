

#include <mockdata/DriverStationData.h>
#include "SnobotSimGui/Joysticks/SystemJoystick.h"

#include <string>

struct WpiJoystick {
  std::string name;
  std::string guid;
  const SystemJoystick* sys = nullptr;
  bool useGamepad = false;

  HAL_JoystickDescriptor desc;
  HAL_JoystickAxes axes;
  HAL_JoystickButtons buttons;
  HAL_JoystickPOVs povs;

  void Update();
  void SetHAL(int i);
  bool IsButtonPressed(int i) { return (buttons.buttons & (1u << i)) != 0; }
};