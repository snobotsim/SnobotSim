/*
 * II2CWrapper.cpp
 *
 *  Created on: Jun 22, 2017
 *      Author: preiniger
 */

#include "SnobotSim/SimulatorComponents/Spi/ISpiWrapper.h"
#include "SnobotSim/Logging/SnobotLogger.h"

void NullSpiWrapper::HandleRead()
{
    SNOBOT_LOG(SnobotLogging::WARN, "Null Wrapper: " << __FUNCTION_NAME__);
}

void NullSpiWrapper::HandleWrite()
{
    SNOBOT_LOG(SnobotLogging::WARN, "Null Wrapper: " << __FUNCTION_NAME__);
}

void NullSpiWrapper::HandleTransaction()
{
    SNOBOT_LOG(SnobotLogging::WARN, "Null Wrapper: " << __FUNCTION_NAME__);
}
