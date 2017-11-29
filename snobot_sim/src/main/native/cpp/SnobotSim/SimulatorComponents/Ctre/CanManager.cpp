/*
 * CanManager.cpp
 *
 *  Created on: Nov 29, 2017
 *      Author: PJ
 */

#include "SnobotSim/SimulatorComponents/Ctre/CanManager.h"
#include "MockData/CanData.h"

#include "SnobotSim/SimulatorComponents/Ctre/CtrePigeonImuDeviceManager.h"
#include "SnobotSim/SimulatorComponents/Ctre/CtreTalonSrxDeviceManager.h"

#include <iostream>

void CanSendMessageCallback(
        const char* name, void* param,
        uint32_t messageID, const uint8_t* data,
        uint8_t dataSize, int32_t periodMs, int32_t* status);

void CanReadMessageCallback(
        const char* name, void* param,
        uint32_t* messageID, uint32_t messageIDMask,
        uint8_t* data, uint8_t* dataSize,
        uint32_t* timeStamp, int32_t* status);

void CanOpenStreamCallback(
        const char* name, void* param,
        uint32_t* sessionHandle, uint32_t messageID,
        uint32_t messageIDMask, uint32_t maxMessages,
        int32_t* status);

void CanCloseStreamSessionCallback(
        const char* name, void* param,
        uint32_t sessionHandle);

void CanReadStreamSessionCallback(
        const char* name, void* param,
        uint32_t sessionHandle,
        struct HAL_CANStreamMessage* messages,
        uint32_t messagesToRead, uint32_t* messagesRead,
        int32_t* status);

void CanGetCANStatusCallback(
        const char* name, void* param,
        float* percentBusUtilization, uint32_t* busOffCount,
        uint32_t* txFullCount, uint32_t* receiveErrorCount,
        uint32_t* transmitErrorCount, int32_t* status);

int CanManager::sSTREAM_CTR = 1;

CanManager::CanManager()
{
    mSendMessageCallbackId = HALSIM_RegisterCanSendMessageCallback(&CanSendMessageCallback, this);
    mReceiveMessageCallbackId = HALSIM_RegisterCanReceiveMessageCallback(&CanReadMessageCallback, this);
    mOpenStreamCallbackId = HALSIM_RegisterCanOpenStreamCallback(&CanOpenStreamCallback, this);
    mCloseStreamCallbackId = HALSIM_RegisterCanCloseStreamCallback(&CanCloseStreamSessionCallback, this);
    mReadStreamCallbackId = HALSIM_RegisterCanReadStreamCallback(&CanReadStreamSessionCallback, this);
    mGetCanStatusCallbackId = HALSIM_RegisterCanGetCANStatusCallback(&CanGetCANStatusCallback, this);

    std::shared_ptr<ICanDeviceManager> talonManager(new CtreTalonSrxDeviceManager());
    mMessageIdToDeviceManagerMap[0x02041840] = talonManager; // Stream
    mMessageIdToDeviceManagerMap[0x02041400] = talonManager; // Send
    mMessageIdToDeviceManagerMap[0x02041440] = talonManager; // Send
    mMessageIdToDeviceManagerMap[0x02041480] = talonManager; // Send
    mMessageIdToDeviceManagerMap[0x020414C0] = talonManager; // Send
    mMessageIdToDeviceManagerMap[0x02040000] = talonManager; // Recv
    mMessageIdToDeviceManagerMap[0x02041880] = talonManager; // Recv
    mMessageIdToDeviceManagerMap[0x02041800] = talonManager; // Recv

    std::shared_ptr<ICanDeviceManager> pigeonManager(new CtrePigeonImuDeviceManager());
    mMessageIdToDeviceManagerMap[0x15042800] = pigeonManager; // Send, Series
    mMessageIdToDeviceManagerMap[0x15042140] = pigeonManager; // Recv, Series
    mMessageIdToDeviceManagerMap[0x15041C40] = pigeonManager; // Recv, Series
    mMessageIdToDeviceManagerMap[0x02042800] = pigeonManager; // Send, Talon
    mMessageIdToDeviceManagerMap[0x02041C40] = pigeonManager; // Recv, Talon
    mMessageIdToDeviceManagerMap[0x02042140] = pigeonManager; // Recv, Talon
}

CanManager::~CanManager()
{
    HALSIM_CancelCanSendMessageCallback(mSendMessageCallbackId);
    HALSIM_CancelCanReceiveMessageCallback(mReceiveMessageCallbackId);
    HALSIM_CancelCanOpenStreamCallback(mOpenStreamCallbackId);
    HALSIM_CancelCanCloseStreamCallback(mCloseStreamCallbackId);
    HALSIM_CancelCanReadStreamCallback(mReadStreamCallbackId);
    HALSIM_CancelCanReadStreamCallback(mGetCanStatusCallbackId);
}


