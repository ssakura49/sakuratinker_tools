package com.ssakura49.sakuratinker_tools.compat.goety.eventhandler;

import com.Polarice3.Goety.utils.WandUtil;
import com.ssakura49.sakuratinker_tools.STTConfig;
import com.ssakura49.sakuratinker_tools.compat.goety.init.GoetyModifier;
import com.ssakura49.sakuratinker_tools.compat.goety.library.tool.stat.GoetyToolStats;
import com.ssakura49.sakuratinker_tools.compat.goety.tool.item.base.ModifiableDarkWandItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class SpellAttackHandler {
    public static void init() {
        MinecraftForge.EVENT_BUS.addListener(SpellAttackHandler::onLivingHurt);
    }

    private static final boolean bonus = STTConfig.COMMON.TINKER_DARK_WAND_ATTACK_BONUS.get();
    private static final boolean disabled = STTConfig.COMMON.TINKER_DARK_WAND_TRIGGER_MODIFIER.get();

    public static void onLivingHurt(LivingHurtEvent event) {
        DamageSource source = event.getSource();
        LivingEntity target = event.getEntity();
        if (target.level().isClientSide) return;
        if (source.getEntity() instanceof Player player) {
            ItemStack wandStack = WandUtil.findWand(player);
            if (!wandStack.isEmpty() && wandStack.getItem() instanceof ModifiableDarkWandItem) {
                ToolStack tool = ToolStack.from(wandStack);
                if (tool.getModifierLevel(GoetyModifier.G_SOUL_INFUSION.get())>0) {
                    boolean a = bonus;
                    float baseDamage = tool.getStats().get(ToolStats.ATTACK_DAMAGE);
                    float soulPower = tool.getStats().get(GoetyToolStats.SOUL_POWER);
                    float soulIncrease = tool.getStats().get(GoetyToolStats.SOUL_INCREASE);
                    if (!tool.isBroken()) {
                        boolean dis = disabled;
                        if (!dis) {
                            @SuppressWarnings("removal")
                            ToolAttackContext context = new ToolAttackContext(
                                    player,
                                    player,
                                    InteractionHand.MAIN_HAND,
                                    target,
                                    target,
                                    false,
                                    0,
                                    false
                            );
                            float eventDamage = event.getAmount();
                            float modifiedDamage = (eventDamage + (a ? baseDamage : 0) + soulPower) * (1 + soulIncrease);

                            for (ModifierEntry entry : tool.getModifierList()) {
                                if (STTConfig.isBlacklisted(entry)) {
                                    continue;
                                }
                                modifiedDamage = entry.getHook(ModifierHooks.MELEE_DAMAGE).getMeleeDamage(tool, entry, context, baseDamage, modifiedDamage);
                            }
                            event.setAmount(modifiedDamage);

                            for (ModifierEntry entry : tool.getModifierList()) {
                                entry.getHook(ModifierHooks.MELEE_HIT).beforeMeleeHit(tool, entry, context, modifiedDamage, 0, 0);
                            }
                            for (ModifierEntry entry : tool.getModifierList()) {
                                entry.getHook(ModifierHooks.MELEE_HIT).afterMeleeHit(tool, entry, context, modifiedDamage);
                            }
                        } else {
                            float eventDamage = event.getAmount();
                            float modifiedDamage = (eventDamage + (a ? baseDamage : 0) + soulPower) * (1 + soulIncrease);
                            event.setAmount(modifiedDamage);
                        }
                    }
                }
            }
        }
    }
}
