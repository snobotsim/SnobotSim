/*
 * CtreTalonxSrxDeviceManager.cpp
 *
 *  Created on: Nov 29, 2017
 *      Author: PJ
 */

#include "SnobotSim/SimulatorComponents/Ctre/CtreTalonSrxDeviceManager.h"

#include <cstring>

#include "SnobotSim/SensorActuatorRegistry.h"
#include "SnobotSim/SimulatorComponents/Ctre/CtreTalonSrxSimulator.h"

std::shared_ptr<CtreTalonSrxSimulator> GetWrapperHelper(int aPort)
{
    std::shared_ptr<SpeedControllerWrapper> sc = SensorActuatorRegistry::Get().GetSpeedControllerWrapper(aPort + 100);
    return std::static_pointer_cast<CtreTalonSrxSimulator>(sc);
}

CtreTalonSrxDeviceManager::CtreTalonSrxDeviceManager()
{
}

CtreTalonSrxDeviceManager::~CtreTalonSrxDeviceManager()
{
}

void CtreTalonSrxDeviceManager::HandleSend(uint32_t aCanMessageId, uint32_t aCanPort, const uint8_t* aData, uint32_t aDataSize)
{
    if (aCanMessageId == 0x02040000)
    {
        HandleTx1(aData, aCanPort);
    }
    else if (aCanMessageId == 0x02041880)
    {
        HandleSetParamCommand(aData, aCanPort);
    }
    else if (aCanMessageId == 0x02041800)
    {
        HandleParamRequest(aData, aCanPort);
    }
    else
    {
        SNOBOT_LOG(SnobotLogging::CRITICAL, "Unsupported send " << std::hex << aCanMessageId << std::dec);
    }
}

void CtreTalonSrxDeviceManager::HandleReceive(uint32_t aCanMessageId, uint32_t aCanPort, uint8_t* aData, uint8_t& aOutDataSize, int32_t& aOutStatus)
{
    bool success = true;

    if (aCanMessageId == 0x02041400)
    {
        PopulateStatus1(aCanPort, aData);
    }
    else if (aCanMessageId == 0x02041440)
    {
        PopulateStatus2(aCanPort, aData);
    }
    else if (aCanMessageId == 0x02041480)
    {
        PopulateStatus3(aCanPort, aData);
    }
    else if (aCanMessageId == 0x020414C0)
    {
        PopulateStatus4(aCanPort, aData);
    }
    else
    {
        success = false;
        SNOBOT_LOG(SnobotLogging::CRITICAL, "Unsupported recv " << std::hex << aCanMessageId << std::dec);
    }

    if (success)
    {
        aOutDataSize = 8;
    }
}

uint32_t CtreTalonSrxDeviceManager::ReadStreamSession(uint32_t aSessionHandle, struct HAL_CANStreamMessage* aMessages, uint32_t aMessagesToRead)
{
    std::memcpy(&aMessages[0], &mStreamMessage, sizeof(HAL_CANStreamMessage));

    return 1;
}

void CtreTalonSrxDeviceManager::HandleTx1(const uint8_t* aData, int aPort)
{
    uint8_t command = aData[5] & 0xF0;

    if (command == 0x00)
    {
        std::shared_ptr<CtreTalonSrxSimulator> newSimulator(new CtreTalonSrxSimulator(aPort));
        SensorActuatorRegistry::Get().Register(aPort + 100, newSimulator);
    }
    else if (command == 0x20)
    {
        HandleSetDemandCommand(aData, aPort);
    }
    else
    {
        SNOBOT_LOG(SnobotLogging::CRITICAL, "Unsupported tx1 command 0x" << std::hex << static_cast<int>(command) << std::dec);
    }
}

