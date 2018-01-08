package com.ngc.seaside.jellyfish.service.codegen.api.dto;

/**
 * Interface representing a Java type.
 * 
 * @param <T> class implementing this interface
 */
public interface TypeDto<T> {

   /**
    * Gets the unqualified type name.
    */
   public String getTypeName();

   public T setTypeName(String packageName);

   /**
    * Gets the package of the type.
    */
   public String getPackageName();

   public T setPackageName(String packageName);

   default String getFullyQualifiedName() {
      return getPackageName() + "." + getTypeName();
   }
   
}
