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
