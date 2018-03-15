package com.ngc.seaside.systemdescriptor.model.api.model.properties;

import java.util.Collection;

public interface IPropertyValues<T> extends Collection<T> {

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

   boolean isSet();


   @SuppressWarnings({"unchecked"})
   static <T> IPropertyValues<T> emptyPropertyValues() {
      return EMPTY_VALUES;
   }

   static <T> IPropertyValues<T> of(Collection<T> values) {
      return new PropertiesUtil.ArrayListPropertyValues<T>(values);
   }
}
