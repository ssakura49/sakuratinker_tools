package com.ssakura49.sakuratinker_tools.compat.extrabotany.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.ssakura49.sakuratinker_tools.compat.extrabotany.entity.PhantomSwordEntity;
import com.ssakura49.sakuratinker_tools.content.entity.part.CelestialBladePart;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.client.materials.MaterialRenderInfo;
import slimeknights.tconstruct.library.client.materials.MaterialRenderInfoLoader;
import slimeknights.tconstruct.library.materials.definition.MaterialVariant;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

public class PhantomSwordRenderer extends EntityRenderer<PhantomSwordEntity> {
    private final ItemRenderer itemRenderer;

    public PhantomSwordRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(PhantomSwordEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if (entity.getDelay() > 0)
            return;

        poseStack.pushPose();

        float s = 1.5F;
        poseStack.scale(s, s, s);
        poseStack.mulPose(Axis.YP.rotationDegrees(entity.getRotation() + 90F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(entity.getPitch()));
        poseStack.mulPose(Axis.ZP.rotationDegrees(-45F));

        ItemStack renderStack = entity.getItemStack();
        if (!renderStack.isEmpty()) {
            BakedModel bakedModel = this.itemRenderer.getModel(renderStack, entity.level(), null, entity.getId());
            itemRenderer.render(renderStack, ItemDisplayContext.GROUND, false, poseStack, buffer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, bakedModel);
        }

        poseStack.popPose();

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(PhantomSwordEntity entity) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}
