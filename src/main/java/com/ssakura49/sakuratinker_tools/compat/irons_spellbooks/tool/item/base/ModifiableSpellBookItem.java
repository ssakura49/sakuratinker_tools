package com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.tool.item.base;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.api.ITinkerSpell;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.helper.SchoolAttributeManager;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.library.tool.stat.ISSToolStats;
import com.ssakura49.tinkercuriolib.hook.TCLibHooks;
import com.ssakura49.tinkercuriolib.hook.behavior.CurioAttributeModifierHook;
import com.ssakura49.tinkercuriolib.hook.interation.CurioInventoryTickModifierHook;
import io.redspace.ironsspellbooks.api.item.ISpellbook;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.compat.Curios;
import io.redspace.ironsspellbooks.item.ILecternPlaceable;
import io.redspace.ironsspellbooks.item.SpellBook;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import io.redspace.ironsspellbooks.util.MinecraftInstanceHelper;
import io.redspace.ironsspellbooks.util.TooltipsUtils;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.client.SafeClientAccess;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.hook.behavior.AttributesModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.behavior.EnchantmentModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.build.RarityModule;
import slimeknights.tconstruct.library.tools.IndestructibleItemEntity;
import slimeknights.tconstruct.library.tools.capability.ToolCapabilityProvider;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.definition.module.display.ToolNameHook;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.helper.ToolBuildHandler;
import slimeknights.tconstruct.library.tools.helper.TooltipUtil;
import slimeknights.tconstruct.library.tools.item.IModifiableDisplay;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.StatsNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.FloatToolStat;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class ModifiableSpellBookItem extends SpellBook implements IModifiableDisplay, ICurioItem, ISpellbook, IPresetSpellContainer, ILecternPlaceable, ITinkerSpell {
   private final ToolDefinition toolDefinition;
   private ItemStack toolForRendering;
   private final int maxStackSize;
   @Getter
   protected final int maxSpellSlots;

   public ModifiableSpellBookItem(int maxSpellSlots, Item.Properties properties, ToolDefinition toolDefinition) {
      this(maxSpellSlots, properties, toolDefinition, 1);
   }

   public ModifiableSpellBookItem(int maxSpellSlots, Item.Properties properties, ToolDefinition toolDefinition, int maxStackSize) {
      super(maxSpellSlots, (new Item.Properties()).stacksTo(maxStackSize));
      this.toolDefinition = toolDefinition;
      this.maxSpellSlots = maxSpellSlots;
      this.maxStackSize = maxStackSize;
   }

    public boolean isUnique() {
      return false;
   }

   public void appendHoverText(@NotNull ItemStack itemStack, Level context, @NotNull List<Component> lines, @NotNull TooltipFlag flag) {
      if (this.isUnique()) {
         lines.add(Component.translatable("tooltip.irons_spellbooks.spellbook_rarity", new Object[]{Component.translatable("tooltip.irons_spellbooks.spellbook_unique").withStyle(TooltipsUtils.UNIQUE_STYLE)}).withStyle(ChatFormatting.GRAY));
      }

      Player player = MinecraftInstanceHelper.getPlayer();
      if (player != null && ISpellContainer.isSpellContainer(itemStack)) {
         ISpellContainer spellList = ISpellContainer.get(itemStack);
         lines.add(Component.translatable("tooltip.irons_spellbooks.spellbook_spell_count", new Object[]{spellList.getMaxSpellCount()}).withStyle(ChatFormatting.GRAY));
         List<SpellSlot> activeSpellSlots = spellList.getActiveSpells();
         if (!activeSpellSlots.isEmpty()) {
            lines.add(Component.empty());
            lines.add(Component.translatable("tooltip.irons_spellbooks.press_to_cast", new Object[]{Component.keybind("key.irons_spellbooks.spellbook_cast")}).withStyle(ChatFormatting.GOLD));
            lines.add(Component.empty());
            lines.add(Component.translatable("tooltip.irons_spellbooks.spellbook_tooltip").withStyle(ChatFormatting.GRAY));
            SpellSelectionManager spellSelectionManager = ClientMagicData.getSpellSelectionManager();

            for(int i = 0; i < activeSpellSlots.size(); ++i) {
               MutableComponent spellText = TooltipsUtils.getTitleComponent(((SpellSlot)activeSpellSlots.get(i)).spellData(), (LocalPlayer)player).setStyle(Style.EMPTY);
               SpellSelectionManager.SelectionOption option = spellSelectionManager.getSpellSlot(spellSelectionManager.getSelectionIndex());
               if (MinecraftInstanceHelper.getPlayer() != null && Utils.getPlayerSpellbookStack(MinecraftInstanceHelper.getPlayer()) == itemStack && option != null && option.slot.equals(Curios.SPELLBOOK_SLOT) && option.slotIndex == i) {
                  List<MutableComponent> shiftMessage = TooltipsUtils.formatActiveSpellTooltip(itemStack, spellSelectionManager.getSelectedSpellData(), CastSource.SPELLBOOK, (LocalPlayer)player);
                  shiftMessage.remove(0);
                  TooltipsUtils.addShiftTooltip(lines, Component.literal("> ").append(spellText).withStyle(ChatFormatting.YELLOW), shiftMessage.stream().map((component) -> Component.literal(" ").append(component)).collect(Collectors.toList()));
               } else {
                  lines.add(Component.literal(" ").append(spellText.withStyle(Style.EMPTY.withColor(8947966))));
               }
            }
         }
      }

      TooltipUtil.addInformation(this, itemStack, context, lines, SafeClientAccess.getTooltipKey(), flag);
   }

   public int getMaxStackSize(ItemStack stack) {
      return stack.isDamaged() ? 1 : this.maxStackSize;
   }

   public boolean isEnchantable(@NotNull ItemStack stack) {
      return false;
   }

   public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
      return false;
   }

   @Override
   public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
      return enchantment.isCurse() && super.canApplyAtEnchantingTable(stack, enchantment);
   }

   public int getEnchantmentLevel(ItemStack stack, Enchantment enchantment) {
      return EnchantmentModifierHook.getEnchantmentLevel(stack, enchantment);
   }

   public Map<Enchantment, Integer> getAllEnchantments(ItemStack stack) {
      return EnchantmentModifierHook.getAllEnchantments(stack);
   }

   @Nullable
   public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
      this.initializeSpellContainer(stack);
      return new ToolCapabilityProvider(stack);
   }

   public void tick(@NotNull ItemStack stack, @NotNull Level worldIn, @NotNull Entity entityIn, int itemSlot, boolean isSelected) {
      InventoryTickModifierHook.heldInventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
   }

   public void curioTick(SlotContext slotContext, ItemStack stack) {
      ToolStack tool = ToolStack.from(stack);

      for(ModifierEntry entry : tool.getModifierList()) {
         CurioInventoryTickModifierHook hook = (CurioInventoryTickModifierHook)entry.getHook(TCLibHooks.CURIO_TICK);
         hook.onCurioTick(tool, entry, slotContext, slotContext.entity(), stack);
      }

   }

   public @NotNull Multimap<Attribute, AttributeModifier> getAttributeModifiers(@NotNull IToolStackView tool, @NotNull EquipmentSlot slot) {
      return AttributesModifierHook.getHeldAttributeModifiers(tool, slot);
   }

   public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
      CompoundTag nbt = stack.getTag();
      return nbt != null ? this.getAttributeModifiers((IToolStackView)ToolStack.from(stack), (EquipmentSlot)slot) : ImmutableMultimap.of();
   }

   public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
      Multimap<Attribute, AttributeModifier> map = LinkedHashMultimap.create();
      ToolStack toolStack = ToolStack.from(stack);
      StatsNBT toolStats = toolStack.getStats();
      map.put((Attribute)AttributeRegistry.MAX_MANA.get(), new AttributeModifier(uuid, "max_mana_bonus", (double)(Float)toolStats.get(ISSToolStats.MANA_VALUE), Operation.ADDITION));
      map.put((Attribute)AttributeRegistry.MANA_REGEN.get(), new AttributeModifier(uuid, "mana_regen_bonus", (double)(Float)toolStats.get(ISSToolStats.MANA_REGEN), Operation.MULTIPLY_TOTAL));
      map.put((Attribute)AttributeRegistry.CAST_TIME_REDUCTION.get(), new AttributeModifier(uuid, "cast_time_reduce_bonus", (double)(Float)toolStats.get(ISSToolStats.CAST_TIME_REDUCE), Operation.MULTIPLY_TOTAL));

      for(Map.Entry<SchoolType, FloatToolStat> entry : ISSToolStats.SCHOOL_BONUS.entrySet()) {
         SchoolType school = (SchoolType)entry.getKey();
         FloatToolStat stat = (FloatToolStat)entry.getValue();
         float bonus = (Float)toolStats.get(stat);
         if (school != null && bonus != 0.0F) {
            Attribute spellPower = this.getSpellPowerForSchool(school);
             map.put(spellPower, new AttributeModifier(uuid, school.getId().getPath() + "_power_bonus", (double) bonus, Operation.ADDITION));
         }
      }

      for(ModifierEntry entry : toolStack.getModifierList()) {
         CurioAttributeModifierHook hook = (CurioAttributeModifierHook)entry.getHook(TCLibHooks.CURIO_ATTRIBUTE);
         Objects.requireNonNull(map);
         hook.modifyCurioAttribute(toolStack, entry, slotContext, uuid, map::put);
      }

      return map;
   }

   private Attribute getSpellPowerForSchool(SchoolType school) {
      return SchoolAttributeManager.getPowerAttribute(school);
   }

   private Attribute getMagicResistForSchool(SchoolType school) {
      if (school == SchoolRegistry.FIRE.get()) {
         return (Attribute)AttributeRegistry.FIRE_MAGIC_RESIST.get();
      } else if (school == SchoolRegistry.ICE.get()) {
         return (Attribute)AttributeRegistry.ICE_MAGIC_RESIST.get();
      } else if (school == SchoolRegistry.LIGHTNING.get()) {
         return (Attribute)AttributeRegistry.LIGHTNING_MAGIC_RESIST.get();
      } else if (school == SchoolRegistry.HOLY.get()) {
         return (Attribute)AttributeRegistry.HOLY_MAGIC_RESIST.get();
      } else if (school == SchoolRegistry.ENDER.get()) {
         return (Attribute)AttributeRegistry.ENDER_MAGIC_RESIST.get();
      } else if (school == SchoolRegistry.BLOOD.get()) {
         return (Attribute)AttributeRegistry.BLOOD_MAGIC_RESIST.get();
      } else if (school == SchoolRegistry.EVOCATION.get()) {
         return (Attribute)AttributeRegistry.EVOCATION_MAGIC_RESIST.get();
      } else if (school == SchoolRegistry.NATURE.get()) {
         return (Attribute)AttributeRegistry.NATURE_MAGIC_RESIST.get();
      } else {
         return school == SchoolRegistry.ELDRITCH.get() ? (Attribute)AttributeRegistry.ELDRITCH_MAGIC_RESIST.get() : null;
      }
   }

   public void verifyTagAfterLoad(@NotNull CompoundTag nbt) {
      ToolStack.verifyTag(this, nbt, this.getToolDefinition());
   }

   public void onCraftedBy(@NotNull ItemStack stack, @NotNull Level worldIn, @NotNull Player playerIn) {
      ToolStack.ensureInitialized(stack, this.getToolDefinition());
   }

   public boolean isFoil(@NotNull ItemStack stack) {
      return ModifierUtil.checkVolatileFlag(stack, SHINY);
   }

   public @NotNull Rarity getRarity(@NotNull ItemStack stack) {
      return RarityModule.getRarity(stack);
   }

   public boolean hasCustomEntity(ItemStack stack) {
      return IndestructibleItemEntity.hasCustomEntity(stack);
   }

   public Entity createEntity(Level world, Entity original, ItemStack stack) {
      return IndestructibleItemEntity.createFrom(world, original, stack);
   }

   public boolean isRepairable(@NotNull ItemStack stack) {
      return false;
   }

   public boolean isValidRepairItem(@NotNull ItemStack pToRepair, @NotNull ItemStack pRepair) {
      return false;
   }

   public boolean canBeDepleted() {
      return true;
   }

   public @NotNull Component getName(@NotNull ItemStack stack) {
      return ToolNameHook.getName(getToolDefinition(), stack);
   }

   public int getDefaultTooltipHideFlags(ItemStack stack) {
      return TooltipUtil.getModifierHideFlags(this.getToolDefinition());
   }

   public @NotNull ItemStack getRenderTool() {
      if (this.toolForRendering == null) {
         this.toolForRendering = ToolBuildHandler.buildToolForRendering(this, this.getToolDefinition());
      }

      return this.toolForRendering;
   }

   @Override
   public void initializeSpellContainer(ItemStack itemStack) {
      this.syncSpellContainer(itemStack);
   }

   /**
    * Keeps the stored spell container size in sync with the SPELL_SLOT tool stat.
    * Iron's Spellbooks calls this from the ItemStack constructor, so existing books pick up stat changes too.
    */
   public void syncSpellContainer(ItemStack itemStack) {
      if (itemStack == null || itemStack.isEmpty()) {
         return;
      }
      boolean hasContainer = ISpellContainer.isSpellContainer(itemStack);
      ToolStack tool = ToolStack.from(itemStack);
      if (!ToolStack.isInitialized(itemStack)) {
         tool.rebuildStats();
         // stats could not be built yet (datapack/registry not loaded): keep the existing container
         if (!ToolStack.isInitialized(itemStack) && hasContainer) {
            return;
         }
      }
      int targetSlots = Math.max(1, tool.getStats().getInt(ISSToolStats.SPELL_SLOT));
      if (!hasContainer) {
         ISpellContainer.set(itemStack, ISpellContainer.create(targetSlots, true, true));
         return;
      }
      ISpellContainer container = ISpellContainer.get(itemStack);
      if (container == null) {
         ISpellContainer.set(itemStack, ISpellContainer.create(targetSlots, true, true));
         return;
      }
      if (container.getMaxSpellCount() == targetSlots) {
         return;
      }
      // never shrink below the highest inscribed slot, otherwise spells would be silently dropped
      int highestUsed = 0;
      for (SpellSlot slot : container.getActiveSpells()) {
         highestUsed = Math.max(highestUsed, slot.index() + 1);
      }
      ISpellContainerMutable mutable = container.mutableCopy();
      mutable.setMaxSpellCount(Math.max(targetSlots, highestUsed));
      ISpellContainer.set(itemStack, mutable.toImmutable());
   }

   public @NotNull ToolDefinition getToolDefinition() {
      return this.toolDefinition;
   }
}
