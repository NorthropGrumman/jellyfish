package com.ngc.seaside.systemdescriptor.model.api.model.properties;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.FieldCardinality;
import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A type of collection that contains properties. This type of collection contains extra operations to help resolve the
 * values of properties. For example, given the following model
 *
 * <pre> {@code
 * package my.servers
 * model RedHatServer refines Server {
 *   properties {
 *     serverConfig.vendor = "HP"
 *     serverConfig.cores = 16
 *     serverConfig.performanceScore = 0.95
 *     serverConfig.isVirtual = true
 *   }
 * }
 * }
 * </pre>
 *
 * it is possible to resolve the "vendor" property as follows:
 *
 * <pre> {@code
 *    IModel model = systemDescriptor.findModel("my.servers.RedHatServer").get();
 *    String vendor = model.getProperties().resolveAsString("serverConfig", "vendor").get();
 * }
 * </pre>
 *
 * Properties and their values are not meant to be mutated.
 */
public interface IProperties extends INamedChildCollection<IProperties, IProperty> {

   /**
    * An immutable singleton that is used to indicate empty or missing properties.
    */
   IProperties EMPTY_PROPERTIES = new PropertiesUtil.SimplePropertiesImpl() {
      @Override
      public Optional<IProperty> getByName(String name) {
         return Optional.empty();
      }

      @Override
      public IProperty get(int index) {
         throw new IndexOutOfBoundsException("properties is empty");
      }

      @Override
      public int size() {
         return 0;
      }
   };

   /**
    * Attempts to resolve the value of the property with the given name. Returns {@link Optional#empty()} if the values
    * cannot be resolved, including but not limited to the following cases:
    *
    * <pre>
    * <ul>
    *    <li>The property or any of the nested fields are not defined</li>
    *    <li>The property is unset</li>
    *    <li>The property does not have a cardinality of {@link FieldCardinality#SINGLE}</li>
    *    <li>The property has an intermediate field of type {@link DataTypes#DATA} with cardinality of
    *    {@link FieldCardinality#MANY}
    *    </li>
    * </ul>
    * </pre>
    *
    * If present, the returned value will have cardinality of {@link FieldCardinality#SINGLE}.
    *
    * @param propertyName the name of the property
    * @param fieldNames   the optional names of the fields in the nested type
    * @return the value of the property, or {@link Optional#empty()} if the value cannot be determined
    */
   default Optional<IPropertyValue> resolveValue(String propertyName, String... fieldNames) {
      Preconditions.checkNotNull(propertyName, "property name must not be null!");
      Preconditions.checkNotNull(fieldNames, "field names must not be null!");
      Optional<IPropertyValue> value = getByName(propertyName)
            .filter(property -> property.getCardinality() == FieldCardinality.SINGLE)
            .map(IProperty::getValue);
      for (String fieldName : fieldNames) {
         Preconditions.checkNotNull(fieldName, "field names cannot contain a null value!");
         value = value.filter(IPropertyValue::isData)
               .map(IPropertyDataValue.class::cast)
               .flatMap(dataValue -> dataValue.getFieldByName(fieldName)
                     .filter(field -> field.getCardinality() == FieldCardinality.SINGLE)
                     .map(dataValue::getValue));
      }
      return value;
   }

   /**
    * Attempts to resolve the data value of the property with the given name. Returns {@link Optional#empty()} if the
    * values cannot be resolved, including but not limited to the following cases:
    *
    * <pre>
    * <ul>
    *    <li>The property or any of the nested fields are not defined</li>
    *    <li>The property is unset</li> <li>The type of the property is not {@link DataTypes#DATA}</li>
    *    <li>The property does not have a cardinality of {@link FieldCardinality#SINGLE}</li>
    *    <li>The property has an intermediate field of type {@link DataTypes#DATA} with cardinality of
    *    {@link FieldCardinality#MANY}</li>
    * </ul>
    * </pre>
    *
    * If present, the returned value will have cardinality of {@link FieldCardinality#SINGLE}.
    *
    * @param propertyName the name of the property
    * @param fieldNames   the optional names of the fields in the nested type
    * @return the data value of the property, or {@link Optional#empty()} if the value cannot be determined
    */
   default Optional<IPropertyDataValue> resolveAsData(String propertyName, String... fieldNames) {
      return resolveValue(propertyName, fieldNames)
            .filter(IPropertyValue::isData)
            .map(IPropertyDataValue.class::cast);
   }

