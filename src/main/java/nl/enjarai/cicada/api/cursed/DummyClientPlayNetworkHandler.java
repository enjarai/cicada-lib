package nl.enjarai.cicada.api.cursed;

import com.mojang.serialization.Lifecycle;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.damage.DamageScaling;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkSide;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;
import nl.enjarai.cicada.Cicada;

import java.time.Duration;


import java.util.Optional;
import java.util.OptionalLong;
import java.util.stream.Stream;

/*? if >=1.20.5 {*/

import net.minecraft.client.gui.hud.ChatHud;

import java.util.List;
import java.util.Map;

/*?}*/

/*? if <=1.20.1 {*/
/*import java.time.temporal.ChronoUnit;

 *//*?} else {*/

import net.minecraft.client.network.ClientConnectionState;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.resource.featuretoggle.FeatureSet;

/*?}*/

public class DummyClientPlayNetworkHandler extends ClientPlayNetworkHandler {
    public static final Registry<DimensionType> CURSED_DIMENSION_TYPE_REGISTRY = new SimpleRegistry<>(RegistryKeys.DIMENSION_TYPE, Lifecycle.stable());

    static {
        Registry.register(CURSED_DIMENSION_TYPE_REGISTRY, Cicada.id("dummy"), new DimensionType(
                OptionalLong.of(6000L),
                true,
                false,
                false,
                true,
                1.0,
                true,
                false,
                -64,
                384,
                384,
                BlockTags.INFINIBURN_OVERWORLD,
                DimensionTypes.OVERWORLD_ID,
                0.0f,
                /*? if >=1.21.6 {*/
                Optional.of(384),
                /*?}*/
                new DimensionType.MonsterSettings(
                        false,
                        true,
                        UniformIntProvider.create(0, 7),
                        0
                )
        ));
    }

    private static DummyClientPlayNetworkHandler instance;

    public static DummyClientPlayNetworkHandler getInstance() {
        if (instance == null) instance = new DummyClientPlayNetworkHandler();
        return instance;
    }

    private static final Registry<Biome> cursedBiomeRegistry = new CursedRegistry<>(RegistryKeys.BIOME, Cicada.id("fake_biomes"), null);

    private static final Registry<BannerPattern> cursedBannerRegistry = new SimpleDefaultedRegistry<>("dummy", RegistryKeys.BANNER_PATTERN, Lifecycle.stable(), true);

    private static final DynamicRegistryManager.Immutable cursedRegistryManager = new DynamicRegistryManager.Immutable() {
        private final CursedRegistry<DamageType> damageTypes = new CursedRegistry<>(RegistryKeys.DAMAGE_TYPE, Cicada.id("fake_damage"),
                new DamageType("", DamageScaling.NEVER, 0));
        private final CursedRegistry<Item> items = new CursedRegistry<>(RegistryKeys.ITEM, Cicada.id("fake_items"),
                Items.AIR);

        @SuppressWarnings({"unchecked", "rawtypes"})
        @Override
        public Optional<Registry> getOptional(RegistryKey key) {
            var x = Registries.REGISTRIES.get(key);
            if (x != null) {
                return Optional.of(x);
            } else if (RegistryKeys.DAMAGE_TYPE.equals(key)) {
                return Optional.of(damageTypes);
            } else if (RegistryKeys.BIOME.equals(key)) {
                return Optional.of(cursedBiomeRegistry);
            } else if (RegistryKeys.DIMENSION_TYPE.equals(key)) {
                return Optional.of(CURSED_DIMENSION_TYPE_REGISTRY);
            } else if (RegistryKeys.BANNER_PATTERN.equals(key)) {
                // This fixes lithium compat post-1.20.5
                return Optional.of(cursedBannerRegistry);
            } else if (RegistryKeys.ITEM.equals(key)) {
                return Optional.of(items);
            }

            return Optional.empty();
        }

        //? if >1.21.1 {
        @SuppressWarnings({"rawtypes", "unchecked", "UnnecessaryLocalVariable"})
        @Override
        public <E> Registry<E> getOrThrow(RegistryKey<? extends Registry<? extends E>> key) {
            return getOptional(key).orElseGet(() -> {
                RegistryKey sillyKey = key;
                return new CursedRegistry<>(sillyKey, Cicada.id("fake"), null);
            });
        }
        //?}

        @Override
        public Stream<Entry<?>> streamAllRegistries() {
            return Stream.empty();
        }
    };

