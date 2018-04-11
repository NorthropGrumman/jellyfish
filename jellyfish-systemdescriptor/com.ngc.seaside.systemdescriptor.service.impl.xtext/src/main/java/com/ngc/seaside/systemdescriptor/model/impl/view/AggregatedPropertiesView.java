package com.ngc.seaside.systemdescriptor.model.impl.view;

import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.link.IModelLink;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperties;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperty;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyDataValue;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyValue;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.properties.Properties;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.properties.Property;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.properties.PropertyDataValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Serves as a factory for aggregating properties for model objects.
 */
public class AggregatedPropertiesView {

   private AggregatedPropertiesView() {}

   /**
    * Gets an instance of {@code IProperties} that contains all the properties from the given model and its refined
    * types.
    */
   public static IProperties getAggregatedProperties(IModel model) {
      return getAggregatedProperties(model, m -> m.getRefinedModel().orElse(null), IModel::getProperties);
   }

   /**
    * Gets an instance of {@code IProperties} that contains all the properties from the given part or required field and
    * its extended types.
    */
   public static IProperties getAggregatedProperties(IModelReferenceField field) {
      return getAggregatedProperties(field, f -> f.getRefinedField().orElse(null), IModelReferenceField::getProperties);
   }

   /**
    * Gets an instance of {@code IProperties} that contains all the properties from the given link and its extended
    * types.
    */
   public static IProperties getAggregatedProperties(IModelLink<?> link) {
      return getAggregatedProperties(link, l -> l.getRefinedLink().orElse(null), IModelLink::getProperties);
   }

   /**
    * Gets an instance of {@code IProperties} that contains all the properties from the given object and its extended
    * types.
    *
    * @param initial initial value
    * @param parentFunction function to get the refined/parent value from the current value
    * @param propertiesFunction function to get the properties from the current value
    * @return the aggregated properties for the type
    */
   private static <T> IProperties getAggregatedProperties(T initial,
            Function<T, T> parentFunction,
            Function<T, IProperties> propertiesFunction) {
      Properties properties = new Properties();
      T current = initial;

      while (current != null) {
         for (IProperty property : propertiesFunction.apply(current)) {
            if (!properties.hasProperty(property.getName())) {
               properties.add(getAggregatedProperty(property));
            }
         }
         current = parentFunction.apply(current);
      }

      return properties;
   }

   private static IProperty getAggregatedProperty(IProperty property) {
      if (property.getType() != DataTypes.DATA) {
         return property;
      }

      final Collection<IPropertyValue> aggregatedValues;
      switch (property.getCardinality()) {
         case MANY:
            aggregatedValues = new ArrayList<>();
            for (IPropertyValue value : property.getValues()) {
               aggregatedValues.add(getAggregatedPropertyValue((IPropertyDataValue) value));
            }
            break;
         case SINGLE:
            aggregatedValues = Collections.singleton(
               getAggregatedPropertyValue((IPropertyDataValue) property.getValue()));
            break;
         default:
            throw new IllegalStateException("Unknown cardinality: " + property.getCardinality());
      }
      Property aggregated = new Property(
            property.getName(),
            property.getType(),
            property.getCardinality(),
            aggregatedValues,
            new AggregatedDataView(property.getReferencedDataType()));

      return aggregated;
   }

   private static IPropertyDataValue getAggregatedPropertyValue(IPropertyDataValue value) {
      Map<String, Collection<? extends IPropertyValue>> aggregatedFields = new LinkedHashMap<>();
      IData data;
      try {
         data = new AggregatedDataView(value.getReferencedDataType());
      } catch (IllegalStateException e) {
         return value;
      }
      for (IDataField field : data.getFields()) {
         Collection<? extends IPropertyValue> fieldValues;
         switch (field.getCardinality()) {
            case MANY:
               fieldValues = value.getValues(field);
               break;
            case SINGLE:
               fieldValues = Collections.singleton(value.getValue(field));
               break;
            default:
               throw new IllegalStateException("Unknown cardinality: " + field.getCardinality());
         }
         switch (field.getType()) {
            case DATA:
               Collection<IPropertyDataValue> dataValues = new ArrayList<>(fieldValues.size());
               for (IPropertyValue fieldValue : fieldValues) {
                  dataValues.add(getAggregatedPropertyValue((IPropertyDataValue) fieldValue));
               }
               fieldValues = dataValues;
               break;
            default:
               break;
         }
         aggregatedFields.put(field.getName(), fieldValues);
      }

      PropertyDataValue aggregated = new PropertyDataValue(data, aggregatedFields);
      return aggregated;
   }
}
