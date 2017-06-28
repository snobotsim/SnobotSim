/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2016-2017. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

#include "HAL/SPI.h"

#include <atomic>
#include <cstdio>
#include <cstring>

#include "HAL/DIO.h"
#include "HAL/HAL.h"
#include "HAL/Notifier.h"
#include "HAL/cpp/make_unique.h"
#include "HAL/cpp/priority_mutex.h"
#include "HAL/handles/HandlesInternal.h"

#include "SnobotSim/SensorActuatorRegistry.h"
#include "SnobotSim/SimulatorComponents/Gyro/SpiGyro.h"

using namespace hal;


extern "C" {

/*
 * Initialize the spi port. Opens the port if necessary and saves the handle.
 * If opening the MXP port, also sets up the channel functions appropriately
 * @param port The number of the port to use. 0-3 for Onboard CS0-CS3, 4 for MXP
 */
void HAL_InitializeSPI(HAL_SPIPort port, int32_t* status) {

    std::shared_ptr<SpiGyro> spiGyro(new SpiGyro);
    std::shared_ptr<ISpiWrapper> wrapper(spiGyro);
    std::shared_ptr<GyroWrapper> castGyro = spiGyro;

    SensorActuatorRegistry::Get().Register(port, wrapper);
    SensorActuatorRegistry::Get().Register(port + 100, castGyro);
}

/**
 * Generic transaction.
 *
 * This is a lower-level interface to the spi hardware giving you more control
 * over each transaction.
 *
 * @param port The number of the port to use. 0-3 for Onboard CS0-CS2, 4 for MXP
 * @param dataToSend Buffer of data to send as part of the transaction.
 * @param dataReceived Buffer to read data into.
 * @param size Number of bytes to transfer. [0..7]
 * @return Number of bytes transferred, -1 for error
 */
int32_t HAL_TransactionSPI(HAL_SPIPort port, uint8_t* dataToSend,
                           uint8_t* dataReceived, int32_t size) {
    LOG_UNSUPPORTED();
    return 0;
}

/**
 * Execute a write transaction with the device.
 *
 * Write to a device and wait until the transaction is complete.
 *
 * @param port The number of the port to use. 0-3 for Onboard CS0-CS2, 4 for MXP
 * @param datToSend The data to write to the register on the device.
 * @param sendSize The number of bytes to be written
 * @return The number of bytes written. -1 for an error
 */
int32_t HAL_WriteSPI(HAL_SPIPort port, uint8_t* dataToSend, int32_t sendSize) {
    LOG_UNSUPPORTED();
    return 0;
}

/**
 * Execute a read from the device.
 *
 *   This method does not write any data out to the device
 *   Most spi devices will require a register address to be written before
 *   they begin returning data
 *
 * @param port The number of the port to use. 0-3 for Onboard CS0-CS2, 4 for MXP
 * @param buffer A pointer to the array of bytes to store the data read from the
 * device.
 * @param count The number of bytes to read in the transaction. [1..7]
 * @return Number of bytes read. -1 for error.
 */
int32_t HAL_ReadSPI(HAL_SPIPort port, uint8_t* buffer, int32_t count) {

    return SensorActuatorRegistry::Get().GetISpiWrapper(port)->Read(buffer, count);
}

/**
 * Close the SPI port
 *
 * @param port The number of the port to use. 0-3 for Onboard CS0-CS2, 4 for MXP
 */
void HAL_CloseSPI(HAL_SPIPort port) {
    LOG_UNSUPPORTED();
}

/**
 * Set the clock speed for the SPI bus.
 *
 * @param port The number of the port to use. 0-3 for Onboard CS0-CS2, 4 for MXP
 * @param speed The speed in Hz (0-1MHz)
 */
void HAL_SetSPISpeed(HAL_SPIPort port, int32_t speed) {
    LOG_UNSUPPORTED();
}

/**
 * Set the SPI options
 *
 * @param port The number of the port to use. 0-3 for Onboard CS0-CS2, 4 for MXP
 * @param msbFirst True to write the MSB first, False for LSB first
 * @param sampleOnTrailing True to sample on the trailing edge, False to sample
 * on the leading edge
 * @param clkIdleHigh True to set the clock to active low, False to set the
 * clock active high
 */
void HAL_SetSPIOpts(HAL_SPIPort port, HAL_Bool msbFirst, HAL_Bool sampleOnTrailing,
                    HAL_Bool clkIdleHigh) {
    LOG_UNSUPPORTED();
}

/**
 * Set the CS Active high for a SPI port
 *
 * @param port The number of the port to use. 0-3 for Onboard CS0-CS2, 4 for MXP
 */
void HAL_SetSPIChipSelectActiveHigh(HAL_SPIPort port, int32_t* status) {
    LOG_UNSUPPORTED();
}

/**
 * Set the CS Active low for a SPI port
 *
 * @param port The number of the port to use. 0-3 for Onboard CS0-CS2, 4 for MXP
 */
void HAL_SetSPIChipSelectActiveLow(HAL_SPIPort port, int32_t* status) {
    LOG_UNSUPPORTED();
}

/**
 * Get the stored handle for a SPI port
 *
 * @param port The number of the port to use. 0-3 for Onboard CS0-CS2, 4 for MXP
 * @return The stored handle for the SPI port. 0 represents no stored handle.
 */
int32_t HAL_GetSPIHandle(HAL_SPIPort port) {
    LOG_UNSUPPORTED();
    return 0;
}

/**
 * Set the stored handle for a SPI port
 *
 * @param port The number of the port to use. 0-3 for Onboard CS0-CS2, 4 for
 * MXP.
 * @param handle The value of the handle for the port.
 */
void HAL_SetSPIHandle(HAL_SPIPort port, int32_t handle) {
    LOG_UNSUPPORTED();
}

/**
 * Initialize a SPI accumulator.
 *
 * @param port SPI port
 * @param period Time between reads, in us
 * @param cmd SPI command to send to request data
 * @param xferSize SPI transfer size, in bytes
 * @param validMask Mask to apply to received data for validity checking
 * @param valid_data After validMask is applied, required matching value for
 *                   validity checking
 * @param dataShift Bit shift to apply to received data to get actual data
 *                   value
 * @param dataSize Size (in bits) of data field
 * @param isSigned Is data field signed?
 * @param bigEndian Is device big endian?
 */
void HAL_InitSPIAccumulator(HAL_SPIPort port, int32_t period, int32_t cmd,
                            int32_t xferSize, int32_t validMask,
                            int32_t validValue, int32_t dataShift,
                            int32_t dataSize, HAL_Bool isSigned,
                            HAL_Bool bigEndian, int32_t* status) {
    LOG_UNSUPPORTED();
}

/**
 * Frees a SPI accumulator.
 */
void HAL_FreeSPIAccumulator(HAL_SPIPort port, int32_t* status) {
    LOG_UNSUPPORTED();
}

/**
 * Resets the accumulator to zero.
 */
void HAL_ResetSPIAccumulator(HAL_SPIPort port, int32_t* status) {
    return SensorActuatorRegistry::Get().GetISpiWrapper(port)->ResetAccumulatorValue();
}

/**
 * Set the center value of the accumulator.
 *
 * The center value is subtracted from each value before it is added to the
 * accumulator. This
 * is used for the center value of devices like gyros and accelerometers to make
 * integration work
 * and to take the device offset into account when integrating.
 */
void HAL_SetSPIAccumulatorCenter(HAL_SPIPort port, int32_t center,
                                 int32_t* status) {
    LOG_UNSUPPORTED();
}

/**
 * Set the accumulator's deadband.
 */
void HAL_SetSPIAccumulatorDeadband(HAL_SPIPort port, int32_t deadband,
                                   int32_t* status) {
    LOG_UNSUPPORTED();
}

/**
 * Read the last value read by the accumulator engine.
 */
int32_t HAL_GetSPIAccumulatorLastValue(HAL_SPIPort port, int32_t* status) {
    LOG_UNSUPPORTED();
    return 0;
}

/**
 * Read the accumulated value.
 *
 * @return The 64-bit value accumulated since the last Reset().
 */
int64_t HAL_GetSPIAccumulatorValue(HAL_SPIPort port, int32_t* status) {
    return SensorActuatorRegistry::Get().GetISpiWrapper(port)->GetAccumulatorValue();
}

/**
 * Read the number of accumulated values.
 *
 * Read the count of the accumulated values since the accumulator was last
 * Reset().
 *
 * @return The number of times samples from the channel were accumulated.
 */
int64_t HAL_GetSPIAccumulatorCount(HAL_SPIPort port, int32_t* status) {
    LOG_UNSUPPORTED();
    return 0;
}

/**
 * Read the average of the accumulated value.
 *
 * @return The accumulated average value (value / count).
 */
double HAL_GetSPIAccumulatorAverage(HAL_SPIPort port, int32_t* status) {
    LOG_UNSUPPORTED();
    return 0;
}

/**
 * Read the accumulated value and the number of accumulated values atomically.
 *
 * This function reads the value and count atomically.
 * This can be used for averaging.
 *
 * @param value Pointer to the 64-bit accumulated output.
 * @param count Pointer to the number of accumulation cycles.
 */
void HAL_GetSPIAccumulatorOutput(HAL_SPIPort port, int64_t* value, int64_t* count,
                                 int32_t* status) {
    LOG_UNSUPPORTED();

}
}
