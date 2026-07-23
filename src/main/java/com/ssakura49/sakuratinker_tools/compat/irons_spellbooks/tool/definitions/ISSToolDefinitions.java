package com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.tool.definitions;

import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.init.ISSItem;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;

public class ISSToolDefinitions {
   public static final ToolDefinition TINKER_SPELL_BOOK;

   static {
      TINKER_SPELL_BOOK = ToolDefinition.create(ISSItem.tinker_spell_book);
   }
}
