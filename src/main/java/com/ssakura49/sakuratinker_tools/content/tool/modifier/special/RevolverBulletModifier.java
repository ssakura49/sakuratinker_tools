package com.ssakura49.sakuratinker_tools.content.tool.modifier.special;

import com.ssakura49.sakuratinker.core.tools.modifiers.base.BaseModifier;
import com.ssakura49.sakuratinker_tools.content.capability.ToolBulletSlotCapability;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.ToolDataNBT;

public class RevolverBulletModifier extends BaseModifier {
   public boolean shouldDisplay(boolean advanced) {
      return false;
   }

   public void addVolatileData(IToolContext context, ModifierEntry modifier, ToolDataNBT volatileData) {
      volatileData.putBoolean(ToolBulletSlotCapability.KEY_MODIFIABLE, true);
   }
}
