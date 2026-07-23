package com.ssakura49.sakuratinker_tools.data.base;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import slimeknights.tconstruct.library.data.AbstractToolItemModelProvider;

public abstract class ToolModelProvider extends AbstractToolItemModelProvider {
   protected final PackOutput packOutput;

   public ToolModelProvider(PackOutput packOutput, ExistingFileHelper existingFileHelper, String modId) {
      super(packOutput, existingFileHelper, modId);
      this.packOutput = packOutput;
   }

   private void validateTexturePhysicalPath(String textureResLoc, Item item) throws FileNotFoundException {
      ResourceLocation res = ResourceLocation.tryParse(textureResLoc);
      if (res == null) {
         throw new IllegalArgumentException("不合法的贴图资源路径格式: " + textureResLoc);
      } else {
         Path resourcePath = this.packOutput.getOutputFolder().resolve("../../main/resources/assets").resolve(res.getNamespace()).resolve("textures").resolve(res.getPath() + ".png").normalize();
         if (!Files.exists(resourcePath)) {
            String itemName = ForgeRegistries.ITEMS.getKey(item) != null ? ForgeRegistries.ITEMS.getKey(item).toString() : item.toString();
            throw new FileNotFoundException(String.format("\n=======================================================\n[贴图缺失报错] 物品: %s 声明了不存在的贴图！\n贴图文件必须存放在:\n -> %s\n=======================================================", itemName, resourcePath.toAbsolutePath()));
         }
      }
   }

   protected void generateCustomToolJson(Item tool, String parent, boolean isLarge, String textureFolder, String[] partNames, String[] texturePaths, int[] indexes, String[] brokenPartKeys, String[] brokenPartValues, double[] largeOffset) throws IOException {
      ResourceLocation id = ForgeRegistries.ITEMS.getKey(tool);
      if (id == null) {
         throw new IllegalArgumentException("Item is not registered!");
      } else {
         String path = id.getPath();
         JsonObject baseJson = new JsonObject();
         baseJson.addProperty("loader", "tconstruct:tool");
         baseJson.addProperty("parent", parent);
         JsonObject textures = new JsonObject();

         for(int i = 0; i < partNames.length; ++i) {
            String part = partNames[i];
            String actualPath = texturePaths != null && texturePaths.length > i ? texturePaths[i] : this.modId + ":item/tool/" + textureFolder + "/" + part;
            this.validateTexturePhysicalPath(actualPath, tool);
            textures.addProperty(part, actualPath);
            if (isLarge) {
               textures.addProperty("large_" + part, actualPath);
            }
         }

         if (texturePaths == null && brokenPartValues != null) {
            for(String bVal : brokenPartValues) {
               if (!textures.has(bVal)) {
                  String actualBrokenPath = this.modId + ":item/tool/" + textureFolder + "/" + bVal;
                  this.validateTexturePhysicalPath(actualBrokenPath, tool);
                  textures.addProperty(bVal, this.modId + ":item/tool/" + textureFolder + "/" + bVal);
                  if (isLarge) {
                     textures.addProperty("large_" + bVal, this.modId + ":item/tool/" + textureFolder + "/" + bVal);
                  }
               }
            }
         }

         baseJson.add("textures", textures);
         if (isLarge) {
            baseJson.addProperty("large", true);
            if (largeOffset != null && largeOffset.length == 2) {
               JsonArray offsetArr = new JsonArray();
               offsetArr.add(largeOffset[0]);
               offsetArr.add(largeOffset[1]);
               baseJson.add("large_offset", offsetArr);
            }

            JsonObject modifierRoots = new JsonObject();
            JsonArray smallArray = new JsonArray();
            smallArray.add(this.modId + ":item/tool/" + textureFolder + "/modifiers/");
            JsonArray largeArray = new JsonArray();
            largeArray.add(this.modId + ":item/tool/" + textureFolder + "/large/modifiers/");
            modifierRoots.add("small", smallArray);
            modifierRoots.add("large", largeArray);
            baseJson.add("modifier_roots", modifierRoots);
         } else {
            JsonArray modifierRoots = new JsonArray();
            modifierRoots.add(this.modId + ":item/tool/" + textureFolder + "/modifiers/");
            baseJson.add("modifier_roots", modifierRoots);
         }

         JsonArray partsArray = new JsonArray();

         for(int i = 0; i < partNames.length; ++i) {
            JsonObject partObj = new JsonObject();
            partObj.addProperty("name", partNames[i]);
            partObj.addProperty("index", indexes[i]);
            if (brokenPartKeys != null && brokenPartValues != null) {
               for(int j = 0; j < brokenPartKeys.length; ++j) {
                  if (partNames[i].equals(brokenPartValues[j].replace("broken_", "").replace("_broken", ""))) {
                     partObj.addProperty(brokenPartKeys[j], brokenPartValues[j]);
                  }
               }
            }

            partsArray.add(partObj);
         }

         baseJson.add("parts", partsArray);
         this.models.put(path, baseJson);
         if (brokenPartValues != null && brokenPartValues.length > 0) {
            String[] updatedTextures = Arrays.stream(brokenPartValues).map((s) -> s.replace("broken_", "").replace("_broken", "")).toArray((x$0) -> new String[x$0]);
            this.transformTool("tool/" + path + "/broken", baseJson, "", isLarge, '_', "broken", updatedTextures);
         }

      }
   }
}
