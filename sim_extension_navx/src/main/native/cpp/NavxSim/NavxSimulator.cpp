/*
 * NavxSimulator.cpp
 *
 *  Created on: Jul 8, 2017
 *      Author: PJ
 */

#include "NavxSim/NavxSimulator.h"

#include "MockData/NotifyCallbackHelpers.h"

NavxSimulator::NavxSimulator()
{

}

NavxSimulator::~NavxSimulator()
{

}

void NavxSimulator::ResetData() {
  mX = 0.0;
  mXCallbacks = nullptr;
  mY = 0.0;
  mYCallbacks = nullptr;
  mZ = 0.0;
  mZCallbacks = nullptr;

  mYaw = 0.0;
  mYawCallbacks = nullptr;
  mPitch = 0.0;
  mPitchCallbacks = nullptr;
  mRoll = 0.0;
  mRollCallbacks = nullptr;
}

void NavxSimulator::GetWriteConfig(uint8_t* aBuffer)
{
    aBuffer[ 0] = 0x01; // Type
    aBuffer[ 1] = 0x02; // HW rev
    aBuffer[ 2] = 0x03; // FW Major
    aBuffer[ 3] = 0x04; // FW Minor
    aBuffer[ 4] = 0x05; // Update Rate
    aBuffer[ 5] = 0x06; // Accel g
    aBuffer[ 6] = 0x07; // Gyro DPS
    aBuffer[ 7] = 0x08; // Gyro DPS
    aBuffer[ 8] = 0x09; // Op Status
    aBuffer[ 9] = 0x10; // Cal Status
    aBuffer[10] = 0x11; // Self Test Status
    aBuffer[11] = 0x12; // Capability Flags
    aBuffer[12] = 0x13; // Capability Flags
    aBuffer[16] = 0x14; // Sensor Status
    aBuffer[17] = 0x15; // Sensor Status
}

void NavxSimulator::GetCurrentData(uint8_t* aBuffer, int aFirstAddress)
{
    static uint32_t timestamp = 1;

    PutTheValue(aBuffer, 0x12 - aFirstAddress, timestamp, 4); // Op Status
    ++timestamp;

    PutTheValue(aBuffer, 0x08 - aFirstAddress, 1, 1); // Op Status
    PutTheValue(aBuffer, 0x0A - aFirstAddress, 2, 1); // Self Test
    PutTheValue(aBuffer, 0x09 - aFirstAddress, 3, 1); // Cal Status
    PutTheValue(aBuffer, 0x10 - aFirstAddress, 4, 1); // Sensor Status

    double yaw = mYaw;
    double pitch = mPitch;
    double roll = mRoll;

    yaw = BoundBetweenNeg180Pos180(yaw);
    pitch = BoundBetweenNeg180Pos180(pitch);
    roll = BoundBetweenNeg180Pos180(roll);

    PutTheValue(aBuffer, 0x16 - aFirstAddress, int16_t(yaw * 100), 2); // Yaw
    PutTheValue(aBuffer, 0x1A - aFirstAddress, int16_t(pitch * 100), 2); // Pitch
    PutTheValue(aBuffer, 0x18 - aFirstAddress, int16_t(roll * 100), 2); // Roll
//    PutTheValue(aBuffer, 0x1C - aFirstAddress, uint16_t(0 * 100), 2); // Heading
//    PutTheValue(aBuffer, 0x32 - aFirstAddress, int16_t(0 * 100), 2); // Temperature
//    PutTheValue(aBuffer, 0x24 - aFirstAddress, int16_t(mX * 1000), 2); // Linear Accel X
//    PutTheValue(aBuffer, 0x26 - aFirstAddress, int16_t(mY * 1000), 2); // Linear Accel Y
//    PutTheValue(aBuffer, 0x28 - aFirstAddress, int16_t(mZ * 1000), 2); // Linear Accel Z

}

int32_t NavxSimulator::RegisterXCallback(
    HAL_NotifyCallback callback, void* param, HAL_Bool initialNotify) {
  // Must return -1 on a null callback for error handling
  if (callback == nullptr) return -1;
  int32_t newUid = 0;
  {
    std::lock_guard<std::mutex> lock(m_registerMutex);
    mXCallbacks =
        RegisterCallback(mXCallbacks, "X", callback, param, &newUid);
  }
  if (initialNotify) {
    // We know that the callback is not null because of earlier null check
    HAL_Value value = MakeDouble(GetX());
    callback("X", param, &value);
  }
  return newUid;
}

void NavxSimulator::CancelXCallback(int32_t uid) {
  mXCallbacks = CancelCallback(mXCallbacks, uid);
}

void NavxSimulator::InvokeXCallback(HAL_Value value) {
  InvokeCallback(mXCallbacks, "X", &value);
}

double NavxSimulator::GetX() { return mX; }

void NavxSimulator::SetX(double x) {
  double oldValue = mX.exchange(x);
  if (oldValue != x) {
    InvokeXCallback(MakeDouble(x));
  }
}

int32_t NavxSimulator::RegisterYCallback(
    HAL_NotifyCallback callback, void* param, HAL_Bool initialNotify) {
  // Must return -1 on a null callback for error handling
  if (callback == nullptr) return -1;
  int32_t newUid = 0;
  {
    std::lock_guard<std::mutex> lock(m_registerMutex);
    mYCallbacks =
        RegisterCallback(mYCallbacks, "Y", callback, param, &newUid);
  }
  if (initialNotify) {
    // We know that the callback is not null because of earlier null check
    HAL_Value value = MakeDouble(GetY());
    callback("Y", param, &value);
  }
  return newUid;
}

