package nl.enjarai.cicada.util;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class MixinPlugin implements IMixinConfigPlugin {
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private static final Version MC_VERSION = FabricLoader.getInstance()
            .getModContainer("minecraft").get().getMetadata().getVersion();

    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        try {
            var splitClassName = mixinClassName.split("\\.");
            var subPackage = splitClassName[splitClassName.length - 2];

            return switch (subPackage) {
                case "post_20_2" -> MC_VERSION.compareTo(Version.parse("1.20.1")) > 0;
                case "pre_20_2" -> MC_VERSION.compareTo(Version.parse("1.20.1")) <= 0;
                default -> true;
            };
        } catch (RuntimeException | VersionParsingException e) {
            return false;
        }
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
