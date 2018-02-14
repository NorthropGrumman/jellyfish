package com.ngc.seaside.jellyfish.service.codegen.api.dto;

import java.util.Set;

/**
 * Represents a description of a Java enum class.
 */
public class EnumDto extends ClassDto {

   private Set<String> values;

   /**
    * Gets the set of enum values.
    */
   public Set<String> getValues() {
      return values;
   }

   public EnumDto setValues(Set<String> values) {
      this.values = values;
      return this;
   }

}