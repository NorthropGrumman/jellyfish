/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
