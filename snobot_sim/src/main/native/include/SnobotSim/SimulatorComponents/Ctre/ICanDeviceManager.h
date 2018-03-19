/*
 * ICanDeviceManager.h
 *
 *  Created on: Nov 29, 2017
 *      Author: PJ
 */

#ifndef SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_SIMULATORCOMPONENTS_CTRE_ICANDEVICEMANAGER_H_
#define SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_SIMULATORCOMPONENTS_CTRE_ICANDEVICEMANAGER_H_

#include <stdint.h>

class ICanDeviceManager
{
public:
    virtual void HandleSend(uint32_t aCanMessageId, uint32_t aCanPort, const uint8_t* aData, uint32_t aDataSize) = 0;

    virtual void HandleReceive(uint32_t aCanMessageId, uint32_t aCanPort, uint8_t* aData, uint8_t& aOutDataSize, int32_t& aOutStatus) = 0;

    virtual uint32_t ReadStreamSession(uint32_t aSessionHandle, struct HAL_CANStreamMessage* aMessages, uint32_t aMessagesToRead) = 0;
};

#endif // SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_SIMULATORCOMPONENTS_CTRE_ICANDEVICEMANAGER_H_
