

#include "SnobotSimGui/ModuleWrapperDisplay/IWidgetDisplay.h"
#include "SnobotSim/ModuleWrapper/Interfaces/ISpeedControllerWrapper.h"
#include "SnobotSim/Config/SimulatorConfigV1.h"

class SpeedControllerWidget : public IWidgetDisplay
{
public:
    SpeedControllerWidget(SaveCallback callback);

    void updateDisplay() override;

protected:

    void RenderMotorConfigPopup(std::shared_ptr<ISpeedControllerWrapper>& wrapper, FullMotorSimConfig& motorConfig);

    SaveCallback mSaveCallback;
};
