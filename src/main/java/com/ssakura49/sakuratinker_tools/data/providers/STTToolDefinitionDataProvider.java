package com.ssakura49.sakuratinker_tools.data.providers;

import com.ssakura49.sakuratinker_tools.compat.extrabotany.init.EBItem;
import com.ssakura49.sakuratinker_tools.compat.extrabotany.tool.definition.EBToolDefinitions;
import com.ssakura49.sakuratinker_tools.compat.goety.init.GoetyItem;
import com.ssakura49.sakuratinker_tools.compat.goety.tool.definition.GoetyToolDefinitions;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.init.ISSItem;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.tool.definitions.ISSToolDefinitions;
import com.ssakura49.sakuratinker_tools.content.tool.STArmorDefinitions;
import com.ssakura49.sakuratinker_tools.content.tool.STToolDefinitions;
import com.ssakura49.sakuratinker_tools.data.STTModifierIds;
import com.ssakura49.sakuratinker_tools.init.STTItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ArmorItem.Type;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.data.tinkering.AbstractToolDefinitionDataProvider;
import slimeknights.tconstruct.library.materials.RandomMaterial;
import slimeknights.tconstruct.library.tools.SlotType;
import slimeknights.tconstruct.library.tools.definition.module.ToolModule;
import slimeknights.tconstruct.library.tools.definition.module.build.MultiplyStatsModule;
import slimeknights.tconstruct.library.tools.definition.module.build.SetStatsModule;
import slimeknights.tconstruct.library.tools.definition.module.build.ToolActionsModule;
import slimeknights.tconstruct.library.tools.definition.module.build.ToolSlotsModule;
import slimeknights.tconstruct.library.tools.definition.module.build.ToolTraitsModule;
import slimeknights.tconstruct.library.tools.definition.module.display.FixedMaterialToolName;
import slimeknights.tconstruct.library.tools.definition.module.display.MaterialToolNameModule;
import slimeknights.tconstruct.library.tools.definition.module.material.DefaultMaterialsModule;
import slimeknights.tconstruct.library.tools.definition.module.material.PartStatsModule;
import slimeknights.tconstruct.library.tools.definition.module.mining.IsEffectiveModule;
import slimeknights.tconstruct.library.tools.definition.module.mining.MiningSpeedModifierModule;
import slimeknights.tconstruct.library.tools.definition.module.weapon.SweepWeaponAttack;
import slimeknights.tconstruct.library.tools.nbt.MultiplierNBT;
import slimeknights.tconstruct.library.tools.nbt.StatsNBT;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.tools.TinkerToolParts;
import slimeknights.tconstruct.tools.data.ModifierIds;

public class STTToolDefinitionDataProvider extends AbstractToolDefinitionDataProvider {
   public STTToolDefinitionDataProvider(PackOutput packOutput) {
      super(packOutput, "sakuratinker");
   }

