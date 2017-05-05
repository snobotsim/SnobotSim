/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2016-2017. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

#include <chrono>
#include <cstdio>
#include <cstdlib>
#include <cstring>
#include <limits>

#include "HAL/DriverStation.h"
#include "HAL/cpp/priority_condition_variable.h"
#include "HAL/cpp/priority_mutex.h"
#include "SnobotSim/RobotStateSingleton.h"

static_assert(sizeof(int32_t) >= sizeof(int),
              "FRC_NetworkComm status variable is larger than 32 bits");

struct HAL_JoystickAxesInt {
  int16_t count;
  int16_t axes[HAL_kMaxJoystickAxes];
};

static priority_mutex msgMutex;
static priority_condition_variable newDSDataAvailableCond;
static priority_mutex newDSDataAvailableMutex;

extern "C" {
int32_t HAL_SetErrorData(const char* errors, int32_t errorsLength,
                         int32_t waitMs) {
	return 0;
}

int32_t HAL_SendError(HAL_Bool isError, int32_t errorCode, HAL_Bool isLVCode,
                      const char* details, const char* location,
                      const char* callStack, HAL_Bool printMsg) {
	return 0;
}

int32_t HAL_GetControlWord(HAL_ControlWord* controlWord) {

    controlWord->enabled = !RobotStateSingleton::Get().GetDisabled();
    controlWord->autonomous = RobotStateSingleton::Get().GetAutonomous();
    controlWord->test = RobotStateSingleton::Get().GetTest();
    controlWord->eStop = false;
    controlWord->fmsAttached = false;
    controlWord->dsAttached = true;

    return 0;
}

HAL_AllianceStationID HAL_GetAllianceStation(int32_t* status) {
  return HAL_AllianceStationID_kRed1;
}

int32_t HAL_GetJoystickAxes(int32_t joystickNum, HAL_JoystickAxes* axes) {

  axes->count = HAL_kMaxJoystickAxes;
  
  for(int i = 0; i < HAL_kMaxJoystickAxes; ++i)
  {
      axes->axes[i] = i * i;
  }
  
	return 0;
}

int32_t HAL_GetJoystickPOVs(int32_t joystickNum, HAL_JoystickPOVs* povs) {

  povs->count = HAL_kMaxJoystickPOVs;
  
  for(int i = 0; i < HAL_kMaxJoystickPOVs; ++i)
  {
      povs->povs[i] = i * i;
  }
  
	return 0;
}

int32_t HAL_GetJoystickButtons(int32_t joystickNum,
                               HAL_JoystickButtons* buttons) {
    buttons->buttons = 0xF;
    buttons->count = 4;
    
	return 0;
}
/**
 * Retrieve the Joystick Descriptor for particular slot
 * @param desc [out] descriptor (data transfer object) to fill in.  desc is
 * filled in regardless of success. In other words, if descriptor is not
 * available, desc is filled in with default values matching the init-values in
 * Java and C++ Driverstation for when caller requests a too-large joystick
 * index.
 *
 * @return error code reported from Network Comm back-end.  Zero is good,
 * nonzero is bad.
 */
int32_t HAL_GetJoystickDescriptor(int32_t joystickNum,
                                  HAL_JoystickDescriptor* desc) {
	return 0;
}

HAL_Bool HAL_GetJoystickIsXbox(int32_t joystickNum) {
	return 0;
}

int32_t HAL_GetJoystickType(int32_t joystickNum) {
	return 0;
}

char* HAL_GetJoystickName(int32_t joystickNum) {
  HAL_JoystickDescriptor joystickDesc;
  if (HAL_GetJoystickDescriptor(joystickNum, &joystickDesc) < 0) {
    char* name = static_cast<char*>(std::malloc(1));
    name[0] = '\0';
    return name;
  } else {
    size_t len = std::strlen(joystickDesc.name);
    char* name = static_cast<char*>(std::malloc(len + 1));
    std::strncpy(name, joystickDesc.name, len);
    name[len] = '\0';
    return name;
  }
}

int32_t HAL_GetJoystickAxisType(int32_t joystickNum, int32_t axis) {
	return 0;
}

int32_t HAL_SetJoystickOutputs(int32_t joystickNum, int64_t outputs,
                               int32_t leftRumble, int32_t rightRumble) {
	return 0;
}

double HAL_GetMatchTime(int32_t* status) {
	return 0;
}

void HAL_ObserveUserProgramStarting(void) {

}

void HAL_ObserveUserProgramDisabled(void) {

}

void HAL_ObserveUserProgramAutonomous(void) {

}

void HAL_ObserveUserProgramTeleop(void) {

}

void HAL_ObserveUserProgramTest(void) {

}

/**
 * Waits for the newest DS packet to arrive. Note that this is a blocking call.
 */
void HAL_WaitForDSData(void) {

}

void HAL_InitializeDriverStation(void) {

}

}  // extern "C"
