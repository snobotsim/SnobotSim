
#include <iostream>

namespace SnobotSim
{

namespace CanTalonSim
{



#define SNOBOT_SIM_CAN_LOG_UNSUPPORTED() std::cout << "Unsupported" << std::endl;
#define SNOBOT_SIM_CAN_LOG(y) std::cout << " - " << y << std::endl;
#define SNOBOT_SIM_CAN_LOG_UNSUPPORTED_WITH_MESSAGE(x) std::cout << x << std::endl;

}

}