    private DummyClientPlayNetworkHandler() {
        /*? if >1.21.1 {*/
        super(
                MinecraftClient.getInstance(),
                new ClientConnection(NetworkSide.CLIENTBOUND),
                new ClientConnectionState(
                        MinecraftClient.getInstance().getGameProfile(),
                        MinecraftClient.getInstance().getTelemetryManager().createWorldSession(true, Duration.ZERO, null),
                        cursedRegistryManager,
                        FeatureSet.empty(),
                        "",
                        new ServerInfo("", "", ServerInfo.ServerType.OTHER),
                        null,
                        Map.of(),
                        new ChatHud.ChatState(List.of(), List.of(), List.of()),
                        Map.of(),
                        net.minecraft.server.ServerLinks.EMPTY
                )
        );
        /*?} elif >=1.21 {*/
        /*super(
                MinecraftClient.getInstance(),
                new ClientConnection(NetworkSide.CLIENTBOUND),
                new ClientConnectionState(
                        MinecraftClient.getInstance().getGameProfile(),
                        MinecraftClient.getInstance().getTelemetryManager().createWorldSession(true, Duration.ZERO, null),
                        cursedRegistryManager.toImmutable(),
                        FeatureSet.empty(),
                        "",
                        new ServerInfo("", "", ServerInfo.ServerType.OTHER),
                        null,
                        Map.of(),
                        new ChatHud.ChatState(List.of(), List.of(), List.of()),
                        false,
                        Map.of(),
                        net.minecraft.server.ServerLinks.EMPTY
                )
        );
        *//*?} elif >=1.20.5 {*/
        /*super(
                MinecraftClient.getInstance(),
                new ClientConnection(NetworkSide.CLIENTBOUND),
                new ClientConnectionState(
                        MinecraftClient.getInstance().getGameProfile(),
                        MinecraftClient.getInstance().getTelemetryManager().createWorldSession(true, Duration.ZERO, null),
                        cursedRegistryManager.toImmutable(),
                        FeatureSet.empty(),
                        "",
                        new ServerInfo("", "", ServerInfo.ServerType.OTHER),
                        null,
                        Map.of(),
                        new ChatHud.ChatState(List.of(), List.of(), List.of()),
                        false
                )
        );
        *//*?} elif >=1.20.2 {*/
        /*super(
                MinecraftClient.getInstance(),
                new ClientConnection(NetworkSide.CLIENTBOUND),
                new ClientConnectionState(
                        MinecraftClient.getInstance().getGameProfile(),
                        MinecraftClient.getInstance().getTelemetryManager().createWorldSession(true, Duration.ZERO, null),
                        cursedRegistryManager.toImmutable(),
                        FeatureSet.empty(),
                        "",
                        new ServerInfo("", "", ServerInfo.ServerType.OTHER),
                        null
                )
        );
        *//*?} elif =1.20.1 {*/
        /*super(
                MinecraftClient.getInstance(),
                null,
                new ClientConnection(NetworkSide.CLIENTBOUND),
                MinecraftClient.getInstance().getCurrentServerEntry(),
                MinecraftClient.getInstance().getSession().getProfile(),
                MinecraftClient.getInstance().getTelemetryManager().createWorldSession(true, Duration.of(0, ChronoUnit.SECONDS), null)
        );
        *//*?} else {*/
        /*super(
                MinecraftClient.getInstance(),
                null,
                new ClientConnection(NetworkSide.CLIENTBOUND),
                MinecraftClient.getInstance().getCurrentServerEntry(),
                MinecraftClient.getInstance().getSession().getProfile(),
                MinecraftClient.getInstance().getTelemetryManager().createWorldSession(true, Duration.of(0, ChronoUnit.SECONDS))
        );
        *//*?}*/
    }

    @Override
    public DynamicRegistryManager.Immutable getRegistryManager() {
        return cursedRegistryManager;
    }
}