void CanManager::HandleSendMessage(uint32_t aCanMessageId, uint32_t aCanPort, const uint8_t* aData, uint32_t aDataSize)
{
    IdToDeviceManagerMap_t::iterator findIter = mMessageIdToDeviceManagerMap.find(aCanMessageId);
    if(findIter != mMessageIdToDeviceManagerMap.end())
    {
        findIter->second->HandleSend(aCanMessageId, aCanPort, aData, aDataSize);
    }
    else
    {
        std::cout << "Unknown send device " << std::hex << "0x" << aCanMessageId << std::dec << ", " << aCanPort << std::endl;
    }
}

void CanManager::HandleReceiveMessage(uint32_t aCanMessageId, uint32_t aCanPort, uint8_t* aData, uint8_t& aOutDataSize, int32_t& aOutStatus)
{
    std::memset(aData, 0, 8);

    IdToDeviceManagerMap_t::iterator findIter = mMessageIdToDeviceManagerMap.find(aCanMessageId);
    if(findIter != mMessageIdToDeviceManagerMap.end())
    {
        findIter->second->HandleReceive(aCanMessageId, aCanPort, aData, aOutDataSize, aOutStatus);
    }
    else
    {
        std::cout << "Unknown recv device " << std::hex << "0x" << aCanMessageId << std::dec << ", " << aCanPort << std::endl;
    }
}

uint32_t CanManager::HandleOpenStream(uint32_t aMessageId, uint32_t aMessageIdMask, uint32_t aMaxMessages, int32_t& aOutStatus)
{
    IdToDeviceManagerMap_t::iterator findIter = mMessageIdToDeviceManagerMap.find(aMessageId);
    if(findIter != mMessageIdToDeviceManagerMap.end())
    {
        uint32_t streamSession = sSTREAM_CTR++;
        mStreamIdToDeviceManagerMap[streamSession] = findIter->second;
        return streamSession;
    }
    else
    {
        std::cout << "Unknown open stream device " << std::hex << "0x" << aMessageId << std::dec << std::endl;
    }

    return 0;
}

uint32_t CanManager::HandleReadStream(uint32_t aSessionHandle, struct HAL_CANStreamMessage* aMessages, uint32_t aMessagesToRead)
{
    IdToDeviceManagerMap_t::iterator findIter = mStreamIdToDeviceManagerMap.find(aSessionHandle);
    if(findIter != mStreamIdToDeviceManagerMap.end())
    {
        return findIter->second->ReadStreamSession(aSessionHandle, aMessages, aMessagesToRead);
    }
    else
    {
        std::cout << "Unknown read stream device " << aSessionHandle << std::endl;
    }

    return 0;
}

void CanManager::HandleCloseStream(uint32_t aSessionHandle)
{
    mStreamIdToDeviceManagerMap.erase(aSessionHandle);
}

//////////////////////////////////////////
// Callbacks....
//////////////////////////////////////////

void CanSendMessageCallback(
        const char* name, void* param,
        uint32_t messageID, const uint8_t* data,
        uint8_t dataSize, int32_t periodMs, int32_t* status)
{
    uint32_t canMessageId = messageID & 0xFFFFFFC0;
    uint32_t canPort = messageID & 0x3F;

    CanManager* canManager = (CanManager*) param;
    canManager->HandleSendMessage(canMessageId, canPort, data, dataSize);
}

void CanReadMessageCallback(
        const char* name, void* param,
        uint32_t* messageID, uint32_t messageIDMask,
        uint8_t* data, uint8_t* dataSize,
        uint32_t* timeStamp, int32_t* status)
{
    uint32_t canMessageId = (*messageID) & 0xFFFFFFC0;
    uint32_t canPort = (*messageID) & 0x3F;

    CanManager* canManager = (CanManager*) param;
    canManager->HandleReceiveMessage(canMessageId, canPort, data, *dataSize, *status);
}

void CanOpenStreamCallback(
        const char* name, void* param,
        uint32_t* sessionHandle, uint32_t messageID,
        uint32_t messageIDMask, uint32_t maxMessages,
        int32_t* status)
{
    uint32_t canMessageId = messageID & 0xFFFFFFC0;

    CanManager* canManager = (CanManager*) param;
    *sessionHandle = canManager->HandleOpenStream(canMessageId, messageIDMask, maxMessages, *status);
}

void CanCloseStreamSessionCallback(
        const char* name, void* param,
        uint32_t sessionHandle)
{
    CanManager* canManager = (CanManager*) param;
    canManager->HandleCloseStream(sessionHandle);
}

void CanReadStreamSessionCallback(
        const char* name, void* param,
        uint32_t sessionHandle,
        struct HAL_CANStreamMessage* messages,
        uint32_t messagesToRead, uint32_t* messagesRead,
        int32_t* status)
{
    CanManager* canManager = (CanManager*) param;
    *messagesRead = canManager->HandleReadStream(sessionHandle, messages, messagesToRead);
}

void CanGetCANStatusCallback(
        const char* name, void* param,
        float* percentBusUtilization, uint32_t* busOffCount,
        uint32_t* txFullCount, uint32_t* receiveErrorCount,
        uint32_t* transmitErrorCount, int32_t* status)
{
    CanManager* canManager = (CanManager*) param;
}
