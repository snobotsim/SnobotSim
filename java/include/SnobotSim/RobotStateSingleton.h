/*
 * RobotStateSingleton.hpp
 *
 *  Created on: May 5, 2017
 *      Author: preiniger
 */

#ifndef ROBOTSTATESINGLETON_H_
#define ROBOTSTATESINGLETON_H_

class RobotStateSingleton
{
private:
    RobotStateSingleton();
    static RobotStateSingleton sINSTANCE;

public:
    virtual ~RobotStateSingleton();

    static RobotStateSingleton& Get()
    {
        return sINSTANCE;
    }

    void UpdateLoop();

    double GetMatchTime();

    void SetDisabled(bool aDisabled);

    void SetAutonomous(bool aAuto);

    void SetTest(bool aTest);

    bool GetDisabled() const;

    bool GetAutonomous() const;

    bool GetTest() const;

protected:

    bool mEnabled;
    bool mAutonomous;
    bool mTest;
};

#endif /* ROBOTSTATESINGLETON_H_ */
