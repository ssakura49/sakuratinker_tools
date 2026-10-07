package com.ssakura49.sakuratinker_tools.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.ssakura49.sakuratinker_tools.content.entity.ShurikenEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;

public class ShurikenRenderer extends EntityRenderer<ShurikenEntity> {
   private final ItemRenderer itemRenderer;

   public ShurikenRenderer(EntityRendererProvider.Context context) {
      super(context);
      this.itemRenderer = context.getItemRenderer();
   }

   @Override
   public void render(ShurikenEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
      if (entity.tickCount >= 2 || !(this.entityRenderDispatcher.distanceToSqr(entity) < 12.25F)) {
         poseStack.pushPose();
//         float yaw = Mth.lerp(partialTicks, entity.yRotO, entity.getFlightYaw());
//         float pitch = Mth.lerp(partialTicks, entity.xRotO, entity.getFlightPitch());
         float yaw = Mth.lerp(partialTicks, entity.yRotO, entity.getYRot());
         float pitch = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
         poseStack.mulPose(Axis.YP.rotationDegrees(yaw));
         poseStack.mulPose(Axis.XP.rotationDegrees(-pitch));

         poseStack.mulPose(Axis.XP.rotationDegrees(90));
         if (!entity.isInGround()){
            poseStack.mulPose(Axis.ZP.rotationDegrees((entity.tickCount + partialTicks) * 60.0F % 360.0F));
         }
         poseStack.translate(-0.03125, -0.09375, 0);
         this.itemRenderer.renderStatic(
                 entity.getItem(),
                 ItemDisplayContext.GROUND,
                 packedLight,
                 OverlayTexture.NO_OVERLAY,
                 poseStack,
                 buffer,
                 entity.level(),
                 entity.getId());
         poseStack.popPose();
      }
   }

   public ResourceLocation getTextureLocation(ShurikenEntity entity) {
      return InventoryMenu.BLOCK_ATLAS;
   }
}