void NavxSimulator::CancelYCallback(int32_t uid) {
  mYCallbacks = CancelCallback(mYCallbacks, uid);
}

void NavxSimulator::InvokeYCallback(HAL_Value value) {
  InvokeCallback(mYCallbacks, "Y", &value);
}

double NavxSimulator::GetY() { return mY; }

void NavxSimulator::SetY(double y) {
  double oldValue = mY.exchange(y);
  if (oldValue != y) {
    InvokeYCallback(MakeDouble(y));
  }
}

int32_t NavxSimulator::RegisterZCallback(
    HAL_NotifyCallback callback, void* param, HAL_Bool initialNotify) {
  // Must return -1 on a null callback for error handling
  if (callback == nullptr) return -1;
  int32_t newUid = 0;
  {
    std::lock_guard<std::mutex> lock(m_registerMutex);
    mZCallbacks =
        RegisterCallback(mZCallbacks, "Z", callback, param, &newUid);
  }
  if (initialNotify) {
    // We know that the callback is not null because of earlier null check
    HAL_Value value = MakeDouble(GetZ());
    callback("Z", param, &value);
  }
  return newUid;
}

void NavxSimulator::CancelZCallback(int32_t uid) {
  mZCallbacks = CancelCallback(mZCallbacks, uid);
}

void NavxSimulator::InvokeZCallback(HAL_Value value) {
  InvokeCallback(mZCallbacks, "Z", &value);
}

double NavxSimulator::GetZ() { return mZ; }

void NavxSimulator::SetZ(double z) {
  double oldValue = mZ.exchange(z);
  if (oldValue != z) {
    InvokeZCallback(MakeDouble(z));
  }
}

int32_t NavxSimulator::RegisterYawCallback(
    HAL_NotifyCallback callback, void* param, HAL_Bool initialNotify) {
  // Must return -1 on a null callback for error handling
  if (callback == nullptr) return -1;
  int32_t newUid = 0;
  {
    std::lock_guard<std::mutex> lock(m_registerMutex);
    mYawCallbacks =
        RegisterCallback(mYawCallbacks, "Yaw", callback, param, &newUid);
  }
  if (initialNotify) {
    // We know that the callback is not null because of earlier null check
    HAL_Value value = MakeDouble(GetX());
    callback("Yaw", param, &value);
  }
  return newUid;
}

void NavxSimulator::CancelYawCallback(int32_t uid) {
  mYawCallbacks = CancelCallback(mYawCallbacks, uid);
}

void NavxSimulator::InvokeYawCallback(HAL_Value value) {
  InvokeCallback(mYawCallbacks, "Yaw", &value);
}

double NavxSimulator::GetYaw() { return mYaw; }

void NavxSimulator::SetYaw(double yaw) {
  double oldValue = mYaw.exchange(yaw);
  if (oldValue != yaw) {
    InvokeYawCallback(MakeDouble(yaw));
  }
}

int32_t NavxSimulator::RegisterPitchCallback(
    HAL_NotifyCallback callback, void* param, HAL_Bool initialNotify) {
  // Must return -1 on a null callback for error handling
  if (callback == nullptr) return -1;
  int32_t newUid = 0;
  {
    std::lock_guard<std::mutex> lock(m_registerMutex);
    mPitchCallbacks =
        RegisterCallback(mPitchCallbacks, "Pitch", callback, param, &newUid);
  }
  if (initialNotify) {
    // We know that the callback is not null because of earlier null check
    HAL_Value value = MakeDouble(GetPitch());
    callback("Pitch", param, &value);
  }
  return newUid;
}

void NavxSimulator::CancelPitchCallback(int32_t uid) {
  mPitchCallbacks = CancelCallback(mPitchCallbacks, uid);
}

void NavxSimulator::InvokePitchCallback(HAL_Value value) {
  InvokeCallback(mPitchCallbacks, "Pitch", &value);
}

double NavxSimulator::GetPitch() { return mPitch; }

void NavxSimulator::SetPitch(double pitch) {
  double oldValue = mPitch.exchange(pitch);
  if (oldValue != pitch) {
    InvokePitchCallback(MakeDouble(pitch));
  }
}

int32_t NavxSimulator::RegisterRollCallback(
    HAL_NotifyCallback callback, void* param, HAL_Bool initialNotify) {
  // Must return -1 on a null callback for error handling
  if (callback == nullptr) return -1;
  int32_t newUid = 0;
  {
    std::lock_guard<std::mutex> lock(m_registerMutex);
    mRollCallbacks =
        RegisterCallback(mRollCallbacks, "Roll", callback, param, &newUid);
  }
  if (initialNotify) {
    // We know that the callback is not null because of earlier null check
    HAL_Value value = MakeDouble(GetRoll());
    callback("Roll", param, &value);
  }
  return newUid;
}

void NavxSimulator::CancelRollCallback(int32_t uid) {
  mRollCallbacks = CancelCallback(mRollCallbacks, uid);
}

void NavxSimulator::InvokeRollCallback(HAL_Value value) {
  InvokeCallback(mRollCallbacks, "Roll", &value);
}

double NavxSimulator::GetRoll() { return mRoll; }

void NavxSimulator::SetRoll(double roll) {
  double oldValue = mRoll.exchange(roll);
  if (oldValue != roll) {
    InvokeRollCallback(MakeDouble(roll));
  }
}
