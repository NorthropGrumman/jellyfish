package com.ngc.seaside.systemdescriptor.model.impl.view;

import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.link.IModelLink;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperties;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperty;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.properties.Properties;

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
   @SuppressWarnings({"unchecked", "rawtypes"})
   public static IProperties getAggregatedProperties(IModelLink<?> link) {
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
            if (!properties.hasProperty(property.getName())) {
               properties.add(property);
            }
         }
         current = parentFunction.apply(current);
      }

      return properties;
   }
}
