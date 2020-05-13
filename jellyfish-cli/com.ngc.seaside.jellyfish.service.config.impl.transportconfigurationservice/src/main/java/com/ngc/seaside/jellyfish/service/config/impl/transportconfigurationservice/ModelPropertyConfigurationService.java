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
package com.ngc.seaside.jellyfish.service.config.impl.transportconfigurationservice;

import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IParameter;
import com.ngc.seaside.jellyfish.service.config.api.IModelPropertyConfigurationService;
import com.ngc.seaside.systemdescriptor.model.api.FieldCardinality;
import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperties;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperty;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyDataValue;
import com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Abstract class for getting the configurations of a system descriptor property.
 * 
 * @param <T> configuration type
 */
public abstract class ModelPropertyConfigurationService<T> implements IModelPropertyConfigurationService<T> {

   private ISystemDescriptorService sdService;

   @Override
   public Collection<T> getConfigurations(IJellyFishCommandOptions options, IModel model) {
      IModel aggregatedModel = sdService.getAggregatedView(model);
      Collection<T> configurations = new LinkedHashSet<>();
      configurations.addAll(getPropertyConfigurations(aggregatedModel::getProperties,
               this::isConfigurationProperty,
               this::convert,
               () -> String.format("Configuration is not completely set for model %s",
                        aggregatedModel.getFullyQualifiedName())));

      Collection<IModelReferenceField> parts = getOptionalDeploymentModel(options)
               .map(sdService::getAggregatedView)
               .map(deploymentModel -> (Collection<IModelReferenceField>) deploymentModel.getParts())
               .orElse(Collections.emptySet());

      for (IModelReferenceField part : parts) {
         if (Objects.equals(part.getType().getFullyQualifiedName(), model.getFullyQualifiedName())) {
            configurations.addAll(getPropertyConfigurations(part::getProperties,
                     this::isConfigurationProperty,
                     this::convert,
                     () -> String.format("Configuration is not completely set part %s in deployment model",
                              part.getName())));
         }
      }

      return configurations;
   }

   public void setSystemDescriptorService(ISystemDescriptorService ref) {
      this.sdService = ref;
   }

   public void removeSystemDescriptorService(ISystemDescriptorService ref) {
      setSystemDescriptorService(null);
   }

   /**
    * Helper method for getting a field from a property data value.
    * 
    * @param value property data value
    * @param fieldName field name
    * @return data field
    */
   protected IDataField getField(IPropertyDataValue value, String fieldName) {
      return value.getFieldByName(fieldName)
               .orElseThrow(() -> new IllegalStateException("Missing " + fieldName + " field"));
   }

   /**
    * Returns true if this configuration service handles system descriptor properties with the given fully-qualified
    * name.
    * 
    * @return true if this configuration service handles system descriptor properties with the given fully-qualified
    *         name
    */
   protected abstract boolean isConfigurationProperty(String qualifiedName);

   /**
    * Converts the given property to a configuration.
    * 
    * @param value property
    * @return configuration
    */
   protected abstract T convert(IPropertyDataValue value);

   /**
    * Returns the collection of configurations from the provided properties for a given type.
    * 
    * @param propertiesSupplier supplier for properties
    * @param qualifiedNamePredicate predicate for accepting a qualified name of property type
    * @param function function to convert property to the desired value configuration type
    * @param notSetErrorMessage error message if property is not set
    * @return collection of configurations
    */
   private static <T> Collection<T> getPropertyConfigurations(Supplier<IProperties> propertiesSupplier,
            Predicate<String> qualifiedNamePredicate,
            Function<IPropertyDataValue, T> function,
            Supplier<String> notSetErrorMessage) {
      Collection<IPropertyDataValue> propertyValues = propertiesSupplier.get()
               .stream()
               .filter(property -> DataTypes.DATA == property.getType())
               .filter(property -> qualifiedNamePredicate.test(
                        property.getReferencedDataType().getFullyQualifiedName()))
               .filter(
                        property -> FieldCardinality.SINGLE == property.getCardinality())
               .map(IProperty::getData)
               .collect(Collectors.toList());
      Collection<T> configurations = new ArrayList<>(propertyValues.size());
      for (IPropertyDataValue value : propertyValues) {
         if (!value.isSet()) {
            throw new IllegalStateException(notSetErrorMessage.get());
         }
         configurations.add(function.apply(value));
      }
      return configurations;
   }

   /**
    * Gets the deployment model from the options, or {@link Optional#empty()} if there is none.
    * 
    * @param options jellyfish command options
    * @return deployment model
    */
   private static Optional<IModel> getOptionalDeploymentModel(IJellyFishCommandOptions options) {
      IParameter<?> deploymentModelParameter = options.getParameters()
            .getParameter(CommonParameters.DEPLOYMENT_MODEL.getName());
      if (deploymentModelParameter == null || deploymentModelParameter.getValue() == null) {
         return Optional.empty();
      }
      String deploymentModel = deploymentModelParameter.getStringValue();
      if (deploymentModel == null) {
         return Optional.empty();
      }
      return options.getSystemDescriptor().findModel(deploymentModel);
   }
   
}
