package com.ssakura49.sakuratinker_tools.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.ssakura49.sakuratinker_tools.content.entity.BulletEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class BulletRenderer extends EntityRenderer<BulletEntity> {
   public BulletRenderer(EntityRendererProvider.Context context) {
      super(context);
   }

   public void render(BulletEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
   }

   public @NotNull ResourceLocation getTextureLocation(@NotNull BulletEntity entity) {
      return ResourceLocation.withDefaultNamespace("textures/item/iron_ingot.png");
   }
}
