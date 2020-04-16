

#include "SnobotSimGui/ModuleWrapperDisplay/SolenoidWidget.h"
#include "SnobotSimGui/Utilities/ColorFormatters.h"
#include "SnobotSimGui/EditNamePopup.h"
#include "SnobotSimGui/EditNamePopup.h"

#include <imgui.h>

#include "SnobotSim/SensorActuatorRegistry.h"
#include "SnobotSimGui/Utilities/IndicatorDrawer.h"

void SolenoidWidget::updateDisplay()
{
    ImGui::Begin("Solenoids");

    ImGui::PushItemWidth(ImGui::GetFontSize() * 8);
    for (const auto& pair : SensorActuatorRegistry::Get().GetISolenoidWrapperMap())
    {
        auto wrapper = pair.second;
        bool open = ImGui::CollapsingHeader(
                wrapper->GetName().c_str(), true ? ImGuiTreeNodeFlags_DefaultOpen : 0);
        if(PopupEditName(pair.first, wrapper))
        {
            mSaveCallback();
        }

        ImU32 color = wrapper->GetState() ? GREEN_COLOR : BLACK_COLOR;
        AddIndicator(color);
    }
    ImGui::PopItemWidth();

    ImGui::End();
}
