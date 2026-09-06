package com.ssakura49.sakuratinker_tools;

import com.ssakura49.sakura_library.api.config.ConfigProperty;
import com.ssakura49.sakura_library.api.config.SakuraTomlConfig;
import com.ssakura49.sakura_library.api.config.TomlBuilder;
import com.ssakura49.sakuratinker.SakuraTinker;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierId;

import java.util.List;

@EventBusSubscriber(
   modid = "sakuratinker_tools",
   bus = Bus.MOD
)
public class STTConfig {
   public static final SakuraTomlConfig COMPAT_INSTANCE;
   public static final Compat COMPAT;

   static {
      COMPAT_INSTANCE = new SakuraTomlConfig(SakuraTinker.MODID, "ssakura49/sakuratinker-tools-compat.toml");
      TomlBuilder builder = new TomlBuilder(COMPAT_INSTANCE);
      COMPAT = new Compat(builder);
      builder.build();
   }

   public static class Compat {
      public final ConfigProperty<Boolean> ENABLE_ISS_COMPAT;
      public final ConfigProperty<Boolean> ENABLE_GOETY_COMPAT;
      public final ConfigProperty<Boolean> ENABLE_EXTRABOTANY_COMPAT;

      public Compat(TomlBuilder builder) {
         builder.pushCategory("compat");

         ENABLE_ISS_COMPAT = builder
                 .comment("是否启用 Iron's Spells 'n Spellbooks 联动")
                 .getBoolean("enable_irons_spellbooks", true);

         ENABLE_GOETY_COMPAT = builder
                 .comment("是否启用 Goety 联动")
                 .getBoolean("enable_goety", true);

         ENABLE_EXTRABOTANY_COMPAT = builder
                 .comment("是否启用 ExtraBotany 额外植物学联动")
                 .getBoolean("enable_extrabotany", true);

         builder.popCategory();
      }
   }

   public static final ForgeConfigSpec commonSpec;
   public static final Common COMMON;
   static {
      final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
      commonSpec = specPair.getRight();
      COMMON = specPair.getLeft();
   }
   public static final ForgeConfigSpec clientSpec;
   public static final Client CLIENT;

   static {
      final Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
      clientSpec = specPair.getRight();
      CLIENT = specPair.getLeft();
   }

   public static class Common {
      // [平衡性与功能]
      public final ForgeConfigSpec.ConfigValue<List<? extends String>> MODIFIER_BLACKLIST;
      public final ForgeConfigSpec.BooleanValue CHARMS_ALLOW_MULTIPLE;

      // [吸血鬼刀]
      public final ForgeConfigSpec.DoubleValue LIFE_STEAL_CHANCE;
      public final ForgeConfigSpec.DoubleValue LIFE_STEAL_PERCENT;
      public final ForgeConfigSpec.BooleanValue ENABLE_MAX_HEALTH_HEAL;
      public final ForgeConfigSpec.DoubleValue MAX_HEALTH_HEAL_AMOUNT;

      // [工匠箭矢]
      public final ForgeConfigSpec.DoubleValue TINKER_ARROW_FIXES;

      // [激光步枪]
      public final ForgeConfigSpec.IntValue LASER_GUN_BASE_ENERGY;
      public final ForgeConfigSpec.IntValue LASER_GUN_ENERGY_PER_TICK;
      public final ForgeConfigSpec.DoubleValue LASER_GUN_DAMAGE_PER_TICK;
      public final ForgeConfigSpec.IntValue LASER_GUN_C_MOD_NORMAL_DAMAGE_BONUS;
      public final ForgeConfigSpec.BooleanValue LASER_GUN_MODIFIER_EFFECT;

      // [万剑归一]
      public final ForgeConfigSpec.BooleanValue ENABLE_ZENITH_ALL_RANDOM_MATERIALS;
      public final ForgeConfigSpec.BooleanValue BLADE_CONVERGENCE_CAN_TRIGGER_MODIFIER;

      // [Goety 联动]
      public final ForgeConfigSpec.BooleanValue TINKER_DARK_WAND_ATTACK_BONUS;
      public final ForgeConfigSpec.BooleanValue TINKER_DARK_WAND_TRIGGER_MODIFIER;


