package com.ngc.seaside.jellyfish.service.codegen.api.proto;

import com.ngc.seaside.jellyfish.service.codegen.api.java.IGeneratedJavaField;

public interface IGeneratedJavaProtoField extends IGeneratedJavaField {

   /**
    * Returns the name of the count method for a repeated field.
    * 
    * @return the name of the count method for a repeated field
    * @throws RuntimeException if {@link #isMultiple()} is false for this field
    */
   String getRepeatedJavaCountName();

   /**
    * Returns the name of the add method for a repeated field.
    * 
    * @return the name of the add method for a repeated field
    * @throws RuntimeException if {@link #isMultiple()} is false for this field
    */
   String getRepeatedJavaAddName();

   /**
    * Returns the name of the get-at-index method for a repeated field.
    * @return the name of the get-at-index method for a repeated field
    * @throws RuntimeException if {@link #isMultiple()} is false for this field
    */
   String getRepeatedJavaGetterName();

}