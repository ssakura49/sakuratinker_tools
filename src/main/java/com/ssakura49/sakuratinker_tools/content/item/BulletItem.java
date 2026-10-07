package com.ssakura49.sakuratinker_tools.content.item;

import com.ssakura49.sakuratinker_tools.content.bullet.BulletRegistry;
import com.ssakura49.sakuratinker_tools.content.entity.BulletEntity;
import lombok.Getter;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;


@Getter
public class BulletItem extends Item {

    private final String bulletType;

    private final float bulletDamage;

   public BulletItem(Item.Properties pProperties) {
      this(pProperties, BulletRegistry.NORMAL, 6.0F);
   }

   public BulletItem(Item.Properties pProperties, String bulletType, float bulletDamage) {
      super(pProperties);
      this.bulletType = bulletType;
      this.bulletDamage = bulletDamage;
   }

}