   /**
    * Attempts to resolve the enumeration value of the property with the given name. Returns {@link Optional#empty()} if
    * the values cannot be resolved, including but not limited to the following cases:
    *
    * <pre>
    * <ul>
    *    <li>The property or any of the nested fields are not defined</li>
    *    <li>The property is unset</li>
    *    <li>The type of the property is not {@link DataTypes#ENUM}</li>
    *    <li>The property does not have a cardinality of {@link FieldCardinality#SINGLE}</li>
    *    <li>The property has an intermediate field of type {@link DataTypes#DATA} with cardinality of
    *    {@link FieldCardinality#MANY}</li>
    * </ul>
    * </pre>
    *
    * If present, the returned value will have cardinality of {@link FieldCardinality#SINGLE}.
    *
    * @param propertyName the name of the property
    * @param fieldNames   the optional names of the fields in the nested type
    * @return the enumeration value of the property, or {@link Optional#empty()} if the value cannot be determined
    */
   default Optional<IPropertyEnumerationValue> resolveAsEnumeration(String propertyName, String... fieldNames) {
      return resolveValue(propertyName, fieldNames)
            .filter(IPropertyValue::isEnumeration)
            .map(IPropertyEnumerationValue.class::cast);
   }

   /**
    * Attempts to resolve the primitive value of the property with the given name. Returns {@link Optional#empty()} if
    * the values cannot be resolved, including but not limited to the following cases:
    *
    * <pre>
    * <ul>
    *    <li>The property or any of the nested fields are not defined</li>
    *    <li>The property is unset</li>
    *    <li>The type of the property is not a primitive type</li>
    *    <li>The property does not have a cardinality of {@link FieldCardinality#SINGLE}</li>
    *    <li>The property has an intermediate field of type {@link DataTypes#DATA} with cardinality of
    *    {@link FieldCardinality#MANY}</li>
    * </ul>
    * </pre>
    *
    * If present, the returned value will have cardinality of {@link FieldCardinality#SINGLE}.
    *
    * @param propertyName the name of the property
    * @param fieldNames   the optional names of the fields in the nested type
    * @return the primitive value of the property, or {@link Optional#empty()} if the value cannot be determined
    */
   default Optional<IPropertyPrimitiveValue> resolveAsPrimitive(String propertyName, String... fieldNames) {
      return resolveValue(propertyName, fieldNames)
            .filter(IPropertyValue::isPrimitive)
            .map(IPropertyPrimitiveValue.class::cast);
   }

   /**
    * Attempts to resolve the integer value of the property with the given name. Returns {@link Optional#empty()} if the
    * values cannot be resolved, including but not limited to the following cases:
    *
    * <pre>
    * <ul>
    *    <li>The property or any of the nested fields are not defined</li>
    *    <li>The property is unset</li>
    *    <li>The type of the property is not {@link DataTypes#INT}</li>
    *    <li>The property does not have a cardinality of {@link FieldCardinality#SINGLE}</li>
    *    <li>The property has an intermediate field of type {@link DataTypes#DATA} with cardinality of
    *    {@link FieldCardinality#MANY}</li>
    * </ul>
    * </pre>
    *
    * @param propertyName the name of the property
    * @param fieldNames   the optional names of the fields in the nested type
    * @return the integer value of the property, or {@link Optional#empty()} if the values cannot be determined
    */
   default Optional<BigInteger> resolveAsInteger(String propertyName, String... fieldNames) {
      return resolveAsPrimitive(propertyName, fieldNames)
            .filter(v -> v.getType() == DataTypes.INT)
            .map(IPropertyPrimitiveValue::getInteger);
   }

   /**
    * Attempts to resolve the decimal value of the property with the given name. Returns {@link Optional#empty()} if the
    * values cannot be resolved, including but not limited to the following cases:
    *
    * <pre>
    * <ul>
    *    <li>The property or any of the nested fields are not defined</li>
    *    <li>The property is unset</li>
    *    <li>The type of the property is not {@link DataTypes#FLOAT}</li>
    *    <li>The property does not have a cardinality of {@link FieldCardinality#SINGLE}</li>
    *    <li>The property has an intermediate field of type {@link DataTypes#DATA} with cardinality of
    *    {@link FieldCardinality#MANY}</li>
    * </ul>
    * </pre>
    *
    * @param propertyName the name of the property
    * @param fieldNames   the optional names of the fields in the nested type
    * @return the decimal value of the property, or {@link Optional#empty()} if the values cannot be determined
    */
   default Optional<BigDecimal> resolveAsDecimal(String propertyName, String... fieldNames) {
      return resolveAsPrimitive(propertyName, fieldNames)
            .filter(v -> v.getType() == DataTypes.FLOAT)
            .map(IPropertyPrimitiveValue::getDecimal);
   }

