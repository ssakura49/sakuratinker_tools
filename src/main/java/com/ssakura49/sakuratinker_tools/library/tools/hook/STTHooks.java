package com.ssakura49.sakuratinker_tools.library.tools.hook;

import com.ssakura49.sakuratinker_tools.SakuraTinkerTools;
import com.ssakura49.sakuratinker_tools.library.tools.hook.bullet.BulletAmmoModifierHook;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.module.ModuleHook;

public class STTHooks {
   public static final ModuleHook<BulletAmmoModifierHook> BULLET_AMMO = ModifierHooks.register(
           SakuraTinkerTools.getResource("bullet_ammo"),
           BulletAmmoModifierHook.class,
           BulletAmmoModifierHook.EMPTY
   );

}
