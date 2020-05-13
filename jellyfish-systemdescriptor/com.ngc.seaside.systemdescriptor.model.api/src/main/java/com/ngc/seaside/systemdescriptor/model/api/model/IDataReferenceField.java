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
package com.ngc.seaside.systemdescriptor.model.api.model;

import com.ngc.seaside.systemdescriptor.model.api.FieldCardinality;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;

/**
 * Represents a field declared in an {@link IModel} that references another {@link IData} type.  Operations that change
 * the state of this object may throw {@code UnsupportedOperationException}s if the object is immutable.
 */
public interface IDataReferenceField extends IReferenceField {

   /**
    * Gets the model type of this field that is being referenced
    *
    * @return the model type of this field that is being referenced
    */
   IData getType();

   /**
    * Sets the data type of this field that is being referenced.
    *
    * @param type the data type of this field that is being referenced
    * @return this field
    */
   IDataReferenceField setType(IData type);

   /**
    * Gets the cardinality associated with this field.
    *
    * @return the cardinality associated with this field
    */
   FieldCardinality getCardinality();

   /**
    * Sets the cardinality associated with this field.
    *
    * @param cardinality the cardinality associated with this field
    * @return this field
    */
   IDataReferenceField setCardinality(FieldCardinality cardinality);
}
