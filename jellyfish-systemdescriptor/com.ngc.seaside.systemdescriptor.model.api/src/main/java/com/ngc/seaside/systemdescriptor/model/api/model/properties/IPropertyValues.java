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
package com.ngc.seaside.systemdescriptor.model.api.model.properties;

import java.util.Collection;

/**
 * A collection of property values.  Property values may have one of the following three states:
 * <pre>
 *    <ol>
 *       <li>{@link #isSet() set} and non empty.  This indicates this collection has at least one value set.</li>
 *       <li>{@link #isSet() set} and empty.  This indicates this collection has been explicitly set to an empty
 *       collection of values.</li>
 *       <li>{@link #isSet() unset} and empty.  This indicates this collection does not have any values set.</li>
 *    </ol>
 * </pre>
 *
 * @param <T> the type of value
 */
public interface IPropertyValues<T> extends Collection<T> {

   /**
    * An immutable instance of {@code IPropertyValues} that is empty and not set.
    */
   @SuppressWarnings("rawtypes")
   IPropertyValues EMPTY_VALUES = new PropertiesUtil.SimplePropertyValues() {
      @Override
      public boolean isSet() {
         return false;
      }

      @Override
      public Object get(int index) {
         throw new IndexOutOfBoundsException("property values is empty");
      }

      @Override
      public int size() {
         return 0;
      }
   };

   /**
    * Returns true if this collection of values has actually been set.  This method may return true even if this
    * collection is empty.  This indicates that an empty collection of values has been explicitly set.
    *
    * @return true if the collection has values set, false if no values have been set.
    */
   boolean isSet();


   /**
    * A type-safe method version of {@link #EMPTY_VALUES}.
    *
    * @param <T> the type of values
    * @return an immutable instance of {@code IPropertyValues} that is not set
    */
   @SuppressWarnings({"unchecked"})
   static <T> IPropertyValues<T> emptyPropertyValues() {
      return EMPTY_VALUES;
   }

   /**
    * Returns an instance of {@code IPropertyValues} that contains the given values
    *
    * @param values the values to include in the collection
    * @param <T>    the type of values
    * @return an {@code IPropertyValues} when the given values
    */
   static <T> IPropertyValues<T> of(Collection<T> values) {
      return new PropertiesUtil.ArrayListPropertyValues<>(values);
   }
}
