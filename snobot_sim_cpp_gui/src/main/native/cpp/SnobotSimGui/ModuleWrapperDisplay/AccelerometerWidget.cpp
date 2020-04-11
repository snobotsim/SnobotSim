

#include "SnobotSimGui/ModuleWrapperDisplay/AccelerometerWidget.h"

#include <imgui.h>

#include "SnobotSim/SensorActuatorRegistry.h"

void AccelerometerWidget::updateDisplay()
{
    ImGui::Begin("Acclerometers");

    ImGui::PushItemWidth(ImGui::GetFontSize() * 8);
    for (const auto& pair : SensorActuatorRegistry::Get().GetIAccelerometerWrapperMap())
    {
        auto wrapper = pair.second;
        bool open = ImGui::CollapsingHeader(
                wrapper->GetName().c_str(), true ? ImGuiTreeNodeFlags_DefaultOpen : 0);
        // std::cout << "  SC: " << pair.first << ", " << pair.second << std::endl;
    }
    ImGui::PopItemWidth();

    ImGui::End();
}
