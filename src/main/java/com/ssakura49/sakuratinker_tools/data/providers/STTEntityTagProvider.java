package com.ssakura49.sakuratinker_tools.data.providers;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker_tools.init.STTTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class STTEntityTagProvider extends IntrinsicHolderTagsProvider<EntityType<?>> {
    public STTEntityTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, Registries.ENTITY_TYPE, lookupProvider, (entityType) -> entityType.builtInRegistryHolder().key(), SakuraTinker.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        this.tag(STTTags.Entities.NO_GRAPPLE)
                .add(
                        EntityType.ENDER_DRAGON,
                        EntityType.WITHER,
                        EntityType.WARDEN,
                        EntityType.ELDER_GUARDIAN
                )
                .addTag(TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath("forge", "bosses")));
    }
}
