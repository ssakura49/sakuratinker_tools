package com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.tool.item;

import com.ssakura49.sakuratinker.core.utils.TooltipUtil;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.library.tool.stat.ISSToolStats;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.tool.definitions.ISSToolDefinitions;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.tool.item.base.ModifiableSpellBookItem;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.tools.helper.TooltipBuilder;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.utils.Util;

public class TinkerSpellBook extends ModifiableSpellBookItem {
   private int maxSpellSlots;

   public TinkerSpellBook(int maxSpellSlots, Item.Properties properties) {
      super(1, properties, ISSToolDefinitions.TINKER_SPELL_BOOK);
      this.maxSpellSlots = maxSpellSlots;
   }

   public void setMaxSpellSlots(int maxSpellSlots) {
      this.maxSpellSlots = maxSpellSlots;
   }

   public @NotNull List<Component> getStatInformation(@NotNull IToolStackView tool, @Nullable Player player, @NotNull List<Component> tooltips, @NotNull TooltipKey key, @NotNull TooltipFlag tooltipFlag) {
      TooltipBuilder builder = new TooltipBuilder(tool, tooltips);
      TooltipUtil.addToolStatTooltip(builder, tool, ISSToolStats.SPELL_DAMAGE);
      TooltipUtil.addToolStatTooltip(builder, tool, ISSToolStats.MANA_VALUE);
      TooltipUtil.addToolStatTooltip(builder, tool, ISSToolStats.CAST_TIME_REDUCE);
      TooltipUtil.addToolStatTooltip(builder, tool, ISSToolStats.MANA_REGEN);
      TooltipUtil.addToolStatTooltip(builder, tool, ISSToolStats.MANA_REDUCE);
      TooltipUtil.addToolStatTooltip(builder, tool, ISSToolStats.SPELL_SLOT);
      addSchoolBonusTooltip(builder, tool);
      builder.addAllFreeSlots();

      for(ModifierEntry entry : tool.getModifierList()) {
         ((TooltipModifierHook)entry.getHook(ModifierHooks.TOOLTIP)).addTooltip(tool, entry, player, tooltips, key, tooltipFlag);
      }

      return tooltips;
   }

   public static <K> void addMapStatTooltip(TooltipBuilder builder, String titleKey, Map<K, Float> map, Function<K, Component> keyFormatter, DecimalFormat format) {
      if (map != null && !map.isEmpty()) {
         builder.add(Component.translatable(titleKey));
         map.forEach((key, value) -> builder.add(Component.literal("  ").append((Component)keyFormatter.apply(key)).append(": ").append(format.format(value))));
      }
   }

   public static void addSchoolBonusTooltip(TooltipBuilder builder, IToolStackView tool) {
      Map<SchoolType, Float> map = ISSToolStats.getSchoolBonuses(tool);
      addMapStatTooltip(builder, "tool_stat.sakuratinker.school_bonus", map, SchoolType::getDisplayName, Util.BONUS_FORMAT);
   }
}
