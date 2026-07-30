package com.ssakura49.sakuratinker_tools.init;

import com.ssakura49.sakura_library.util.annotation.LoadClass;
import com.ssakura49.sakuratinker.core.STCrModule;
import com.ssakura49.sakuratinker_tools.content.tool.modifier.special.BladeConvergenceModifier;
import com.ssakura49.sakuratinker_tools.content.tool.modifier.special.EnergyDisplayModifier;
import com.ssakura49.sakuratinker_tools.content.tool.modifier.special.EnergySurge;
import com.ssakura49.sakuratinker_tools.content.tool.modifier.special.HeavyModifier;
import com.ssakura49.sakuratinker_tools.content.tool.modifier.special.RevolverBulletModifier;
import com.ssakura49.sakuratinker_tools.content.tool.modifier.special.ScytheSweepingModifier;
import com.ssakura49.sakuratinker_tools.content.tool.modifier.special.SwiftSwordTechniqueModifier;
import com.ssakura49.sakuratinker_tools.content.tool.modifier.special.battleflag.AttackModifier;
import com.ssakura49.sakuratinker_tools.content.tool.modifier.special.battleflag.DefenceModifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;

@LoadClass
@Mod.EventBusSubscriber
public class STTModifiers {
   public static StaticModifier<AttackModifier> ATTACK_MODIFIER = STCrModule.MODIFIERS.register("attack_flag", AttackModifier::new);
   public static StaticModifier<DefenceModifier> DEFENCE_MODIFIER = STCrModule.MODIFIERS.register("defence_flag", DefenceModifier::new);
   public static StaticModifier<EnergySurge> ENERGY_SURGE = STCrModule.MODIFIERS.register("energy_surge", EnergySurge::new);
   public static StaticModifier<HeavyModifier> HEAVY = STCrModule.MODIFIERS.register("heavy", HeavyModifier::new);
   public static StaticModifier<EnergyDisplayModifier> ENERGY_DISPLAY = STCrModule.MODIFIERS.register("energy_display", EnergyDisplayModifier::new);
   public static StaticModifier<BladeConvergenceModifier> BLADE_CONVERGENCE = STCrModule.MODIFIERS.register("blade_convergence", BladeConvergenceModifier::new);
   public static StaticModifier<RevolverBulletModifier> REVOLVER_BULLET = STCrModule.MODIFIERS.register("revolver_bullet", RevolverBulletModifier::new);
   public static StaticModifier<ScytheSweepingModifier> SCYTHE_SWEEPING = STCrModule.MODIFIERS.register("scythe_sweeping", ScytheSweepingModifier::new);
   public static StaticModifier<SwiftSwordTechniqueModifier> SWIFT_SWORD_TECHNIQUE = STCrModule.MODIFIERS.register("swift_sword_technique", SwiftSwordTechniqueModifier::new);

}
