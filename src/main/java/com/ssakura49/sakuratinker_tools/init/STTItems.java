package com.ssakura49.sakuratinker_tools.init;

import com.ssakura49.sakuratinker_tools.content.item.BulletItem;
import com.ssakura49.sakuratinker_tools.content.tool.STArmorDefinitions;
import com.ssakura49.sakuratinker_tools.content.tool.STToolDefinitions;
import com.ssakura49.sakuratinker_tools.content.tool.item.BattleFlagItem;
import com.ssakura49.sakuratinker_tools.content.tool.item.BladeConvergence;
import com.ssakura49.sakuratinker_tools.content.tool.item.EnergyChargerItem;
import com.ssakura49.sakuratinker_tools.content.tool.item.GrapplingHookItem;
import com.ssakura49.sakuratinker_tools.content.tool.item.LaserGun;
import com.ssakura49.sakuratinker_tools.content.tool.item.RevolverItem;
import com.ssakura49.sakuratinker_tools.content.tool.item.ScytheItem;
import com.ssakura49.sakuratinker_tools.content.tool.item.Shuriken;
import com.ssakura49.sakuratinker_tools.content.tool.item.SwiftSword;
import com.ssakura49.sakuratinker_tools.content.tool.item.TinkerArrow;
import com.ssakura49.sakuratinker_tools.content.tool.item.TinkerBullet;
import com.ssakura49.sakuratinker_tools.content.tool.item.TinkerCharm;
import com.ssakura49.sakuratinker_tools.content.tool.item.VampireKnives;
import com.ssakura49.sakuratinker_tools.content.tool.item.YoyoItem;
import com.ssakura49.sakuratinker_tools.library.tools.item.ModifiableArrowItem;
import com.ssakura49.sakuratinker_tools.library.tools.item.ModifiableBulletItem;
import com.ssakura49.sakuratinker_tools.library.tools.stats.AxleMaterialStats;
import com.ssakura49.sakuratinker_tools.library.tools.stats.BattleFlagMaterialStats;
import com.ssakura49.sakuratinker_tools.library.tools.stats.CharmChainMaterialStats;
import com.ssakura49.sakuratinker_tools.library.tools.stats.ChordMaterialStats;
import com.ssakura49.sakuratinker_tools.library.tools.stats.EnergyUnitMaterialStats;
import com.ssakura49.sakuratinker_tools.library.tools.stats.FletchingMaterialStats;
import com.ssakura49.sakuratinker_tools.library.tools.stats.FoxMaskMaterialStats;
import com.ssakura49.sakuratinker_tools.library.tools.stats.LaserMediumMaterialStats;
import com.ssakura49.sakuratinker_tools.library.tools.stats.RangeMaterialStats;
import com.ssakura49.sakuratinker_tools.library.tools.stats.STTStatlessMaterialStats;
import com.ssakura49.sakuratinker_tools.library.tools.stats.YoYoRingMaterialStats;
import com.ssakura49.tinkercuriolib.tools.item.ModifiableCurioItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ArmorItem.Type;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.mantle.registration.object.EnumObject;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.tconstruct.common.registration.CastItemObject;
import slimeknights.tconstruct.common.registration.ItemDeferredRegisterExtension;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.item.armor.ModifiableArmorItem;
import slimeknights.tconstruct.library.tools.item.armor.MultilayerArmorItem;
import slimeknights.tconstruct.library.tools.part.ToolPartItem;
import slimeknights.tconstruct.tools.stats.HandleMaterialStats;
import slimeknights.tconstruct.tools.stats.HeadMaterialStats;
import slimeknights.tconstruct.tools.stats.StatlessMaterialStats;

import static com.ssakura49.sakuratinker.SakuraTinker.MODID;

public class STTItems {
   public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
   public static final ItemDeferredRegisterExtension TINKER_ITEMS = new ItemDeferredRegisterExtension(MODID);

   public static final Item.Properties PartItem = new Item.Properties().stacksTo(64);
   public static final Item.Properties CastItem = new Item.Properties().stacksTo(64);
   public static final Item.Properties ToolItem = new Item.Properties().stacksTo(1);

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

   public static final ItemObject<ToolPartItem> charm_chain = registerPart("charm_chain", () -> new ToolPartItem(PartItem, CharmChainMaterialStats.ID));
   public static final ItemObject<ToolPartItem> charm_core = registerPart("charm_core", () -> new ToolPartItem(PartItem, STTStatlessMaterialStats.CHARM_CORE.getIdentifier()));
   public static final ItemObject<ModifiableCurioItem> tinker_charm = registerTool("tinker_charm", () -> new TinkerCharm(ToolItem, STToolDefinitions.TINKER_CHARM));
   public static final CastItemObject charmChainCast = registerCast("charm_chain", CastItem);
   public static final CastItemObject charmCoreCast = registerCast("charm_core", CastItem);