   /**
    * Attempts to resolve the boolean value of the property with the given name. Returns {@link Optional#empty()} if the
    * values cannot be resolved, including but not limited to the following cases:
    *
    * <pre>
    * <ul>
    *    <li>The property or any of the nested fields are not defined</li>
    *    <li>The property is unset</li>
    *    <li>The type of the property is not {@link DataTypes#BOOLEAN}</li>
    *    <li>The property does not have a cardinality of {@link FieldCardinality#SINGLE}</li>
    *    <li>The property has an intermediate field of type {@link DataTypes#DATA} with cardinality of
    *    {@link FieldCardinality#MANY}</li>
    * </ul>
    * </pre>
    *
    * @param propertyName the name of the property
    * @param fieldNames   the optional names of the fields in the nested type
    * @return the boolean value of the property, or {@link Optional#empty()} if the values cannot be determined
    */
   default Optional<Boolean> resolveAsBoolean(String propertyName, String... fieldNames) {
      return resolveAsPrimitive(propertyName, fieldNames)
            .filter(v -> v.getType() == DataTypes.BOOLEAN)
            .map(IPropertyPrimitiveValue::getBoolean);
   }

   /**
    * Attempts to resolve the string value of the property with the given name. Returns {@link Optional#empty()} if the
    * values cannot be resolved, including but not limited to the following cases:
    *
    * <pre>
    * <ul>
    *    <li>The property or any of the nested fields are not defined</li>
    *    <li>The property is unset</li> <li>The type of the property is not {@link DataTypes#STRING}</li>
    *    <li>The property does not have a cardinality of {@link FieldCardinality#SINGLE}</li>
    *    <li>The property has an intermediate field of type {@link DataTypes#DATA} with cardinality of
    *    {@link FieldCardinality#MANY}</li>
    * </ul>
    * </pre>
    *
    * @param propertyName the name of the property
    * @param fieldNames   the optional names of the fields in the nested type
    * @return the string value of the property, or {@link Optional#empty()} if the values cannot be determined
    */
   default Optional<String> resolveAsString(String propertyName, String... fieldNames) {
      return resolveAsPrimitive(propertyName, fieldNames)
            .filter(v -> v.getType() == DataTypes.STRING)
            .map(IPropertyPrimitiveValue::getString);
   }

   /**
    * Attempts to resolve the values of the property with the given name. Returns {@link Optional#empty()} if the values
    * cannot be resolved, including but not limited to the following cases:
    *
    * <pre>
    * <ul>
    *    <li>The property or any of the nested fields are not defined</li>
    *    <li>The property is unset</li>
    *    <li>The property does not have a cardinality of {@link FieldCardinality#MANY}</li>
    *    <li>The property has an intermediate field of type {@link DataTypes#DATA} with cardinality of
    *    {@link FieldCardinality#MANY}</li>
    * </ul>
    * </pre>
    *
    * @param propertyName the name of the property
    * @param fieldNames   the optional names of the fields in the nested type
    * @return the values of the property,
    */
   @SuppressWarnings({"unchecked"})
//   default IPropertyValues<? extends IPropertyValue> resolveValues(String propertyName, String... fieldNames) {
//      return PropertiesUtil.resolveCollection(this,
//                                              null, //__ -> true
//                                              p -> (IPropertyValues) p.getValues(), //IProperty::getValues
//                                              propertyName,
//                                              fieldNames);
//   }

   /**
    * Attempts to resolve the data values of the property with the given name. Returns {@link Optional#empty()} if the
    * values cannot be resolved, including but not limited to the following cases:
    *
    * <pre>
    * <ul>
    *    <li>The property or any of the nested fields are not defined</li>
    *    <li>The property is unset</li>
    *    <li>The type of the property is not {@link DataTypes#DATA}</li>
    *    <li>The property does not have a cardinality of {@link FieldCardinality#MANY}</li>
    *    <li>The property has an intermediate field of type {@link DataTypes#DATA} with
    *    cardinality of {@link FieldCardinality#MANY}</li>
    * </ul>
    * </pre>
    *
    * @param propertyName the name of the property
    * @param fieldNames   the optional names of the fields in the nested type
    * @return the data values of the property, or {@link Optional#empty()} if the values cannot be determined
    */
//   default Collection<IPropertyDataValue> resolveAsDatas(String propertyName, String... fieldNames) {
//      return PropertiesUtil.resolveCollection(this,
//                                              DataTypes.DATA::equals,
//                                              IProperty::getDatas,
//                                              IPropertyDataValue::getDatas,
//                                              propertyName,
//                                              fieldNames);
//   }

