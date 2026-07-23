package com.ssakura49.sakuratinker_tools.compat.extrabotany.init;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker_tools.compat.extrabotany.entity.PhantomSwordEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.mantle.registration.deferred.EntityTypeDeferredRegister;

public class EBEntity {
    public static final EntityTypeDeferredRegister ENTITIES = new EntityTypeDeferredRegister(SakuraTinker.MODID);

    public static final RegistryObject<EntityType<PhantomSwordEntity>> PHANTOM_SWORD = ENTITIES.register("phantom_sword", () ->
            EntityType.Builder.<PhantomSwordEntity>of(PhantomSwordEntity::new, MobCategory.MISC)
                    .sized(0.2f, 0.2f)
                    .setCustomClientFactory(PhantomSwordEntity::new)
                    .clientTrackingRange(128)
                    .setShouldReceiveVelocityUpdates(true)
                    .updateInterval(-1));
}
