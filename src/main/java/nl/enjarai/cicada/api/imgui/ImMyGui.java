package nl.enjarai.cicada.api.imgui;

import imgui.ImFontAtlas;
import imgui.ImFontConfig;
import imgui.ImFontGlyphRangesBuilder;
import imgui.ImGui;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import nl.enjarai.cicada.Cicada;
import org.jetbrains.annotations.ApiStatus;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
            ImFontGlyphRangesBuilder glyphRangesBuilder = new ImFontGlyphRangesBuilder();

            glyphRangesBuilder.addRanges(fontAtlas.getGlyphRangesDefault());
            glyphRangesBuilder.addChar('…');

            ImGuiEvents.SETUP_FONT_RANGES.invoker().onSetup(glyphRangesBuilder);

//            fontConfig.setGlyphRanges();
//            fontConfig.setEllipsisChar('…');
//            fontConfig.setMergeMode(true); // When enabled, all fonts added with this config would be merged with the previously added font
//            fontConfig.setPixelSnapH(true);

            var glyphRanges = glyphRangesBuilder.buildRanges();

            fontAtlas.setLocked(false);

//            fontAtlas.addFontDefault();
            fontAtlas.addFontFromMemoryTTF(
                    loadFromResources("/font/RobotoMono-VariableFont_wght.ttf"), 16,
                    fontConfig, glyphRanges
            );

            ImGuiEvents.SETUP_FONTS.invoker().onSetup(fontAtlas, glyphRanges);

            fontAtlas.build();

            fontConfig.destroy();

            imguiGlfw.init(window, true);
            imguiGl3.init();

            initialized = true;
        } catch (Throwable e) {
            Cicada.LOGGER.error("Failed to load ImGui. Are we missing platform binaries? Some dependent mods may not work as expected.", e);
            errored = true;
        }
    }

    public static byte[] loadFromResources(String name) {
        try {
            //noinspection DataFlowIssue
            return Files.readAllBytes(Paths.get(ImMyGui.class.getResource(name).toURI()));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @ApiStatus.Internal
    public static void destroy() {
        if (!initialized) {
            return;
        }

        imguiGl3.shutdown();
        imguiGlfw.shutdown();

        initialized = false;
    }

    public static void render(ImGuiThing thing) {
        if (!initialized || errored || !ImGui.getIO().getFonts().isBuilt()) {
            return;
        }

        try {
            imguiGl3.newFrame();
            imguiGlfw.newFrame();
            ImGui.newFrame();

            thing.render();

            ImGui.render();
            imguiGl3.renderDrawData(ImGui.getDrawData());
        } catch (Throwable e) {
            Cicada.LOGGER.error("Failed to render ImGui. Will stop trying for now. Some dependent mods may not work as expected.", e);
            errored = true;
        }
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
