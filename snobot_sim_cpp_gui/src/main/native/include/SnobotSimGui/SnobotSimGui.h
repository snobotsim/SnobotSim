/*
 * StandaloneSimulator.h
 *
 *  Created on: Jul 18, 2018
 *      Author: PJ
 */

#pragma once

#ifdef _MSC_VER
#define EXPORT_ __declspec(dllexport)
#else
#define EXPORT_
#endif


#include "SnobotSimGui/ModuleWrapperDisplay/IWidgetDisplay.h"
#include "SnobotSimGui/Joysticks/JoystickManager.h"
#include "SnobotSimGui/DriverStationWidget.h"
#include <vector>
#include <memory>

namespace SnobotSim
{
class EXPORT_ SnobotSimGui
{
public:
    SnobotSimGui();

    void StartThread();
    void Stop();

protected:

    void RenderLoop();

    bool mRunning;

    
    std::vector<std::shared_ptr<IWidgetDisplay>> mWidgets;
    JoystickManager mJoystickManager;
    DriverStationWidget mDriverStationWidget;
};

} // namespace SnobotSim
