package com.ngc.seaside.systemdescriptor.model.impl.view;

import com.ngc.seaside.systemdescriptor.model.api.FieldCardinality;
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
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyValues;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.properties.Properties;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.properties.Property;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.properties.PropertyDataValue;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * Serves as a factory for aggregating properties for model objects.
 */
public class AggregatedPropertiesView {

   private AggregatedPropertiesView() {
   }

   /**
    * Gets an instance of {@code IProperties} that contains all the properties from the given model and its refined
    * types.
    */
   public static IProperties getAggregatedProperties(IModel model) {
      System.out.println("Begin for model");
      return getAggregatedProperties(model, m -> m.getRefinedModel().orElse(null), IModel::getProperties);
   }

   /**
    * Gets an instance of {@code IProperties} that contains all the properties from the given part or required field and
    * its extended types.
    */
   public static IProperties getAggregatedProperties(IModelReferenceField field) {
      System.out.println("Begin for model ref field");
      return getAggregatedProperties(field, f -> f.getRefinedField().orElse(null), IModelReferenceField::getProperties);
   }

   /**
    * Gets an instance of {@code IProperties} that contains all the properties from the given link and its extended
    * types.
    */
   @SuppressWarnings({"unchecked", "rawtypes"})
   public static IProperties getAggregatedProperties(IModelLink<?> link) {
      System.out.println("Begin for link");
      return getAggregatedProperties(link, l -> l.getRefinedLink().orElse(null), IModelLink::getProperties);
   }

   /**
    * Gets an instance of {@code IProperties} that contains all the properties from the given object and its extended
    * types.
    *
    * @param initial            initial value
    * @param parentFunction     function to get the refined/parent value from the current value
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
            // TODO TH: Remove this
            System.out.println("Found declaratoin " + property.getName());

            Optional<IProperty> newerPropertyOptional = properties.getByName(property.getName());
            if (newerPropertyOptional.isPresent()) {
               IProperty newerProperty = newerPropertyOptional.get();
               if (newerProperty.getType() == DataTypes.DATA
                   && newerProperty.getCardinality() == FieldCardinality.SINGLE) {
                  IPropertyDataValue merged = merge(property.getData(), newerProperty.getData());
                  Property mergedProperty = new Property(newerProperty.getName(),
                                                         newerProperty.getType(),
                                                         newerProperty.getCardinality(),
                                                         Collections.singletonList(merged),
                                                         newerProperty.getReferencedDataType());
                  mergedProperty.setProperties(newerProperty.getParent());
                  properties.add(mergedProperty);
               }
            } else {
               properties.add(property);
            }
         }
         current = parentFunction.apply(current);
      }

      return properties;
   }

   /**
    * Merges the two values, where any fields set in the {@code newProperty} will overwrite those set in {@code
    * baseProperty}.
    *
    * <p> Currently, data fields with cardinality many cannot be merged, the new property (if set for the field), will
    * override the base property.
    *
    * @return the merged IPropertyDataValue
    */
   private static IPropertyDataValue merge(IPropertyDataValue baseProperty, IPropertyDataValue newProperty) {
      IData data = newProperty.getReferencedDataType();
      Map<String, Collection<IPropertyValue>> map = new HashMap<>();
      for (IDataField field : data.getFields()) {
         String name = field.getName();
         if (field.getCardinality() == FieldCardinality.MANY) {
            @SuppressWarnings({"unchecked"})
            IPropertyValues<IPropertyValue> newValues =
                  (IPropertyValues<IPropertyValue>) newProperty.getValues(field);
            @SuppressWarnings({"unchecked"})
            IPropertyValues<IPropertyValue> baseValues =
                  (IPropertyValues<IPropertyValue>) baseProperty.getValues(field);
            if (newValues.isSet()) {
               map.put(name, newValues);
            } else if (baseValues.isSet()) {
               map.put(name, baseValues);
            } else {
               map.put(name, null);
            }
         } else if (field.getType() == DataTypes.DATA) {
            IPropertyDataValue newValue = newProperty.getData(field);
            IPropertyDataValue baseValue = baseProperty.getData(field);
            IPropertyDataValue mergedValue = merge(baseValue, newValue);
            map.put(name, Collections.singleton(mergedValue));
         } else {
            IPropertyValue newValue = newProperty.getValue(field);
            IPropertyValue baseValue = baseProperty.getValue(field);
            if (newValue.isSet() || !baseValue.isSet()) {
               map.put(name, Collections.singleton(newValue));
            } else {
               map.put(name, Collections.singleton(baseValue));
            }
         }
      }
      return new PropertyDataValue(data, map);
   }

}
