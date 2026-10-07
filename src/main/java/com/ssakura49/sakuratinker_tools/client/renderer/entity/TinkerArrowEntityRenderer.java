package com.ssakura49.sakuratinker_tools.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker_tools.content.entity.TinkerArrowEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import slimeknights.tconstruct.library.client.materials.MaterialRenderInfo;
import slimeknights.tconstruct.library.client.materials.MaterialRenderInfoLoader;
import slimeknights.tconstruct.library.materials.definition.MaterialVariant;

import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class TinkerArrowEntityRenderer extends ArrowRenderer<TinkerArrowEntity> {

   private static final ResourceLocation FALLBACK_TEXTURE =
           SakuraTinker.getResource("textures/entity/projectiles/arrow.png");

   private static final ResourceLocation[] PART_TEXTURES = {
           SakuraTinker.getResource("textures/entity/projectiles/arrow_head.png"),
           SakuraTinker.getResource("textures/entity/projectiles/arrow_shaft.png"),
           SakuraTinker.getResource("textures/entity/projectiles/arrow_fletching.png")
   };

   public TinkerArrowEntityRenderer(EntityRendererProvider.Context context) {
      super(context);
   }

   @Override
   public @NotNull ResourceLocation getTextureLocation(TinkerArrowEntity arrow) {
      return FALLBACK_TEXTURE;
   }

   @Override
   public void render(TinkerArrowEntity arrow, float entityYaw, float partialTicks,
                      PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
      poseStack.pushPose();
      poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, arrow.yRotO, arrow.getYRot()) - 90.0F));
      poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, arrow.xRotO, arrow.getXRot())));

      float shake = (float) arrow.shakeTime - partialTicks;
      if (shake > 0.0F) {
         poseStack.mulPose(Axis.ZP.rotationDegrees(-Mth.sin(shake * 3.0F) * shake));
      }

      poseStack.mulPose(Axis.XP.rotationDegrees(45.0F));
      poseStack.scale(0.05625F, 0.05625F, 0.05625F);
      poseStack.translate(-4.0D, 0.0D, 0.0D);

      if (arrow.getTool() != null && arrow.getTool().getMaterials().size() > 0) {
         int count = Math.min(arrow.getTool().getMaterials().size(), PART_TEXTURES.length);
         for (int i = 0; i < count; i++) {
            renderPart(i, arrow, poseStack, bufferSource, packedLight);
         }
      } else {
         renderPartless(arrow, poseStack, bufferSource, packedLight);
      }
      poseStack.popPose();
   }

   private void renderPart(int partIndex, TinkerArrowEntity arrow, PoseStack poseStack,
                           MultiBufferSource bufferSource, int packedLight) {
      TextureInformation info = getTextureInformation(arrow, partIndex);
      VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutout(info.texture));
      performRendering(consumer, poseStack, packedLight, info.color);
   }

   private void renderPartless(TinkerArrowEntity arrow, PoseStack poseStack,
                               MultiBufferSource bufferSource, int packedLight) {
      VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutout(getTextureLocation(arrow)));
      performRendering(consumer, poseStack, packedLight, 0xFFFFFFFF);
   }

   private TextureInformation getTextureInformation(TinkerArrowEntity arrow, int partIndex) {
      if (partIndex >= 0 && partIndex < PART_TEXTURES.length && arrow.getTool() != null) {
         MaterialVariant material = arrow.getTool().getMaterials().get(partIndex);
         if (material != null) {
            Optional<MaterialRenderInfo> optional =
                    MaterialRenderInfoLoader.INSTANCE.getRenderInfo(material.getVariant());
            if (optional.isPresent()) {
               MaterialRenderInfo info = optional.get();
               return new TextureInformation(PART_TEXTURES[partIndex], info.vertexColor());
            }
         }
      }
      return new TextureInformation(getTextureLocation(arrow), 0xFFFFFFFF);
   }

   private void performRendering(VertexConsumer consumer, PoseStack poseStack, int packedLight, int color) {
      PoseStack.Pose pose = poseStack.last();
      Matrix4f matrix4f = pose.pose();
      Matrix3f matrix3f = pose.normal();

      vertex(matrix4f, matrix3f, consumer, -7, -2, -2, 0.0F, 0.15625F, -1, 0, 0, packedLight, color);
      vertex(matrix4f, matrix3f, consumer, -7, -2,  2, 0.15625F, 0.15625F, -1, 0, 0, packedLight, color);
      vertex(matrix4f, matrix3f, consumer, -7,  2,  2, 0.15625F, 0.3125F, -1, 0, 0, packedLight, color);
      vertex(matrix4f, matrix3f, consumer, -7,  2, -2, 0.0F, 0.3125F, -1, 0, 0, packedLight, color);
      vertex(matrix4f, matrix3f, consumer, -7,  2, -2, 0.0F, 0.15625F,  1, 0, 0, packedLight, color);
      vertex(matrix4f, matrix3f, consumer, -7,  2,  2, 0.15625F, 0.15625F,  1, 0, 0, packedLight, color);
      vertex(matrix4f, matrix3f, consumer, -7, -2,  2, 0.15625F, 0.3125F,  1, 0, 0, packedLight, color);
      vertex(matrix4f, matrix3f, consumer, -7, -2, -2, 0.0F, 0.3125F,  1, 0, 0, packedLight, color);

      for (int j = 0; j < 4; ++j) {
         poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
         vertex(matrix4f, matrix3f, consumer, -8, -2, 0, 0.0F, 0.0F, 0, 1, 0, packedLight, color);
         vertex(matrix4f, matrix3f, consumer,  8, -2, 0, 0.5F, 0.0F, 0, 1, 0, packedLight, color);
         vertex(matrix4f, matrix3f, consumer,  8,  2, 0, 0.5F, 0.15625F, 0, 1, 0, packedLight, color);
         vertex(matrix4f, matrix3f, consumer, -8,  2, 0, 0.0F, 0.15625F, 0, 1, 0, packedLight, color);
      }
   }

   private void vertex(Matrix4f matrix, Matrix3f normalMatrix, VertexConsumer consumer,
                       int x, int y, int z, float u, float v,
                       int nx, int ny, int nz, int packedLight, int color) {
      consumer.vertex(matrix, x, y, z)
              .color((color >>> 16) & 0xFF, (color >>> 8) & 0xFF, color & 0xFF, (color >>> 24) & 0xFF)
              .uv(u, v)
              .overlayCoords(OverlayTexture.NO_OVERLAY)
              .uv2(packedLight)
              .normal(normalMatrix, nx, ny, nz)
              .endVertex();
   }

   private static final class TextureInformation {
      final ResourceLocation texture;
      final int color;
      TextureInformation(ResourceLocation texture, int color) {
         this.texture = texture;
         this.color = color;
      }
   }
}