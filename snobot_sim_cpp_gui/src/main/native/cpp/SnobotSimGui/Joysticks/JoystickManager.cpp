
#include "SnobotSimGui/Joysticks/JoystickManager.h"

#include <imgui.h>
#include <imgui_internal.h>
#include <wpi/Format.h>
#include <wpi/SmallString.h>
#include <wpi/StringRef.h>
#include <wpi/raw_ostream.h>

#include <GLFW/glfw3.h>

JoystickManager::JoystickManager() :
        mSystemJoysticks(GLFW_JOYSTICK_LAST + 1),
        mWpiJoysticks(HAL_kMaxJoysticks)
{
}

void JoystickManager::RenderSystemJoysticks()
{
    ImGui::Text("(Drag and drop to Joysticks)");
    int numShowJoysticks = (std::min)(6, static_cast<int>(mSystemJoysticks.size()));
    for (int i = 0; i < numShowJoysticks; ++i)
    {
        auto& joy = mSystemJoysticks[i];
        wpi::SmallString<128> label;
        wpi::raw_svector_ostream os(label);
        os << wpi::format("%d: %s", i, joy.name);

        // highlight if any buttons pressed
        if (joy.anyButtonPressed)
        {
            ImGui::PushStyleColor(ImGuiCol_Text, IM_COL32(255, 255, 0, 255));
        }
        ImGui::Selectable(label.c_str(), false,
                joy.present ? ImGuiSelectableFlags_None
                            : ImGuiSelectableFlags_Disabled);
        if (joy.anyButtonPressed)
        {
            ImGui::PopStyleColor();
        }

        // drag and drop sources are the low level joysticks
        if (ImGui::BeginDragDropSource())
        {
            SystemJoystick* joyPtr = &joy;
            ImGui::SetDragDropPayload("Joystick", &joyPtr, sizeof(joyPtr));
            ImGui::Text("%d: %s", i, joy.name);
            ImGui::EndDragDropSource();
        }
    }
}
void JoystickManager::RenderWpiJoysticks()
{
    // imgui doesn't size columns properly with autoresize, so force it
    ImGui::Dummy(ImVec2(ImGui::GetFontSize() * 10 * HAL_kMaxJoysticks, 0));

    ImGui::Columns(HAL_kMaxJoysticks, "Joysticks", false);
    for (int i = 0; i < HAL_kMaxJoysticks; ++i)
    {
        auto& joy = mWpiJoysticks[i];
        char label[128];
        // joy.name.GetName(label, sizeof(label), "Joystick", i);
        if (joy.sys)
        {
            ImGui::Selectable(label, false);
            if (ImGui::BeginDragDropSource())
            {
                ImGui::SetDragDropPayload("Joystick", &joy.sys, sizeof(joy.sys));
                ImGui::Text("%d: %s", -1, joy.sys->name);
                ImGui::EndDragDropSource();
            }
        }
        else
        {
            ImGui::Selectable(label, false, ImGuiSelectableFlags_Disabled);
        }
        if (ImGui::BeginDragDropTarget())
        {
            if (const ImGuiPayload* payload = ImGui::AcceptDragDropPayload("Joystick"))
            {
                IM_ASSERT(payload->DataSize == sizeof(SystemJoystick*));
                SystemJoystick* payload_sys = *static_cast<SystemJoystick* const*>(payload->Data);
                // clear it from the other joysticks
                for (auto&& joy2 : mWpiJoysticks)
                {
                    if (joy2.sys == payload_sys)
                        joy2.sys = nullptr;
                }
                joy.sys = payload_sys;
                joy.guid.clear();
                joy.useGamepad = false;
            }
            ImGui::EndDragDropTarget();
        }
        // joy.name.PopupEditName(i);
        ImGui::NextColumn();
    }
    ImGui::Separator();

    for (int i = 0; i < HAL_kMaxJoysticks; ++i)
    {
        auto& joy = mWpiJoysticks[i];

        if (joy.sys && joy.sys->present)
        {
            // update GUI display
            ImGui::PushID(i);
            ImGui::Text("%d: %s", -1, joy.sys->name);

            if (joy.sys->isGamepad)
            {
                ImGui::Checkbox("Map gamepad", &joy.useGamepad);
            }

            for (int j = 0; j < joy.axes.count; ++j)
            {
                ImGui::Text("Axis[%d]: %.3f", j, joy.axes.axes[j]);
            }

            for (int j = 0; j < joy.povs.count; ++j)
            {
                ImGui::Text("POVs[%d]: %d", j, joy.povs.povs[j]);
            }

            // show buttons as multiple lines of LED indicators, 8 per line
            static const ImU32 color = IM_COL32(255, 255, 102, 255);
            wpi::SmallVector<int, 64> buttons;
            buttons.resize(joy.buttons.count);
            for (int j = 0; j < joy.buttons.count; ++j)
            {
                buttons[j] = joy.IsButtonPressed(j) ? 1 : -1;
            }
            //   DrawLEDs(buttons.data(), buttons.size(), 8, &color);
            ImGui::PopID();
        }
        else
        {
            ImGui::Text("Unassigned");
        }
        ImGui::NextColumn();
    }
    ImGui::Columns(1);
}

void JoystickManager::UpdateJoysticks()
{
    // update system joysticks
    for (int i = 0; i <= GLFW_JOYSTICK_LAST; ++i)
    {
        mSystemJoysticks[i].Update(i);
        // if (mSystemJoysticks[i].present)
        // {
        //     gNumSystemJoysticks = i + 1;
        // }
    }
    for (int i = 0; i < HAL_kMaxJoysticks; ++i)
    {
        // gRobotJoysticks[i].SetHAL(i);
    }
}

void JoystickManager::Update()
{
    UpdateJoysticks();
    RenderSystemJoysticks();
    RenderWpiJoysticks();
}
