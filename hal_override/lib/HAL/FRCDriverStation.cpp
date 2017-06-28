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
#include <iostream>

#include "HAL/DriverStation.h"
#include "HAL/cpp/priority_condition_variable.h"
#include "HAL/cpp/priority_mutex.h"

#include "SnobotSim/RobotStateSingleton.h"
#include "SnobotSim/JoystickManager.h"
#include "SnobotSim/Logging/SnobotLogger.h"

static_assert(sizeof(int32_t) >= sizeof(int),
              "FRC_NetworkComm status variable is larger than 32 bits");

struct HAL_JoystickAxesInt {
  int16_t count;
  int16_t axes[HAL_kMaxJoystickAxes];
};

static hal::priority_mutex msgMutex;
static hal::priority_condition_variable newDSDataAvailableCond;
static hal::priority_mutex newDSDataAvailableMutex;

extern "C" {
int32_t HAL_SetErrorData(const char* errors, int32_t errorsLength,
                         int32_t waitMs) {
 std::cout << "Set Error Data..." << errors << std::endl;
 return 0;
}

int32_t HAL_SendError(HAL_Bool isError, int32_t errorCode, HAL_Bool isLVCode,
                      const char* details, const char* location,
                      const char* callStack, HAL_Bool printMsg) {
  // Avoid flooding console by keeping track of previous 5 error
  // messages and only printing again if they're longer than 1 second old.
  static constexpr int KEEP_MSGS = 5;
  std::lock_guard<hal::priority_mutex> lock(msgMutex);
  static std::string prevMsg[KEEP_MSGS];
  static std::chrono::time_point<std::chrono::steady_clock>
      prevMsgTime[KEEP_MSGS];
  static bool initialized = false;
  if (!initialized) {
    for (int i = 0; i < KEEP_MSGS; i++) {
      prevMsgTime[i] =
          std::chrono::steady_clock::now() - std::chrono::seconds(2);
    }
    initialized = true;
  }

  auto curTime = std::chrono::steady_clock::now();
  int i;
  for (i = 0; i < KEEP_MSGS; ++i) {
    if (prevMsg[i] == details) break;
  }
  int retval = 0;
  if (i == KEEP_MSGS || (curTime - prevMsgTime[i]) >= std::chrono::seconds(1)) {
      retval = 0;
      std::cout << "Sending error: " << isError << ", " << details << std::endl;

    if (printMsg) {
      if (location && location[0] != '\0') {
          std::cerr << "HAL!!!: " << (isError ? "Error" : "Warning") << " at " << location << std::endl;
      }
      std::cerr << details << std::endl;

      if (callStack && callStack[0] != '\0') {
          std::cerr << callStack << std::endl;
      }
    }
    if (i == KEEP_MSGS) {
      // replace the oldest one
      i = 0;
      auto first = prevMsgTime[0];
      for (int j = 1; j < KEEP_MSGS; ++j) {
        if (prevMsgTime[j] < first) {
          first = prevMsgTime[j];
          i = j;
        }
      }
      prevMsg[i] = details;
    }
    prevMsgTime[i] = curTime;
  }
  return retval;
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

    JoystickInformation& info = JoystickManager::Get().GetJoystick(joystickNum);

    for (int i = 0; i < HAL_kMaxJoystickAxes; ++i)
    {
        axes->axes[i] = info.mAxes.axes[i];
    }
  
    return 0;
}

int32_t HAL_GetJoystickPOVs(int32_t joystickNum, HAL_JoystickPOVs* povs) {

    povs->count = HAL_kMaxJoystickPOVs;

    JoystickInformation& info = JoystickManager::Get().GetJoystick(joystickNum);

    for (int i = 0; i < HAL_kMaxJoystickPOVs; ++i)
    {
        povs->povs[i] = info.mPovs.povs[i];
    }
  
    return 0;
}

int32_t HAL_GetJoystickButtons(int32_t joystickNum,
                               HAL_JoystickButtons* buttons) {

    JoystickInformation& info = JoystickManager::Get().GetJoystick(joystickNum);

    buttons->count = info.mButtons.count;
    buttons->buttons = info.mButtons.buttons;

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

    JoystickInformation& info = JoystickManager::Get().GetJoystick(joystickNum);

	desc->isXbox      = false;
	desc->type        = 0;
//	desc->name        = "";
	desc->axisCount   = HAL_kMaxJoystickAxes;
//	desc->axisTypes[HAL_kMaxJoystickAxes];
	desc->buttonCount = info.mButtons.count;
	desc->povCount    = HAL_kMaxJoystickPOVs;

    return 0;
}

HAL_Bool HAL_GetJoystickIsXbox(int32_t joystickNum) {
    LOG_UNSUPPORTED();
    return 0;
}

int32_t HAL_GetJoystickType(int32_t joystickNum) {
    LOG_UNSUPPORTED();
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
  HAL_JoystickDescriptor joystickDesc;
  if (HAL_GetJoystickDescriptor(joystickNum, &joystickDesc) < 0) {
    return -1;
  } else {
    return joystickDesc.axisTypes[axis];
  }
}

int32_t HAL_SetJoystickOutputs(int32_t joystickNum, int64_t outputs,
                               int32_t leftRumble, int32_t rightRumble) {
    return 0;
}

double HAL_GetMatchTime(int32_t* status) {
    return RobotStateSingleton::Get().GetMatchTime();
}

void HAL_ObserveUserProgramStarting(void) {
	RobotStateSingleton::Get().HandleRobotInitialized();
}

void HAL_ObserveUserProgramDisabled(void) {
    // Nothing to do...
}

void HAL_ObserveUserProgramAutonomous(void) {
    // Nothing to do...
}

void HAL_ObserveUserProgramTeleop(void) {
    // Nothing to do...
}

void HAL_ObserveUserProgramTest(void) {
    // Nothing to do...
}

bool HAL_IsNewControlData(void) {
  return true;
}

/**
 * Waits for the newest DS packet to arrive. Note that this is a blocking call.
 */
void HAL_WaitForDSData(void) {
	HAL_WaitForDSDataTimeout(0);
}

/**
 * Waits for the newest DS packet to arrive. If timeout is <= 0, this will wait
 * forever. Otherwise, it will wait until either a new packet, or the timeout
 * time has passed. Returns true on new data, false on timeout.
 */
HAL_Bool HAL_WaitForDSDataTimeout(double timeout) {

  RobotStateSingleton::Get().WaitForNextControlLoop();
  return true;
}

void HAL_InitializeDriverStation(void) {
    // Nothing to do...
}

}  // extern "C"
