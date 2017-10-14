/*
 * SnobotCoutLogger.cpp
 *
 *  Created on: Jun 21, 2017
 *      Author: PJ
 */

#include "SnobotSimJava/Logging/SnobotLogger.h"


namespace SnobotLogging
{
    ISnobotLogger* sLogger = NULL;

    void SetLogger(ISnobotLogger* aLogger)
    {
        sLogger = aLogger;
    }

    void Log(
            LogLevel aLogLevel,
            int aLineNumber,
            const std::string& aFileName,
            const std::string& aMessage)
    {
        if(sLogger != NULL)
        {
            sLogger->Log(aLogLevel, aLineNumber, aFileName, aMessage);
        }
    }

}






