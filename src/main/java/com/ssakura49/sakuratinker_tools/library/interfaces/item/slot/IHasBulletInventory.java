package com.ssakura49.sakuratinker_tools.library.interfaces.item.slot;

import com.ssakura49.sakuratinker_tools.content.capability.ToolBulletSlotCapability;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public interface IHasBulletInventory {
   int getDefaultSlots();

   default void setSlots(ToolBulletSlotCapability capability) {
      ToolBulletSlotCapability.setSlotCount((IToolStackView)capability.tool().get(), this.getDefaultSlots());
   }
}
