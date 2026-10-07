package com.ssakura49.sakuratinker_tools.data.providers;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker_tools.compat.extrabotany.init.EBItem;
import com.ssakura49.sakuratinker_tools.compat.goety.init.GoetyItem;
import com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.init.ISSItem;
import com.ssakura49.sakuratinker_tools.init.STTItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.loaders.DynamicFluidContainerModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.mantle.registration.object.FluidObject;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.tconstruct.common.data.model.MaterialModelBuilder;
import slimeknights.tconstruct.common.registration.CastItemObject;
import slimeknights.tconstruct.library.tools.part.MaterialItem;

public class STTItemModelProvider extends ItemModelProvider {
   public static final String ITEM = "item/generated";
   public static final String BUCKET_FLUID = "forge:item/bucket_drip";
   private final ModelFile.UncheckedModelFile GENERATED = new ModelFile.UncheckedModelFile("item/generated");

   public STTItemModelProvider(PackOutput output, ExistingFileHelper helper) {
      super(output, "sakuratinker", helper);
   }

   protected void registerModels() {
      STTItems.getListParts().forEach(this::part);
      STTItems.getListCasts().forEach(this::cast);

      ISSItem.getListParts().forEach(this::part);
      ISSItem.getListCasts().forEach(this::cast);

      GoetyItem.getListParts().forEach(this::part);
      GoetyItem.getListCasts().forEach(this::cast);

      EBItem.getListParts().forEach(this::part);
      EBItem.getListCasts().forEach(this::cast);


      generateItemModel(STTItems.bulletItem,"bullet");
      generateItemModel(STTItems.lightningBulletItem,"bullet");
      generateItemModel(STTItems.explosiveBulletItem,"bullet");
      generateItemModel(STTItems.piercingBulletItem,"bullet");
      generateItemModel(STTItems.ricochetBulletItem,"bullet");
   }

   public void generateItemModel(RegistryObject<Item> object, String typePath) {
      ((ItemModelBuilder)this.withExistingParent(object.getId().getPath(), "item/generated")).texture("layer0", this.getItemLocation(object.getId().getPath(), typePath));
   }

   public void generateBlockItemModel(RegistryObject<BlockItem> object) {
      this.withExistingParent(object.getId().getPath(), this.getBlockItemLocation(object.getId().getPath()));
   }

   public void generateBucketItemModel(FluidObject<ForgeFlowingFluid> object, boolean flip) {
      ((ItemModelBuilder)this.withExistingParent(object.getId().getPath() + "_bucket", "forge:item/bucket_drip")).customLoader((itemModelBuilder, helper) -> DynamicFluidContainerModelBuilder.begin(itemModelBuilder, helper).fluid(object.get()).flipGas(flip));
   }

   public ResourceLocation getItemLocation(String path, String typePath) {
      return ResourceLocation.fromNamespaceAndPath("sakuratinker", "item/" + typePath + "/" + path);
   }

   public ResourceLocation getBlockItemLocation(String path) {
      return ResourceLocation.fromNamespaceAndPath("sakuratinker", "block/" + path);
   }

   private ResourceLocation id(ItemLike item) {
      return BuiltInRegistries.ITEM.getKey(item.asItem());
   }

   private ItemModelBuilder generated(ResourceLocation item, ResourceLocation texture) {
      return (ItemModelBuilder)((ItemModelBuilder)((ItemModelBuilder)this.getBuilder(item.toString())).parent(this.GENERATED)).texture("layer0", texture);
   }

   private ItemModelBuilder generated(ResourceLocation item, String texture) {
      return this.generated(item, ResourceLocation.fromNamespaceAndPath(item.getNamespace(), texture));
   }

   private ItemModelBuilder generated(ItemLike item, String texture) {
      return this.generated(this.id(item), texture);
   }

   private ItemModelBuilder basicItem(ResourceLocation item, String texture) {
      return this.generated(item, "item/" + texture);
   }

   private ItemModelBuilder basicItem(ItemLike item, String texture) {
      return this.basicItem(this.id(item), texture);
   }

   private MaterialModelBuilder<ItemModelBuilder> part(ResourceLocation part, String texture) {
      return (MaterialModelBuilder)((ItemModelBuilder)((ItemModelBuilder)this.withExistingParent(part.getPath(), "forge:item/default")).texture("texture", SakuraTinker.getResource("item/tool/" + texture))).customLoader(MaterialModelBuilder::new);
   }

   private MaterialModelBuilder<ItemModelBuilder> part(Item item, String texture) {
      return this.part(this.id(item), texture);
   }

   public MaterialModelBuilder<ItemModelBuilder> part(ItemObject<? extends MaterialItem> part, String texture) {
      return this.part(part.getId(), texture);
   }

   private void part(ItemObject<? extends MaterialItem> part) {
      String var10002 = part.getId().getPath();
      this.part(part, "part/" + var10002 + "/" + part.getId().getPath());
   }

   private void cast(CastItemObject cast) {
      String name = cast.getName().getPath();
      this.basicItem(cast.getId(), "cast/" + name);
      this.basicItem((ItemLike)cast.getSand(), "sand_cast/" + name);
      this.basicItem((ItemLike)cast.getRedSand(), "red_sand_cast/" + name);
   }
}
