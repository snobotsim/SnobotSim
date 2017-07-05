/*
 * II2CWrapper.cpp
 *
 *  Created on: Jun 22, 2017
 *      Author: preiniger
 */

#include "SnobotSim/SimulatorComponents/ISpiWrapper.h"
#include "SnobotSim/Logging/SnobotLogger.h"


double NullSpiWrapper::GetAccumulatorValue()
{
    SNOBOT_LOG(SnobotLogging::WARN, "Null Wrapper: " << __FUNCTION_NAME__);
    return 0;
}

void NullSpiWrapper::ResetAccumulatorValue()
{
    SNOBOT_LOG(SnobotLogging::WARN, "Null Wrapper" << __FUNCTION_NAME__);
}


int32_t NullSpiWrapper::Read(uint8_t* buffer, int32_t count)
{
    SNOBOT_LOG(SnobotLogging::WARN, "Null Wrapper" << __FUNCTION_NAME__);
    return 0;
}
