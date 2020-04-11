
#pragma once

#include "SnobotSimGui/ModuleWrapperDisplay/IWidgetDisplay.h"

class GyroWidget : public IWidgetDisplay
{
public:
    GyroWidget(SaveCallback callback) :
            mSaveCallback(callback)
    {
    }

    void updateDisplay() override;

protected:
    SaveCallback mSaveCallback;
};
