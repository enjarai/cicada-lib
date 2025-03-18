package nl.enjarai.cicada.api.imgui;

import imgui.ImGui;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.jetbrains.annotations.ApiStatus;

public class ImMyGui {
    private static boolean initialized = false;
    private static ImGuiImplGl3 imguiGl3;
    private static ImGuiImplGlfw imguiGlfw;

    @ApiStatus.Internal
    public static void init(long window) {
        if (initialized) {
            return;
        }

        imguiGl3 = new ImGuiImplGl3();
        imguiGlfw = new ImGuiImplGlfw();

        ImGui.createContext();
        var io = ImGui.getIO();
        io.setIniFilename(null);
        io.addConfigFlags(ImGuiConfigFlags.NavEnableKeyboard);

        imguiGlfw.init(window, true);
        imguiGl3.init();

        initialized = true;
    }

    @ApiStatus.Internal
    public static void destroy() {
        if (!initialized) {
            return;
        }

        imguiGl3.dispose();
        imguiGlfw.dispose();

        initialized = false;
    }

    public static void render(ImGuiThing thing) {
        imguiGlfw.newFrame();
        ImGui.newFrame();

        thing.render();

        ImGui.render();
        imguiGl3.renderDrawData(ImGui.getDrawData());
    }
}
