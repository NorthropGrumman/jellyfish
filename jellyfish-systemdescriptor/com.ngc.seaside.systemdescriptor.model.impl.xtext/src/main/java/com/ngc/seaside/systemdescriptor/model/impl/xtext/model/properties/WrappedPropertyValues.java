package com.ngc.seaside.systemdescriptor.model.impl.xtext.model.properties;

import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyDataValue;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyEnumerationValue;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyPrimitiveValue;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyValues;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.systemDescriptor.EnumPropertyValue;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Properties;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PropertyValueAssignment;

import java.util.Collections;
import java.util.Optional;

public class WrappedPropertyValues {

   private WrappedPropertyValues() {
   }

   public static IPropertyValues<IPropertyPrimitiveValue> getValues(IWrapperResolver resolver,
                                                                    WrappedPrimitiveProperty property) {
      IPropertyValues<IPropertyPrimitiveValue> values = IPropertyValues.emptyPropertyValues();

      Optional<PropertyValueAssignment> assignment = getAssignment(property);
      if (assignment.isPresent()) {
         values = IPropertyValues.of(Collections.singleton(new WrappedPrimitivePropertyValue(
               resolver,
               assignment.get().getValue())));
      }

      return values;
   }

   public static IPropertyValues<IPropertyEnumerationValue> getValues(IWrapperResolver resolver,
                                                                      WrappedEnumerationProperty property) {
      IPropertyValues<IPropertyEnumerationValue> values = IPropertyValues.emptyPropertyValues();

      Optional<PropertyValueAssignment> assignment = getAssignment(property);
      if (assignment.isPresent()) {
         values = IPropertyValues.of(Collections.singleton(new WrappedEnumerationPropertyValue(
               resolver,
               (EnumPropertyValue) assignment.get().getValue())));
      }

      return values;
   }

   public static IPropertyValues<IPropertyDataValue> getValues(IWrapperResolver resolver,
                                                               WrappedDataProperty property) {
      return null;
   }

   private static Optional<PropertyValueAssignment> getAssignment(AbstractWrappedProperty<?> property) {
      return ((Properties) property.unwrap().eContainer()).getAssignments()
            .stream()
            .filter(a -> a.getExpression().getDeclaration().equals(property.unwrap()))
            .findFirst();
   }
}