   /**
    * Attempts to resolve the enumeration values of the property with the given name. Returns {@link Optional#empty()}
    * if the values cannot be resolved, including but not limited to the following cases:
    *
    * <pre>
    * <ul>
    *    <li>The property or any of the nested fields are not defined</li>
    *    <li>The property is unset</li> <li>The type of the property is not {@link DataTypes#ENUM}</li>
    *    <li>The property does not have a cardinality of {@link FieldCardinality#MANY}</li>
    *    <li>The property has an intermediate field of type {@link DataTypes#DATA} with cardinality of
    *    {@link FieldCardinality#MANY}</li>
    * </ul>
    * </pre>
    *
    * @param propertyName the name of the property
    * @param fieldNames   the optional names of the fields in the nested type
    * @return the enumeration values of the property, or {@link Optional#empty()} if the values cannot be determined
    */
   default Collection<IPropertyEnumerationValue> resolveAsEnumerations(String propertyName,
                                                                       String... fieldNames) {
      return PropertiesUtil.resolveCollection(this,
                                              DataTypes.ENUM::equals,
                                              IProperty::getEnumerations,
                                              IPropertyDataValue::getEnumerations,
                                              propertyName,
                                              fieldNames);
   }

   /**
    * Attempts to resolve the primitive values of the property with the given name. Returns {@link Optional#empty()} if
    * the values cannot be resolved, including but not limited to the following cases:
    *
    * <pre>
    * <ul>
    *    <li>The property or any of the nested fields are not defined</li>
    *    <li>The property is unset</li>
    *    <li>The type of the property is not a primitive</li>
    *    <li>The property does not have a cardinality of {@link FieldCardinality#MANY}</li>
    *    <li>The property has an intermediate field of type {@link DataTypes#DATA} with cardinality of
    *    {@link FieldCardinality#MANY}</li>
    * </ul>
    * </pre>
    *
    * @param propertyName the name of the property
    * @param fieldNames   the optional names of the fields in the nested type
    * @return the primitive values of the property, or {@link Optional#empty()} if the values cannot be determined
    */
//   default Collection<IPropertyPrimitiveValue> resolveAsPrimitives(String propertyName,
//                                                                             String... fieldNames) {
//      return PropertiesUtil.resolveCollection(this,
//                                              type -> type != DataTypes.DATA && type != DataTypes.ENUM,
//                                              IProperty::getPrimitives,
//                                              IPropertyDataValue::getPrimitives,
//                                              propertyName,
//                                              fieldNames);
//   }

   /**
    * Attempts to resolve the integer values of the property with the given name. Returns {@link Optional#empty()} if
    * the values cannot be resolved, including but not limited to the following cases:
    *
    * <pre>
    * <ul>
    *    <li>The property or any of the nested fields are not defined</li>
    *    <li>The property is unset</li>
    *    <li>The type of the property is not {@link DataTypes#INT}</li>
    *    <li>The property does not have a cardinality of {@link FieldCardinality#MANY}</li>
    *    <li>The property has an intermediate field of type {@link DataTypes#DATA} with cardinality of
    *    {@link FieldCardinality#MANY}</li>
    * </ul>
    * </pre>
    *
    * @param propertyName the name of the property
    * @param fieldNames   the optional names of the fields in the nested type
    * @return the integer values of the property, or {@link Optional#empty()} if the values cannot be determined
    */
   default IPropertyValues<BigInteger> resolveAsIntegers(String propertyName, String... fieldNames) {
      final Function<Optional<Collection<IPropertyPrimitiveValue>>, Optional<Collection<BigInteger>>> fcn =
            primitives -> primitives.map(p -> p.stream()
                  .map(IPropertyPrimitiveValue::getInteger)
                  .collect(Collectors.toList()));
      return PropertiesUtil.resolveValues(this,
                                          p -> p.isPrimitive() && p.getType() == DataTypes.INT,
                                          p -> ((IPropertyPrimitiveValue) p).getInteger(),
                                          propertyName,
                                          fieldNames);
   }

