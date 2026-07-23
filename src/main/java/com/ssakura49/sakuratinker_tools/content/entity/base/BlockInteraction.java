package com.ssakura49.sakuratinker_tools.content.entity.base;

import com.ssakura49.sakuratinker_tools.content.entity.YoyoEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

@FunctionalInterface
public interface BlockInteraction {
   boolean apply(ItemStack var1, Player var2, BlockPos var3, BlockState var4, Block var5, YoyoEntity var6);
}
