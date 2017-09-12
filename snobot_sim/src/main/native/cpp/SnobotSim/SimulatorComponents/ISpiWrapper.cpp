/*
 * II2CWrapper.cpp
 *
 *  Created on: Jun 22, 2017
 *      Author: preiniger
 */

#include "SnobotSim/SimulatorComponents/ISpiWrapper.h"
#include "SnobotSim/Logging/SnobotLogger.h"

void NullSpiWrapper::HandleRead()
{
    SNOBOT_LOG(SnobotLogging::WARN, "Null Wrapper: " << __FUNCTION_NAME__);
}
