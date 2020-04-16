
// void NameInfo::PopupEditName(int index)
// {
//     char id[64];
//     std::snprintf(id, sizeof(id), "Name%d", index);
//     if (ImGui::BeginPopupContextItem(id))
//     {
//         ImGui::Text("Edit name:");
//         if (ImGui::InputText("##edit", m_name, sizeof(m_name),
//                     ImGuiInputTextFlags_EnterReturnsTrue))
//             ImGui::CloseCurrentPopup();
//         if (ImGui::Button("Close"))
//             ImGui::CloseCurrentPopup();
//         ImGui::EndPopup();
//     }
// }