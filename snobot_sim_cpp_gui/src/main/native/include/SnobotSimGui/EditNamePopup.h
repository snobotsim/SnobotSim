
#pragma once

#include <memory>
#include <string>
#include <imgui.h>

template<typename Type>
bool PopupEditName(int handle, std::shared_ptr<Type> module)
{
    bool changed = false;

    char name[64];
    memcpy(name, module->GetName().c_str(), module->GetName().size());

    char id[64];
    std::snprintf(id, sizeof(id), "Name%d", handle);
    if (ImGui::BeginPopupContextItem(id))
    {
        ImGui::Text("Edit name:");
        if (ImGui::InputText("##edit", name, sizeof(name),
                    ImGuiInputTextFlags_EnterReturnsTrue))
        {
            module->SetName(name);
            ImGui::CloseCurrentPopup();
            changed = true;
        }
        if (ImGui::Button("Close"))
        {
            ImGui::CloseCurrentPopup();
        }
        ImGui::EndPopup();
    }

    return changed;
}