package com.ssakura49.sakuratinker_tools.init;

import com.ssakura49.sakuratinker_tools.content.entity.part.CelestialBladePart;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries.Keys;

public class STTEntityDataSerializers {
   public static final DeferredRegister<EntityDataSerializer<?>> ENTITY_DATA_SERIALIZERS;
   public static final RegistryObject<EntityDataSerializer<CelestialBladePart>> CELESTIAL_BLADE_PART;

   static {
      ENTITY_DATA_SERIALIZERS = DeferredRegister.create(Keys.ENTITY_DATA_SERIALIZERS, "sakuratinker");
      CELESTIAL_BLADE_PART = ENTITY_DATA_SERIALIZERS.register("celestial_blade_part_item", () -> EntityDataSerializer.simple((byteBuf, part) -> part.toNetwork(byteBuf), CelestialBladePart::fromNetwork));
   }
}
