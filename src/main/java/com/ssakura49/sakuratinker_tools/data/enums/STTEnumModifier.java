package com.ssakura49.sakuratinker_tools.data.enums;

import javax.annotation.Nullable;
import net.minecraftforge.common.crafting.conditions.ICondition;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.modifiers.impl.BasicModifier;
import slimeknights.tconstruct.library.modifiers.modules.ModifierModule;
import slimeknights.tconstruct.library.modifiers.util.ModifierLevelDisplay;
import slimeknights.tconstruct.library.recipe.modifiers.adding.ModifierRecipeBuilder;

public enum STTEnumModifier {
   ;
   public final ModifierId id;
   public final ModifierRecipeBuilder builder;
   public final BasicModifier.TooltipDisplay tooltipDisplay;
   public final ModifierModule[] modules;
   public final ICondition condition;
   public final ModifierLevelDisplay display;

   private STTEnumModifier(ModifierId id, ModifierRecipeBuilder builder, @Nullable ICondition condition, BasicModifier.TooltipDisplay tooltipDisplay, ModifierLevelDisplay display, ModifierModule... modules) {
      this.id = id;
      this.builder = builder;
      this.tooltipDisplay = tooltipDisplay;
      this.modules = modules;
      this.condition = condition;
      this.display = display;
   }
}
