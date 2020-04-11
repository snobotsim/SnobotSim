

#include "SnobotSimGui/ModuleWrapperDisplay/AnalogOutWidget.h"

#include <imgui.h>

#include "SnobotSim/SensorActuatorRegistry.h"

void AnalogOutWidget::updateDisplay()
{
    ImGui::Begin("Analog Out");

    ImGui::PushItemWidth(ImGui::GetFontSize() * 8);
    for (const auto& pair : SensorActuatorRegistry::Get().GetIAnalogOutWrapperMap())
    {
        auto wrapper = pair.second;
        bool open = ImGui::CollapsingHeader(
                wrapper->GetName().c_str(), true ? ImGuiTreeNodeFlags_DefaultOpen : 0);
        // std::cout << "  SC: " << pair.first << ", " << pair.second << std::endl;
    }
    ImGui::PopItemWidth();

    ImGui::End();
}
