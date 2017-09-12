/*
 * II2CWrapper.cpp
 *
 *  Created on: Jun 22, 2017
 *      Author: preiniger
 */

#include "SnobotSim/SimulatorComponents/II2CWrapper.h"
#include "SnobotSim/Logging/SnobotLogger.h"


void NullI2CWrapper::HandleRead()
{
    SNOBOT_LOG(SnobotLogging::WARN, "Null Wrapper: " << __FUNCTION_NAME__);
}

//int NullI2CWrapper::Transaction(
//        uint8_t* dataToSend, int32_t sendSize,
//        uint8_t* dataReceived, int32_t receiveSize)
//{
//    // Header
//    dataReceived[0] = 0x55;
//    dataReceived[1] = 0xaa;
//
//    int i = 2;
//
//    // Block 1
//    dataReceived[i++] = 0x55; //sync
//    dataReceived[i++] = 0xaa;
//    dataReceived[i++] = 0xBE; //Checksum
//    dataReceived[i++] = 0xEF;
//    dataReceived[i++] = 0x01; //Signature
//    dataReceived[i++] = 0x01;
//    dataReceived[i++] = 160; // Centroid X
//    dataReceived[i++] = 0;
//    dataReceived[i++] = 200; // Centroid Y
//    dataReceived[i++] = 0;
//    dataReceived[i++] = 5; // Width
//    dataReceived[i++] = 0;
//    dataReceived[i++] = 36; // Height
//    dataReceived[i++] = 0;
//
//    // Block 2
//    dataReceived[i++] = 0x55; //sync
//    dataReceived[i++] = 0xaa;
//    dataReceived[i++] = 0xBE; //Checksum
//    dataReceived[i++] = 0xEF;
//    dataReceived[i++] = 0x01; //Signature
//    dataReceived[i++] = 0x01;
//    dataReceived[i++] = 30; // Centroid X
//    dataReceived[i++] = 0;
//    dataReceived[i++] = 100; // Centroid Y
//    dataReceived[i++] = 0;
//    dataReceived[i++] = 8; // Width
//    dataReceived[i++] = 0;
//    dataReceived[i++] = 43; // Height
//    dataReceived[i++] = 0;
//
//    SNOBOT_LOG(SnobotLogging::WARN, "Null Wrapper: " << __FUNCTION_NAME__);
//
//    return 0;
//}
//
//
//int32_t NullI2CWrapper::Read(
//        int32_t deviceAddress, uint8_t* buffer, int32_t count)
//{
//    SNOBOT_LOG(SnobotLogging::WARN, "Null Wrapper: " << __FUNCTION_NAME__);
//    return count;
//}
//
//int32_t NullI2CWrapper::Write(
//        int32_t deviceAddress, uint8_t* dataToSend, int32_t sendSize)
//{
//    SNOBOT_LOG(SnobotLogging::WARN, "Null Wrapper: " << __FUNCTION_NAME__);
//    return sendSize;
//}
