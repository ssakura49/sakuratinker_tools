package com.ssakura49.sakuratinker_tools.mixin.tinker;

import com.ssakura49.sakuratinker_tools.library.tools.item.ModifiableArrowItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.common.TinkerTags.Items;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.capability.inventory.ToolInventoryCapability;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.tools.modules.ranged.TrickQuiverModule;

@Mixin(
   value = {TrickQuiverModule.class},
   remap = false
)
public abstract class TrickQuiverModuleMixin {
   @Final
   @Shadow(
      remap = false
   )
   private static ResourceLocation SELECTED_SLOT;

   @Inject(
      method = {"shrinkAmmo"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onShrinkAmmo(IToolStackView tool, ModifierEntry modifier, LivingEntity shooter, ItemStack ammo, int needed, CallbackInfo ci) {
      if (ammo.getItem() instanceof ModifiableArrowItem&&ammo.is(TinkerTags.Items.MODIFIABLE)) {
         ToolStack toolStack = ToolStack.from(ammo);
         if (!toolStack.isBroken()) {
            ToolDamageUtil.damageAnimated(toolStack, needed, shooter);
         }

         int slot = tool.getPersistentData().getInt(SELECTED_SLOT);
         modifier.getHook(ToolInventoryCapability.HOOK).setStack(tool, modifier, slot, ammo);
         ci.cancel();
      }

   }
}