void CtreTalonSrxDeviceManager::HandleSetDemandCommand(const uint8_t* aData, int aPort)
{
    uint8_t commandType = aData[6] >> 4;
    int demand = ((aData[2] << 24) | (aData[3] << 16) | (aData[4] << 8)) >> 8;

    std::shared_ptr<CtreTalonSrxSimulator> wrapper = GetWrapperHelper(aPort);

    if (commandType == 0x00)
    {
        double appliedVoltageDemand = demand / 1023.0;
        wrapper->SetVoltagePercentage(appliedVoltageDemand);
    }
    else if (commandType == 0x01)
    {
        double position = demand / 4096.0;
        wrapper->SetPositionGoal(position);
        SNOBOT_LOG(SnobotLogging::DEBUG, "  Setting by position." << position);
    }
    else if (commandType == 0x02)
    {
        double speed = demand * 600.0 / 4096.0;
        wrapper->SetSpeedGoal(speed);
        SNOBOT_LOG(SnobotLogging::DEBUG, " Setting by speed. " << speed);
    }
    else if (commandType == 0x03)
    {
        SNOBOT_LOG(SnobotLogging::DEBUG, "  Setting by current." << demand);
    }
    else if (commandType == 0x04)
    {
        double voltageDemand = demand / 256.0;
        wrapper->SetVoltagePercentage(voltageDemand / 12.0);
        SNOBOT_LOG(SnobotLogging::DEBUG, "  Setting by voltage. " << voltageDemand);
    }
    else if (commandType == 0x05)
    {
        SNOBOT_LOG(SnobotLogging::DEBUG, "Setting by Motion Follower");
    }
    else if (commandType == 0x06)
    {
        SNOBOT_LOG(SnobotLogging::DEBUG, "Setting by Motion Profile");
    }
    else if (commandType == 0x07)
    {
        SNOBOT_LOG(SnobotLogging::DEBUG, "Setting by Motion Magic");
    }
    else if (commandType == 0x0F)
    {
        // Nothing to do, but don't print error
    }
    else
    {
        SNOBOT_LOG(SnobotLogging::CRITICAL, "Unknown command type 0x" << std::hex << static_cast<int>(commandType) << std::dec);
    }
}

void CtreTalonSrxDeviceManager::HandleSetParamCommand(const uint8_t* aData, int aPort)
{
    std::shared_ptr<CtreTalonSrxSimulator> wrapper = GetWrapperHelper(aPort);
    uint8_t commandType = aData[0];

    int rawValue = 0;
    std::memcpy(&rawValue, &aData[1], sizeof(rawValue));
    double floatValue = rawValue * 0.0000002384185791015625;

    // P gain
    if (commandType == 1)
    {
        wrapper->SetPGain(floatValue);
    }
    // I gain
    else if (commandType == 2)
    {
        wrapper->SetIGain(floatValue);
    }
    // D gain
    else if (commandType == 3)
    {
        wrapper->SetDGain(floatValue);
    }
    // F gain
    else if (commandType == 4)
    {
        wrapper->SetFGain(floatValue);
    }
    // I Zone
    else if (commandType == 5)
    {
        wrapper->SetIzoneGain(rawValue);
    }
    else
    {
        SNOBOT_LOG(SnobotLogging::CRITICAL, "Unknown SetParam command " << std::hex << static_cast<int>(commandType) << std::dec);
    }
}

