/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2016-2017. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

#include "HAL/Accelerometer.h"

#include <stdint.h>

#include <cassert>
#include <cstdio>
#include <memory>

#include "HAL/HAL.h"

#include "SnobotSim/Logging/SnobotLogger.h"

// The 7-bit I2C address with a 0 "send" bit
static const uint8_t kSendAddress = (0x1c << 1) | 0;

// The 7-bit I2C address with a 1 "receive" bit
static const uint8_t kReceiveAddress = (0x1c << 1) | 1;

static const uint8_t kControlTxRx = 1;
static const uint8_t kControlStart = 2;
static const uint8_t kControlStop = 4;

// Register addresses
enum Register {
  kReg_Status = 0x00,
  kReg_OutXMSB = 0x01,
  kReg_OutXLSB = 0x02,
  kReg_OutYMSB = 0x03,
  kReg_OutYLSB = 0x04,
  kReg_OutZMSB = 0x05,
  kReg_OutZLSB = 0x06,
  kReg_Sysmod = 0x0B,
  kReg_IntSource = 0x0C,
  kReg_WhoAmI = 0x0D,
  kReg_XYZDataCfg = 0x0E,
  kReg_HPFilterCutoff = 0x0F,
  kReg_PLStatus = 0x10,
  kReg_PLCfg = 0x11,
  kReg_PLCount = 0x12,
  kReg_PLBfZcomp = 0x13,
  kReg_PLThsReg = 0x14,
  kReg_FFMtCfg = 0x15,
  kReg_FFMtSrc = 0x16,
  kReg_FFMtThs = 0x17,
  kReg_FFMtCount = 0x18,
  kReg_TransientCfg = 0x1D,
  kReg_TransientSrc = 0x1E,
  kReg_TransientThs = 0x1F,
  kReg_TransientCount = 0x20,
  kReg_PulseCfg = 0x21,
  kReg_PulseSrc = 0x22,
  kReg_PulseThsx = 0x23,
  kReg_PulseThsy = 0x24,
  kReg_PulseThsz = 0x25,
  kReg_PulseTmlt = 0x26,
  kReg_PulseLtcy = 0x27,
  kReg_PulseWind = 0x28,
  kReg_ASlpCount = 0x29,
  kReg_CtrlReg1 = 0x2A,
  kReg_CtrlReg2 = 0x2B,
  kReg_CtrlReg3 = 0x2C,
  kReg_CtrlReg4 = 0x2D,
  kReg_CtrlReg5 = 0x2E,
  kReg_OffX = 0x2F,
  kReg_OffY = 0x30,
  kReg_OffZ = 0x31
};

extern "C" {

/**
 * Set the accelerometer to active or standby mode.  It must be in standby
 * mode to change any configuration.
 */
void HAL_SetAccelerometerActive(HAL_Bool active) {
    LOG_UNSUPPORTED();
}

/**
 * Set the range of values that can be measured (either 2, 4, or 8 g-forces).
 * The accelerometer should be in standby mode when this is called.
 */
void HAL_SetAccelerometerRange(HAL_AccelerometerRange range) {
    LOG_UNSUPPORTED();
}

/**
 * Get the x-axis acceleration
 *
 * This is a floating point value in units of 1 g-force
 */
double HAL_GetAccelerometerX() {
    LOG_UNSUPPORTED();
    return 0;
}

/**
 * Get the y-axis acceleration
 *
 * This is a floating point value in units of 1 g-force
 */
double HAL_GetAccelerometerY() {
    LOG_UNSUPPORTED();
    return 0;
}

/**
 * Get the z-axis acceleration
 *
 * This is a floating point value in units of 1 g-force
 */
double HAL_GetAccelerometerZ() {
    LOG_UNSUPPORTED();
    return 0;
}

}  // extern "C"
