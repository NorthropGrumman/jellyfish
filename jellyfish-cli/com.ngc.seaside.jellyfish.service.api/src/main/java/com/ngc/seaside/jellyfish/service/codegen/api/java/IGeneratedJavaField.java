package com.ngc.seaside.jellyfish.service.codegen.api.java;

import com.ngc.seaside.jellyfish.service.codegen.api.IGeneratedField;

public interface IGeneratedJavaField extends IGeneratedField {

   /**
    * Returns the fully qualified name of the java type of this field. If this field {@link #isMultiple()},
    * returns the java type of the elements.
    *
    * @return the fully qualified name of the java type of this field
    */
   String getJavaType();

   /**
    * @return the name of the java field
    */
   String getJavaFieldName();

   /**
    * @return the name of the getter method for this java field
    */
   String getJavaGetterName();

   /**
    * @return the name of the setter method for this java field
    */
   String getJavaSetterName();

}