void CtreTalonSrxDeviceManager::HandleParamRequest(const uint8_t* aData, int aPort)
{
    std::shared_ptr<CtreTalonSrxSimulator> wrapper = GetWrapperHelper(aPort);
    uint8_t commandType = aData[0];

    double floatValue = 0;
    bool isFloat = true;

    // P gain
    if (commandType == 1)
    {
        floatValue = wrapper->GetPidConstants().mP;
    }
    // I gain
    else if (commandType == 2)
    {
        floatValue = wrapper->GetPidConstants().mI;
    }
    // D gain
    else if (commandType == 3)
    {
        floatValue = wrapper->GetPidConstants().mD;
    }
    // F gain
    else if (commandType == 4)
    {
        floatValue = wrapper->GetPidConstants().mF;
    }
    // I Zone
    else if (commandType == 5)
    {
        floatValue = wrapper->GetPidConstants().mIZone;
        isFloat = false;
    }
    else
    {
        SNOBOT_LOG(SnobotLogging::CRITICAL, "Unknown GetParam command " << std::hex << static_cast<int>(commandType) << std::dec);
    }
    int rawValue;
    if (isFloat)
    {
        rawValue = static_cast<int>(floatValue / 0.0000002384185791015625);
    }
    else
    {
        rawValue = static_cast<int>(floatValue);
    }

    mStreamMessage.messageID = 0x2041840 | aPort;
    mStreamMessage.timeStamp = 0xDEADBEEF;
    mStreamMessage.dataSize = 0;
    mStreamMessage.data[0] = commandType;

    std::memcpy(&mStreamMessage.data[1], &rawValue, sizeof(rawValue));
}

void CtreTalonSrxDeviceManager::PopulateStatus1(int aPort, uint8_t* aData)
{
    std::shared_ptr<CtreTalonSrxSimulator> wrapper = GetWrapperHelper(aPort);

    int16_t value = static_cast<int16_t>(wrapper->GetVoltagePercentage() * 1023);
    aData[3] = (value >> 8) & 0xFF;
    aData[4] = (value >> 0) & 0xFF;
}

void CtreTalonSrxDeviceManager::PopulateStatus2(int aPort, uint8_t* aData)
{
    std::shared_ptr<CtreTalonSrxSimulator> wrapper = GetWrapperHelper(aPort);

    int binnedPosition = static_cast<int>(wrapper->GetPosition() * 4096);
    int binnedVelocity = static_cast<int>(wrapper->GetVelocity() * 6.9);

    aData[0] = ((binnedPosition >> 0) & 0xFF);
    aData[1] = ((binnedPosition >> 8) & 0xFF);
    aData[2] = ((binnedPosition >> 16) & 0xFF);
    aData[3] = ((binnedVelocity >> 0) & 0xFF);
    aData[4] = ((binnedVelocity >> 8) & 0xFF);
}

void CtreTalonSrxDeviceManager::PopulateStatus3(int aPort, uint8_t* aData)
{
    std::shared_ptr<CtreTalonSrxSimulator> wrapper = GetWrapperHelper(aPort);

    int binnedPosition = static_cast<int>(wrapper->GetPosition());
    int binnedVelocity = static_cast<int>(wrapper->GetVelocity());

    aData[0] = ((binnedPosition >> 0) & 0xFF);
    aData[1] = ((binnedPosition >> 8) & 0xFF);
    aData[2] = ((binnedPosition >> 16) & 0xFF);
    aData[3] = ((binnedVelocity >> 0) & 0xFF);
    aData[4] = ((binnedVelocity >> 8) & 0xFF);
}

void CtreTalonSrxDeviceManager::PopulateStatus4(int aPort, uint8_t* aData)
{
    double temperature = 30;
    double batteryVoltage = 12;

    uint8_t temperatureBinned = ((temperature + 50) / 0.6451612903);
    uint8_t batterBinned = ((batteryVoltage - 4) / 0.05);

    std::shared_ptr<CtreTalonSrxSimulator> wrapper = GetWrapperHelper(aPort);
    int binnedPosition = static_cast<int>(wrapper->GetPosition());
    int binnedVelocity = static_cast<int>(wrapper->GetVelocity());

    aData[0] = ((binnedPosition >> 0) & 0xFF);
    aData[1] = ((binnedPosition >> 8) & 0xFF);
    aData[2] = ((binnedPosition >> 16) & 0xFF);
    aData[3] = ((binnedVelocity >> 0) & 0xFF);
    aData[4] = ((binnedVelocity >> 8) & 0xFF);
    aData[5] = temperatureBinned;
    aData[6] = batterBinned;
}
