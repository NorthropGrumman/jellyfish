package com.ngc.seaside.systemdescriptor.model.api;

import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

/**
 * A package contains one or more data types or models.  Operations that change the state of this object may throw
 * {@code UnsupportedOperationException}s if the object is immutable.
 */
public interface IPackage extends INamedChild<ISystemDescriptor> {

  /**
   * Gets a collection of all declared data types in this package.  The returned collection may not be modifiable if
   * this object is immutable.
   *
   * @return a collection of all declared data types in this package
   */
  INamedChildCollection<IPackage, IData> getData();

  /**
   * Gets a collection of all declared model types in this package.  The returned collection may not be modifiable if
   * this object is immutable.
   *
   * @return a collection of all declared model types in this package
   */
  INamedChildCollection<IPackage, IModel> getModels();
}
