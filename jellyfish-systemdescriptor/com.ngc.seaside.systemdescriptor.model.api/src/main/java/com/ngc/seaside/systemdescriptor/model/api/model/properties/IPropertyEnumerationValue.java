package com.ngc.seaside.systemdescriptor.model.api.model.properties;

import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;

/**
 * Contains the value of a property when that value is a user defined enumeration type.
 */
public interface IPropertyEnumerationValue extends IPropertyValue {

   /**
    * Gets the enumeration type that is property is declared as.  The value of this property, if set, will be one of the
    * values contained in {@link IEnumeration#getValues()}.
    */
   IEnumeration getReferencedEnumeration();

   /**
    * Gets the value of this property.
    *
    * @return the value of this property
    * @throws IllegalStateException if this property is not {@link #isSet() set}
    */
   String getValue();

}
