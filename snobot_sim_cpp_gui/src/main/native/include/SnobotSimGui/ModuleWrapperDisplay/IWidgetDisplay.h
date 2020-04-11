

#pragma once

#include <functional>

class IWidgetDisplay
{
public:
    using SaveCallback = std::function<void()>;

    virtual void updateDisplay() = 0;
};
