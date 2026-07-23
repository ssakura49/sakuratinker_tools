package com.ssakura49.sakuratinker_tools.content.item;

import com.ssakura49.sakuratinker_tools.content.entity.BulletEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BulletItem extends Item {
   public BulletItem(Item.Properties pProperties) {
      super(pProperties);
   }

   public BulletEntity createBullet(Level level, ItemStack stack, LivingEntity shooter) {
      BulletEntity entity = new BulletEntity(level, shooter);
      entity.setTool(stack);
      return entity;
   }
}
