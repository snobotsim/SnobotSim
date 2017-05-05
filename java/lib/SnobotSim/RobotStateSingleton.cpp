/*
 * RobotStateSingleton.cpp
 *
 *  Created on: May 5, 2017
 *      Author: preiniger
 */

#include "SnobotSim/RobotStateSingleton.h"
#include <iostream>

RobotStateSingleton RobotStateSingleton::sINSTANCE = RobotStateSingleton();

RobotStateSingleton::RobotStateSingleton() :
        mEnabled(false), mAutonomous(false), mTest(false)
{

}

RobotStateSingleton::~RobotStateSingleton()
{

}



double RobotStateSingleton::GetMatchTime()
{
    return 0;
}

void RobotStateSingleton::SetDisabled(bool aDisabled)
{
    mEnabled = !aDisabled;
    std::cout << "Setting enabled: " << mEnabled << std::endl;
}

void RobotStateSingleton::SetAutonomous(bool aAutonomous)
{
    mAutonomous = aAutonomous;
    std::cout << "Setting Auto: " << mAutonomous << std::endl;
}

void RobotStateSingleton::SetTest(bool aTest)
{
    mTest = aTest;
}

bool RobotStateSingleton::GetDisabled() const
{
    return !mEnabled;
}

bool RobotStateSingleton::GetAutonomous() const
{
    return mAutonomous;
}

bool RobotStateSingleton::GetTest() const
{
    return mTest;
}
