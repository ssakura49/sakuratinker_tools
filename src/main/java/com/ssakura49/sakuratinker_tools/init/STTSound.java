package com.ssakura49.sakuratinker_tools.init;

import com.ssakura49.sakuratinker_tools.SakuraTinkerTools;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class STTSound {
   public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, SakuraTinkerTools.MODID);

   public static final RegistryObject<SoundEvent> REVOLVER_SHOOT = SOUNDS.register("revolver_shoot", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(SakuraTinkerTools.MODID, "revolver_shoot")));
}
