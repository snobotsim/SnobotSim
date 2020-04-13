
#pragma once

#include <imgui.h>

ImColor GetClampedColor(double aValue, double aMin, double aMax);


constexpr ImU32 RED_COLOR = IM_COL32(255, 0, 0, 255);
constexpr ImU32 GREEN_COLOR = IM_COL32(0, 255, 0, 255);
constexpr ImU32 BLACK_COLOR = IM_COL32(0, 0, 0, 255);

constexpr ImU32 DISABLED_TEXT_COLOR = IM_COL32(96, 96, 96, 255);