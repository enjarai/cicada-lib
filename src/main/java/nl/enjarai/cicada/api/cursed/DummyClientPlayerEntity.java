package nl.enjarai.cicada.api.cursed;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
/*? if >=1.21 {*/
import net.minecraft.entity.player.PlayerModelPart;
/*?} else {*/
/*import net.minecraft.client.render.entity.PlayerModelPart;
*//*?}*/
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Function;

public class DummyClientPlayerEntity extends ClientPlayerEntity {
    private static DummyClientPlayerEntity instance;
    /*? if >=1.20.2 {*/
    private net.minecraft.client.util.SkinTextures skinTextures = null;
    /*?} else {*/
    /*private Identifier skinIdentifier = null;
    private String model = null;
    *//*?}*/
    private PlayerEntity player = null;
    private Text name = null;
    public Function<EquipmentSlot, ItemStack> equippedStackSupplier = slot -> ItemStack.EMPTY;

    public static DummyClientPlayerEntity getInstance() {
        if (instance == null) instance = new DummyClientPlayerEntity();
        return instance;
    }

    private DummyClientPlayerEntity() {
        super(MinecraftClient.getInstance(), DummyClientWorld.getInstance(), DummyClientPlayNetworkHandler.getInstance(), null, null, false, false);
        setUuid(UUID.randomUUID());
        /*? if >=1.21.4 {*/
        MinecraftClient.getInstance().getSkinProvider().fetchSkinTextures(getGameProfile()).thenAccept((textures) -> {
            skinTextures = textures.orElse(DefaultSkinHelper.getSkinTextures(getGameProfile()));
        });
        /*?} else if >=1.20.2 {*//*
        MinecraftClient.getInstance().getSkinProvider().fetchSkinTextures(getGameProfile()).thenAccept((textures) -> {
            skinTextures = textures;
        });
        *//*?} else {*/
        /*MinecraftClient.getInstance().getSkinProvider().loadSkin(getGameProfile(), (type, identifier, texture) -> {
            if (type == MinecraftProfileTexture.Type.SKIN) {
                skinIdentifier = identifier;
                model = texture.getMetadata("model");
                if (model == null) {
                    model = "default";
                }
            }
        }, true);
        *//*?}*/
    }

    public DummyClientPlayerEntity(Text name) {
        this();
        this.name = name;
    }

    /*? if >=1.20.2 {*/
    public DummyClientPlayerEntity(@Nullable PlayerEntity player, UUID uuid, net.minecraft.client.util.SkinTextures skinTextures) {
        this(player, uuid, skinTextures, DummyClientWorld.getInstance(), DummyClientPlayNetworkHandler.getInstance());
    }

    public DummyClientPlayerEntity(@Nullable PlayerEntity player, UUID uuid, net.minecraft.client.util.SkinTextures skinTextures, ClientWorld world, ClientPlayNetworkHandler networkHandler) {
        super(MinecraftClient.getInstance(), world, networkHandler, null, null, false, false);
        this.player = player;
        setUuid(uuid);
        this.skinTextures = skinTextures;
    }
    /*?} else {*/
    /*public DummyClientPlayerEntity(@Nullable PlayerEntity player, UUID uuid, Identifier skinIdentifier, @Nullable String model) {
        this(player, uuid, skinIdentifier, model, DummyClientWorld.getInstance(), DummyClientPlayNetworkHandler.getInstance());
    }

    public DummyClientPlayerEntity(@Nullable PlayerEntity player, UUID uuid, Identifier skinIdentifier, @Nullable String model, ClientWorld world, ClientPlayNetworkHandler networkHandler) {
        super(MinecraftClient.getInstance(), world, networkHandler, null, null,false, false);
        this.player = player;
        setUuid(uuid);
        this.skinIdentifier = skinIdentifier;
        this.model = model;
    }
    *//*?}*/

    @Override
    public boolean isPartVisible(PlayerModelPart modelPart) {
        return true;
    }

    /*? if >=1.20.2 {*/
    @Override
    public net.minecraft.client.util.SkinTextures getSkinTextures() {
        return skinTextures == null ? DefaultSkinHelper.getSkinTextures(this.getUuid()) : skinTextures;
    }
    /*?} else {*/
    /*@Override
    public boolean hasSkinTexture() {
        return true;
    }

    @Override
    public Identifier getSkinTexture() {
        return skinIdentifier == null ? DefaultSkinHelper.getTexture(getUuid()) : skinIdentifier;
    }

    @Override
    public String getModel() {
        return model == null ? DefaultSkinHelper.getModel(getUuid()) : model;
    }
    *//*?}*/

    @Nullable
    @Override
    protected PlayerListEntry getPlayerListEntry() {
        return null;
    }

    @Override
    public boolean isSpectator() {
        return false;
    }

    @Override
    public boolean isCreative() {
        return true;
    }

    @Override
    public ItemStack getEquippedStack(EquipmentSlot slot) {
        if (player != null) {
            return player.getEquippedStack(slot);
        }
        return equippedStackSupplier.apply(slot);
    }

    @Override
    public Text getName() {
        if (name == null) {
            return super.getName();
        } else {
            return name;
        }
    }
}
