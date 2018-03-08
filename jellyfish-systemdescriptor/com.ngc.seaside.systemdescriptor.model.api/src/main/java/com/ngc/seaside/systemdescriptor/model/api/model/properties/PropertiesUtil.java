package com.ngc.seaside.systemdescriptor.model.api.model.properties;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.FieldCardinality;
import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Internal utility used to implement default methods of {@link IProperties}.
 */
class PropertiesUtil {

   /**
    * Helper method to resolve the values of a property with the given name and fields.
    *
    * @param properties        instance of IProperties
    * @param predicate         checks if the given type is the correct type for the collection
    * @param propertyFunction  a function that, given a property and no fields to access, returns the collection of the
    *                          correct type
    * @param dataValueFunction a function that, given the second to last data value and the last data field, returns the
    *                          collection of the correct type
    * @param propertyName      the name of the property
    * @param fieldNames        the optional names of the fields in the nested type
    * @return the values of the property, or {@link Optional#empty()} if the values cannot be determined
    */
   static <T> Optional<Collection<T>> resolveCollection(
         IProperties properties,
         Predicate<DataTypes> predicate,
         Function<IProperty, Collection<T>> propertyFunction,
         BiFunction<IPropertyDataValue, IDataField, Collection<T>> dataValueFunction,
         String propertyName,
         String[] fieldNames) {
      Preconditions.checkNotNull(propertyName, "property name must not be null!");
      Preconditions.checkNotNull(fieldNames, "field names must not be null!");
      for (String fieldName : fieldNames) {
         Preconditions.checkNotNull(fieldName, "field names cannot contain a null value!");
      }

      Optional<IProperty> property = properties.getByName(propertyName);
      if (fieldNames.length == 0) {
         return property.filter(p -> p.getCardinality() == FieldCardinality.MANY)
               .filter(p -> predicate.test(p.getType()))
               .map(propertyFunction);
      }

      Optional<IPropertyValue> value = property.filter(p -> p.getCardinality() == FieldCardinality.SINGLE)
            .map(IProperty::getValue);

      for (String fieldName : Arrays.asList(fieldNames).subList(0, fieldNames.length - 1)) {
         // Intermediate property values must be IPropertyDataValues and have a cardinality of single
         value = value.filter(IPropertyValue::isData)
               .map(IPropertyDataValue.class::cast)
               .flatMap(dataValue -> dataValue.getFieldByName(fieldName)
                     .filter(f -> f.getCardinality() == FieldCardinality.SINGLE)
                     .map(dataValue::getValue));
      }

      String lastField = fieldNames[fieldNames.length - 1];

      return value.filter(IPropertyValue::isData)
            .map(IPropertyDataValue.class::cast)
            .flatMap(dataValue -> dataValue.getFieldByName(lastField)
                  .filter(field -> predicate.test(field.getType()))
                  .filter(field -> field.getCardinality() == FieldCardinality.MANY)
                  .map(field -> dataValueFunction.apply(dataValue, field)));
   }
}
