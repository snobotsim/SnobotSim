/*
 * SnobotCoutLogger.cpp
 *
 *  Created on: Jun 21, 2017
 *      Author: PJ
 */

#include "SnobotSim/Logging/SnobotLogger.h"


SnobotLogging::NullLogger SnobotLogging::LoggerWrapper::sNullLogger;
SnobotLogging::ISnobotLogger* SnobotLogging::LoggerWrapper::sLogger = &sNullLogger;


void SnobotLogging::NullLogger::Log(
        LogLevel aLogLevel,
        int aLineNumber,
        const std::string& aFileName,
        const std::string& aMessage)
{

}


void SnobotLogging::LoggerWrapper::SetLogger(ISnobotLogger* aLogger)
{
    if(aLogger == NULL)
    {
        sLogger = &sNullLogger;
    }
    else
    {
        sLogger = aLogger;
    }
}

void SnobotLogging::LoggerWrapper::Log(
        LogLevel aLogLevel,
        int aLineNumber,
        const std::string& aFileName,
        const std::string& aMessage)
{
    sLogger->Log(aLogLevel, aLineNumber, aFileName, aMessage);
}
