package com.ngc.seaside.jellyfish.service.codegen.api;

import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;

/**
 * Common interface for generating fields from an {@link IDataField}.
 */
public interface IGeneratedField {

   /**
    * @return the {@link IDataField} on which this generated field is based
    */
   IDataField getDataField();

   /**
    * @return true if this type represents a collection of elements
    */
   boolean isMultiple();

}
