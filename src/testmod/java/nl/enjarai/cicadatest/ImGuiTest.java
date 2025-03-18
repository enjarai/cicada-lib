package nl.enjarai.cicadatest;

import imgui.ImGui;
import nl.enjarai.cicada.api.imgui.ImGuiThing;

public class ImGuiTest implements ImGuiThing {
    boolean text;

    @Override
    public void render() {
        ImGui.begin("ImGui Test!");

        ImGui.text("This is a test");

        if (ImGui.button("Click Me")) {
            text = !text;
        }

        if (text) {
            ImGui.text("Woah magic text!");
        }

        if (ImGui.checkbox("Box!", text)) {
            text = !text;
        }

        ImGui.end();
    }
}
