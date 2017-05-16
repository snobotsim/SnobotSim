/*
 * JoystickManager.h
 *
 *  Created on: May 9, 2017
 *      Author: preiniger
 */

#ifndef JOYSTICKMANAGER_H_
#define JOYSTICKMANAGER_H_

#include <map>

struct JoystickInformation
{
	static const int kMaxJoystickAxes = 12;
	static const int kMaxJoystickPOVs = 12;

	struct JoystickAxes {
	  int16_t count;
	  float axes[kMaxJoystickAxes];
	};

	struct JoystickPOVs {
	  int16_t count;
	  int16_t povs[kMaxJoystickPOVs];
	};

	struct JoystickButtons {
	  uint32_t buttons;
	  uint8_t count;
	};

    JoystickAxes mAxes;
    JoystickPOVs mPovs;
    JoystickButtons mButtons;

    JoystickInformation();
};

class JoystickManager
{
private:
    JoystickManager();

    static JoystickManager sINSTANCE;

public:

    virtual ~JoystickManager();

    static JoystickManager& Get();
    bool HasJoystick(int aHandle);
    JoystickInformation& GetJoystick(int aHandle);

protected:

    std::map<int, JoystickInformation> mJoystickInformation;

    static JoystickInformation sNULL_INSTANCE;

};

#endif /* JOYSTICKMANAGER_H_ */
