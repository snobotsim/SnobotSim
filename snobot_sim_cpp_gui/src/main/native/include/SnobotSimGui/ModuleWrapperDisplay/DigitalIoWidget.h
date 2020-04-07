

#include "SnobotSimGui/ModuleWrapperDisplay/IWidgetDisplay.h"

class DigitalIoWidget : public IWidgetDisplay
{
public:
    DigitalIoWidget(SaveCallback callback) : mSaveCallback(callback) {}

    void updateDisplay() override;

protected:
    SaveCallback mSaveCallback;
};
