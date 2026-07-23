package com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.tool.modifiers;

import com.ssakura49.tinkercuriolib.hook.TCLibHooks;
import com.ssakura49.tinkercuriolib.hook.behavior.CurioAttributeModifierHook;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import java.util.UUID;
import java.util.function.BiConsumer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import top.theillusivec4.curios.api.SlotContext;

public class CobaltSpellModifier extends NoLevelsModifier implements CurioAttributeModifierHook {
   protected void registerHooks(ModuleHookMap.@NotNull Builder builder) {
      super.registerHooks(builder);
      builder.addHook(this, TCLibHooks.CURIO_ATTRIBUTE);
   }

   public void modifyCurioAttribute(IToolStackView curio, ModifierEntry entry, SlotContext context, UUID uuid, BiConsumer<Attribute, AttributeModifier> consumer) {
      consumer.accept((Attribute)AttributeRegistry.SPELL_POWER.get(), new AttributeModifier(uuid, "spell_attr", (double)0.1F, Operation.ADDITION));
      consumer.accept((Attribute)AttributeRegistry.CAST_TIME_REDUCTION.get(), new AttributeModifier(uuid, "cast_time_attr", (double)0.1F, Operation.ADDITION));
   }
}
