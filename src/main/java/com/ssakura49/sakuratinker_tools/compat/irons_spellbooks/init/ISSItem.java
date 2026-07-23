package com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.init;

import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.tool.item.TinkerSpellBook;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.tool.item.base.ModifiableSpellBookItem;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.tool.stats.BookMarkMaterialStats;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.tool.stats.EnvelopeMaterialStats;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.tool.stats.ISSStatlessMaterialStats;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.tool.stats.ManuScriptMaterialStats;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.tconstruct.common.registration.CastItemObject;
import slimeknights.tconstruct.common.registration.ItemDeferredRegisterExtension;
import slimeknights.tconstruct.library.tools.part.ToolPartItem;

public class ISSItem {
   public static final ItemDeferredRegisterExtension TINKER_ITEMS = new ItemDeferredRegisterExtension("sakuratinker");
   public static final Item.Properties PartItem = (new Item.Properties()).stacksTo(64);
   public static final Item.Properties CastItem = (new Item.Properties()).stacksTo(64);
   public static final Item.Properties ToolItem = (new Item.Properties()).stacksTo(1);
   private static final List<ItemObject<ToolPartItem>> LIST_PARTS = new ArrayList<>();
   private static final List<CastItemObject> LIST_CASTS = new ArrayList<>();
   private static final List<ItemObject<? extends Item>> LIST_TOOLS = new ArrayList<>();

   public static List<ItemObject<ToolPartItem>> getListParts() {
      return Collections.unmodifiableList(LIST_PARTS);
   }

   public static List<CastItemObject> getListCasts() {
      return Collections.unmodifiableList(LIST_CASTS);
   }

   public static List<ItemObject<? extends Item>> getListTools() {
      return Collections.unmodifiableList(LIST_TOOLS);
   }

   private static ItemObject<ToolPartItem> registerPart(String name, Supplier<? extends ToolPartItem> sup) {
      ItemObject<ToolPartItem> part = TINKER_ITEMS.register(name, sup);
      LIST_PARTS.add(part);
      return part;
   }

   private static CastItemObject registerCast(String name, Item.Properties props) {
      CastItemObject cast = TINKER_ITEMS.registerCast(name, props);
      LIST_CASTS.add(cast);
      return cast;
   }

   private static <T extends Item> ItemObject<T> registerTool(String name, Supplier<T> sup) {
      ItemObject<T> tool = TINKER_ITEMS.register(name, sup);
      LIST_TOOLS.add(tool);
      return tool;
   }
   public static void init(IEventBus bus) {
      TINKER_ITEMS.register(bus);
   }

   public static final ItemObject<ToolPartItem> manu_script = registerPart("manu_script", () -> new ToolPartItem(PartItem, ManuScriptMaterialStats.ID));
   public static final ItemObject<ToolPartItem> envelope = registerPart("envelope", () -> new ToolPartItem(PartItem, EnvelopeMaterialStats.ID));
   public static final ItemObject<ToolPartItem> book_mark = registerPart("book_mark", () -> new ToolPartItem(PartItem, BookMarkMaterialStats.ID));
   public static final ItemObject<ToolPartItem> gutter = registerPart("gutter", () -> new ToolPartItem(PartItem, ISSStatlessMaterialStats.GUTTER.getIdentifier()));
   public static final ItemObject<ModifiableSpellBookItem> tinker_spell_book = registerTool("tinker_spell_book", () -> new TinkerSpellBook(1, ToolItem));
   public static final CastItemObject manuScriptCast = registerCast("manu_script", CastItem);
   public static final CastItemObject envelopeCast = registerCast("envelope", CastItem);
   public static final CastItemObject bookMarkCast = registerCast("book_mark", CastItem);
   public static final CastItemObject gutterCast = registerCast("gutter", CastItem);


}
