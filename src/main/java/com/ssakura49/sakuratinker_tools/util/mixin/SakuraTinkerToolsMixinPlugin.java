package com.ssakura49.sakuratinker_tools.util.mixin;

import com.ssakura49.sakura_library.coremod.forge.LaunchPluginServiceBuilder;
import com.ssakura49.sakura_library.util.mixin.ApplyCheckMixinConfigPlugin;
import com.ssakura49.sakuratinker_tools.util.asm.FantasyEndingInscriptionCompatProcessor;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;


public class SakuraTinkerToolsMixinPlugin extends ApplyCheckMixinConfigPlugin {
    static {
        LaunchPluginServiceBuilder.builder().name("SakuraTinkerToolsCore-Main")
                .processor(FantasyEndingInscriptionCompatProcessor.INSTANCE)
                .build();
    }

    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
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