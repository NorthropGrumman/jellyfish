package com.ngc.seaside.jellyfish.service.codegen.api.dto;

import java.util.Set;

/**
 * Represents a description of a Java enum class.
 * 
 * @param <T> the type of the MethodDto
 */
public class EnumDto<T extends MethodDto> extends ClassDto<T> {

   private Set<String> values;

   /**
    * Gets the set of enum values.
    */
   public Set<String> getValues() {
      return values;
   }

   public EnumDto<T> setValues(Set<String> values) {
      this.values = values;
      return this;
   }

}