   public static final ItemObject<ToolPartItem> swift_blade = registerPart("swift_blade", () -> new ToolPartItem(PartItem, HeadMaterialStats.ID));
   public static final ItemObject<ToolPartItem> swift_guard = registerPart("swift_guard", () -> new ToolPartItem(PartItem, HandleMaterialStats.ID));
   public static final ItemObject<ModifiableItem> swift_sword = registerTool("swift_sword", () -> new SwiftSword(ToolItem, STToolDefinitions.SWIFT_SWORD));
   public static final CastItemObject swiftBladeCast = registerCast("swift_blade", CastItem);
   public static final CastItemObject swiftGuardCast = registerCast("swift_guard", CastItem);

   public static final ItemObject<ToolPartItem> laser_medium = registerPart("laser_medium", () -> new ToolPartItem(PartItem, LaserMediumMaterialStats.ID));
   public static final ItemObject<ToolPartItem> energy_unit = registerPart("energy_unit", () -> new ToolPartItem(PartItem, EnergyUnitMaterialStats.ID));
   public static final ItemObject<ToolPartItem> barrel = registerPart("barrel", () -> new ToolPartItem(PartItem, HeadMaterialStats.ID));
   public static final ItemObject<ToolPartItem> blade = registerPart("blade", () -> new ToolPartItem(PartItem, HeadMaterialStats.ID));
   public static final ItemObject<ModifiableItem> laser_gun = registerTool("laser_gun", () -> new LaserGun(ToolItem, STToolDefinitions.LASER_GUN));
   public static final CastItemObject barrelCast = registerCast("barrel", CastItem);
   public static final CastItemObject energyUnitCast = registerCast("energy_unit", CastItem);
   public static final CastItemObject laserMediumCast = registerCast("laser_medium", CastItem);
   public static final CastItemObject bladeCast = registerCast("blade", CastItem);

   public static final ItemObject<ToolPartItem> arrow_head = registerPart("arrow_head", () -> new ToolPartItem(PartItem, HeadMaterialStats.ID));
   public static final ItemObject<ToolPartItem> arrow_shaft = registerPart("arrow_shaft", () -> new ToolPartItem(PartItem, HandleMaterialStats.ID));
   public static final ItemObject<ToolPartItem> fletching = registerPart("fletching", () -> new ToolPartItem(PartItem, FletchingMaterialStats.ID));
   public static final ItemObject<ModifiableArrowItem> tinker_arrow = registerTool("tinker_arrow", () -> new TinkerArrow(ToolItem, STToolDefinitions.TINKER_ARROW));
   public static final CastItemObject arrowHeadCast = registerCast("arrow_head", CastItem);
   public static final CastItemObject arrowShaftCast = registerCast("arrow_shaft", CastItem);

   public static final ItemObject<ToolPartItem> blade_box = registerPart("blade_box", () -> new ToolPartItem(PartItem, StatlessMaterialStats.BINDING.getIdentifier()));
   public static final ItemObject<ModifiableItem> vampire_knife = registerTool("vampire_knife", () -> new VampireKnives(ToolItem, STToolDefinitions.VAMPIRE_KNIVES));

   public static final ItemObject<ToolPartItem> great_blade = registerPart("great_blade", () -> new ToolPartItem(PartItem, HeadMaterialStats.ID));
   public static final ItemObject<ModifiableItem> great_sword = registerTool("great_sword",() -> new ModifiableItem(ToolItem, STToolDefinitions.GREAT_SWORD));
   public static final CastItemObject greatBladeCast = registerCast("great_blade", CastItem);

   public static final ItemObject<ToolPartItem> shell = registerPart("shell", () -> new ToolPartItem(PartItem, StatlessMaterialStats.BINDING.getIdentifier()));
   public static final ItemObject<ModifiableCurioItem> power_bank = registerTool("power_bank", () -> new EnergyChargerItem(ToolItem, STToolDefinitions.POWER_BANK));
   public static final CastItemObject shellCast = registerCast("shell", CastItem);

   public static final ItemObject<ToolPartItem> flag = registerPart("flag", () -> new ToolPartItem(PartItem, BattleFlagMaterialStats.ID));
   public static final ItemObject<ModifiableItem> battle_flag = registerTool("battle_flag", () -> new BattleFlagItem(ToolItem, STToolDefinitions.BATTLE_FLAG));
   public static final CastItemObject flagCast = registerCast("flag", CastItem);

