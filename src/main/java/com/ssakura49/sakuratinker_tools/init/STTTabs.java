package com.ssakura49.sakuratinker_tools.init;

import com.ssakura49.sakura_library.api.client.creativetab.SideBarCategory;
import com.ssakura49.sakuratinker_tools.compat.extrabotany.init.EBItem;
import com.ssakura49.sakuratinker_tools.compat.goety.init.GoetyItem;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.init.ISSItem;
import com.ssakura49.sakuratinker_tools.util.SafeClass;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.registration.object.EnumObject;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.tconstruct.common.registration.CastItemObject;
import slimeknights.tconstruct.library.tools.helper.ToolBuildHandler;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.part.IMaterialItem;
import slimeknights.tconstruct.library.tools.part.ToolPartItem;

public class STTTabs {
   public static void acceptTool(Consumer<ItemStack> output, Supplier<? extends IModifiable> tool) {
      ToolBuildHandler.addVariants(output, (IModifiable)tool.get(), "");
   }

   public static void acceptTools(Consumer<ItemStack> output, EnumObject<?, ? extends IModifiable> tools) {
      tools.forEach((tool) -> ToolBuildHandler.addVariants(output, tool, ""));
   }

   public static void acceptPart(Consumer<ItemStack> output, Supplier<? extends IMaterialItem> item) {
      ((IMaterialItem)item.get()).addVariants(output, "");
   }

   public static void accept(CreativeModeTab.Output output, Function<CastItemObject, ItemLike> getter, CastItemObject cast) {
      output.accept((ItemLike)getter.apply(cast));
   }

   public static void addCasts(CreativeModeTab.Output output, Function<CastItemObject, ItemLike> getter) {
      for(CastItemObject cast : STTItems.getListCasts()) {
         accept(output, getter, cast);
      }

      if (SafeClass.isISSLoaded()) {
         for(CastItemObject cast : ISSItem.getListCasts()) {
            accept(output, getter, cast);
         }
      }
      if (SafeClass.isGOETYLoaded()) {
         for (CastItemObject castItemObject: GoetyItem.getListCasts()){
            accept(output,getter,castItemObject);
         }
      }
      if (SafeClass.isExtraBotanyLoaded()) {
         for (CastItemObject castItemObject: EBItem.getListCasts()){
            accept(output,getter,castItemObject);
         }
      }

   }

   public static void fillParts(HolderLookup.Provider provider, SideBarCategory.Output output) {
      Objects.requireNonNull(output);
      Consumer<ItemStack> outputPart = output::accept;

      for(ItemObject<ToolPartItem> part : STTItems.getListParts()) {
         acceptPart(outputPart, part);
      }

      if (SafeClass.isISSLoaded()) {
         for(ItemObject<ToolPartItem> part : ISSItem.getListParts()) {
            acceptPart(outputPart, part);
         }
      }
      if (SafeClass.isGOETYLoaded()){
         for (ItemObject<ToolPartItem> part:GoetyItem.getListParts()){
            acceptPart(outputPart,part);
         }
      }
      if (SafeClass.isExtraBotanyLoaded()){
         for (ItemObject<ToolPartItem> part:EBItem.getListParts()){
            acceptPart(outputPart,part);
         }
      }

   }

   public static void fillCasts(HolderLookup.Provider provider, final SideBarCategory.Output output) {
      CreativeModeTab.Output vanillaWrapper = new CreativeModeTab.Output() {
         public void accept(@NotNull ItemLike item) {
            output.accept(item);
         }

         public void accept(@NotNull ItemStack stack) {
            output.accept(stack);
         }

         public void accept(@NotNull ItemStack stack, CreativeModeTab.@NotNull TabVisibility visibility) {
            output.accept(stack);
         }
      };
      addCasts(vanillaWrapper, ItemObject::get);
      addCasts(vanillaWrapper, CastItemObject::getSand);
      addCasts(vanillaWrapper, CastItemObject::getRedSand);
   }

   public static void fillTools(HolderLookup.Provider provider, SideBarCategory.Output output) {
      Objects.requireNonNull(output);
      Consumer<ItemStack> outputTool = output::accept;

      acceptTools(outputTool, STTItems.embeddedArmor);
      for(ItemObject<? extends Item> tool : STTItems.getListTools()) {
         ItemLike var6 = tool.get();
         if (var6 instanceof IModifiable modifiable) {
            acceptTool(outputTool, () -> modifiable);
         }
      }

      if (SafeClass.isISSLoaded()) {
         for(ItemObject<? extends Item> tool : ISSItem.getListTools()) {
            ItemLike var10 = tool.get();
            if (var10 instanceof IModifiable modifiable) {
                acceptTool(outputTool, () -> modifiable);
            }
         }
      }
      if (SafeClass.isGOETYLoaded()) {
         for(ItemObject<? extends Item> tool : GoetyItem.getListTools()) {
            ItemLike var10 = tool.get();
            if (var10 instanceof IModifiable modifiable) {
               acceptTool(outputTool, () -> modifiable);
            }
         }
      }if (SafeClass.isExtraBotanyLoaded()) {
         for(ItemObject<? extends Item> tool : EBItem.getListTools()) {
            ItemLike var10 = tool.get();
            if (var10 instanceof IModifiable modifiable) {
               acceptTool(outputTool, () -> modifiable);
            }
         }
      }
   }
}
