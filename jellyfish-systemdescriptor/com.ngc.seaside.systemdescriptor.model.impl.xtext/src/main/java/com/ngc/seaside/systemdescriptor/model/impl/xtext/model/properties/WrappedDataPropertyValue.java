package com.ngc.seaside.systemdescriptor.model.impl.xtext.model.properties;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.SystemDescriptors;
import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyDataValue;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyEnumerationValue;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyPrimitiveValue;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyValues;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.exception.UnrecognizedXtextTypeException;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Data;
import com.ngc.seaside.systemdescriptor.systemDescriptor.DataFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.DataModel;
import com.ngc.seaside.systemdescriptor.systemDescriptor.EnumPropertyValue;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Properties;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PropertyValueAssignment;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PropertyValueExpressionPathSegment;
import com.ngc.seaside.systemdescriptor.systemDescriptor.ReferencedDataModelFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.ReferencedPropertyFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class WrappedDataPropertyValue implements IPropertyDataValue {

   private final IWrapperResolver resolver;
   private final ReferencedPropertyFieldDeclaration propertyDeclaration;

   public WrappedDataPropertyValue(IWrapperResolver resolver, ReferencedPropertyFieldDeclaration propertyDeclaration) {
      this.resolver = Preconditions.checkNotNull(resolver, "resolver may not be null!");
      this.propertyDeclaration = Preconditions.checkNotNull(propertyDeclaration,
                                                            "propertyDeclaration may not be null!");
      Preconditions.checkArgument(propertyDeclaration.getDataModel() instanceof Data,
                                  "propertyDeclaration must reference a Data object!");
   }

   @Override
   public IData getReferencedDataType() {
      return resolver.getWrapperFor((Data) propertyDeclaration.getDataModel());
   }

   @Override
   public IPropertyPrimitiveValue getPrimitive(IDataField field) {
      Preconditions.checkNotNull(field, "field may not be null!");
      Preconditions.checkArgument(SystemDescriptors.isPrimitiveDataFieldDeclaration(field),
                                  "cannot get the primitive value of a field whose type is %s!",
                                  field.getType());
      Preconditions.checkState(isSet(), "this value is not set!");

      IPropertyPrimitiveValue value = UnsetProperties.UNSET_PRIMITIVE_VALUE;
      Optional<PropertyValueAssignment> optional = getAssignmentFor(Collections.singleton(field.getName()));
      if (optional.isPresent()) {
         value = new WrappedPrimitivePropertyValue(resolver, optional.get().getValue());
      }

      return value;
   }

   @Override
   public IPropertyEnumerationValue getEnumeration(IDataField field) {
      Preconditions.checkNotNull(field, "field may not be null!");
      Preconditions.checkArgument(field.getType() == DataTypes.ENUM,
                                  "cannot get the enum value of a field whose type is %s!",
                                  field.getType());
      Preconditions.checkState(isSet(), "this value is not set!");

      IPropertyEnumerationValue value = UnsetProperties.UNSET_ENUMERATION_VALUE;
      Optional<PropertyValueAssignment> optional = getAssignmentFor(Collections.singleton(field.getName()));
      if (optional.isPresent()) {
         value = new WrappedEnumerationPropertyValue(resolver, (EnumPropertyValue) optional.get().getValue());
      }

      return value;
   }

   @Override
   public IPropertyDataValue getData(IDataField field) {
      Preconditions.checkNotNull(field, "field may not be null!");
      Preconditions.checkArgument(field.getType() == DataTypes.DATA,
                                  "cannot get the data value of a field whose type is %s!",
                                  field.getType());
      Preconditions.checkState(isSet(), "this value is not set!");

      IPropertyDataValue value = UnsetProperties.UNSET_DATA_VALUE;
      Collection<String> fieldNames = Collections.singleton(field.getName());
      Optional<PropertyValueAssignment> optional = getAssignmentFor(fieldNames);
      if (optional.isPresent()) {
         List<PropertyValueExpressionPathSegment> segments = optional.get().getExpression().getPathSegments();
         value = new NestedDataPropertyValue(
               resolver.getWrapperFor((Data) segments.get(segments.size() - 1).getFieldDeclaration().eContainer()),
               fieldNames);
      }
      return value;
   }

   @Override
   public IPropertyValues<IPropertyPrimitiveValue> getPrimitives(IDataField field) {
      throw new UnsupportedOperationException("cardinality of many not currently supported!");
   }

   @Override
   public IPropertyValues<IPropertyEnumerationValue> getEnumerations(IDataField field) {
      throw new UnsupportedOperationException("cardinality of many not currently supported!");
   }

   @Override
   public IPropertyValues<IPropertyDataValue> getDatas(IDataField field) {
      throw new UnsupportedOperationException("cardinality of many not currently supported!");
   }

   @Override
   public DataTypes getType() {
      return DataTypes.DATA;
   }

   @Override
   public boolean isSet() {
      return doIsSet(propertyDeclaration.getDataModel(), Collections.emptyList());
   }

   private boolean doIsSet(DataModel dataModel, Collection<String> fieldNames) {
      boolean set;

      switch (dataModel.eClass().getClassifierID()) {
         case SystemDescriptorPackage.DATA:
            set = doIsSet((Data) dataModel, fieldNames);
            break;
         case SystemDescriptorPackage.ENUMERATION:
            set = getAssignmentFor(fieldNames).isPresent();
            break;
         default:
            throw new UnrecognizedXtextTypeException(dataModel);
      }

      return set;
   }

   private boolean doIsSet(Data data, Collection<String> fieldNames) {
      boolean set = true;

      for (DataFieldDeclaration field : data.getFields()) {
         switch (field.eClass().getClassifierID()) {
            case SystemDescriptorPackage.PRIMITIVE_DATA_FIELD_DECLARATION:
               set &= getAssignmentFor(appendTo(fieldNames, field.getName())).isPresent();
               break;
            case SystemDescriptorPackage.REFERENCED_DATA_MODEL_FIELD_DECLARATION:
               set &= doIsSet(((ReferencedDataModelFieldDeclaration) field).getDataModel(),
                              appendTo(fieldNames, field.getName()));
               break;
            default:
               throw new UnrecognizedXtextTypeException(field);
         }
      }

      return set;
   }

   private Optional<PropertyValueAssignment> getAssignmentFor(Collection<String> fieldNames) {
      String flatPath = fieldNames.stream().collect(Collectors.joining("."));
      Properties properties = (Properties) propertyDeclaration.eContainer();
      return properties.getAssignments()
            .stream()
            .filter(a -> a.getExpression().getDeclaration().equals(propertyDeclaration))
            .filter(a -> arePathsSame(flatPath, a.getExpression().getPathSegments()))
            .findFirst();
   }

   private static Collection<String> appendTo(Collection<String> collection, String value) {
      Collection<String> copy = new ArrayList<>(collection);
      copy.add(value);
      return copy;
   }

   private static boolean arePathsSame(String flatPath,
                                       Collection<PropertyValueExpressionPathSegment> segments) {
      String flatSegmentPath = segments.stream()
            .map(s -> s.getFieldDeclaration().getName())
            .collect(Collectors.joining("."));
      return flatPath.equals(flatSegmentPath);
   }

   private class NestedDataPropertyValue implements IPropertyDataValue {

      private final IData data;
      private final Collection<String> paths;

      private NestedDataPropertyValue(IData data, Collection<String> paths) {
         this.data = data;
         this.paths = paths;
      }

      @Override
      public IData getReferencedDataType() {
         return data;
      }

      @Override
      public IPropertyPrimitiveValue getPrimitive(IDataField field) {
         Preconditions.checkNotNull(field, "field may not be null!");
         Preconditions.checkArgument(SystemDescriptors.isPrimitiveDataFieldDeclaration(field),
                                     "cannot get the primitive value of a field whose type is %s!",
                                     field.getType());

         IPropertyPrimitiveValue value = UnsetProperties.UNSET_PRIMITIVE_VALUE;
         Optional<PropertyValueAssignment> optional = getAssignmentFor(appendTo(paths, field.getName()));
         if (optional.isPresent()) {
            value = new WrappedPrimitivePropertyValue(resolver, optional.get().getValue());
         }

         return value;
      }

      @Override
      public IPropertyEnumerationValue getEnumeration(IDataField field) {
         throw new UnsupportedOperationException("not implemented");
      }

      @Override
      public IPropertyDataValue getData(IDataField field) {
         throw new UnsupportedOperationException("not implemented");
      }

      @Override
      public IPropertyValues<IPropertyPrimitiveValue> getPrimitives(IDataField field) {
         throw new UnsupportedOperationException("cardinality of many not currently supported!");
      }

      @Override
      public IPropertyValues<IPropertyEnumerationValue> getEnumerations(IDataField field) {
         throw new UnsupportedOperationException("cardinality of many not currently supported!");
      }

      @Override
      public IPropertyValues<IPropertyDataValue> getDatas(IDataField field) {
         throw new UnsupportedOperationException("cardinality of many not currently supported!");
      }

      @Override
      public DataTypes getType() {
         return DataTypes.DATA;
      }

      @Override
      public boolean isSet() {
         return true;
      }
   }
}