      public final ForgeConfigSpec.BooleanValue ENABLE_FIRST_FRACTAL_ALL_RANDOM_MATERIALS;
      public final ForgeConfigSpec.BooleanValue FIRST_FRACTAL_CAN_TRIGGER_MODIFIER;
      public Common(ForgeConfigSpec.Builder builder) {
         builder.comment("=================================================",
                 " 游玩与平衡性设置 (提示：此处修改不会同步改变语言文件描述，需手动覆盖 Lang)",
                 " Gameplay & Balancing Settings",
                 "=================================================").push("Gameplay");

         MODIFIER_BLACKLIST = builder
                 .comment("Modifier黑名单。在此黑名单上的词条 ID 将不会被触发。", "Blacklist of modifiers (by ID), these won't be triggered.")
                 .defineList("modifierBlacklist", List.of("cloudertinker:steelbone"), obj -> obj instanceof String);
         builder.pop();

         builder.push("TinkerCharms");
         CHARMS_ALLOW_MULTIPLE = builder
                 .comment("是否允许同时装备多件相同的护符", "Set to true to allow equipping multiple charms of the same type.")
                 .define("allowMultipleCharms", false);
         builder.pop();

         builder.push("VampireKnives");
         LIFE_STEAL_CHANCE = builder
                 .comment("攻击时触发吸血的概率 (0.0 = 从不, 1.0 = 100%)")
                 .defineInRange("lifeStealChance", 0.5, 0.0, 1.0);
         LIFE_STEAL_PERCENT = builder
                 .comment("单次触发吸血时，吸取伤害的百分比 (0.1 = 10%)")
                 .defineInRange("lifeStealPercent", 0.1, 0.0, 10.0);
         ENABLE_MAX_HEALTH_HEAL = builder
                 .comment("吸血时是否允许恢复超过当前生命值上限的‘虚空血量’/额外生命上限值")
                 .define("enableMaxHealthHeal", true);
         MAX_HEALTH_HEAL_AMOUNT = builder
                 .comment("突破最大生命值上限时的恢复系数")
                 .defineInRange("maxHealthHealAmount", 0.05, 0.0, 10.0);
         builder.pop();

         builder.push("TinkerArrow");
         TINKER_ARROW_FIXES = builder
                 .comment("箭矢伤害修正系数，最小为1.0")
                 .defineInRange("arrowDamageBonusModifier", 1.2, 1.0, (double) Integer.MAX_VALUE);
         builder.pop();

         builder.push("LaserGun");
         LASER_GUN_BASE_ENERGY = builder
                 .comment("激光普通单发模式的能量消耗")
                 .defineInRange("laserBaseEnergy", 500, 0, Integer.MAX_VALUE);
         LASER_GUN_ENERGY_PER_TICK = builder
                 .comment("激光连发模式下，每持续 Tick 增加的额外能量消耗")
                 .defineInRange("continuousModePerTickEnergyCost", 50, 0, Integer.MAX_VALUE);
         LASER_GUN_DAMAGE_PER_TICK = builder
                 .comment("激光连发模式下，每持续 Tick 的伤害增幅")
                 .defineInRange("continuousModePerTickDamageIncrease", 0.05, 0.0, Double.MAX_VALUE);
         LASER_GUN_C_MOD_NORMAL_DAMAGE_BONUS = builder
                 .comment("激光连发模式累积伤害增幅，持平普通模式所需要的时长 (Tick数)")
                 .defineInRange("continuousModeDamageToBaseBonus", 40, 1, Integer.MAX_VALUE);
         LASER_GUN_MODIFIER_EFFECT = builder
                 .comment("激光枪是否会受到匠魂 Modifier 词条效果的影响")
                 .define("laserGunModifierEffect", true);
         builder.pop();

         builder.push("Blade Convergence");
         ENABLE_ZENITH_ALL_RANDOM_MATERIALS=builder
                 .comment("Enable All Materials Render").
                 comment("允许完全随机的材料渲染")
                 .define("enableRandom", true);
         BLADE_CONVERGENCE_CAN_TRIGGER_MODIFIER=builder
                 .comment("Materials Can Trigger All  Modifier")
                 .comment("允许随机的材料能够正常触发词条效果")
                 .define("can trigger modifier,default false", false);
         builder.pop();

         builder.push("First Fractal");
         ENABLE_FIRST_FRACTAL_ALL_RANDOM_MATERIALS=builder
                 .comment("Enable All Materials Render").
                 comment("允许完全随机的材料渲染").define("enableRandom", true);
         FIRST_FRACTAL_CAN_TRIGGER_MODIFIER=builder
                 .define("can trigger modifier,default false", false);
         builder.pop();

         builder.comment("Goety Compatible Configuration").push("GoetyCompat");
         TINKER_DARK_WAND_ATTACK_BONUS = builder
                 .comment("聚晶伤害是否会受到工具基础攻击力的加成影响")
                 .define("attackBonus", false);
         TINKER_DARK_WAND_TRIGGER_MODIFIER = builder
                 .comment("黑暗手杖是否可以触发匠魂词条")
                 .define("tinkerWandCanTriggerModifiers", true);
         builder.pop();
      }
   }

   public static class Client {
      public Client(ForgeConfigSpec.Builder builder) {

      }
   }

   @SuppressWarnings("removal")
   public static void init() {
      String common_path = "ssakura49/sakuratinker-tools-common.toml";
      String client_path = "ssakura49/sakuratinker-tools-client.toml";
      ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, commonSpec, common_path);
      ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, clientSpec, client_path);
   }

   public static boolean isBlacklisted(ModifierEntry entry) {
      return isBlacklisted(entry.getId());
   }

   public static boolean isBlacklisted(ModifierId modifierId) {
      if (COMMON == null || COMMON.MODIFIER_BLACKLIST == null) return false;

      List<? extends String> blacklist = COMMON.MODIFIER_BLACKLIST.get();
      String targetId = modifierId.toString(); // "namespace:path" 格式字符串

      for (String blacklistedStr : blacklist) {
         if (targetId.equals(blacklistedStr)) {
            return true;
         }
      }
      return false;
   }
}
