

#include "SnobotSimGui/ModuleWrapperDisplay/IWidgetDisplay.h"

class AnalogInWidget : public IWidgetDisplay
{
public:
    AnalogInWidget(SaveCallback callback) : mSaveCallback(callback) {}

    void updateDisplay() override;

protected:
    SaveCallback mSaveCallback;
};
