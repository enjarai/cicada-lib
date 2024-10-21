package nl.enjarai.cicada.api.cursed;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.Difficulty;
import nl.enjarai.cicada.Cicada;

public class DummyClientWorld extends ClientWorld {

    private static DummyClientWorld instance;

    public static DummyClientWorld getInstance() {
        if (instance == null) instance = new DummyClientWorld();
        return instance;
    }

    private DummyClientWorld() {
        //? if >1.21.1 {
        super(
                DummyClientPlayNetworkHandler.getInstance(),
                new Properties(Difficulty.EASY, false, true),
                RegistryKey.of(RegistryKeys.WORLD, Cicada.id("dummy")),
                DummyClientPlayNetworkHandler.CURSED_DIMENSION_TYPE_REGISTRY.getOrThrow(
                        RegistryKey.of(RegistryKeys.DIMENSION_TYPE, Cicada.id("dummy"))),
                0,
                0,
                MinecraftClient.getInstance().worldRenderer,
                false,
                0L,
                60
        );
        //?} else {
        /*super(
                DummyClientPlayNetworkHandler.getInstance(),
                new Properties(Difficulty.EASY, false, true),
                RegistryKey.of(RegistryKeys.WORLD, Cicada.id("dummy")),
                DummyClientPlayNetworkHandler.CURSED_DIMENSION_TYPE_REGISTRY.entryOf(
                        RegistryKey.of(RegistryKeys.DIMENSION_TYPE, Cicada.id("dummy"))),
                0,
                0,
                () -> MinecraftClient.getInstance().getProfiler(),
                MinecraftClient.getInstance().worldRenderer,
                false,
                0L
        );
        *///?}
    }

    @Override
    public DynamicRegistryManager getRegistryManager() {
        return super.getRegistryManager();
    }
}
