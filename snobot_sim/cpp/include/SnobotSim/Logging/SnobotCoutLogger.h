/*
 * SnobotCoutLogger.h
 *
 *  Created on: Jun 21, 2017
 *      Author: PJ
 */

#ifndef SNOBOTCOUTLOGGER_H_
#define SNOBOTCOUTLOGGER_H_

#include "SnobotSim/Logging/SnobotLogger.h"

namespace SnobotLogging
{
    class SnobotCoutLogger : public ISnobotLogger
    {
    public:
        SnobotCoutLogger();
        virtual ~SnobotCoutLogger();

        void Log(
                LogLevel aLogLevel,
                int aLineNumber,
                const std::string& aFileName,
                const std::string& aMessage);
    };
}
#endif /* SNOBOTCOUTLOGGER_H_ */