   @Override
   protected void addToolDefinitions() {
      RandomMaterial tier1Material = RandomMaterial.random().tier(1).build();
      RandomMaterial anyMaterial = RandomMaterial.random().allowHidden().build();
      RandomMaterial nonHiddenMaterial = RandomMaterial.random().build();
      DefaultMaterialsModule defaultTwoParts = DefaultMaterialsModule.builder().material(new RandomMaterial[]{tier1Material, tier1Material}).build();
      DefaultMaterialsModule defaultThreeParts = DefaultMaterialsModule.builder().material(new RandomMaterial[]{tier1Material, tier1Material, tier1Material}).build();
      DefaultMaterialsModule defaultFourParts = DefaultMaterialsModule.builder().material(new RandomMaterial[]{tier1Material, tier1Material, tier1Material, tier1Material}).build();
      DefaultMaterialsModule ancientTwoParts = DefaultMaterialsModule.builder().material(new RandomMaterial[]{anyMaterial, anyMaterial}).build();
      DefaultMaterialsModule ancientThreeParts = DefaultMaterialsModule.builder().material(new RandomMaterial[]{anyMaterial, anyMaterial, anyMaterial}).build();
      DefaultMaterialsModule defaultFiveParts = DefaultMaterialsModule.builder().material(new RandomMaterial[]{tier1Material, tier1Material, tier1Material, tier1Material, tier1Material}).build();
      DefaultMaterialsModule embeddedArmorMaterials = DefaultMaterialsModule.builder().material(new RandomMaterial[]{nonHiddenMaterial, nonHiddenMaterial, nonHiddenMaterial}).build();
      ToolModule armorSlots = ToolSlotsModule.builder().slots(SlotType.UPGRADE, 1).build();
      PartStatsModule.Builder greatSwordParts = PartStatsModule.parts().part(STTItems.great_blade, 0.75F).part(TinkerToolParts.largePlate, 0.75F).part(TinkerToolParts.largePlate, 0.75F).part(TinkerToolParts.toughBinding, 0.25F).part(TinkerToolParts.toughHandle, 0.5F);
      this.define(STToolDefinitions.GREAT_SWORD).module(greatSwordParts.build()).module(defaultFiveParts).module(new SetStatsModule(StatsNBT.builder().set(ToolStats.ATTACK_DAMAGE, 6.0F).set(ToolStats.ATTACK_SPEED, 0.5F).build())).module(new MultiplyStatsModule(MultiplierNBT.builder().set(ToolStats.ATTACK_DAMAGE, 1.8F).set(ToolStats.DURABILITY, 4.0F).build())).module(ToolSlotsModule.builder().slots(SlotType.ABILITY, 1).slots(SlotType.UPGRADE, 2).build()).module(ToolTraitsModule.builder().trait(ModifierIds.reach, 1).trait(STTModifierIds.HEAVY, 1).build()).module(ToolActionsModule.of(new ToolAction[]{ToolActions.SWORD_DIG})).module(IsEffectiveModule.tag(BlockTags.create(ResourceLocation.fromNamespaceAndPath("forge", "mineable/sword")))).module(MiningSpeedModifierModule.blocks(7.5F, new Block[]{Blocks.COBWEB})).module(new SweepWeaponAttack(2.0F)).module(FixedMaterialToolName.FIRST);
      this.define(STToolDefinitions.BATTLE_FLAG).module(PartStatsModule.parts().part(TinkerToolParts.smallBlade, 1.0F).part(STTItems.flag, 1.0F).part(STTItems.flag, 1.0F).part(TinkerToolParts.toughHandle, 1.0F).part(TinkerToolParts.toughHandle, 1.0F).build()).module(defaultFourParts).module(new SetStatsModule(StatsNBT.builder().set(ToolStats.ATTACK_DAMAGE, 3.0F).set(ToolStats.ATTACK_SPEED, 0.5F).build())).module(new MultiplyStatsModule(MultiplierNBT.builder().set(ToolStats.ATTACK_DAMAGE, 0.5F).set(ToolStats.DURABILITY, 1.2F).build())).module(ToolSlotsModule.builder().slots(SlotType.ABILITY, 1).slots(SlotType.UPGRADE, 2).build()).module(ToolTraitsModule.builder().trait(ModifierIds.reach, 1).build()).module(ToolActionsModule.of(new ToolAction[]{ToolActions.SWORD_DIG})).module(IsEffectiveModule.tag(BlockTags.create(ResourceLocation.fromNamespaceAndPath("forge", "mineable/weapon")))).module(FixedMaterialToolName.FIRST);
      this.define(STToolDefinitions.BLADE_CONVERGENCE).module(PartStatsModule.parts().part(TinkerToolParts.smallBlade, 1.0F).part(TinkerToolParts.largePlate, 1.0F).part(STTItems.range_core, 1.0F).part(TinkerToolParts.toolHandle, 1.0F).build()).module(defaultThreeParts).module(new SetStatsModule(StatsNBT.builder().set(ToolStats.ATTACK_DAMAGE, 3.0F).set(ToolStats.ATTACK_SPEED, 2.0F).build())).module(new MultiplyStatsModule(MultiplierNBT.builder().set(ToolStats.ATTACK_DAMAGE, 1.5F).set(ToolStats.DURABILITY, 2.0F).build())).module(ToolSlotsModule.builder().slots(SlotType.ABILITY, 1).slots(SlotType.UPGRADE, 3).build()).module(ToolTraitsModule.builder().trait(STTModifierIds.BLADE_CONVERGENCE, 1).build()).module(ToolActionsModule.of(new ToolAction[]{ToolActions.SWORD_DIG})).module(IsEffectiveModule.tag(BlockTags.create(ResourceLocation.fromNamespaceAndPath("forge", "mineable/sword")))).module(FixedMaterialToolName.FIRST);
      this.defineArmor(STArmorDefinitions.EMBEDDED).modules((slots) -> PartStatsModule.armor(slots).part(TinkerToolParts.plating, 1.0F).part(TinkerToolParts.plating, 1.0F).part(TinkerToolParts.maille, 1.0F)).module(embeddedArmorMaterials).module(Type.CHESTPLATE, new MultiplyStatsModule(MultiplierNBT.builder().set(ToolStats.ATTACK_DAMAGE, 0.4F).build())).module(armorSlots).module(FixedMaterialToolName.FIRST);
      this.define(STToolDefinitions.HOOK).module(PartStatsModule.parts().part(TinkerToolParts.smallBlade, 1.0F).part(TinkerToolParts.bowLimb, 1.0F).part(TinkerToolParts.bowLimb, 1.0F).part(TinkerToolParts.bowstring, 1.0F).part(TinkerToolParts.toughHandle, 1.0F).build()).module(defaultThreeParts).module(new SetStatsModule(StatsNBT.builder().set(ToolStats.ATTACK_DAMAGE, 2.0F).set(ToolStats.ATTACK_SPEED, 1.0F).build())).module(new MultiplyStatsModule(MultiplierNBT.builder().set(ToolStats.ATTACK_DAMAGE, 1.4F).set(ToolStats.DURABILITY, 1.0F).build())).module(ToolSlotsModule.builder().slots(SlotType.ABILITY, 1).slots(SlotType.UPGRADE, 2).build()).module(FixedMaterialToolName.FIRST);
      this.define(STToolDefinitions.LASER_GUN).module(PartStatsModule.parts().part(STTItems.barrel, 0.75F).part(STTItems.energy_unit, 1.0F).part(STTItems.laser_medium, 1.0F).part(TinkerToolParts.toughHandle, 1.0F).build()).module(defaultFourParts).module(new SetStatsModule(StatsNBT.builder().set(ToolStats.DURABILITY, 100.0F).set(ToolStats.ATTACK_DAMAGE, 1.8F).set(ToolStats.ATTACK_SPEED, 1.1F).build())).module(new MultiplyStatsModule(MultiplierNBT.builder().set(ToolStats.DURABILITY, 1.2F).build())).module(ToolSlotsModule.builder().slots(SlotType.ABILITY, 1).slots(SlotType.UPGRADE, 3).build()).module(ToolTraitsModule.builder().trait(STTModifierIds.ENERGY_DISPLAY, 1).build()).module(FixedMaterialToolName.FIRST);
      this.define(STToolDefinitions.POWER_BANK).module(PartStatsModule.parts().part(STTItems.shell, 1.0F).part(STTItems.energy_unit, 2.0F).part(STTItems.energy_unit, 2.0F).part(STTItems.energy_unit, 2.0F).build()).module(defaultFourParts).module(new SetStatsModule(StatsNBT.builder().set(ToolStats.ATTACK_DAMAGE, 1.0F).build())).module(new MultiplyStatsModule(MultiplierNBT.builder().set(ToolStats.ATTACK_DAMAGE, 0.5F).build())).module(ToolSlotsModule.builder().slots(SlotType.ABILITY, 1).slots(SlotType.UPGRADE, 2).build()).module(ToolTraitsModule.builder().trait(STTModifierIds.ENERGY_DISPLAY, 1).trait(STTModifierIds.ENERGY_SURGE, 3).build()).module(MaterialToolNameModule.ALL);
      this.define(STToolDefinitions.TINKER_REVOLVER).module(PartStatsModule.parts().part(TinkerToolParts.bowLimb).part(TinkerToolParts.bowGrip).part(TinkerToolParts.toolHandle).build()).module(defaultThreeParts).module(new SetStatsModule(StatsNBT.builder().set(ToolStats.ATTACK_DAMAGE, 1.0F).set(ToolStats.ATTACK_SPEED, 1.25F).build())).module(new MultiplyStatsModule(MultiplierNBT.builder().set(ToolStats.ATTACK_SPEED, 1.1F).set(ToolStats.DURABILITY, 2.0F).build())).module(ToolSlotsModule.builder().slots(SlotType.ABILITY, 1).slots(SlotType.UPGRADE, 3).build()).module(ToolTraitsModule.builder().trait(STTModifierIds.REVOLVER_BULLET, 1).build()).module(FixedMaterialToolName.FIRST);
      this.define(STToolDefinitions.SCYTHE).module(PartStatsModule.parts().part(STTItems.scythe_blade, 1.0F).part(TinkerToolParts.largePlate, 1.0F).part(TinkerToolParts.toughBinding, 1.0F).part(TinkerToolParts.toughHandle, 1.0F).build()).module(defaultFourParts).module(new SetStatsModule(StatsNBT.builder().set(ToolStats.ATTACK_DAMAGE, 4.0F).set(ToolStats.ATTACK_SPEED, 1.8F).build())).module(new MultiplyStatsModule(MultiplierNBT.builder().set(ToolStats.ATTACK_DAMAGE, 0.4F).set(ToolStats.DURABILITY, 2.5F).set(ToolStats.ATTACK_SPEED, 0.6F).build())).module(ToolSlotsModule.builder().slots(SlotType.ABILITY, 1).slots(SlotType.UPGRADE, 2).build()).module(ToolTraitsModule.builder().trait(STTModifierIds.SCYTHE_SWEEPING, 1).build()).module(ToolActionsModule.of(new ToolAction[]{ToolActions.SWORD_DIG})).module(IsEffectiveModule.tag(BlockTags.create(ResourceLocation.fromNamespaceAndPath("forge", "mineable/sword")))).module(MiningSpeedModifierModule.blocks(7.5F, new Block[]{Blocks.COBWEB})).module(new SweepWeaponAttack(5.0F)).module(FixedMaterialToolName.FIRST);
      this.define(STToolDefinitions.SHURIKEN).module(PartStatsModule.parts().part(STTItems.blade).part(STTItems.blade).part(STTItems.blade).part(STTItems.blade).build()).module(defaultFourParts).module(new SetStatsModule(StatsNBT.builder().set(ToolStats.ATTACK_DAMAGE, 1.0F).set(ToolStats.ATTACK_SPEED, 1.5F).build())).module(new MultiplyStatsModule(MultiplierNBT.builder().set(ToolStats.DURABILITY, 0.05F).set(ToolStats.ATTACK_DAMAGE, 0.6F).build())).module(ToolSlotsModule.builder().slots(SlotType.ABILITY, 1).slots(SlotType.UPGRADE, 2).build()).module(FixedMaterialToolName.FIRST);
      this.define(STToolDefinitions.SWIFT_SWORD).module(PartStatsModule.parts().part(STTItems.swift_blade, 1.0F).part(STTItems.swift_guard, 1.0F).part(TinkerToolParts.toolBinding, 1.0F).part(TinkerToolParts.toolHandle, 1.0F).build()).module(defaultFourParts).module(new SetStatsModule(StatsNBT.builder().set(ToolStats.ATTACK_DAMAGE, 1.2F).set(ToolStats.ATTACK_SPEED, 2.5F).build())).module(new MultiplyStatsModule(MultiplierNBT.builder().set(ToolStats.ATTACK_DAMAGE, 0.6F).set(ToolStats.DURABILITY, 1.25F).build())).module(ToolSlotsModule.builder().slots(SlotType.ABILITY, 1).slots(SlotType.UPGRADE, 3).build()).module(ToolTraitsModule.builder().trait(STTModifierIds.SWIFT_SWORD_TECHNIQUE, 1).build()).module(ToolActionsModule.of(new ToolAction[]{ToolActions.SWORD_DIG})).module(IsEffectiveModule.tag(BlockTags.create(ResourceLocation.fromNamespaceAndPath("forge", "mineable/sword")))).module(MiningSpeedModifierModule.blocks(7.5F, new Block[]{Blocks.COBWEB})).module(new SweepWeaponAttack(1.0F)).module(FixedMaterialToolName.FIRST);
      this.define(STToolDefinitions.TINKER_ARROW).module(PartStatsModule.parts().part(STTItems.arrow_head, 1.0F).part(STTItems.arrow_shaft, 1.0F).part(STTItems.fletching, 1.0F).build()).module(defaultThreeParts).module(new SetStatsModule(StatsNBT.builder().set(ToolStats.ATTACK_DAMAGE, 0.8F).set(ToolStats.ATTACK_SPEED, 1.0F).build())).module(new MultiplyStatsModule(MultiplierNBT.builder().set(ToolStats.ATTACK_DAMAGE, 0.4F).set(ToolStats.DURABILITY, 0.2F).set(ToolStats.MINING_SPEED, 0.5F).build())).module(ToolSlotsModule.builder().slots(SlotType.UPGRADE, 2).slots(SlotType.ABILITY, 1).build()).module(FixedMaterialToolName.FIRST);
      this.define(STToolDefinitions.TINKER_BULLET).module(PartStatsModule.parts().part(TinkerToolParts.smallBlade, 1.0F).part(TinkerToolParts.smallBlade, 1.0F).build()).module(defaultTwoParts).module(new SetStatsModule(StatsNBT.builder().set(ToolStats.ATTACK_DAMAGE, 0.8F).set(ToolStats.DURABILITY, 0.1F).build())).module(new MultiplyStatsModule(MultiplierNBT.builder().set(ToolStats.ATTACK_DAMAGE, 0.4F).build())).module(ToolSlotsModule.builder().slots(SlotType.UPGRADE, 2).slots(SlotType.ABILITY, 1).build()).module(FixedMaterialToolName.FIRST);
      this.define(STToolDefinitions.TINKER_CHARM).module(PartStatsModule.parts().part(STTItems.charm_core, 1.0F).part(STTItems.charm_chain, 1.0F).build()).module(defaultTwoParts).module(new SetStatsModule(StatsNBT.builder().set(ToolStats.ATTACK_DAMAGE, 1.0F).set(ToolStats.ARMOR, 1.0F).build())).module(new MultiplyStatsModule(MultiplierNBT.builder().set(ToolStats.ATTACK_DAMAGE, 1.0F).build())).module(ToolSlotsModule.builder().slots(SlotType.ABILITY, 1).slots(SlotType.UPGRADE, 3).build()).module(MaterialToolNameModule.ALL);
      this.define(ISSToolDefinitions.TINKER_SPELL_BOOK)
              .module(PartStatsModule.parts()
                      .part(ISSItem.manu_script)
                      .part(ISSItem.envelope)
                      .part(ISSItem.book_mark)
                      .part(ISSItem.gutter).build())
              .module(defaultFourParts)
              .module(new SetStatsModule(StatsNBT.builder().set(ToolStats.ATTACK_DAMAGE, 1.5F).set(ToolStats.ATTACK_SPEED, 0.8F).build()))
              .module(ToolSlotsModule.builder().slots(SlotType.UPGRADE, 5).build())
              .module(ToolTraitsModule.builder().trait(STTModifierIds.ARCANE_TINKERING, 1).build())
              .module(MaterialToolNameModule.ALL);
      this.define(STToolDefinitions.VAMPIRE_KNIVES).module(PartStatsModule.parts().part(TinkerToolParts.smallBlade, 1.0F).part(TinkerToolParts.smallBlade, 1.0F).part(TinkerToolParts.smallBlade, 1.0F).part(STTItems.blade_box, 1.0F).build()).module(defaultFourParts).module(new SetStatsModule(StatsNBT.builder().set(ToolStats.ATTACK_DAMAGE, 1.2F).set(ToolStats.ATTACK_SPEED, 2.5F).build())).module(new MultiplyStatsModule(MultiplierNBT.builder().set(ToolStats.ATTACK_DAMAGE, 0.6F).set(ToolStats.DURABILITY, 1.25F).build())).module(ToolSlotsModule.builder().slots(SlotType.ABILITY, 1).slots(SlotType.UPGRADE, 3).build()).module(ToolTraitsModule.builder().trait(ModifierIds.silkyShears, 1).build()).module(ToolActionsModule.of(new ToolAction[]{ToolActions.SWORD_DIG})).module(IsEffectiveModule.tag(BlockTags.create(ResourceLocation.fromNamespaceAndPath("forge", "mineable/sword")))).module(MiningSpeedModifierModule.blocks(7.5F, new Block[]{Blocks.COBWEB})).module(new SweepWeaponAttack(1.0F)).module(FixedMaterialToolName.FIRST);
      this.define(STToolDefinitions.YOYO).module(PartStatsModule.parts().part(STTItems.yoyo_body, 1.0F).part(STTItems.yoyo_body, 1.0F).part(STTItems.axle, 1.0F).part(STTItems.chord, 1.0F).part(STTItems.yoyo_ring, 1.0F).build()).module(defaultFiveParts).module(new SetStatsModule(StatsNBT.builder().set(ToolStats.ATTACK_DAMAGE, 0.5F).set(ToolStats.ATTACK_SPEED, 0.8F).build())).module(new MultiplyStatsModule(MultiplierNBT.builder().set(ToolStats.ATTACK_DAMAGE, 0.45F).set(ToolStats.DURABILITY, 1.2F).set(ToolStats.MINING_SPEED, 1.0F).build())).module(ToolSlotsModule.builder().slots(SlotType.UPGRADE, 2).slots(SlotType.ABILITY, 1).build()).module(FixedMaterialToolName.FIRST);
      this.define(GoetyToolDefinitions.TINKER_WAND)
              .module(PartStatsModule.parts()
                      .part(GoetyItem.soul_gatherer).part(TinkerToolParts.largePlate).part(TinkerToolParts.toughHandle).part(TinkerToolParts.toughBinding).build())
              .module(defaultFourParts)
              .module(new SetStatsModule(StatsNBT.builder().set(ToolStats.ATTACK_DAMAGE, 1.5F).set(ToolStats.ATTACK_SPEED, 0.8F).build()))
              .module(ToolSlotsModule.builder().slots(SlotType.UPGRADE, 3).build())
              .module(ToolSlotsModule.builder().slots(SlotType.ABILITY, 2).build())
              .module(ToolTraitsModule.builder().trait(STTModifierIds.G_SOUL_GETTER,1).build())
              .module(MaterialToolNameModule.ALL);
      this.define(EBToolDefinitions.FIRST_FRACTAL)
              .module(PartStatsModule.parts()
                      .part(TinkerToolParts.broadBlade)
                      .part(EBItem.phantom_core)
                      .part(TinkerToolParts.toughBinding)
                      .part(TinkerToolParts.toolHandle)
                      .build())
              .module(defaultFourParts)
              .module(new SetStatsModule(StatsNBT.builder()
                      .set(ToolStats.ATTACK_DAMAGE, 5f)
                      .set(ToolStats.ATTACK_SPEED,1.0f).build()))
              .module(new MultiplyStatsModule(MultiplierNBT.builder()
                      .set(ToolStats.ATTACK_DAMAGE, 1.6f)
                      .set(ToolStats.MINING_SPEED, 0.4f)
                      .set(ToolStats.DURABILITY, 1.6f).build()))
              .module(ToolSlotsModule.builder().slots(SlotType.UPGRADE, 3).build())
              .module(ToolSlotsModule.builder().slots(SlotType.ABILITY, 2).build())
              .module(ToolTraitsModule.builder().trait(STTModifierIds.FIRST_FRACTAL,1).build())
              .module(MaterialToolNameModule.ALL);
   }

   public @NotNull String getName() {
      return "Sakura Tinker-Tool Definition Data Generator";
   }
}