   /**
    * Attempts to resolve the decimal values of the property with the given name. Returns {@link Optional#empty()} if
    * the values cannot be resolved, including but not limited to the following cases:
    *
    * <pre>
    * <ul>
    *    <li>The property or any of the nested fields are not defined</li>
    *    <li>The property is unset</li>
    *    <li>The type of the property is not {@link DataTypes#FLOAT}</li>
    *    <li>The property does not have a cardinality of {@link FieldCardinality#MANY}</li>
    *    <li>The property has an intermediate field of type {@link DataTypes#DATA} with cardinality of
    *    {@link FieldCardinality#MANY}</li>
    * </ul>
    * </pre>
    *
    * @param propertyName the name of the property
    * @param fieldNames   the optional names of the fields in the nested type
    * @return the decimal values of the property, or {@link Optional#empty()} if the values cannot be determined
    */
//   default Optional<Collection<BigDecimal>> resolveAsDecimals(String propertyName, String... fieldNames) {
//      final Function<Optional<Collection<IPropertyPrimitiveValue>>, Optional<Collection<BigDecimal>>> fcn =
//            primitives -> primitives.map(p -> p.stream()
//                  .map(IPropertyPrimitiveValue::getDecimal)
//                  .collect(Collectors.toList()));
//      return PropertiesUtil.resolveCollection(this,
//                                              DataTypes.FLOAT::equals,
//                                              property -> fcn.apply(property.getPrimitives()),
//                                              (dataValue, field) -> fcn.apply(dataValue.getPrimitives(field)),
//                                              propertyName,
//                                              fieldNames);
//   }

   /**
    * Attempts to resolve the boolean values of the property with the given name. Returns {@link Optional#empty()} if
    * the values cannot be resolved, including but not limited to the following cases:
    *
    * <pre>
    * <ul>
    *    <li>The property or any of the nested fields are not defined</li>
    *    <li>The property is unset</li>
    *    <li>The type of the property is not {@link DataTypes#BOOLEAN}</li>
    *    <li>The property does not have a cardinality of {@link FieldCardinality#MANY}</li>
    *    <li>The property has an intermediate field of type {@link DataTypes#DATA} with cardinality of
    *    {@link FieldCardinality#MANY}</li>
    * </ul>
    * </pre>
    *
    * @param propertyName the name of the property
    * @param fieldNames   the optional names of the fields in the nested type
    * @return the boolean values of the property, or {@link Optional#empty()} if the values cannot be determined
    */
//   default Optional<Collection<Boolean>> resolveAsBooleans(String propertyName, String... fieldNames) {
//      final Function<Optional<Collection<IPropertyPrimitiveValue>>, Optional<Collection<Boolean>>> fcn =
//            primitives -> primitives.map(p -> p.stream()
//            .map(IPropertyPrimitiveValue::getBoolean)
//            .collect(Collectors.toList()));
//      return PropertiesUtil.resolveCollection(this,
//                                              DataTypes.BOOLEAN::equals,
//                                              property -> fcn.apply(property.getPrimitives()),
//                                              (dataValue, field) -> fcn.apply(dataValue.getPrimitives(field)),
//                                              propertyName,
//                                              fieldNames);
//   }

   /**
    * Attempts to resolve the string values of the property with the given name. Returns {@link Optional#empty()} if the
    * values cannot be resolved, including but not limited to the following cases:

    * <pre>
    * <ul>
    *    <li>The property or any of the nested fields are not defined</li>
    *    <li>The property is unset</li> <li>The type of the property is not {@link DataTypes#STRING}</li>
    *    <li>The property does not have a cardinality of {@link FieldCardinality#MANY}</li>
    *    <li>The property has an intermediate field of type {@link DataTypes#DATA} with cardinality of
    *    {@link FieldCardinality#MANY}</li>
    * </ul>
    * </pre>
    *
    * @param propertyName the name of the property
    * @param fieldNames   the optional names of the fields in the nested type
    * @return the string values of the property, or {@link Optional#empty()} if the values cannot be determined
    */
//   default Optional<Collection<String>> resolveAsStrings(String propertyName, String... fieldNames) {
//      final Function<Optional<Collection<IPropertyPrimitiveValue>>, Optional<Collection<String>>> fcn =
//            primitives -> primitives.map(p -> p.stream()
//            .map(IPropertyPrimitiveValue::getString)
//            .collect(Collectors.toList()));
//      return PropertiesUtil.resolveCollection(this,
//                                              DataTypes.STRING::equals,
//                                              property -> fcn.apply(property.getPrimitives()),
//                                              (dataValue, field) -> fcn.apply(dataValue.getPrimitives(field)),
//                                              propertyName,
//                                              fieldNames);
//   }
}

