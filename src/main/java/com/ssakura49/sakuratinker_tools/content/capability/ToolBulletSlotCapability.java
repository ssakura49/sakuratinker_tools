package com.ssakura49.sakuratinker_tools.content.capability;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker_tools.content.item.BulletItem;
import com.ssakura49.sakuratinker_tools.library.interfaces.item.slot.IHasBulletInventory;

import java.util.function.Predicate;
import java.util.function.Supplier;
import javax.annotation.Nonnull;

import com.ssakura49.sakuratinker_tools.library.tools.item.ModifiableBulletItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.tools.capability.ToolCapabilityProvider;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;


public record ToolBulletSlotCapability(Supplier<? extends IToolStackView> tool) implements IItemHandlerModifiable {
   private static final int DEFAULT_SLOTS = 6;
   private static int stackCount = 1;
   private static final ResourceLocation BULLET_SLOT_COUNT = SakuraTinker.getResource("bullet_slots");

   public static final ResourceLocation KEY_CURRENT_CHAMBER = SakuraTinker.getResource("current_chamber");

   public static final ResourceLocation KEY_MODIFIABLE = SakuraTinker.getResource("is_modifiable");

   public ToolBulletSlotCapability(Supplier<? extends IToolStackView> tool) {
      this.tool = tool;
   }

   private static ModDataNBT getData(IToolStackView tool) {
      return tool.getPersistentData();
   }

   public boolean isModifiable() {
      return this.tool().get().getVolatileData().contains(KEY_MODIFIABLE);
   }

   public static int setStackCount(int count) {
      return stackCount = count;
   }

   public static int getStackCount() {
      return stackCount;
   }

   public static int getSlotCount(IToolStackView tool) {
      ModDataNBT data = getData(tool);
      return data.contains(BULLET_SLOT_COUNT) ? data.getInt(BULLET_SLOT_COUNT) : DEFAULT_SLOTS;
   }

   public static void setSlotCount(IToolStackView tool, int count) {
      getData(tool).putInt(BULLET_SLOT_COUNT, Math.max(1, count));
   }

   public static ToolBulletSlotCapability get(ItemStack stack) {
      if (stack.isEmpty()) return null;
      return stack.getCapability(ForgeCapabilities.ITEM_HANDLER)
              .filter(ToolBulletSlotCapability.class::isInstance)
              .map(ToolBulletSlotCapability.class::cast)
              .orElse(null);
   }

   public static boolean isBulletAmmo(ItemStack stack) {
      if (stack.isEmpty()) return false;
      if (!(stack.getItem() instanceof BulletItem)) return false;
      if (stack.getItem() instanceof ModifiableBulletItem) {
         return !ToolStack.from(stack).isBroken();
      }
      return true;
   }

   public boolean hasAmmo(Predicate<ItemStack> predicate) {
      return getFirstAmmoSlot(predicate) >= 0;
   }

   public int getFirstAmmoSlot(Predicate<ItemStack> predicate) {
      int max = getSlotCount(tool.get());
      for (int i = 0; i < max; i++) {
         ItemStack slot = getStackInSlot(i);
         if (!slot.isEmpty() && predicate.test(slot)) return i;
      }
      return -1;
   }

   public ItemStack getFirstAmmo(Predicate<ItemStack> predicate) {
      int slot = getFirstAmmoSlot(predicate);
      return slot < 0 ? ItemStack.EMPTY : getStackInSlot(slot);
   }

   public int countLoaded(Predicate<ItemStack> predicate) {
      int max = getSlotCount(tool.get());
      int count = 0;
      for (int i = 0; i < max; i++) {
         ItemStack slot = getStackInSlot(i);
         if (!slot.isEmpty() && predicate.test(slot)) count++;
      }
      return count;
   }

   private static ResourceLocation makeKey(int slot) {
      return SakuraTinker.getResource("bullet_" + slot);
   }

   @Nonnull
   @Override
   public ItemStack getStackInSlot(int slot) {
      int max = getSlotCount(tool.get());
      if (slot < 0 || slot >= max) return ItemStack.EMPTY;
      ModDataNBT data = getData(tool.get());
      ResourceLocation key = makeKey(slot);
      if (data.contains(key)) {
         return ItemStack.of(data.getCompound(key));
      }
      return ItemStack.EMPTY;
   }

   @Override
   public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
      int max = getSlotCount(tool.get());
      if (slot < 0 || slot >= max) return;
      ModDataNBT data = getData(tool.get());
      ResourceLocation key = makeKey(slot);
      if (stack.isEmpty()) {
         data.remove(key);
      } else {
         data.put(key, stack.save(new CompoundTag()));
      }
   }

   @Override
   public int getSlots() {
      return getSlotCount(tool.get());
   }

   @Nonnull
   @Override
   public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
      int max = getSlotCount(tool.get());
      if (stack.isEmpty() || slot < 0 || slot >= max) return stack;
      ItemStack current = getStackInSlot(slot);
      if (!current.isEmpty()) return stack;

      int maxInsert = Math.min(stack.getCount(), getSlotLimit(slot));
      if (!simulate) setStackInSlot(slot, stack.copyWithCount(maxInsert));
      return ItemStack.EMPTY;
   }

   @Override
   public int getSlotLimit(int slot) {
      return isModifiable() ? 1 : 64;
   }

   @Override
   public boolean isItemValid(int slot, @NotNull ItemStack stack) {
      int max = getSlotCount(tool.get());
      if (slot < 0 || slot >= max || stack.isEmpty()) return false;
      return stack.getItem() instanceof BulletItem;
   }

   @Nonnull
   @Override
   public ItemStack extractItem(int slot, int amount, boolean simulate) {
      int max = getSlotCount(tool.get());
      if (slot < 0 || slot >= max) return ItemStack.EMPTY;
      ItemStack current = getStackInSlot(slot);
      if (current.isEmpty()) return ItemStack.EMPTY;

      int take = Math.min(amount, current.getCount());
      ItemStack result = ItemHandlerHelper.copyStackWithSize(current, take);
      if (!simulate) {
         current.shrink(take);
         setStackInSlot(slot, current.isEmpty() ? ItemStack.EMPTY : current);
      }
      return result;
   }

   public static class Provider implements ToolCapabilityProvider.IToolCapabilityProvider {
      private final LazyOptional<ToolBulletSlotCapability> cap;

      public Provider(Supplier<? extends IToolStackView> tool) {
         this.cap = LazyOptional.of(() -> new ToolBulletSlotCapability(tool));
      }

      @Override
      public <T> @NotNull LazyOptional<T> getCapability(IToolStackView tool, @NotNull Capability<T> capability) {
         if (capability == ForgeCapabilities.ITEM_HANDLER && tool.getItem() instanceof IHasBulletInventory) {
            return cap.cast();
         }
         return LazyOptional.empty();
      }
   }
}
