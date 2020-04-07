
#include "SnobotSimGui/ModuleWrapperDisplay/IWidgetDisplay.h"


class SolenoidWidget : public IWidgetDisplay
{
public:
    SolenoidWidget(SaveCallback callback) : mSaveCallback(callback) {}

    void updateDisplay() override;

protected:
    SaveCallback mSaveCallback;
};
