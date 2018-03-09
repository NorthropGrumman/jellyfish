package com.ngc.seaside.systemdescriptor.model.impl.view;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.ngc.seaside.systemdescriptor.model.api.FieldCardinality;
import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperties;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperty;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyDataValue;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyValue;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.properties.Properties;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.properties.Property;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.properties.PropertyDataValue;

/**
 * Serves as a factory for aggregating properties for model objects.
 */
public class AggregatedPropertiesView {

   private AggregatedPropertiesView() {}

   /**
    * Gets an instance of {@code IProperties} that contains all the properties from the given object and its extended
    * types.
    */
   public static IProperties getAggregatedProperties(IModel model) {
      Properties properties = new Properties();

      while (true) {
         for (IProperty property : model.getProperties()) {
            Optional<IProperty> newerPropertyOptional = properties.getByName(property.getName());
            if (newerPropertyOptional.isPresent()) {
               IProperty newerProperty = newerPropertyOptional.get();
               if (newerProperty.getType() == DataTypes.DATA
                  && newerProperty.getCardinality() == FieldCardinality.SINGLE) {
                  IPropertyDataValue merged = merge((IPropertyDataValue) property.getData(),
                     (IPropertyDataValue) newerProperty.getData());
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
         Optional<IModel> refined = model.getRefinedModel();
         if (!refined.isPresent()) {
            break;
         }
         model = refined.get();
      }

      return properties;
   }

   /**
    * Merges the two values, where any fields set in the {@code newProperty} will overwrite those set in {@code baseProperty}.
    * 
    * <p>
    * Currently, data fields with cardinality many cannot be merged, the new property (if set for the field), will override the base property.
    * 
    * @param baseProperty
    * @param newProperty
    * @return the merged IPropertyDataValue
    */
   private static IPropertyDataValue merge(IPropertyDataValue baseProperty, IPropertyDataValue newProperty) {
      IData data = newProperty.getReferencedDataType();
      Map<String, Collection<IPropertyValue>> map = new HashMap<>();
      for (IDataField field : data.getFields()) {
         String name = field.getName();
         if (field.getCardinality() == FieldCardinality.MANY) {
            Optional<Collection<IPropertyValue>> newValues = newProperty.getValues(field);
            Optional<Collection<IPropertyValue>> baseValues = baseProperty.getValues(field);
            if (newValues.isPresent()) {
               map.put(name, newValues.get());
            } else if (baseValues.isPresent()) {
               map.put(name, baseValues.get());
            } else {
               map.put(name, null);
            }
         } else if (field.getType() == DataTypes.DATA) {
            IPropertyDataValue newValue = newProperty.getData(field);
            IPropertyDataValue baseValue = baseProperty.getData(field);
            IPropertyDataValue mergedValue = merge(baseValue, newValue);
            map.put(name, Collections.singletonList(mergedValue));
         } else {
            IPropertyValue newValue = newProperty.getValue(field);
            IPropertyValue baseValue = baseProperty.getValue(field);
            if (newValue.isSet() || !baseValue.isSet()) {
               map.put(name, Collections.singletonList(newValue));
            } else {
               map.put(name, Collections.singletonList(baseValue));
            }
         }
      }
      return new PropertyDataValue(data, map);
   }

}
