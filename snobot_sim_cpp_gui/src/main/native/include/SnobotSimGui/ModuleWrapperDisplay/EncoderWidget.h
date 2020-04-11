
#pragma once

#include "SnobotSimGui/ModuleWrapperDisplay/IWidgetDisplay.h"

class EncoderWidget : public IWidgetDisplay
{
public:
    EncoderWidget(SaveCallback callback) :
            mSaveCallback(callback)
    {
    }

    void updateDisplay() override;

protected:
    SaveCallback mSaveCallback;
};
