package com.ssakura49.sakuratinker_tools.data.providers;

import com.ssakura49.sakuratinker_tools.compat.extrabotany.init.EBItem;
import com.ssakura49.sakuratinker_tools.compat.goety.init.GoetyItem;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.init.ISSItem;
import com.ssakura49.sakuratinker_tools.compat.wizardry.init.WizardryItem;
import com.ssakura49.sakuratinker_tools.data.base.ToolModelProvider;
import com.ssakura49.sakuratinker_tools.init.STTItems;
import java.io.IOException;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ArmorItem.Type;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

public class STTToolItemModelProvider extends ToolModelProvider {
   public STTToolItemModelProvider(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
      super(packOutput, existingFileHelper, "sakuratinker");
   }

   protected void addModels() throws IOException {
      String defTool = "forge:item/default-tool";
      String defArmor = "forge:item/default";
      this.generateCustomToolJson(STTItems.blade_convergence.asItem(), defTool, false, "blade_convergence", new String[]{"blade", "plate", "handle", "core"}, (String[])null, new int[]{0, 1, 2, 2}, new String[]{"broken"}, new String[]{"blade_broken"}, (double[])null);
      this.generateCustomToolJson(STTItems.swift_sword.asItem(), defTool, false, "swift_sword", new String[]{"blade", "guard", "handle", "binding"}, (String[])null, new int[]{0, 1, 2, 3}, new String[]{"broken"}, new String[]{"blade_broken"}, (double[])null);
      this.generateCustomToolJson(STTItems.shuriken.asItem(), defTool, false, "shuriken", new String[]{"blade_1", "blade_2", "blade_3", "blade_4"}, (String[])null, new int[]{0, 1, 2, 3}, new String[]{"broken"}, new String[]{"blade_1"}, (double[])null);
      this.generateCustomToolJson(STTItems.vampire_knife.asItem(), defTool, false, "vampire_knife", new String[]{"blade1", "blade2", "blade3", "blade_box"}, (String[])null, new int[]{0, 1, 2, 3}, (String[])null, (String[])null, (double[])null);
      this.generateCustomToolJson(STTItems.yoyo.asItem(), defTool, false, "yoyo", new String[]{"body_1", "body_2", "axle", "chord", "ring"}, (String[])null, new int[]{0, 1, 2, 3, 4}, (String[])null, (String[])null, (double[])null);
      this.generateCustomToolJson(ISSItem.tinker_spell_book.asItem(), defTool, false, "tinker_spell_book", new String[]{"envelope", "book_mark", "manu_script", "gutter"}, (String[])null, new int[]{0, 2, 1, 3}, (String[])null, (String[])null, (double[])null);
      this.generateCustomToolJson(STTItems.scythe.asItem(), defTool, true, "scythe", new String[]{"scythe_blade", "plate", "tough_binding", "tough_handle"}, (String[])null, new int[]{0, 1, 2, 3}, new String[]{"broken"}, new String[]{"scythe_blade_broken"}, (double[])null);
      this.generateCustomToolJson(STTItems.great_sword.asItem(), defTool, true, "great_sword", new String[]{"head", "plate_1", "plate_2", "tough_binding", "tough_handle"}, (String[])null, new int[]{0, 1, 2, 3, 4}, new String[]{"broken"}, new String[]{"head_broken"}, new double[]{(double)-2.0F, (double)2.0F});
      this.generateCustomToolJson(STTItems.battle_flag.asItem(), defTool, true, "battle_flag", new String[]{"head", "flag", "handle_1", "handle_2"}, (String[])null, new int[]{0, 1, 2, 3}, new String[]{"broken", "broken"}, new String[]{"head_broken", "flag_broken"}, (double[])null);
      this.generateCustomToolJson(STTItems.tinker_arrow.asItem(), defTool, false, "tinker_arrow", new String[]{"head", "shaft", "fletching"}, new String[]{this.modId + ":item/tool/tinker_arrow/arrow_head", this.modId + ":item/tool/tinker_arrow/arrow_shaft", this.modId + ":item/tool/tinker_arrow/fletching"}, new int[]{0, 1, 2}, (String[])null, (String[])null, (double[])null);
      this.generateCustomToolJson(STTItems.tinker_bullet.asItem(), defTool, false, "tinker_bullet", new String[]{"bullet_1", "bullet_2"}, (String[])null, new int[]{0, 1}, (String[])null, (String[])null, (double[])null);
      this.generateCustomToolJson(STTItems.grappling_hook.asItem(), defTool, false, "grappling_hook", new String[]{"blade", "limb1", "limb2", "handle", "bowstring"}, (String[])null, new int[]{3, 1, 2, 0, 4}, new String[]{" broken"}, new String[]{"blade_broken"}, (double[])null);
      this.generateCustomToolJson(STTItems.laser_gun.asItem(), defTool, false, "laser_gun", new String[]{"barrel", "energy_unit", "laser_medium", "tough_handle"}, (String[])null, new int[]{0, 1, 2, 3}, new String[]{"broken"}, new String[]{"barrel_broken"}, (double[])null);
      this.generateCustomToolJson(STTItems.power_bank.asItem(), defTool, false, "power_bank", new String[]{"shell", "battery_1", "battery_2", "battery_3"}, (String[])null, new int[]{0, 1, 2, 3}, (String[])null, (String[])null, (double[])null);
      this.generateCustomToolJson(
              STTItems.revolver.asItem(),
              defTool,
              false,
              "revolver",
              new String[]{"grip", "handle", "limb","chamber"},
              (String[])null,
              new int[]{0, 1, 2, 3 },
              (String[])null, (String[])null, (double[])null);

      this.generateCustomToolJson(GoetyItem.tinker_wand.asItem(),defTool,false,"tinker_wand",new String[]{"soul_gatherer","large_plate","tough_handle","tough_binding"},(String[])null, new int[]{0, 1, 2, 3},(String[])null,(String[])null,(double[])null);
      this.generateCustomToolJson(
              EBItem.first_fractal.asItem(),
              defTool,
              false,
              "first_fractal",
              new String[]{"head","core","binding","handle"},
              (String[])null,
              new int[]{0, 1, 2, 3},
              new String[]{"broken"}, new String[]{"head_broken"},
              (double[])null
      );

      for(ArmorItem.Type type : Type.values()) {
         String typeName = type.getName();
         String[] armorParts;
         if (type == Type.HELMET) {
            armorParts = new String[]{"head1", "head2", "head3"};
         } else {
            armorParts = new String[]{typeName + "1", typeName + "2", typeName + "3"};
         }

         this.generateCustomToolJson((Item)STTItems.embeddedArmor.get(type), defArmor, false, "armor/embedded/" + typeName, armorParts, (String[])null, new int[]{0, 1, 2}, (String[])null, (String[])null, (double[])null);
      }

      this.generateCustomToolJson(
              STTItems.tinker_charm.asItem(),
              defTool,
              false,
              "tinker_charm",
              new String[]{"charm_chain", "charm_core","charm_setting"},
              new String[]{
                      this.modId + ":item/tool/tinker_charm/charm_chain",
                      this.modId + ":item/tool/tinker_charm/charm_core",
                      this.modId + ":item/tool/tinker_charm/charm_setting"},
              new int[]{2, 1, 0}, (String[])null, (String[])null, (double[])null);

      this.generateCustomToolJson(
              WizardryItem.wizardryWand.asItem(),
              defTool,
              false,
              "wizardry_wand",
              new String[]{"mana_crystal", "handle","binding"},
              new String[]{
                      this.modId + ":item/tool/wizardry_wand/mana_crystal",
                      this.modId + ":item/tool/wizardry_wand/handle",
                      this.modId + ":item/tool/wizardry_wand/binding"},
              new int[]{2, 1, 0}, (String[])null, (String[])null, (double[])null);
   }

   public @NotNull String getName() {
      return "Sakura Tinker-Tools Tool Model Data Provider";
   }
}
