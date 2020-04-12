
#include "SnobotSimGui/Utilities/IndicatorDrawer.h"

void AddIndicator(ImColor color)
{
    ImGui::SameLine();
    ImDrawList* drawList = ImGui::GetWindowDrawList();
    const ImVec2 p = ImGui::GetCursorScreenPos();

    float size = ImGui::GetFontSize() / 2.0;
    float startX = p.x;
    float startY = p.y + size / 2;
    drawList->AddRectFilled(ImVec2(startX, startY), ImVec2(startX + size, startY + size), color);
    ImGui::Dummy(ImVec2(size, size));
}
