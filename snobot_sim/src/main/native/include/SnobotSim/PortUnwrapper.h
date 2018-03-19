/*
 * PortUnwrapper.h
 *
 *  Created on: May 19, 2017
 *      Author: preiniger
 */

#ifndef SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_PORTUNWRAPPER_H_
#define SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_PORTUNWRAPPER_H_

inline int UnwrapPort(int aInPort)
{
    int output;

    if((aInPort & 0x2000000) == 0)
    {
        output = aInPort;
    }
    else
    {
        output = aInPort & 0xFF;
    }

    return output;
}

inline int WrapPort(int aInPort)
{
    int output = 0x2000100;
    output += aInPort;

    return output;
}




#endif  // SNOBOTSIM_SNOBOT_SIM_SRC_MAIN_NATIVE_INCLUDE_SNOBOTSIM_PORTUNWRAPPER_H_
