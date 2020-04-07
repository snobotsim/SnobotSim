

#include "SnobotSimGui/ModuleWrapperDisplay/IWidgetDisplay.h"

class RelayWidget : public IWidgetDisplay
{
public:
    RelayWidget(SaveCallback callback) : mSaveCallback(callback) {}

    void updateDisplay() override;

protected:
    SaveCallback mSaveCallback;
};
