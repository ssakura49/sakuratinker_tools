package com.ssakura49.sakuratinker_tools.compat.goety.init;

import com.Polarice3.Goety.api.magic.SpellType;
import com.ssakura49.sakuratinker_tools.compat.goety.tool.item.TinkerWandItem;
import com.ssakura49.sakuratinker_tools.compat.goety.tool.item.base.ModifiableWandItem;
import com.ssakura49.sakuratinker_tools.compat.goety.tool.stats.SoulGathererMaterialStats;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.tconstruct.common.registration.CastItemObject;
import slimeknights.tconstruct.common.registration.ItemDeferredRegisterExtension;
import slimeknights.tconstruct.library.tools.part.ToolPartItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class GoetyItem {
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


    public static final ItemObject<ToolPartItem> soul_gatherer = registerPart("soul_gatherer", () -> new ToolPartItem(PartItem, SoulGathererMaterialStats.ID));
    public static final ItemObject<ModifiableWandItem> tinker_wand = registerTool("tinker_wand", () -> new TinkerWandItem(ToolItem, SpellType.NONE));
    public static final CastItemObject soulGathererCast = registerCast("soul_gatherer", CastItem);

}
