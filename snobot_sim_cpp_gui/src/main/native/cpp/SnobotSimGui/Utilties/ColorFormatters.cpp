
#include "SnobotSimGui/Utilities/ColorFormatters.h"

#include <algorithm>

ImColor GetClampedColor(double aValue, double aMin, double aMax)
{
    if (isnan(aValue))
    {
        aValue = 0;
    }

    aValue = std::clamp(aValue, aMin, aMax);

    double percent = ((aValue - aMin) / (aMax - aMin));
    double hue = percent * .33;

    return ImColor::HSV(hue, 1, 1);
}
