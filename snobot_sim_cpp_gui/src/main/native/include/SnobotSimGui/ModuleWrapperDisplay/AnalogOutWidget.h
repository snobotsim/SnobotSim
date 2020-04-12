
#pragma once

#include "SnobotSimGui/ModuleWrapperDisplay/IWidgetDisplay.h"

class AnalogOutWidget : public IWidgetDisplay
{
public:
    explicit AnalogOutWidget(SaveCallback callback) :
            mSaveCallback(callback)
    {
    }

    void updateDisplay() override;

protected:
    SaveCallback mSaveCallback;
};
