

#include "SnobotSimGui/ModuleWrapperDisplay/RelayWidget.h"
#include "SnobotSimGui/Utilities/ColorFormatters.h"

#include <imgui.h>

#include "SnobotSim/SensorActuatorRegistry.h"
#include "SnobotSimGui/Utilities/IndicatorDrawer.h"

namespace
{

constexpr ImU32 REVERSE_COLOR = RED_COLOR;
constexpr ImU32 FORWARD_COLOR = GREEN_COLOR;
constexpr ImU32 OFF_COLOR = BLACK_COLOR;
} // namespace

void RelayWidget::updateDisplay()
{
    ImGui::Begin("Relays");

    ImGui::PushItemWidth(ImGui::GetFontSize() * 8);
    for (const auto& pair : SensorActuatorRegistry::Get().GetIRelayWrapperMap())
    {
        auto wrapper = pair.second;
        bool open = ImGui::CollapsingHeader(
                wrapper->GetName().c_str(), true ? ImGuiTreeNodeFlags_DefaultOpen : 0);

        if (open)
        {
            ImGui::PushID(pair.first);

            ImGui::PopID();
            ImGui::SameLine();

            ImDrawList* drawList = ImGui::GetWindowDrawList();
            const ImVec2 p = ImGui::GetCursorScreenPos();

            float size = ImGui::GetFontSize() / 2.0;
            double forwardX = p.x;
            double forwardY = p.y;
            double reverseX = p.x + size;
            double reverseY = p.y;

            ImU32 forwardsColor = OFF_COLOR;
            ImU32 reverseColor = OFF_COLOR;
            bool forwards = wrapper->GetRelayForwards();
            bool reverse = wrapper->GetRelayReverse();
            if (forwards && reverse)
            {
                forwardsColor = FORWARD_COLOR;
                reverseColor = REVERSE_COLOR;
            }
            else if (forwards)
            {
                forwardsColor = FORWARD_COLOR;
                reverseColor = FORWARD_COLOR;
            }
            else if (reverse)
            {
                forwardsColor = REVERSE_COLOR;
                reverseColor = REVERSE_COLOR;
            }

            drawList->AddRectFilled(ImVec2(forwardX, forwardY), ImVec2(forwardX + size, forwardY + size), forwardsColor);
            drawList->AddRectFilled(ImVec2(reverseX, reverseY), ImVec2(reverseX + size, reverseY + size), reverseColor);
        }
    }
    ImGui::PopItemWidth();

    ImGui::End();
}
