package com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.tool.context;

import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class SpellAttackContext {
   @Nonnull
   private final LivingEntity attacker;
   @Nullable
   private final Player playerAttacker;
   @Nonnull
   private final Entity target;
   @Nullable
   private final LivingEntity livingTarget;
   @Nonnull
   private final SpellDamageSource source;
   private final String spellId;
   private final SchoolType schoolType;

   public SpellAttackContext(@NotNull LivingEntity attacker, @Nullable Player playerAttacker, @NotNull Entity target, @Nullable LivingEntity livingTarget, @Nonnull SpellDamageSource source, String spellId, SchoolType schoolType) {
      this.attacker = attacker;
      this.playerAttacker = playerAttacker;
      this.target = target;
      this.livingTarget = livingTarget;
      this.source = source;
      this.spellId = spellId;
      this.schoolType = schoolType;
   }

   public Level getLevel() {
      return this.attacker.level();
   }

   @Nullable
   public Player getPlayerAttacker() {
      return this.playerAttacker;
   }

   @Nonnull
   public Entity getTarget() {
      return this.target;
   }

   @Nonnull
   public LivingEntity getAttacker() {
      return this.attacker;
   }

   @Nullable
   public LivingEntity getLivingTarget() {
      return this.livingTarget;
   }

   @Nonnull
   public SpellDamageSource getSpellDamageSource() {
      return this.source;
   }

   public String getSpellId() {
      return this.spellId;
   }

   public SchoolType getSchoolType() {
      return this.schoolType;
   }

   public static class Builder {
      private LivingEntity caster;
      private Player playerCaster;
      private SpellDamageSource source;
      private Entity targetEntity;
      private LivingEntity target;
      private String spellId;
      private SchoolType schoolType;

      public Builder caster(LivingEntity caster) {
         this.caster = caster;
         this.playerCaster = caster instanceof Player ? (Player)caster : null;
         return this;
      }

      public Builder spellSource(SpellDamageSource source) {
         this.source = source;
         return this;
      }

      public Builder target(LivingEntity target) {
         this.target = target;
         return this;
      }

      public Builder targetEntity(Entity entity) {
         this.targetEntity = entity;
         return this;
      }

      public Builder spell(String spellId, SchoolType schoolType, int spellLevel) {
         this.spellId = spellId;
         this.schoolType = schoolType;
         return this;
      }

      public SpellAttackContext build() {
         return new SpellAttackContext(this.caster, this.playerCaster, this.targetEntity, this.target, this.source, this.spellId, this.schoolType);
      }
   }
}
