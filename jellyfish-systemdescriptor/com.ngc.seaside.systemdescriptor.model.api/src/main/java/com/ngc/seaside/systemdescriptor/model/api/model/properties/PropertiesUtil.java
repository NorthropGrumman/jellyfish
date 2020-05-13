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

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.FieldCardinality;
import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Internal utility used to implement default methods of {@link IProperties}.
 */
class PropertiesUtil {

   /**
    * Helper method to resolve the values of a property with the given name and fields.
    *
    * @param properties            the properties to start the resolution from
    * @param propertyValueVerifier a predicate that checks if the given property should be considered as a value to
    *                              resolve the values for the property
    * @param propertyValueMapper   a function that maps an {@code IPropertyValue} to an instance of {@code T}
    * @param propertyName          the name of the property
    * @param fieldNames            the optional names of the fields in the nested type
    * @param <T>                   the type of value in the collection of property values
    * @return the values of the property which my be set or unset
    */
   static <T> IPropertyValues<T> resolveValues(IProperties properties,
                                               Predicate<IPropertyValue> propertyValueVerifier,
                                               Function<IPropertyValue, T> propertyValueMapper,
                                               String propertyName,
                                               String... fieldNames) {
      Preconditions.checkNotNull(propertyName, "property name must not be null!");
      Preconditions.checkArgument(!propertyName.trim().isEmpty(), "property name must not be empty!");
      Preconditions.checkNotNull(fieldNames, "field names must not be null!");
      for (String fieldName : fieldNames) {
         Preconditions.checkNotNull(fieldName, "field names cannot contain a null value!");
         Preconditions.checkArgument(!fieldName.trim().isEmpty(), "field name must not be empty!");
      }

      IPropertyValues<T> values;
      if (fieldNames.length == 0) {
         values = resolveValuesDirectlyFromProperty(properties,
                                                    propertyName,
                                                    propertyValueVerifier,
                                                    propertyValueMapper);
      } else {
         values = resolveValuesFromComplexDataType(properties,
                                                   propertyName,
                                                   fieldNames,
                                                   propertyValueVerifier,
                                                   propertyValueMapper);
      }

      return values;
   }

   /**
    * Implementation of {@code IPropertyValues} that can store anything and is mutable.
    *
    * @param <T> the type of values in the collection
    */
   static class ArrayListPropertyValues<T>
         extends ArrayList<T>
         implements IPropertyValues<T> {

      ArrayListPropertyValues() {
      }

      ArrayListPropertyValues(Collection<? extends T> c) {
         super(c);
      }

      @Override
      public boolean isSet() {
         return true;
      }
   }

   /**
    * Implementation of {@code IPropertyValues} that is not mutable.
    *
    * @param <T> the type of value in the collection
    */
   abstract static class SimplePropertyValues<T>
         extends AbstractList<T>
         implements IPropertyValues<T> {

   }

   /**
    * Implementation of {@code IProperties} that is not mutable.
    */
   abstract static class SimplePropertiesImpl
         extends AbstractList<IProperty>
         implements IProperties {

   }

   private static <T> IPropertyValues<T> resolveValuesDirectlyFromProperty(
         IProperties properties,
         String propertyName,
         Predicate<IPropertyValue> propertyValueVerifier,
         Function<IPropertyValue, T> propertyValueMapper) {
      IPropertyValues<T> values = IPropertyValues.emptyPropertyValues();

      Optional<IProperty> optional = properties.getByName(propertyName);
      // Does the property exist?
      if (optional.isPresent()) {
         IProperty property = optional.get();
         // Does the property have multiple values and is the property set?
         if (property.getCardinality() == FieldCardinality.MANY && property.getValues().isSet()) {
            // Get the values, converting them to the appropriate type.
            values = property.getValues()
                  .stream()
                  .filter(propertyValueVerifier) // Ensure that the type of the property value matches the expected type
                  .map(propertyValueMapper) // Map the IPropertyValue to whatever type is is supposed to be.
                  .collect(Collectors.toCollection(ArrayListPropertyValues::new)); // Build a collection of values.
         }
      }
      return values;
   }

   private static <T> IPropertyValues<T> resolveValuesFromComplexDataType(
         IProperties properties,
         String propertyName,
         String[] fieldNames,
         Predicate<IPropertyValue> propertyValueVerifier,
         Function<IPropertyValue, T> propertyValueMapper) {
      IPropertyValues<T> values = IPropertyValues.emptyPropertyValues();

      Optional<IProperty> optional = properties.getByName(propertyName);
      // Does the property exist?
      if (optional.isPresent()) {
         IProperty property = optional.get();

         FieldCardinality cardinality = property.getCardinality();
         DataTypes type = property.getType();

         // Require the cardinality of the property to be single and the value of the property to be a data type.
         if (cardinality == FieldCardinality.SINGLE && type == DataTypes.DATA) {
            IPropertyDataValue data = property.getData();

            // Resolve all the nested fields except for the last field.  Require all the intermediate property values
            // to have a cardinality of single and be a data type.
            for (int i = 0;
                  i < fieldNames.length - 1
                  && cardinality == FieldCardinality.SINGLE
                  && type == DataTypes.DATA;
                  i++) {
               IDataField field = data.getFieldByName(fieldNames[i]).orElse(null);
               // Was the field with the given name actually found.
               if (field != null) {
                  // Continue resolving the data type.
                  data = data.getData(field);
                  type = data.getType();
                  cardinality = field.getCardinality();
               } else {
                  // If not, exit the loop and return unset properties.
                  data = null;
                  type = null;
                  cardinality = null;
               }
            }

            // Did we find the last data type?
            if (data != null) {
               IDataField field = data.getFieldByName(fieldNames[fieldNames.length - 1]).orElse(null);
               // If the field exists and the cardinality is many, get the values.
               if (field != null && field.getCardinality() == FieldCardinality.MANY) {
                  IPropertyValues<? extends IPropertyValue> dataValues = data.getValues(field);
                  // Only convert the values if the values are set.
                  if (dataValues.isSet()) {
                     values = dataValues.stream()
                           // Ensure that the type of the property value matches the expected type
                           .filter(propertyValueVerifier)
                           // Map the IPropertyValue to whatever type is is supposed to be.
                           .map(propertyValueMapper)
                           // Build a collection of values
                           .collect(Collectors.toCollection(ArrayListPropertyValues::new));
                  }
               }
            }
         }
      }

      return values;
   }
}
