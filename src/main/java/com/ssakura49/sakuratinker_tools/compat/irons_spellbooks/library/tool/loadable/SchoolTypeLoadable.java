package com.ssakura49.sakuratinker_tools.compat.irons_spellbooks.library.tool.loadable;

import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import net.minecraft.resources.ResourceLocation;
import slimeknights.mantle.data.loadable.Loadables;
import slimeknights.mantle.data.loadable.mapping.MapLoadable;
import slimeknights.mantle.data.loadable.primitive.FloatLoadable;
import slimeknights.mantle.data.loadable.primitive.StringLoadable;

public class SchoolTypeLoadable {
   public static final StringLoadable<SchoolType> SCHOOL;
   public static final MapLoadable<SchoolType, Float> SCHOOL_BONUS_MAP;

   static {
      SCHOOL = Loadables.RESOURCE_LOCATION.xmap((id, error) -> {
         SchoolType school = SchoolRegistry.getSchool(id);
         if (school != null) {
            return school;
         } else {
            throw error.create("Unknown school " + id);
         }
      }, (school, error) -> {
         ResourceLocation id = school.getId();
         if (id != null) {
            return id;
         } else {
            throw error.create("Attempt to serialize unregistered school " + school);
         }
      });
      SCHOOL_BONUS_MAP = new MapLoadable<>(SCHOOL, FloatLoadable.ANY, 0);
   }
}
