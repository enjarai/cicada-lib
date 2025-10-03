package nl.enjarai.cicada.api.cursed;

import com.mojang.serialization.Lifecycle;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientChunkLoadProgress;
import net.minecraft.entity.damage.DamageScaling;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkSide;
import net.minecraft.registry.*;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.ServerLinks;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;
import net.minecraft.client.network.ClientConnectionState;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.client.gui.hud.ChatHud;
import nl.enjarai.cicada.Cicada;

import java.time.Duration;

import java.util.Optional;
import java.util.OptionalLong;
import java.util.stream.Stream;
import java.util.List;
import java.util.Map;


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
                Optional.of(384),
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

        @SuppressWarnings({"rawtypes", "unchecked", "UnnecessaryLocalVariable"})
        @Override
        public <E> Registry<E> getOrThrow(RegistryKey<? extends Registry<? extends E>> key) {
            return getOptional(key).orElseGet(() -> {
                RegistryKey sillyKey = key;
                return new CursedRegistry<>(sillyKey, Cicada.id("fake"), null);
            });
        }

        @Override
        public Stream<Entry<?>> streamAllRegistries() {
            return Stream.empty();
        }
    };

    private DummyClientPlayNetworkHandler() {
        super(
                MinecraftClient.getInstance(),
                new ClientConnection(NetworkSide.CLIENTBOUND),
                new ClientConnectionState(
                        new ClientChunkLoadProgress(),
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
                        ServerLinks.EMPTY,
                        Map.of(),
                        true
                )
        );
    }

    @Override
    public DynamicRegistryManager.Immutable getRegistryManager() {
        return cursedRegistryManager;
    }
}
