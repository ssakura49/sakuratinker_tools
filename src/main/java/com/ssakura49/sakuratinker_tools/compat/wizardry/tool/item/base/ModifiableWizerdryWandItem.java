package com.ssakura49.sakuratinker_tools.compat.wizardry.tool.item.base;

import com.binaris.wizardry.api.client.util.ClientUtils;
import com.binaris.wizardry.api.content.data.WizardData;
import com.binaris.wizardry.api.content.event.SpellCastEvent;
import com.binaris.wizardry.api.content.item.*;
import com.binaris.wizardry.api.content.spell.Element;
import com.binaris.wizardry.api.content.spell.Spell;
import com.binaris.wizardry.api.content.spell.SpellContexts;
import com.binaris.wizardry.api.content.spell.SpellTier;
import com.binaris.wizardry.api.content.spell.internal.CastContext;
import com.binaris.wizardry.api.content.spell.internal.PlayerCastContext;
import com.binaris.wizardry.api.content.spell.internal.SpellModifiers;
import com.binaris.wizardry.api.content.util.CastItemDataHelper;
import com.binaris.wizardry.api.content.util.CastItemUtils;
import com.binaris.wizardry.api.content.util.WorkbenchUtils;
import com.binaris.wizardry.content.item.ArcaneTomeItem;
import com.binaris.wizardry.content.item.WandItem;
import com.binaris.wizardry.core.ClientSpellSoundManager;
import com.binaris.wizardry.core.EBLogger;
import com.binaris.wizardry.core.config.EBServerConfig;
import com.binaris.wizardry.core.event.WizardryEventBus;
import com.binaris.wizardry.core.platform.Services;
import com.binaris.wizardry.setup.registries.*;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.ssakura49.sakuratinker_tools.STTConfig;
import com.ssakura49.sakuratinker_tools.compat.wizardry.utils.WandManaDataHelper;
import com.ssakura49.sakuratinker_tools.compat.wizardry.library.tier.TinkerWandTier;
import com.ssakura49.sakuratinker_tools.compat.wizardry.library.tier.TinkerWandTiers;
import com.ssakura49.sakuratinker_tools.compat.wizardry.library.tool.stats.WizardryToolStats;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.SafeClientAccess;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.client.item.ModifiableItemClientExtension;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.AttributesModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.behavior.EnchantmentModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.DurabilityDisplayModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.*;
import slimeknights.tconstruct.library.modifiers.modules.build.RarityModule;
import slimeknights.tconstruct.library.tools.IndestructibleItemEntity;
import slimeknights.tconstruct.library.tools.capability.ToolCapabilityProvider;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.definition.module.display.ToolNameHook;
import slimeknights.tconstruct.library.tools.definition.module.mining.IsEffectiveToolHook;
import slimeknights.tconstruct.library.tools.definition.module.mining.MiningSpeedToolHook;
import slimeknights.tconstruct.library.tools.helper.*;
import slimeknights.tconstruct.library.tools.item.IModifiableDisplay;
import slimeknights.tconstruct.library.tools.nbt.IModDataView;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.tools.TinkerToolActions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ModifiableWizerdryWandItem extends WandItem implements ICastItem, IManaItem, IWorkbenchItem, ICustomDamageItem, ITierValue, IElementValue, IModifiableDisplay {
    public static final int BASE_SPELL_SLOTS = 5;
    public static final int COOLDOWN_FORFEIT_TICKS = 60;
    public static final int MAX_USE_DURATION = 72000;
    private final SpellTier tier;
    @Getter
    private final Element element;

    private final ToolDefinition toolDefinition;
    private int maxStackSize;
    private ItemStack toolForRendering;

    public ModifiableWizerdryWandItem(Properties properties ,SpellTier tier, Element element, ToolDefinition definition, int maxStackSize) {
        super(tier,element);
        this.tier = tier;
        this.element = element;
        this.toolDefinition = definition;
        this.maxStackSize = maxStackSize;
    }

    public ModifiableWizerdryWandItem(Properties properties ,SpellTier tier, Element element, ToolDefinition definition){
        this(properties,tier,element,definition,1);
    }

    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        Spell spell = CastItemDataHelper.getCurrentSpell(stack);
        if (spell == Spells.NONE) {
            return InteractionResultHolder.pass(stack);
        } else {
            PlayerCastContext ctx = this.createContext(level, player, hand, 0, stack, spell);
            if (!this.canCastRequirements(stack, spell, ctx)) {
                return InteractionResultHolder.fail(stack);
            } else {
                int charge = CastItemUtils.calcCharge(spell, ctx.modifiers());
                if (spell.isInstantCast() && charge <= 0) {
                    if (this.canCast(stack, spell, ctx)) {
                        if (!level.isClientSide) {
                            this.consumeManaAndSetCooldown(stack, spell, player, ctx.modifiers());
                        }

                        if (this.cast(stack, spell, ctx)) {
                            return InteractionResultHolder.success(stack);
                        }
                    }

                    return InteractionResultHolder.fail(stack);
                } else {
                    return this.startCharging(level, player, hand, stack, charge, ctx);
                }
            }
        }
    }

    public void onUseTick(@NotNull Level level, @NotNull LivingEntity user, @NotNull ItemStack stack, int timeLeft) {
        ToolStack tool = ToolStack.from(stack);
        ModifierEntry activeModifier = GeneralInteractionModifierHook.getActiveModifier(tool);
        GeneralInteractionModifierHook hook = activeModifier.getHook(ModifierHooks.GENERAL_INTERACT);
        int duration = hook.getUseDuration(tool, activeModifier);
        for (ModifierEntry entry : tool.getModifiers()) {
            entry.getHook(ModifierHooks.TOOL_USING).onUsingTick(tool, entry, user, duration, timeLeft, activeModifier);
        }
        if (user instanceof Player player) {
            Spell spell = CastItemDataHelper.getCurrentSpell(stack);
            SpellModifiers modifiers = Services.OBJECT_DATA.getWizardData(player).getSpellModifiers();
            int useTick = stack.getUseDuration() - timeLeft;
            int charge = CastItemUtils.calcCharge(spell, modifiers);
            int castingTick;
            if (spell.isInstantCast()) {
                castingTick = useTick == charge ? 0 : -1;
            } else {
                castingTick = Math.max(0, useTick - charge);
            }

            PlayerCastContext ctx = this.createContext(level, player, user.getUsedItemHand(), castingTick, stack, spell);
            if (spell.isInstantCast()) {
                this.handleInstantSpellTick(ctx, spell, stack, useTick, charge);
            } else {
                this.handleContinuousSpellTick(ctx, spell, stack, useTick, charge);
            }

        }
    }

    public boolean canCast(ItemStack stack, Spell spell, PlayerCastContext ctx) {
        if (CastItemUtils.fireSpellCastEvent(SpellCastEvent.Sources.WAND, spell, ctx)) {
            CastItemUtils.applyCooldownForfeit(ctx.caster(), 60);
            return false;
        } else {
            return this.canCastRequirements(stack, spell, ctx);
        }
    }

    private boolean canCastRequirements(ItemStack stack, Spell spell, PlayerCastContext ctx) {
        int cost = CastItemUtils.calcCastCost(spell, ctx.modifiers());
        if (!spell.isInstantCast()) {
            cost = CastItemUtils.getDistributedCastCost(cost, ctx.castingTicks());
        }

        return cost <= this.getMana(stack) && spell.getTier().getLevel() <= this.getTier(stack).getLevel() && (CastItemDataHelper.getCurrentCooldown(stack, ctx.world().getGameTime()) == 0 || ctx.caster().isCreative());
    }

    public boolean cast(ItemStack stack, Spell spell, PlayerCastContext ctx) {
        if (ctx.world().isClientSide && spell.isInstantCast() && spell.requiresPacket()) {
            return false;
        } else if (!CastItemUtils.executeSpellCast(SpellCastEvent.Sources.WAND, spell, ctx)) {
            return false;
        } else {
            CastItemUtils.sendSpellCastPacket(ctx.caster(), spell, ctx);
            if (!spell.isInstantCast()) {
                ctx.caster().startUsingItem(ctx.hand());
            }

            this.handleProgression(ctx, spell, stack);
            if (spell.isInstantCast()) {
                CastItemUtils.trackSpellUsage(ctx.caster(), spell);
            }

            return true;
        }
    }

    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack stack, Player player, @NotNull LivingEntity interactionTarget, @NotNull InteractionHand usedHand) {
        if (player.isCrouching() && interactionTarget instanceof Player playerTarget) {
            WizardData data = Services.OBJECT_DATA.getWizardData(player);
            String string = data.toggleAlly(playerTarget) ? "item.ebwizardry.wand.add_ally" : "item.ebwizardry.wand.remove_ally";
            if (!player.level().isClientSide) {
                player.sendSystemMessage(Component.translatable(string, new Object[]{playerTarget.getName()}));
            }

            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.FAIL;
        }
    }

    public void releaseUsing(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity livingEntity, int timeCharged) {
        ToolStack tool = ToolStack.from(stack);
        ModifierEntry activeModifier = GeneralInteractionModifierHook.getActiveModifier(tool);
        GeneralInteractionModifierHook hook = activeModifier.getHook(ModifierHooks.GENERAL_INTERACT);
        int duration = hook.getUseDuration(tool, activeModifier);
        for (ModifierEntry entry : tool.getModifiers()) {
            entry.getHook(ModifierHooks.TOOL_USING).beforeReleaseUsing(tool, entry, livingEntity, duration, timeCharged, activeModifier);
        }
        hook.onStoppedUsing(tool, activeModifier, livingEntity, timeCharged);
        if (livingEntity instanceof Player player) {
            Spell spell = CastItemDataHelper.getCurrentSpell(stack);
            WizardData wizardData = Services.OBJECT_DATA.getWizardData(player);
            SpellModifiers modifiers = wizardData.getSpellModifiers();
            int castingTick = stack.getUseDuration() - timeCharged;
            int totalCost = CastItemUtils.calcCastCost(spell, modifiers);
            int accumulatedCost = CastItemUtils.getAccumulatedCastCost(spell, castingTick, totalCost);
            if (!spell.isInstantCast() && spell.getTier().getLevel() <= this.getTier(stack).getLevel()) {
                CastContext ctx = new CastContext(player.level(), player, castingTick, modifiers);
                WizardryEventBus.fireEvent(new SpellCastEvent.Finish(SpellCastEvent.Sources.WAND, spell, ctx));
                spell.endCast(ctx);
                if (!level.isClientSide) {
                    this.consumeMana(stack, accumulatedCost, player);
                    if (!player.isCreative()) {
                        CastItemDataHelper.setCurrentCooldown(stack, totalCost, level.getGameTime());
                    }

                    CastItemUtils.trackSpellUsage(player, spell);
                }
            }

        }
    }

    public boolean onApplyButtonPressed(Player player, Slot centre, Slot crystals, Slot upgrade, Slot[] spellBooks) {
        boolean changed = false;
        if (upgrade.hasItem()) {
            changed = this.applyUpgradeSlot(player, centre, upgrade);
        }

        changed |= WorkbenchUtils.applySpellBooks(centre, spellBooks, SpellContexts.WANDS);
        changed |= WorkbenchUtils.rechargeManaFromCrystals(centre, crystals);
        return changed;
    }

    public ItemStack applyUpgrade(@Nullable Player player, ItemStack wand, ItemStack upgrade) {
        if (upgrade.getItem() instanceof ArcaneTomeItem) {
            return this.applyTierUpgrade(player, wand, upgrade);
        } else {
            if (WandUpgrades.isWandUpgrade(upgrade.getItem())) {
                this.applySpecialUpgrade(player, wand, upgrade);
            }

            return wand;
        }
    }

    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level world, @NotNull Entity entity, int slot, boolean isHeldInMainHand) {
        if (!world.isClientSide && !this.isManaFull(stack) && world.getGameTime() % (long)(Integer) EBServerConfig.CONDENSER_TICK_INTERVAL.get() == 0L) {
            this.rechargeMana(stack, CastItemDataHelper.getUpgradeLevel(stack, (Item) EBItems.CONDENSER_UPGRADE.get()));
        }
        InventoryTickModifierHook.heldInventoryTick(stack, world, entity, slot, isHeldInMainHand);
    }

    protected void handleInstantSpellTick(PlayerCastContext ctx, Spell spell, ItemStack stack, int useTick, int charge) {
        if (useTick == charge) {
            if (this.canCast(stack, spell, ctx)) {
                if (!ctx.world().isClientSide) {
                    this.consumeManaAndSetCooldown(stack, spell, ctx.caster(), ctx.modifiers());
                }

                this.cast(stack, spell, ctx);
            }

            ctx.caster().stopUsingItem();
        }
    }

    private void handleContinuousSpellTick(PlayerCastContext ctx, Spell spell, ItemStack stack, int useTick, int charge) {
        if (useTick >= charge) {
            if (this.canCast(stack, spell, ctx)) {
                this.cast(stack, spell, ctx);
            } else {
                ctx.caster().stopUsingItem();
            }

        }
    }

    private InteractionResultHolder<ItemStack> startCharging(Level level, Player player, InteractionHand hand, ItemStack stack, int charge, PlayerCastContext ctx) {
        if (!player.isUsingItem()) {
            player.startUsingItem(hand);
            Services.OBJECT_DATA.getWizardData(player).setSpellModifiers(ctx.modifiers());
            if (charge > 0 && level.isClientSide) {
                ClientSpellSoundManager.playChargeSound(player);
            }

            return InteractionResultHolder.success(stack);
        } else {
            return InteractionResultHolder.fail(stack);
        }
    }

    protected PlayerCastContext createContext(Level level, Player player, InteractionHand hand, int tick, ItemStack stack, Spell spell) {
        SpellModifiers modifiers = tick == 0 ? CastItemUtils.calculateModifiers(stack, player, spell) : Services.OBJECT_DATA.getWizardData(player).getSpellModifiers();
        return new PlayerCastContext(level, player, hand, tick, modifiers);
    }

    private void consumeManaAndSetCooldown(ItemStack stack, Spell spell, Player player, SpellModifiers modifiers) {
        int cost = CastItemUtils.calcCastCost(spell, modifiers);
        this.consumeMana(stack, cost, player);
        if (!player.isCreative()) {
            CastItemDataHelper.setCurrentCooldown(stack, CastItemUtils.calcCastCooldown(spell, modifiers), player.level().getGameTime());
        }

    }

    protected boolean applyUpgradeSlot(Player player, Slot centre, Slot upgrade) {
        ItemStack original = centre.getItem().copy();
        centre.set(this.applyUpgrade(player, centre.getItem(), upgrade.getItem()));
        return !ItemStack.matches(centre.getItem(), original);
    }

    @Override
    protected ItemStack applyTierUpgrade(@Nullable Player player, ItemStack wand, ItemStack tomeStack) {
        SpellTier currentTier = this.getTier(wand);
        int currentIndex = TinkerWandTiers.indexOf(currentTier);
        if (currentIndex >= TinkerWandTiers.COUNT - 1) {
            return wand;
        }
        TinkerWandTier next = TinkerWandTiers.byIndex(currentIndex + 1);
        if (next == null || !(tomeStack.getItem() instanceof ArcaneTomeItem tomeItem)) {
            return wand;
        }
        if (tomeItem.getTier(tomeStack).getLevel() < next.getLevel()) {
            return wand;
        }
        int progression = CastItemDataHelper.getProgression(wand);
        if (player != null && progression < next.getProgression()) {
            return wand;
        }

        CastItemDataHelper.setProgression(wand, Math.max(0, progression - next.getProgression()));
        ToolStack.from(wand).getPersistentData().putString(TinkerWandTiers.TIER_KEY, next.getId().toString());
        if (player != null) {
            Services.OBJECT_DATA.getWizardData(player).setTierReached(currentTier);
        }
        tomeStack.shrink(1);
        return wand;
    }

    protected void applySpecialUpgrade(@Nullable Player player, ItemStack wand, ItemStack upgrade) {
        Item specialUpgrade = upgrade.getItem();
        int maxUpgrades = this.getTier(wand).getUpgradeLimit() + (this.element == Elements.MAGIC ? (Integer)EBServerConfig.NON_ELEMENTAL_UPGRADE_BONUS.get() : 0);
        if (CastItemDataHelper.getTotalUpgrades(wand) < maxUpgrades && CastItemDataHelper.getUpgradeLevel(wand, specialUpgrade) < (Integer)EBServerConfig.UPGRADE_STACK_LIMIT.get()) {
            int prevMana = this.getMana(wand);
            CastItemDataHelper.applyUpgrade(wand, specialUpgrade);
            if (specialUpgrade == EBItems.STORAGE_UPGRADE.get()) {
                this.setMana(wand, prevMana);
            } else if (specialUpgrade == EBItems.ATTUNEMENT_UPGRADE.get()) {
                this.expandSpellSlots(wand);
            }

            upgrade.shrink(1);
            if (player != null) {
                EBAdvancementTriggers.SPECIAL_UPGRADE.triggerFor(player);
                if (CastItemDataHelper.getTotalUpgrades(wand) == SpellTiers.MASTER.getUpgradeLimit()) {
                    EBAdvancementTriggers.MAX_OUT_WAND.triggerFor(player);
                }

            }
        }
    }

    protected void expandSpellSlots(ItemStack wand) {
        int newSlotCount = this.baseSpellSlots(wand)
                + CastItemDataHelper.getUpgradeLevel(wand, (Item) EBItems.ATTUNEMENT_UPGRADE.get());
        List<Spell> spells = CastItemDataHelper.getSpells(wand);
        Spell[] newSpells = new Spell[newSlotCount];

        for(int i = 0; i < newSpells.length; ++i) {
            newSpells[i] = i < spells.size() && spells.get(i) != null ? (Spell)spells.get(i) : Spells.NONE;
        }

        CastItemDataHelper.setSpells(wand, List.of(newSpells));
    }

    protected void handleProgression(PlayerCastContext ctx, Spell spell, ItemStack stack) {
        if (this.getTier(stack).getLevel() < SpellTiers.MASTER.getLevel()) {
            boolean shouldAddProgression = spell.isInstantCast() ? ctx.castingTicks() == 0 : ctx.castingTicks() % 20 == 0;
            if (shouldAddProgression) {
                EBLogger.info("Recent cast: " + Services.OBJECT_DATA.getWizardData(ctx.caster()).getRecentSpells(), new Object[0]);
                int progression = Math.max(1, CastItemUtils.calcCastProgression(spell, ctx.modifiers()));
                CastItemDataHelper.addProgression(stack, progression);
                this.checkLevelUp(ctx, stack, progression);
            }
        }
    }

    protected void checkLevelUp(PlayerCastContext ctx, ItemStack stack, int progression) {
        Player player = ctx.caster();
        SpellTier nextTier = this.getTier(stack).next();
        int excess = CastItemDataHelper.getProgression(stack) - nextTier.getProgression();
        if (excess >= 0 && excess < progression) {
            player.playSound((SoundEvent)EBSounds.ITEM_WAND_LEVELUP.get(), 1.25F, 1.0F);
            if (!player.level().isClientSide) {
                player.sendSystemMessage(Component.translatable("item.ebwizardry.wand.levelup", new Object[]{this.getName(stack), nextTier.getDescriptionFormatted()}));
            }
        }

    }

    public void onClearButtonPressed(Player player, Slot centre, Slot crystals, Slot upgrade, Slot[] spellBooks) {
        ItemStack stack = centre.getItem();
        if (stack.getOrCreateTag().contains("spells")) {
            List<Spell> spells = CastItemDataHelper.getSpells(stack);
            int expectedSlotCount = this.baseSpellSlots(stack)
                    + CastItemDataHelper.getUpgradeLevel(stack, (Item) EBItems.ATTUNEMENT_UPGRADE.get());
            if (spells.size() < expectedSlotCount) {
                spells = new ArrayList<>();
            }

            CastItemDataHelper.setSpells(stack, spells);
        }
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        Component materialName = ToolNameHook.getName(getToolDefinition(), stack);
        if (this.element != null) {
            materialName = Component.empty().append(materialName).withStyle(this.element.getColor());
        }
        return Component.empty()
                .append(this.getTier(stack).getDescriptionFormatted())
                .append(" ")
                .append(materialName);
    }

    public void selectNextSpell(ItemStack stack) {
        CastItemDataHelper.selectNextSpell(stack);
    }

    public void selectPreviousSpell(ItemStack stack) {
        CastItemDataHelper.selectPreviousSpell(stack);
    }

    public int getCustomMaxDamage(ItemStack stack) {
        return this.getMaxDamage(stack);
    }

    public void setCustomDamage(ItemStack stack, int damage) {
        this.setDamage(stack, damage);
    }



    public boolean canBreak(ItemStack stack) {
        return false;
    }

    public int getBarColor(ItemStack stack) {
        return ClientUtils.mixColor(16747518, 9318116, (float)stack.getDamageValue());
    }

    public int getUseDuration(@NotNull ItemStack stack) {
        ToolStack tool = ToolStack.from(stack);
        ModifierEntry activeModifier = GeneralInteractionModifierHook.getActiveModifier(tool);
        if (activeModifier != ModifierEntry.EMPTY) {
            return activeModifier.getHook(ModifierHooks.GENERAL_INTERACT).getUseDuration(tool, activeModifier);
        };
        return 72000;
    }

    public boolean isClearable() {
        return true;
    }

    public boolean showTooltip(ItemStack stack) {
        return true;
    }

    public boolean selectSpell(ItemStack stack, int index) {
        return CastItemDataHelper.setCurrentSpell(stack, index);
    }

    public int getCurrentCooldown(ItemStack stack, Level level) {
        return CastItemDataHelper.getCurrentCooldown(stack, level.getGameTime());
    }

    public int getCurrentMaxCooldown(ItemStack stack) {
        return CastItemDataHelper.getCurrentMaxCooldown(stack);
    }

    public boolean showSpellHUD(Player player, ItemStack stack) {
        return true;
    }

    public int getMana(ItemStack stack) {
        if (stack.getTag() == null) {
            return this.getManaCapacity(stack);
        }
        IModDataView data = ToolStack.from(stack).getPersistentData();
        if (!data.contains(WandManaDataHelper.MANA)) {
            return this.getManaCapacity(stack);
        }
        return Mth.clamp(data.getInt(WandManaDataHelper.MANA), 0, this.getManaCapacity(stack));
    }

    @Override
    public int getManaCapacity(ItemStack stack) {
        float capacity = getTier(stack).getMaxCharge();
        int result = Math.max(1, Math.round(capacity));
        if (stack.getTag() != null) {
            capacity += ToolStack.from(stack).getStats().get(WizardryToolStats.MANA_CAPACITY);
            capacity *= (float) (1.0D + STTConfig.COMPAT.WIZARDRY_MANA_STORAGE_INCREMENT.get()
                    * CastItemDataHelper.getUpgradeLevel(stack, EBItems.STORAGE_UPGRADE.get()));
            result = Math.max(1, Math.round(capacity));
            ToolStack.from(stack).getPersistentData().putInt(WandManaDataHelper.CAPACITY, result);
        }
        return result;
    }

    public void setMana(ItemStack stack, int mana) {
        int capacity = this.getManaCapacity(stack);
        ToolStack.from(stack).getPersistentData().putInt(WandManaDataHelper.MANA, Mth.clamp(mana, 0, capacity));
    }

    protected int baseSpellSlots(ItemStack stack) {
        return 5 + TinkerWandTiers.indexOf(this.getTier(stack)) / 2;
    }

    public int getSpellSlotCount(ItemStack stack) {
        return this.baseSpellSlots(stack)
                + CastItemDataHelper.getUpgradeLevel(stack, (Item) EBItems.ATTUNEMENT_UPGRADE.get());
    }

    public @NotNull Spell getCurrentSpell(ItemStack stack) {
        return CastItemDataHelper.getCurrentSpell(stack);
    }

    public @NotNull Spell getNextSpell(ItemStack stack) {
        return CastItemDataHelper.getNextSpell(stack);
    }

    public @NotNull Spell getPreviousSpell(ItemStack stack) {
        return CastItemDataHelper.getPreviousSpell(stack);
    }

    public Spell[] getSpells(ItemStack stack) {
        return CastItemDataHelper.getSpells(stack).toArray(new Spell[0]);
    }

    @Override
    public SpellTier getTier(ItemStack stack) {
        if (stack != null && stack.getTag() != null) {
            TinkerWandTier stored = TinkerWandTiers.byIdString(
                    ToolStack.from(stack).getPersistentData().getString(TinkerWandTiers.TIER_KEY));
            if (stored != null) {
                return stored;
            }
        }
        return TinkerWandTiers.first();
    }

    @Override
    public @NotNull ToolDefinition getToolDefinition() {
        return toolDefinition;
    }

    public int getMaxStackSize(ItemStack stack) {
        return stack.isDamaged() ? 1 : this.maxStackSize;
    }

    @Override
    public @NotNull ItemStack getRenderTool() {
        if (this.toolForRendering == null) {
            this.toolForRendering = ToolBuildHandler.buildToolForRendering(this, this.getToolDefinition());
        }

        return this.toolForRendering;
    }


    @Override
    public boolean isNotReplaceableByPickAction(ItemStack stack, Player player, int inventorySlot) {
        return true;
    }

    @javax.annotation.Nullable
    @Override
    public EquipmentSlot getEquipmentSlot(ItemStack stack) {
        if (stack.is(TinkerTags.Items.HELD_ARMOR)) {
            return EquipmentSlot.OFFHAND;
        }
        return null;
    }

    @Override
    public boolean isEnchantable(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment.isCurse() && super.canApplyAtEnchantingTable(stack, enchantment);
    }

    @Override
    public int getEnchantmentLevel(ItemStack stack, Enchantment enchantment) {
        return EnchantmentModifierHook.getEnchantmentLevel(stack, enchantment);
    }

    @Override
    public Map<Enchantment,Integer> getAllEnchantments(ItemStack stack) {
        return EnchantmentModifierHook.getAllEnchantments(stack);
    }

    @javax.annotation.Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @javax.annotation.Nullable CompoundTag nbt) {
        return new ToolCapabilityProvider(stack);
    }

    @Override
    public void verifyTagAfterLoad(@NotNull CompoundTag nbt) {
        ToolStack.verifyTag(this, nbt, getToolDefinition());
    }

    @Override
    public void onCraftedBy(@NotNull ItemStack stack, @NotNull Level worldIn, @NotNull Player playerIn) {
        ToolStack.ensureInitialized(stack, getToolDefinition());
    }


    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return ModifierUtil.checkVolatileFlag(stack, SHINY);
    }

    @Override
    public @NotNull Rarity getRarity(@NotNull ItemStack stack) {
        return RarityModule.getRarity(stack);
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return IndestructibleItemEntity.hasCustomEntity(stack);
    }

    @javax.annotation.Nullable
    @Override
    public Entity createEntity(Level world, Entity original, ItemStack stack) {
        return IndestructibleItemEntity.createFrom(world, original, stack);
    }

    @Override
    public boolean isRepairable(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    public boolean isValidRepairItem(@NotNull ItemStack pToRepair, @NotNull ItemStack pRepair) {
        return false;
    }

    @Override
    public boolean canBeDepleted() {
        return true;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return ToolDamageUtil.getFakeMaxDamage(stack);
    }

    @Override
    public int getDamage(ItemStack stack) {
        if (!canBeDepleted()) {
            return 0;
        }
        return ToolStack.from(stack).getDamage();
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        if (canBeDepleted()) {
            ToolStack.from(stack).setDamage(damage);
        }
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T damager, Consumer<T> onBroken) {
        ToolDamageUtil.handleDamageItem(stack, amount, damager, onBroken);
        return 0;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return stack.getCount() == 1 && DurabilityDisplayModifierHook.showDurabilityBar(stack);
    }

    @Override
    public int getBarWidth(@NotNull ItemStack pStack) {
        return DurabilityDisplayModifierHook.getDurabilityWidth(pStack);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity target) {
        return stack.getCount() > 1 || EntityInteractionModifierHook.leftClickEntity(stack, player, target);
    }

    @Override
    public @NotNull Multimap<Attribute, AttributeModifier> getAttributeModifiers(@NotNull IToolStackView tool, @NotNull EquipmentSlot slot) {
        return AttributesModifierHook.getHeldAttributeModifiers(tool, slot);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        CompoundTag nbt = stack.getTag();
        if (nbt == null || slot.getType() != EquipmentSlot.Type.HAND) {
            return ImmutableMultimap.of();
        }
        return getAttributeModifiers(ToolStack.from(stack), slot);
    }

    @Override
    public boolean canDisableShield(ItemStack stack, ItemStack shield, LivingEntity entity, LivingEntity attacker) {
        return canPerformAction(stack, TinkerToolActions.SHIELD_DISABLE);
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        return IsEffectiveToolHook.isEffective(ToolStack.from(stack), state);
    }

    @Override
    public boolean mineBlock( @NotNull ItemStack stack, @NotNull  Level worldIn, @NotNull  BlockState state, @NotNull  BlockPos pos, @NotNull LivingEntity entityLiving) {
        return ToolHarvestLogic.mineBlock(stack, worldIn, state, pos, entityLiving);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, @NotNull  BlockState state) {
        return stack.getCount() == 1 ? MiningSpeedToolHook.getDestroySpeed(stack, state) : 0;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, Player player) {
        return stack.getCount() > 1 || ToolHarvestLogic.handleBlockBreak(stack, pos, player);
    }

    @Override
    public boolean overrideStackedOnOther( @NotNull ItemStack held,  @NotNull Slot slot,  @NotNull ClickAction action, @NotNull  Player player) {
        return SlotStackModifierHook.overrideStackedOnOther(held, slot, action, player);
    }

    @Override
    public boolean overrideOtherStackedOnMe( @NotNull ItemStack slotStack, @NotNull  ItemStack held,  @NotNull Slot slot,  @NotNull ClickAction action,  @NotNull Player player,  @NotNull SlotAccess access) {
        return SlotStackModifierHook.overrideOtherStackedOnMe(slotStack, held, slot, action, player, access);
    }

    protected static boolean shouldInteract(@javax.annotation.Nullable LivingEntity player, ToolStack toolStack, InteractionHand hand) {
        IModDataView volatileData = toolStack.getVolatileData();
        if (volatileData.getBoolean(NO_INTERACTION)) {
            return false;
        }
        if (hand == InteractionHand.OFF_HAND) {
            return true;
        }
        return player == null || !volatileData.getBoolean(DEFER_OFFHAND) || player.getOffhandItem().isEmpty();
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        if (stack.getCount() == 1) {
            ToolStack tool = ToolStack.from(stack);
            InteractionHand hand = context.getHand();
            if (shouldInteract(context.getPlayer(), tool, hand)) {
                for (ModifierEntry entry : tool.getModifierList()) {
                    InteractionResult result = entry.getHook(ModifierHooks.BLOCK_INTERACT).beforeBlockUse(tool, entry, context, InteractionSource.RIGHT_CLICK);
                    if (result.consumesAction()) {
                        return result;
                    }
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        ItemStack stack = context.getItemInHand();
        if (stack.getCount() == 1) {
            ToolStack tool = ToolStack.from(stack);
            InteractionHand hand = context.getHand();
            if (shouldInteract(context.getPlayer(), tool, hand)) {
                for (ModifierEntry entry : tool.getModifierList()) {
                    InteractionResult result = entry.getHook(ModifierHooks.BLOCK_INTERACT).afterBlockUse(tool, entry, context, InteractionSource.RIGHT_CLICK);
                    if (result.consumesAction()) {
                        return result;
                    }
                }
            }
        }
        return InteractionResult.PASS;
    }

//    @Override
//    public InteractionResult interactLivingEntity(ItemStack stack, Player playerIn, LivingEntity target, InteractionHand hand) {
//        ToolStack tool = ToolStack.from(stack);
//        if (shouldInteract(playerIn, tool, hand)) {
//            for (ModifierEntry entry : tool.getModifierList()) {
//                InteractionResult result = entry.getHook(ModifierHooks.ENTITY_INTERACT).afterEntityUse(tool, entry, playerIn, target, hand, InteractionSource.RIGHT_CLICK);
//                if (result.consumesAction()) {
//                    return result;
//                }
//            }
//        }
//        return InteractionResult.PASS;
//    }

//    @Override
//    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand hand) {
//        ItemStack stack = playerIn.getItemInHand(hand);
//        if (stack.getCount() > 1) {
//            return InteractionResultHolder.pass(stack);
//        }
//        ToolStack tool = ToolStack.from(stack);
//        if (shouldInteract(playerIn, tool, hand)) {
//            for (ModifierEntry entry : tool.getModifierList()) {
//                InteractionResult result = entry.getHook(ModifierHooks.GENERAL_INTERACT).onToolUse(tool, entry, playerIn, hand, InteractionSource.RIGHT_CLICK);
//                if (result.consumesAction()) {
//                    return new InteractionResultHolder<>(result, stack);
//                }
//            }
//        }
//        return InteractionResultHolder.pass(stack);
//    }

    @Override
    public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack) {
        if (super.canContinueUsing(oldStack, newStack)) {
            if (oldStack != newStack) {
                GeneralInteractionModifierHook.finishUsing(ToolStack.from(oldStack));
            }
        }
        return super.canContinueUsing(oldStack, newStack);
    }

    @Override
    public @NotNull  ItemStack finishUsingItem( @NotNull ItemStack stack, @NotNull  Level worldIn, @NotNull  LivingEntity entityLiving) {
        ToolStack tool = ToolStack.from(stack);
        ModifierEntry activeModifier = GeneralInteractionModifierHook.getActiveModifier(tool);
        GeneralInteractionModifierHook hook = activeModifier.getHook(ModifierHooks.GENERAL_INTERACT);
        int duration = hook.getUseDuration(tool, activeModifier);
        for (ModifierEntry entry : tool.getModifiers()) {
            entry.getHook(ModifierHooks.TOOL_USING).beforeReleaseUsing(tool, entry, entityLiving, duration, 0, activeModifier);
        }
        hook.onFinishUsing(tool, activeModifier, entityLiving);
        return stack;
    }

    @Override
    public void onStopUsing(ItemStack stack, LivingEntity entity, int timeLeft) {
        ToolStack tool = ToolStack.from(stack);
        UsingToolModifierHook.afterStopUsing(tool, entity, timeLeft);
        GeneralInteractionModifierHook.finishUsing(tool);
    }

    @Override
    public @NotNull UseAnim getUseAnimation( @NotNull ItemStack stack) {
        ToolStack tool = ToolStack.from(stack);
        ModifierEntry activeModifier = GeneralInteractionModifierHook.getActiveModifier(tool);
        if (activeModifier != ModifierEntry.EMPTY) {
            return activeModifier.getHook(ModifierHooks.GENERAL_INTERACT).getUseAction(tool, activeModifier);
        }
        return UseAnim.NONE;
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return stack.getCount() == 1 && ModifierUtil.canPerformAction(ToolStack.from(stack), toolAction);
    }

    @Override
    public void appendHoverText( @NotNull ItemStack stack, @javax.annotation.Nullable Level level, @NotNull  List<Component> tooltip, @NotNull  TooltipFlag flag) {
        SpellTier currentTier = this.getTier(stack);
        int tierIndex = TinkerWandTiers.indexOf(currentTier);
        TinkerWandTier nextTier = TinkerWandTiers.byIndex(tierIndex + 1);
        int progression = CastItemDataHelper.getProgression(stack);
        TinkerWandTier currentLadderTier = TinkerWandTiers.byIndex(tierIndex);
        int targetProgression = nextTier != null
                ? nextTier.getProgression()
                : (currentLadderTier != null ? currentLadderTier.getProgression() : progression);
        tooltip.add(Component.translatable("tooltip.sakuratinker.wand_tier",
                currentTier.getDescriptionFormatted(), progression, targetProgression)
                .withStyle(net.minecraft.ChatFormatting.GRAY));
        TooltipUtil.addInformation(this, stack, level, tooltip, SafeClientAccess.getTooltipKey(), flag);
    }

    @Override
    public int getDefaultTooltipHideFlags(@NotNull ItemStack stack) {
        return TooltipUtil.getModifierHideFlags(getToolDefinition());
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(ModifiableItemClientExtension.INSTANCE);
    }

    public static boolean shouldCauseReequip(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        if (oldStack == newStack) {
            return false;
        }
        if (slotChanged || oldStack.getItem() != newStack.getItem()) {
            return true;
        }

        ToolStack oldTool = ToolStack.from(oldStack);
        ToolStack newTool = ToolStack.from(newStack);

        if (!oldTool.getMaterials().equals(newTool.getMaterials())) {
            return true;
        }
        if (!oldTool.getModifierList().equals(newTool.getModifierList())) {
            return true;
        }

        Multimap<Attribute,AttributeModifier> attributesNew = newStack.getAttributeModifiers(EquipmentSlot.MAINHAND);
        Multimap<Attribute, AttributeModifier> attributesOld = oldStack.getAttributeModifiers(EquipmentSlot.MAINHAND);
        if (attributesNew.size() != attributesOld.size()) {
            return true;
        }
        for (Attribute attribute : attributesOld.keySet()) {
            if (!attributesNew.containsKey(attribute)) {
                return true;
            }
            Iterator<AttributeModifier> iter1 = attributesNew.get(attribute).iterator();
            Iterator<AttributeModifier> iter2 = attributesOld.get(attribute).iterator();
            while (iter1.hasNext() && iter2.hasNext()) {
                if (!iter1.next().equals(iter2.next())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack) {
        return shouldCauseReequipAnimation(oldStack, newStack, false);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return shouldCauseReequip(oldStack, newStack, slotChanged);
    }

    public static BlockHitResult blockRayTrace(Level worldIn, Player player, ClipContext.Fluid fluidMode) {
        return Item.getPlayerPOVHitResult(worldIn, player, fluidMode);
    }
}


