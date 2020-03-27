

#include "SnobotSimGui/ModuleWrapperDisplay/SolenoidWidget.h"
#include "SnobotSim/SensorActuatorRegistry.h"
#include "SnobotSimGui/Utilities/IndicatorDrawer.h"

#include <imgui.h>

void SolenoidWidget::updateDisplay()
{
    ImGui::Begin("Solenoids");

    ImGui::PushItemWidth(ImGui::GetFontSize() * 8);
    for(const auto& pair : SensorActuatorRegistry::Get().GetISolenoidWrapperMap())
    {
        auto wrapper = pair.second;
        bool open = ImGui::CollapsingHeader(
            wrapper->GetName().c_str(), true ? ImGuiTreeNodeFlags_DefaultOpen : 0);

        ImU32 color = wrapper->GetState() ? IM_COL32(0, 255,0, 255) : IM_COL32(0, 0, 0, 255);
        AddIndicator(color);
    }
    ImGui::PopItemWidth();

    ImGui::End();
}