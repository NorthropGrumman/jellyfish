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
import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;

import java.util.Optional;

/**
 * Represents a data type.  Operations that change the state of this object may throw
 * {@code UnsupportedOperationException}s if the object is immutable.
 */
public interface IData extends INamedChild<IPackage> {

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
   IData setMetadata(IMetadata metadata);

   /**
    * Gets the data type that this data type extends.  If this data type does not extend another data type the
    * returned optional is empty.
    *
    * @return an optional that only contains a value if this data type extends another data type
    */
   Optional<IData> getExtendedDataType();

   /**
    * Sets the data type that this type extends.
    *
    * @param superDataType the data type that this type extends.
    * @return this data type
    */
   IData setExtendedDataType(IData superDataType);

   /**
    * Gets the data fields declared within this data type.  The fields are listed in the order they were declared.
    * The returned collection may not be modifiable if this object is immutable.
    *
    * @return the data fields declared within this data type
    */
   INamedChildCollection<IData, IDataField> getFields();

   /**
    * Gets the fully qualified name of this data type.  The fully qualified name is the name of the parent package,
    * appended with ".", appended with the name of this data type.  For example, the fully qualified name of a data type
    * named "HelloWorld" which resides in the package named "my.package" would be "my.package.HelloWorld".
    *
    * @return the fully qualified name of this data type
    */
   String getFullyQualifiedName();
}
