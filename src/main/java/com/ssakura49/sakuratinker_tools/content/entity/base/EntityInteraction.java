package com.ssakura49.sakuratinker_tools.content.entity.base;

import com.ssakura49.sakuratinker_tools.content.entity.YoyoEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface EntityInteraction {
   boolean apply(ItemStack var1, Player var2, InteractionHand var3, YoyoEntity var4, Entity var5);
}
