package com.ngc.seaside.systemdescriptor.model.api.data;

public interface IReferencedDataField extends IDataField{

   /**
    * Gets the referenced Data object
    */
   IData getData();

  /**
   * Sets the Data associated with this referenced data field
   * 
   * @param data the Data associated with this referenced data field
   * @return this referenced data object
   */
   IReferencedDataField setData(IData data);
}
