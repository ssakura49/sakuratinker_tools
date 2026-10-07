package com.ssakura49.sakuratinker_tools.data.providers;

import java.util.concurrent.CompletableFuture;
import javax.annotation.Nullable;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker_tools.compat.extrabotany.init.EBItem;
import com.ssakura49.sakuratinker_tools.compat.goety.init.GoetyItem;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.init.ISSItem;
import com.ssakura49.sakuratinker_tools.init.STTItems;
import com.ssakura49.sakuratinker_tools.init.STTTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.common.TinkerTags.Items;
import slimeknights.tconstruct.common.registration.CastItemObject;
import slimeknights.tconstruct.library.tools.part.ToolPartItem;

import static slimeknights.tconstruct.common.TinkerTags.Items.*;

public class STTItemTagProvider extends ItemTagsProvider {
   public STTItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagsProvider.TagLookup<Block>> lookup, @Nullable ExistingFileHelper existingFileHelper) {
      super(output, provider, lookup, "sakuratinker", existingFileHelper);
   }

   protected void addTags(HolderLookup.@NotNull Provider provider) {
      var goldCasts = this.tag(TinkerTags.Items.GOLD_CASTS);
      var sandCasts = this.tag(TinkerTags.Items.SAND_CASTS);
      var redSandCasts = this.tag(TinkerTags.Items.RED_SAND_CASTS);

      var multiUseCasts = this.tag(TinkerTags.Items.MULTI_USE_CASTS);
      var singleUseCasts = this.tag(TinkerTags.Items.SINGLE_USE_CASTS);

      for (CastItemObject cast : STTItems.getListCasts()) {
         goldCasts.add(cast.get());
         sandCasts.add(cast.getSand());
         redSandCasts.add(cast.getRedSand());
         addPartCastTags(cast, true, multiUseCasts, singleUseCasts);
      }

      for (CastItemObject cast : ISSItem.getListCasts()) {
         goldCasts.addOptional(cast.getId());
         sandCasts.addOptional(cast.getSand().builtInRegistryHolder().key().location());
         redSandCasts.addOptional(cast.getRedSand().builtInRegistryHolder().key().location());
         addPartCastTags(cast, false, multiUseCasts, singleUseCasts);
      }

      for (CastItemObject cast : GoetyItem.getListCasts()) {
         goldCasts.addOptional(cast.getId());
         sandCasts.addOptional(cast.getSand().builtInRegistryHolder().key().location());
         redSandCasts.addOptional(cast.getRedSand().builtInRegistryHolder().key().location());
         addPartCastTags(cast, false, multiUseCasts, singleUseCasts);
      }

      for (CastItemObject cast : EBItem.getListCasts()) {
         goldCasts.addOptional(cast.getId());
         sandCasts.addOptional(cast.getSand().builtInRegistryHolder().key().location());
         redSandCasts.addOptional(cast.getRedSand().builtInRegistryHolder().key().location());
         addPartCastTags(cast, false, multiUseCasts, singleUseCasts);
      }


      var toolPartsKey = TinkerTags.Items.TOOL_PARTS;
      var toolPartsTag = this.tag(TinkerTags.Items.TOOL_PARTS);
      for (ItemObject<ToolPartItem> part : STTItems.getListParts()) {
         toolPartsTag.add(part.get());
      }
      for (ItemObject<ToolPartItem> part : ISSItem.getListParts()) {
         toolPartsTag.addOptional(part.getId());
      }
      for (ItemObject<ToolPartItem> part : GoetyItem.getListParts()) {
         toolPartsTag.addOptional(part.getId());
      }
      for (ItemObject<ToolPartItem> part : EBItem.getListParts()) {
         toolPartsTag.addOptional(part.getId());
      }

      // ==========================================
      // 1. 部件与材料 (Parts & Materials)
      // ==========================================

      /** 可存放在部件箱中的任何物品 */
      var chestParts = TinkerTags.Items.CHEST_PARTS;
      var chestPartsTag = this.tag(chestParts);
      STTItems.getListParts().forEach(part -> chestPartsTag.add(part.get()));
      ISSItem.getListParts().forEach(part -> chestPartsTag.addOptional(part.getId()));
      GoetyItem.getListParts().forEach(part -> chestPartsTag.addOptional(part.getId()));
      EBItem.getListParts().forEach(part -> chestPartsTag.addOptional(part.getId()));

//      /** 所有材料/部件物品，用于填充 JEI 或书籍中的列表 */
//      STTItems.getListParts().forEach(part -> toolPartsTag.add(part.get()));
//      ISSItem.getListParts().forEach(part -> toolPartsTag.add(part.get()));
//      GoetyItem.getListParts().forEach(part -> toolPartsTag.add(part.get()));
//      EBItem.getListParts().forEach(part -> toolPartsTag.add(part.get()));

      /** 猪灵可能会用来与玩家交易的工具部件（包含下界随机材料） */
      var barteredParts = TinkerTags.Items.BARTERED_PARTS;
      var barteredPartsTag = this.tag(barteredParts);


      // ==========================================
      // 2. 核心锻造与属性 (Core Modifiable & Stats)
      // ==========================================

      /** 任何可以在工匠站/工匠砧中进行强化修改的物品 */
      var modifiable = TinkerTags.Items.MODIFIABLE;
      var modifiableTag = this.tag(modifiable);

      /** 支持提供额外强化槽位（Bonus Slot）强化修饰的物品 */
      var bonusSlots = TinkerTags.Items.BONUS_SLOTS;
      var bonusSlotTag = this.tag(bonusSlots);

      /** 包含多个部件组成的可强化工具 */
      var multipartTool = TinkerTags.Items.MULTIPART_TOOL;
      var multipartToolTag = this.tag(multipartTool);

      /** 仅由 1 个部件组成的可强化工具（在 JEI 中有特殊逻辑） */
      var singlePartTool = TinkerTags.Items.SINGLEPART_TOOL;
      var singlePartToolTag = this.tag(singlePartTool);

      /** 可以提升挖掘/作用范围（AOE）的工具 */
      var aoe = TinkerTags.Items.AOE;
      var aoeTag = this.tag(aoe);

      /** 消耗耐久度且可被修复的工具（支持 DURABILITY 耐久属性） */
      var durability = TinkerTags.Items.DURABILITY;
      var durabilityTag = this.tag(durability);


      // ==========================================
      // 3. 图书分类与获取途径 (Book Categories & Sources)
      // ==========================================

      /** 小型工具：通常攻击速度较快、伤害较低，用于书籍展示 */
      var smallTools = TinkerTags.Items.SMALL_TOOLS;
      var smallToolsTag = this.tag(smallTools);

      /** 大型工具：通常攻击速度较慢、伤害较高，用于书籍展示及副手攻击惩罚 */
      var broadTools = TinkerTags.Items.BROAD_TOOLS;
      var broadToolsTag = this.tag(broadTools);

      /** 特殊工具：不属于大型或小型的特殊工具（如法杖），用于书籍展示 */
      var specialTools = TinkerTags.Items.SPECIAL_TOOLS;
      var specialToolsTag = this.tag(specialTools);

      /** 遗物/古代工具：通过遗迹宝箱战利品获得的工具，用于书籍和特定强化 */
      var ancientTools = TinkerTags.Items.ANCIENT_TOOLS;
      var ancientToolsTag = this.tag(ancientTools);

      /** 游商工具：流浪商人出售的工具（默认包含古代工具，注意回收利用机制） */
      var traderTools = TinkerTags.Items.TRADER_TOOLS;
      var traderToolsTag = this.tag(traderTools);


      // ==========================================
      // 4. 交互与使用行为 (Interaction & Usage)
      // ==========================================

      /** 可调整战利品上下文的工具（用于战利品修改器钩子） */
      var lootCapableTool = TinkerTags.Items.LOOT_CAPABLE_TOOL;
      var lootCapableToolTag = this.tag(lootCapableTool);

      /** 在玩家手持时使用的物品，主要是支持交互的工具 */
      var held = TinkerTags.Items.HELD;
      var heldTag = this.tag(held);

      /** 可以使用交互修饰符（Interaction Modifiers）的任何物品 */
      var interactable = TinkerTags.Items.INTERACTABLE;
      var interactableTag = this.tag(interactable);

      /** 支持右键点击交互的工具 */
      var interactableRight = TinkerTags.Items.INTERACTABLE_RIGHT;
      var interactableRightTag = this.tag(interactableRight);

      /** 支持蓄力交互的工具（包含右键交互工具、弓、盾牌等） */
      var interactableCharge = TinkerTags.Items.INTERACTABLE_CHARGE;
      var interactableChargeTag = this.tag(interactableCharge);

      /** 使用强化修饰符进行主动作蓄力交互的工具（类似 CHARGE，但不包含弓） */
      var interactableChargeModifier = TinkerTags.Items.INTERACTABLE_CHARGE_MODIFIER;
      var interactableChargeModifierTag = this.tag(interactableChargeModifier);

      /** 支持左键点击交互的工具 */
      var interactableLeft = TinkerTags.Items.INTERACTABLE_LEFT;
      var interactableLeftTag = this.tag(interactableLeft);

      /** 作为装备穿戴时支持交互的装备/护甲 */
      var interactableArmor = TinkerTags.Items.INTERACTABLE_ARMOR;
      var interactableArmorTag = this.tag(interactableArmor);

      /** 支持同时进行左键和右键交互的工具 */
      var interactableDual = TinkerTags.Items.INTERACTABLE_DUAL;
      var interactableDualTag = this.tag(interactableDual);


      // ==========================================
      // 5. 近战与战斗工具 (Melee Weapons & Combat)
      // ==========================================

      /** 支持【攻击伤害】属性的物品（通常无需直接添加，优先使用其子标签） */
      var melee = TinkerTags.Items.MELEE;
      var meleeTag = this.tag(melee);

      /** 支持近战攻击的物品，支持【攻击速度】属性（包含 MELEE 标签属性） */
      var meleeWeapon = TinkerTags.Items.MELEE_WEAPON;
      var meleeWeaponTag = this.tag(meleeWeapon);

      /** 专为近战设计的核心武器，会移除近战惩罚，默认假定手持 */
      var meleePrimary = TinkerTags.Items.MELEE_PRIMARY;
      var meleePrimaryTag = this.tag(meleePrimary);

      /** 剑类武器：通常仅用于战斗，默认假定手持 */
      var sword = TinkerTags.Items.SWORD;
      var swordTag = this.tag(sword);

      /** 徒手/徒手强化装备：增强徒手攻击伤害（如胸甲），无法提升攻速，近战不消耗耐久 */
      var unarmed = TinkerTags.Items.UNARMED;
      var unarmedTag = this.tag(unarmed);

      /** 具备招架/格挡能力的近战物品（不能再添加常规格挡修饰符） */
      var parry = TinkerTags.Items.PARRY;
      var parryTag = this.tag(parry);


      // ==========================================
      // 6. 采矿与采集工具 (Harvest Tools)
      // ==========================================

      /** 具备破坏方块能力的工具，支持【采矿速度】和【挖掘等级】属性 */
      var harvest = TinkerTags.Items.HARVEST;
      var harvestTag = this.tag(harvest);

      /** 专为采集设计的主力工具，移除挖掘惩罚 */
      var harvestPrimary = TinkerTags.Items.HARVEST_PRIMARY;
      var harvestPrimaryTag = this.tag(harvestPrimary);

      /** 能够挖掘/破坏石块类方块的工具 */
      var stoneHarvest = TinkerTags.Items.STONE_HARVEST;
      var stoneHarvestTag = this.tag(stoneHarvest);


      // ==========================================
      // 7. 防具与盾牌 (Armor & Shields)
      // ==========================================

      /** 作为防具穿戴的物品，支持【护甲值】、【护甲韧性】和【抗击退】属性 */
      var armor = TinkerTags.Items.ARMOR;
      var armorTag = this.tag(armor);

      /** 鞋子/靴子防具 */
      var boots = TinkerTags.Items.BOOTS;
      var bootsTag = this.tag(boots);

      /** 腿甲/裤子防具 */
      var leggings = TinkerTags.Items.LEGGINGS;
      var leggingsTag = this.tag(leggings);

      /** 胸甲防具 */
      var chestplates = TinkerTags.Items.CHESTPLATES;
      var chestplatesTag = this.tag(chestplates);

      /** 头盔防具 */
      var helmets = TinkerTags.Items.HELMETS;
      var helmetsTag = this.tag(helmets);

      /** 穿戴在任意主防具栏（或饰品栏 Curio）上的可强化物品 */
      var wornArmor = TinkerTags.Items.WORN_ARMOR;
      var wornArmorTag = this.tag(wornArmor);

      /** 握在任意主/副手上的防具/防护物品 */
      var heldArmor = TinkerTags.Items.HELD_ARMOR;
      var heldArmorTag = this.tag(heldArmor);

      /** 原生具备格挡/防护能力的盾牌类物品 */
      var shields = TinkerTags.Items.SHIELDS;
      var shieldsTag = this.tag(shields);

      /** 生物头颅相关物品（防止与头颅替换配方冲突） */
      var skulls = TinkerTags.Items.SKULLS;
      var skullsTag = this.tag(skulls);

      /** 支持槽位 0 进行头颅替换部件的可强化工具 */
      var swappableSkulls = TinkerTags.Items.SWAPPABLE_SKULLS;
      var swappableSkullsTag = this.tag(swappableSkulls);


      // ==========================================
      // 8. 远程与弹药 (Ranged & Ammo)
      // ==========================================

      /** 支持远程攻击的物品，支持【拉弓速度】、【弹道速度】和【精准度】属性 */
      var ranged = TinkerTags.Items.RANGED;
      var rangedTag = this.tag(ranged);

      /** 发射物发射器（如弓/弩），额外支持【弹射物伤害】属性 */
      var launchers = TinkerTags.Items.LAUNCHERS;
      var launchersTag = this.tag(launchers);

      /** 弓类总称（包含十字弩和长弓） */
      var bows = TinkerTags.Items.BOWS;
      var bowsTag = this.tag(bows);

      /** 长弓类：松开右键时发射箭矢 */
      var longbows = TinkerTags.Items.LONGBOWS;
      var longbowsTag = this.tag(longbows);

      /** 十字弩类：装填并储存箭矢，下次使用时发射 */
      var crossbows = TinkerTags.Items.CROSSBOWS;
      var crossbowsTag = this.tag(crossbows);

      /** 法杖类：支持特殊法杖修饰符，属于远程的子分类 */
      var staffs = TinkerTags.Items.STAFFS;
      var staffsTag = this.tag(staffs);

      /** 钓鱼竿类：支持钓鱼相关修饰符 */
      var fishingRods = TinkerTags.Items.FISHING_RODS;
      var fishingRodsTag = this.tag(fishingRods);

      /** 支持【力量 (Power)】强化的远程武器 */
      var rangedPower = TinkerTags.Items.RANGED_POWER;
      var rangedPowerTag = this.tag(rangedPower);

      /** 支持【快速装填 (Quick Charge)】强化的远程武器 */
      var rangedQuickCharge = TinkerTags.Items.RANGED_QUICK_CHARGE;
      var rangedQuickChargeTag = this.tag(rangedQuickCharge);

      /** 支持【弹跳 (Bounce)】强化的远程武器 */
      var rangedBounce = TinkerTags.Items.RANGED_BOUNCE;
      var rangedBounceTag = this.tag(rangedBounce);

      /** 支持【巨弩 (Ballista)】修饰符的弓类 */
      var ballistas = TinkerTags.Items.BALLISTAS;
      var ballistasTag = this.tag(ballistas);

      /** 小型远程工具（用于材料手册与百科全书展示） */
      var smallRanged = TinkerTags.Items.SMALL_RANGED;
      var smallRangedTag = this.tag(smallRanged);

      /** 大型远程工具（用于冶炼手册与百科全书展示） */
      var broadRanged = TinkerTags.Items.BROAD_RANGED;
      var broadRangedTag = this.tag(broadRanged);

      /** 主要用途为弹药的物品 */
      var ammo = TinkerTags.Items.AMMO;
      var ammoTag = this.tag(ammo);

      /** 直接投掷使用的弹药（无需发射器） */
      var thrownAmmo = TinkerTags.Items.THROWN_AMMO;
      var thrownAmmoTag = this.tag(thrownAmmo);

      /** 可作为巨弩（Ballista）修饰符发射弹药的工具/物品 */
      var ballistaAmmo = TinkerTags.Items.BALLISTA_AMMO;
      var ballistaAmmoTag = this.tag(ballistaAmmo);

      /** 一次性/消耗性工具（强化配方消耗更少成本） */
      var singleUse = TinkerTags.Items.SINGLE_USE;
      var singleUseTag = this.tag(singleUse);


      // ==========================================
      // 9. 外观修饰与黑名单 (Cosmetics & Blacklists)
      // ==========================================

      /** 可以接受木质纹理/外观装饰的工具 */
      var embellishmentWood = TinkerTags.Items.EMBELLISHMENT_WOOD;
      var embellishmentWoodTag = this.tag(embellishmentWood);

      /** 可以接受史莱姆纹理/外观装饰的工具 */
      var embellishmentSlime = TinkerTags.Items.EMBELLISHMENT_SLIME;
      var embellishmentSlimeTag = this.tag(embellishmentSlime);

      /** 可以进行染色（Dyeable）的工具 */
      var dyeable = TinkerTags.Items.DYEABLE;
      var dyeableTag = this.tag(dyeable);

      /** 可以应用旗帜图案（Banner）的工具 */
      var banner = TinkerTags.Items.BANNER;
      var bannerTag = this.tag(banner);

      /** 支持盔甲锻造模板纹饰（Trim）的防具 */
      var trim = TinkerTags.Items.TRIM;
      var trimTag = this.tag(trim);

      /** 支持纹饰材质但跳过纹饰图案的装备（如盾牌等非穿戴防具） */
      var trimNoPattern = TinkerTags.Items.TRIM_NO_PATTERN;
      var trimNoPatternTag = this.tag(trimNoPattern);

      /** 黑名单：禁止在默认部件回收配方中拆解的工具 */
      var unrecyclable = TinkerTags.Items.UNRECYCLABLE;
      var unrecyclableTag = this.tag(unrecyclable);

      /** 黑名单：禁止在默认打捞配方中打捞材料的工具 */
      var unsalvagable = TinkerTags.Items.UNSALVAGABLE;
      var unsalvagableTag = this.tag(unsalvagable);

      /** 黑名单：禁止进行任何材料替换（Material Swapping）的工具 */
      var unswappable = TinkerTags.Items.UNSWAPPABLE;
      var unswappableTag = this.tag(unswappable);

      /** 黑名单：禁止进行部件替换的工具 */
      var unswappableParts = TinkerTags.Items.UNSWAPPABLE_PARTS;
      var unswappablePartsTag = this.tag(unswappableParts);

      /** 黑名单：禁止进行消耗工具式材料替换的工具 */
      var unswappableTools = TinkerTags.Items.UNSWAPPABLE_TOOLS;
      var unswappableToolsTag = this.tag(unswappableTools);

      /** 镰刀类工具（通用 Tag，便于其他 Mod 识别收割逻辑） */
      var scythes = TinkerTags.Items.SCYTHES;
      var scythesTag = this.tag(scythes);

      STTItems.getListTools().forEach(tool ->
              this.addToolTags(tool,
                      modifiable,multipartTool
              )
      );
      ISSItem.getListTools().forEach(tool ->
              this.addOptionalToolTags(tool, modifiable, multipartTool));
      GoetyItem.getListTools().forEach(tool ->
              this.addOptionalToolTags(tool, modifiable, multipartTool));
      EBItem.getListTools().forEach(tool ->
              this.addOptionalToolTags(tool, modifiable, multipartTool));


      this.addToolTags(STTItems.swift_sword.get(),
              TinkerTags.Items.SMALL_TOOLS,
              TinkerTags.Items.MELEE_WEAPON,
              TinkerTags.Items.MELEE_PRIMARY,
              TinkerTags.Items.SWORD
      );

      this.addToolTags(STTItems.great_sword.get(),
              TinkerTags.Items.BROAD_TOOLS,
              TinkerTags.Items.MELEE_WEAPON,
              TinkerTags.Items.MELEE_PRIMARY,
              TinkerTags.Items.SWORD,
              TinkerTags.Items.AOE
      );
      this.addToolTags(STTItems.vampire_knife.get(),
              TinkerTags.Items.SMALL_TOOLS,
              TinkerTags.Items.MELEE_WEAPON
      );
      this.addToolTags(STTItems.blade_convergence.get(),
              TinkerTags.Items.MELEE_WEAPON,
              TinkerTags.Items.MELEE_PRIMARY,
              TinkerTags.Items.SWORD
      );
      this.addToolTags(STTItems.scythe.get(),
              TinkerTags.Items.BROAD_TOOLS,
              TinkerTags.Items.AOE,
              TinkerTags.Items.HARVEST,
              TinkerTags.Items.SCYTHES
      );
      this.addToolTags(STTItems.laser_gun.get(),
              TinkerTags.Items.MELEE_WEAPON,
              TinkerTags.Items.INTERACTABLE_RIGHT,
              TinkerTags.Items.INTERACTABLE_CHARGE
      );
      this.addToolTags(STTItems.revolver.get(),
              TinkerTags.Items.RANGED,
              TinkerTags.Items.LAUNCHERS,
              TinkerTags.Items.INTERACTABLE_RIGHT,
              TinkerTags.Items.INTERACTABLE_CHARGE
      );

      this.addToolTags(STTItems.tinker_bullet.get(),
              DURABILITY,
              MELEE,
              MELEE_PRIMARY,
              MELEE_WEAPON
      );
      this.addToolTags(STTItems.shuriken.get(),
              TinkerTags.Items.MELEE_PRIMARY,
              TinkerTags.Items.MELEE_WEAPON
      );
      this.addToolTags(STTItems.tinker_arrow.get(),
              TinkerTags.Items.AMMO,
              TinkerTags.Items.THROWN_AMMO
      );
      this.addToolTags(STTItems.tinker_charm.get(),
              embellishmentWood
      );
      this.addToolTags(STTItems.power_bank.get(),
              TinkerTags.Items.SPECIAL_TOOLS
      );
      this.addToolTags(STTItems.battle_flag.get(),
              TinkerTags.Items.MELEE_PRIMARY,
              TinkerTags.Items.MELEE_WEAPON
      );
      this.addToolTags(STTItems.yoyo.get(),
              TinkerTags.Items.MELEE_PRIMARY,
              TinkerTags.Items.MELEE_WEAPON,
              TinkerTags.Items.INTERACTABLE_RIGHT
      );
      this.addToolTags(STTItems.grappling_hook.get(),
              ranged,
              TinkerTags.Items.MELEE_PRIMARY,
              TinkerTags.Items.MELEE_WEAPON,
              TinkerTags.Items.INTERACTABLE_RIGHT
      );

      STTItems.embeddedArmor.forEach((type, armorItem) -> {
         this.addToolTags(armorItem,
                 armor, wornArmor
         );
         switch (type) {
            case HELMET -> this.addToolTags(armorItem, helmets);
            case CHESTPLATE -> this.addToolTags(armorItem, chestplates);
            case LEGGINGS -> this.addToolTags(armorItem, leggings);
            case BOOTS -> this.addToolTags(armorItem, boots);
         }
      });

      var curioTag = this.tag(STTTags.Items.CURIO_MODIFIABLE);
      curioTag.addTag(STTTags.Items.TINKER_CHARM);
      curioTag.add(STTItems.tinker_charm.get());
      curioTag.add(STTItems.power_bank.get());
      curioTag.addOptional(SakuraTinker.getResource("tinker_spell_book"));

      this.tag(STTTags.Items.TINKER_CHARM).add(STTItems.tinker_charm.get());
      this.tag(STTTags.Items.TINKER_SPELL_BOOK).addOptional(SakuraTinker.getResource("tinker_spell_book"));
      this.tag(TinkerTags.Items.MODIFIABLE).addTag(STTTags.Items.CURIO_MODIFIABLE);

      this.tag(MODIFIABLE).addTags(STTTags.Items.CURIO_MODIFIABLE);
   }

   private void addPartCastTags(CastItemObject cast, boolean own,
                                TagsProvider.TagAppender<Item> multiUseCasts,
                                TagsProvider.TagAppender<Item> singleUseCasts) {
      String path = cast.getId().getPath();
      String name = path.endsWith("_cast")
              ? path.substring(0, path.length() - "_cast".length()) : path;
      TagKey<Item> multiKey = TagKey.create(Registries.ITEM,
              ResourceLocation.fromNamespaceAndPath("tconstruct", "casts/multi_use/" + name));
      TagKey<Item> singleKey = TagKey.create(Registries.ITEM,
              ResourceLocation.fromNamespaceAndPath("tconstruct", "casts/single_use/" + name));
      var multiTag = this.tag(multiKey);
      var singleTag = this.tag(singleKey);
      if (own) {
         multiTag.add(cast.get());
         singleTag.add(cast.getSand()).add(cast.getRedSand());
      } else {
         multiTag.addOptional(cast.getId());
         singleTag.addOptional(cast.getSand().builtInRegistryHolder().key().location())
                 .addOptional(cast.getRedSand().builtInRegistryHolder().key().location());
      }
      multiUseCasts.addTag(multiKey);
      singleUseCasts.addTag(singleKey);


   }

   @SafeVarargs
   private final void addToolTags(ItemLike tool, TagKey<Item>... tags) {
      Item item = tool.asItem();
      for (TagKey<Item> tag : tags) {
         this.tag(tag).add(item);
      }
   }

   @SafeVarargs
   private final void addOptionalToolTags(ItemLike tool, TagKey<Item>... tags) {
      ResourceLocation id = tool.asItem().builtInRegistryHolder().key().location();
      for (TagKey<Item> tag : tags) {
         this.tag(tag).addOptional(id);
      }
   }
}
