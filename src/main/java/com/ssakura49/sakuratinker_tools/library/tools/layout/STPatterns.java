package com.ssakura49.sakuratinker_tools.library.tools.layout;

import slimeknights.tconstruct.library.recipe.partbuilder.Pattern;

public class STPatterns {
   public static final Pattern EMBEDDED_ARMOR = pattern("embedded_armor");

   private static Pattern pattern(String name) {
      return new Pattern("sakuratinker", name);
   }

   private STPatterns() {
   }
}
