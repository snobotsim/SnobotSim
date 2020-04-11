
#pragma once

#include "SnobotSimGui/ModuleWrapperDisplay/IWidgetDisplay.h"

class AccelerometerWidget : public IWidgetDisplay
{
public:
    AccelerometerWidget(SaveCallback callback) :
            mSaveCallback(callback)
    {
    }

    void updateDisplay() override;

protected:
    SaveCallback mSaveCallback;
};
