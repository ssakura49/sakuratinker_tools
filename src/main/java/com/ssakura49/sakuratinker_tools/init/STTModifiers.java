package com.ssakura49.sakuratinker_tools.init;

import com.ssakura49.sakuratinker.core.STCrModule;
import com.ssakura49.sakuratinker.core.library.events.LoadRegistryClassEvent;
import com.ssakura49.sakuratinker_tools.SakuraTinkerTools;
import com.ssakura49.sakuratinker_tools.content.tool.modifier.special.*;
import com.ssakura49.sakuratinker_tools.content.tool.modifier.special.battleflag.AttackModifier;
import com.ssakura49.sakuratinker_tools.content.tool.modifier.special.battleflag.DefenceModifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;

@Mod.EventBusSubscriber(modid = SakuraTinkerTools.MODID)
public class STTModifiers {
   @SubscribeEvent
   public static void init(LoadRegistryClassEvent event){}
   public static StaticModifier<AttackModifier> ATTACK_MODIFIER = STCrModule.MODIFIERS.register("attack_flag", AttackModifier::new);
   public static StaticModifier<DefenceModifier> DEFENCE_MODIFIER = STCrModule.MODIFIERS.register("defence_flag", DefenceModifier::new);
   public static StaticModifier<EnergySurge> ENERGY_SURGE = STCrModule.MODIFIERS.register("energy_surge", EnergySurge::new);
   public static StaticModifier<HeavyModifier> HEAVY = STCrModule.MODIFIERS.register("heavy", HeavyModifier::new);
   public static StaticModifier<EnergyDisplayModifier> ENERGY_DISPLAY = STCrModule.MODIFIERS.register("energy_display", EnergyDisplayModifier::new);
   public static StaticModifier<BladeConvergenceModifier> BLADE_CONVERGENCE = STCrModule.MODIFIERS.register("blade_convergence", BladeConvergenceModifier::new);
   public static StaticModifier<RevolverBulletModifier> REVOLVER_BULLET = STCrModule.MODIFIERS.register("revolver_bullet", RevolverBulletModifier::new);
   public static StaticModifier<ScytheSweepingModifier> SCYTHE_SWEEPING = STCrModule.MODIFIERS.register("scythe_sweeping", ScytheSweepingModifier::new);
   public static StaticModifier<SwiftSwordTechniqueModifier> SWIFT_SWORD_TECHNIQUE = STCrModule.MODIFIERS.register("swift_sword_technique", SwiftSwordTechniqueModifier::new);
   public static StaticModifier<ArrowAmmoModifier> arrowAmmo = STCrModule.MODIFIERS.register("arrow_ammo",ArrowAmmoModifier::new);
}