   public static final ItemObject<ToolPartItem> axle = registerPart("axle", () -> new ToolPartItem(PartItem, AxleMaterialStats.ID));
   public static final ItemObject<ToolPartItem> yoyo_ring = registerPart("yoyo_ring", () -> new ToolPartItem(PartItem, YoYoRingMaterialStats.ID));
   public static final ItemObject<ToolPartItem> chord = registerPart("chord", () -> new ToolPartItem(PartItem, ChordMaterialStats.ID));
   public static final ItemObject<ToolPartItem> yoyo_body = registerPart("yoyo_body", () -> new ToolPartItem(PartItem, HeadMaterialStats.ID));
   public static final ItemObject<ModifiableItem> yoyo = registerTool("yoyo", () -> new YoyoItem(ToolItem, STToolDefinitions.YOYO));
   public static final CastItemObject axleCast = registerCast("axle", CastItem);
   public static final CastItemObject yoyoRingCast = registerCast("yoyo_ring", CastItem);
   public static final CastItemObject chordCast = registerCast("chord", CastItem);
   public static final CastItemObject yoyoBodyCast = registerCast("yoyo_body", CastItem);

   public static final ItemObject<ToolPartItem> scythe_blade = registerPart("scythe_blade", ()->new ToolPartItem(PartItem, HeadMaterialStats.ID));
   public static final ItemObject<ModifiableItem> scythe = registerTool("scythe", () -> new ScytheItem(ToolItem, STToolDefinitions.SCYTHE));
   public static final CastItemObject scytheBladeCast = registerCast("scythe_blade", CastItem);

// public static final ItemObject<ToolPartItem> alchemical_core = TINKER_ITEMS.register("alchemical_core",()->new ToolPartItem(PartItem,AlchemicalCoreMaterialStats.ID));
// public static final ItemObject<ModifiableItem> alchemical_gloves = TINKER_ITEMS.register("alchemical_gloves", () -> new AlchemicalGlovesItem(ToolItem, STToolDefinitions.ALCHEMICAL_GLOVES));
// public static final CastItemObject alchemicalCoreCast = TINKER_ITEMS.registerCast("alchemical_core", CastItem);

   public static final ItemObject<ModifiableItem> blade_convergence = registerTool("blade_convergence", () -> new BladeConvergence(ToolItem, STToolDefinitions.BLADE_CONVERGENCE));

   public static final EnumObject<ArmorItem.Type, ModifiableArmorItem> embeddedArmor = TINKER_ITEMS.registerEnum("embedded", ArmorItem.Type.values(), (type) -> new MultilayerArmorItem(STArmorDefinitions.EMBEDDED, type, ToolItem));

   public static final ItemObject<ModifiableItem> grappling_hook = registerTool("grappling_hook", () -> new GrapplingHookItem(ToolItem, STToolDefinitions.HOOK));

   public static final ItemObject<ModifiableItem> revolver = registerTool("revolver", () -> new RevolverItem(ToolItem, STToolDefinitions.TINKER_REVOLVER));

   public static final ItemObject<ModifiableBulletItem> tinker_bullet = registerTool("tinker_bullet", () -> new TinkerBullet(ToolItem, STToolDefinitions.TINKER_BULLET));
   public static final ItemObject<ModifiableItem> shuriken = registerTool("shuriken", () -> new Shuriken(ToolItem, STToolDefinitions.SHURIKEN));

   public static final ItemObject<ToolPartItem> range_core = registerPart("range_core",()->new ToolPartItem(PartItem,RangeMaterialStats.ID));
   public static final CastItemObject rangeCoreCast = registerCast("range_core", CastItem);

   public static final ItemObject<ToolPartItem> fox_mask_part = registerPart("fox_mask_part",()->new ToolPartItem(PartItem,FoxMaskMaterialStats.ID));

// public static final ItemObject<ToolPartItem> phantom_core = TINKER_ITEMS.register("phantom_core", () -> new ToolPartItem(PartItem, PhantomCoreMaterialStats.ID));
// public static final ItemObject<ModifiableSwordItem> first_fractal = TINKER_ITEMS.register("first_fractal", () -> new FirstFractal(ToolItem));
// public static final CastItemObject phantomCoreCast = TINKER_ITEMS.registerCast("phantom_core", CastItem);


   public static final RegistryObject<Item> ghost_knife = ITEMS.register("ghost_knife", () -> new Item(new Item.Properties()));
   public static final RegistryObject<Item> grappling_blade = ITEMS.register("grappling_blade", () -> new Item(new Item.Properties()));
   public static final RegistryObject<Item> zenith_first_fractal = ITEMS.register("zenith_first_fractal", () -> new Item(new Item.Properties()));
   public static final RegistryObject<Item> bulletItem = ITEMS.register("bullet_ammo", () -> new BulletItem(new Item.Properties()));

   public static void init(IEventBus bus) {
      ITEMS.register(bus);
      TINKER_ITEMS.register(bus);
   }
}
