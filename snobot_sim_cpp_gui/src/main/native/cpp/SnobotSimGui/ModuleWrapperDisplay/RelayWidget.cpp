

#include "SnobotSimGui/ModuleWrapperDisplay/RelayWidget.h"
#include "SnobotSim/SensorActuatorRegistry.h"

#include <imgui.h>

void RelayWidget::updateDisplay()
{
    ImGui::Begin("Relays");

    ImGui::PushItemWidth(ImGui::GetFontSize() * 8);
    for(const auto& pair : SensorActuatorRegistry::Get().GetIRelayWrapperMap())
    {
        auto wrapper = pair.second;
        bool open = ImGui::CollapsingHeader(
            wrapper->GetName().c_str(), true ? ImGuiTreeNodeFlags_DefaultOpen : 0);
        // std::cout << "  SC: " << pair.first << ", " << pair.second << std::endl;
    }
    ImGui::PopItemWidth();

    ImGui::End();
}