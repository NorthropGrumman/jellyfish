/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
package com.ngc.seaside.systemdescriptor.model.api;

import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;
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

   /**
    * Gets a collection of all declared enumeration types in this package.  The returned collection may not be
    * modifiable if this object is immutable.
    *
    * @return a collection of all declared model types in this package
    */
   INamedChildCollection<IPackage, IEnumeration> getEnumerations();
}
