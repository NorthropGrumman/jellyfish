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
package com.ngc.seaside.systemdescriptor.model.api.data;

import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;

import java.util.Collection;

/**
 * Represents a set of discrete values that are referenced in data.
 */
public interface IEnumeration extends INamedChild<IPackage> {

   /**
    * Gets the metadata associated with this data type.
    *
    * @return the metadata associated with this data type
    */
   IMetadata getMetadata();

   /**
    * Sets the metadata associated with this data type.
    *
    * @param metadata the metadata associated with this data type
    * @return this data type
    */
   IEnumeration setMetadata(IMetadata metadata);

   /**
    * Gets the values in the order they are declared.  The returned list may not be modifiable if this object is
    * immutable.
    *
    * @return the values in the order they are declared
    */
   Collection<String> getValues();

   /**
    * Gets the fully qualified name of this enumeration type.  The fully qualified name is the name of the parent
    * package, appended with ".", appended with the name of this enumeration type.  For example, the fully qualified
    * name of a enumeration type named "HelloWorld" which resides in the package named "my.package" would be
    * "my.package.HelloWorld".
    *
    * @return the fully qualified name of this data type
    */
   String getFullyQualifiedName();
}
