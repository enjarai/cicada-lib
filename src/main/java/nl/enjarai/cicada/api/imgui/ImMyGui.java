package nl.enjarai.cicada.api.imgui;

import imgui.ImFontAtlas;
import imgui.ImFontConfig;
import imgui.ImGui;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import nl.enjarai.cicada.Cicada;
import org.jetbrains.annotations.ApiStatus;

public class ImMyGui {
    private static boolean initialized = false;
    private static boolean errored = false;
    private static ImGuiImplGl3 imguiGl3;
    private static ImGuiImplGlfw imguiGlfw;

    @ApiStatus.Internal
    public static void init(long window) {
        if (initialized && !errored) {
            return;
        }

        try {
            imguiGl3 = new ImGuiImplGl3();
            imguiGlfw = new ImGuiImplGlfw();

            ImGui.createContext();
            var io = ImGui.getIO();
            io.setIniFilename(null);
            io.addConfigFlags(ImGuiConfigFlags.NavEnableKeyboard);
//            io.setGetClipboardTextFn(new ImStrSupplier() {
//                @Override
//                public String get() {
//                    return MinecraftClient.getInstance().keyboard.getClipboard();
//                }
//            });
//            io.setSetClipboardTextFn(new ImStrConsumer() {
//                @Override
//                public void accept(String s) {
//                    MinecraftClient.getInstance().keyboard.setClipboard(s);
//                }
//            });
//            io.setWantCaptureKeyboard(true);

            ImFontAtlas fontAtlas = io.getFonts();
            ImFontConfig fontConfig = new ImFontConfig(); // Natively allocated object, should be explicitly destroyed

            fontConfig.setGlyphRanges(fontAtlas.getGlyphRangesCyrillic());

            fontAtlas.addFontDefault();

            fontConfig.setMergeMode(true); // When enabled, all fonts added with this config would be merged with the previously added font
            fontConfig.setPixelSnapH(true);

            fontConfig.destroy();


            imguiGlfw.init(window, true);
            imguiGl3.init(null);

            initialized = true;
        } catch (Throwable e) {
            Cicada.LOGGER.error("Failed to load ImGui. Are we missing platform binaries? Some dependent mods may not work as expected.", e);
            errored = true;
        }
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
        if (!initialized) {
            return;
        }

//        imguiGl3.newFrame();
        imguiGlfw.newFrame();
        ImGui.newFrame();

        thing.render();

        ImGui.render();
        imguiGl3.renderDrawData(ImGui.getDrawData());
    }

    public static boolean shouldCancelGameKeyboardInputs() {
        return ImGui.isAnyItemActive() || ImGui.isAnyItemFocused();
    }

    public static boolean isInitialized() {
        return initialized;
    }

    public static boolean isErrored() {
        return errored;
    }
}
