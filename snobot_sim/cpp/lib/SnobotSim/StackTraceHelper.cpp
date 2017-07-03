

#include "SnobotSim/StackTraceHelper.h"
#include "SnobotSim/Logging/SnobotLogger.h"

#ifdef _WIN32

#include <Windows.h>

namespace StackTraceHelper
{
	void PrintStackTracker()
	{
		void *stack[48];
		USHORT count = CaptureStackBackTrace(0, 48, stack, NULL);
		for(USHORT c = 0; c < count; c++)
		  printf("addr %02d: %p\n", c, stack[c]);
	}
}

#else


namespace StackTraceHelper
{
	void PrintStackTracker()
	{
		LOG_UNSUPPORTED();
	}
}

#endif
