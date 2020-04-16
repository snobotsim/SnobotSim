

#include "SnobotSimGui/ModuleWrapperDisplay/GyroWidget.h"
#include "SnobotSimGui/EditNamePopup.h"

#include <imgui.h>

#include "SnobotSim/SensorActuatorRegistry.h"

void GyroWidget::updateDisplay()
{
    ImGui::Begin("Gyros");

    ImGui::PushItemWidth(ImGui::GetFontSize() * 8);
    for (const auto& pair : SensorActuatorRegistry::Get().GetIGyroWrapperMap())
    {
        auto wrapper = pair.second;
        bool open = ImGui::CollapsingHeader(
                wrapper->GetName().c_str(), true ? ImGuiTreeNodeFlags_DefaultOpen : 0);

        if(PopupEditName(pair.first, wrapper))
        {
            mSaveCallback();
        }
        if (open)
        {
            ImGui::PushID(pair.first);

            ImGui::LabelText("Angle", "%.6f", wrapper->GetAngle());

            ImGui::PopID();
        }
        // std::cout << "  SC: " << pair.first << ", " << pair.second << std::endl;
    }
    ImGui::PopItemWidth();

    ImGui::End();
}
