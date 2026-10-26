package com.ssakura49.sakuratinker_tools.content.tool.modifier.special;

import com.ssakura49.sakuratinker.core.library.tools.modifier.hooks.STHooks;
import com.ssakura49.sakuratinker.core.library.tools.modifier.hooks.display.CustomBarDisplayModifierHook;
import com.ssakura49.sakuratinker.core.library.tools.stats.STToolStats;
import com.ssakura49.sakuratinker.core.tools.modifiers.base.BaseModifier;
import java.awt.Color;

import com.ssakura49.sakuratinker.core.utils.math.MathUtil;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierRemovalHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ValidateModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import static slimeknights.tconstruct.library.tools.capability.ToolEnergyCapability.*;

public class EnergyDisplayModifier extends BaseModifier implements ValidateModifierHook, ModifierRemovalHook, CustomBarDisplayModifierHook, TooltipModifierHook {
   @Override
   protected void registerHooks(ModuleHookMap.@NotNull Builder builder) {
      super.registerHooks(builder);
      builder.addHook(this,
              ModifierHooks.VALIDATE,
              ModifierHooks.TOOLTIP,
              ModifierHooks.REMOVE,
              STHooks.CUSTOM_BAR);
   }

   @Override
   public boolean isNoLevels() {
      return true;
   }

   @Nullable
   @Override
   public Component validate(IToolStackView tool, ModifierEntry modifier) {
      checkEnergy(tool);
      return null;
   }

   @Nullable
   @Override
   public Component onRemoved(IToolStackView tool, Modifier modifier) {
      checkEnergy(tool);
      return null;
   }

   @Override
   public @NotNull Component getDisplayName(IToolStackView tool, ModifierEntry entry, @Nullable RegistryAccess access) {
      int current = getEnergy(tool);
      int max = getMaxEnergy(tool);
      String energyInfo = MathUtil.getEnergyString(current) + " / " + MathUtil.getEnergyString(max);
      String percentage = MathUtil.toPercentage((double)current/max, 1);
      return Component.translatable("tooltip.sakuratinker.energy_storage", energyInfo, percentage).withStyle(style -> style.withColor(getDynamicColor(current, max)));
   }

   private static int getDynamicColor(int current, int max) {
      float ratio = (float)current / max;
      return Color.HSBtoRGB(ratio * 0.35f, 0.9f, 0.9f);
   }

   @Override
   public String getBarId(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
      return "sakuratinker:energy_bar";
   }

   @Override
   @OnlyIn(Dist.CLIENT)
   public boolean shouldShowBar(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
      return barsHadBeenShown == 0
              && tool.getStats().getInt(MAX_STAT) + tool.getStats().getInt(STToolStats.ENERGY_STORAGE) > 0;
   }

   @Override
   public int getBarColor(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
      float percent = (float) getEnergy(tool) / getMaxEnergy(tool);
      int r = (int) (0x1E + (0x3C - 0x1E) * percent);
      int g = (int) (0x90 + (0xB3 - 0x90) * percent);
      int b = (int) (0xFF + (0x71 - 0xFF) * percent);
      return FastColor.ARGB32.color(255, r, g, b);
   }

   @Override
   public float getBarProgress(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
      int current = getEnergy(tool);
      int max = getMaxEnergy(tool);
      if (max <= 0) {
         return 0.0F;
      }
      return (float) current / max;
   }

}
