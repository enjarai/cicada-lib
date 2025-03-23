package nl.enjarai.cicadatest;

import imgui.ImGui;
import imgui.type.ImString;
import nl.enjarai.cicada.api.imgui.ImGuiThing;

public class ImGuiTest implements ImGuiThing {
    boolean text;
    ImString testString = new ImString();

    @Override
    public void render() {
        ImGui.begin(" ");

        ImGui.text("This is a test");

        if (ImGui.button("Click Me")) {
            text = !text;
        }

        if (text) {
            ImGui.text("Woah magic text!");
        }

        if (ImGui.checkbox("Box!", text)) {
            text = !text;
//            if (text) {
//                ImGui.openPopup("test");
//            } else {
//                ImGui.closeCurrentPopup();
//            }
        }

//        ImGui.beginPopup("test");

        ImGui.text("Hai?");
        ImGui.button("test");

//        ImGui.endPopup();

        ImGui.end();

        ImGui.begin(" ");

        ImGui.text("This is a test");
        ImGui.text(testString.get());
        ImGui.inputText("testtext", testString);

        ImGui.end();
    }
}
