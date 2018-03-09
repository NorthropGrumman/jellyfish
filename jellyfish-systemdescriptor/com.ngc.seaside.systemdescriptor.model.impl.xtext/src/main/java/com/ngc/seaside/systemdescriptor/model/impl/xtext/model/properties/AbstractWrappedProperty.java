package com.ngc.seaside.systemdescriptor.model.impl.xtext.model.properties;

import java.util.Collection;

import com.google.common.base.Preconditions;
import com.ngc.seaside.systemdescriptor.model.api.FieldCardinality;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperties;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperty;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyDataValue;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyEnumerationValue;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyPrimitiveValue;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtext;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.util.ConversionUtil;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Data;
import com.ngc.seaside.systemdescriptor.systemDescriptor.DataModel;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Enumeration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PrimitivePropertyFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Properties;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PropertyFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.ReferencedPropertyFieldDeclaration;

public abstract class AbstractWrappedProperty<T extends PropertyFieldDeclaration> extends AbstractWrappedXtext<T>
         implements IProperty {

   public AbstractWrappedProperty(IWrapperResolver resolver, T wrapped) {
      super(resolver, wrapped);
   }

   @Override
   public String getName() {
      return wrapped.getName();
   }

   @Override
   public IProperties getParent() {
      return resolver.getWrapperFor((Properties) wrapped.eContainer());
   }

   @Override
   public FieldCardinality getCardinality() {
      return ConversionUtil.convertCardinalityFromXtext(wrapped.getCardinality());
   }

   @Override
   public IData getReferencedDataType() {
      throw new IllegalStateException("property is not a data type");
   }

   @Override
   public IEnumeration getReferencedEnumeration() {
      throw new IllegalStateException("property is not an enumeration type");
   }


   @Override
   public IPropertyDataValue getData() {
      throw new IllegalStateException("property is not a data type");
   }

   @Override
   public IPropertyEnumerationValue getEnumeration() {
      throw new IllegalStateException("property is not an enumeration type");
   }

   @Override
   public IPropertyPrimitiveValue getPrimitive() {
      throw new IllegalStateException("property is not a primitive type");
   }

   @Override
   public Collection<IPropertyDataValue> getDatas() {
      throw new IllegalStateException("property is not a data type");
   }

   @Override
   public Collection<IPropertyEnumerationValue> getEnumerations() {
      throw new IllegalStateException("property is not an enumeration type");
   }

   @Override
   public Collection<IPropertyPrimitiveValue> getPrimitives() {
      throw new IllegalStateException("property is not a primitive type");
   }

   public static AbstractWrappedProperty<? extends PropertyFieldDeclaration> getWrappedPropertiesFieldReference(IWrapperResolver resolver, PropertyFieldDeclaration property) {
      Preconditions.checkNotNull(resolver, "resolver must not be null!");
      Preconditions.checkNotNull(property, "property must not be null!");
      if (property instanceof PrimitivePropertyFieldDeclaration) {
         return new WrappedPrimitiveProperty(resolver, (PrimitivePropertyFieldDeclaration) property);
      } else if (property instanceof ReferencedPropertyFieldDeclaration) {
         ReferencedPropertyFieldDeclaration referencedProperty = (ReferencedPropertyFieldDeclaration) property;
         DataModel dataModel = referencedProperty.getDataModel();
         if (dataModel instanceof Data) {
            return new WrappedDataProperty(resolver, referencedProperty);
         } else if (dataModel instanceof Enumeration) {
            return new WrappedEnumerationProperty(resolver, referencedProperty);
         } else {
            throw new IllegalStateException("Unknown data model tpye: " + dataModel);
         }
      } else {
         throw new IllegalStateException("Unknown property field declaration: " + property);
      }
   }

   public static PropertyFieldDeclaration toXTextPartDeclaration(IWrapperResolver resolver, IProperty property) {
      Preconditions.checkNotNull(resolver, "resolver must not be null!");
      Preconditions.checkNotNull(property, "property must not be null!");
      switch(property.getType()) {
      case DATA:
         return WrappedDataProperty.toXtextReferencedPropertyFieldDeclaration(resolver, property);
      case ENUM:
         return WrappedEnumerationProperty.toXtextReferencedPropertyFieldDeclaration(resolver, property);
      default:
         return WrappedPrimitiveProperty.toXtextPrimitivePropertyFieldDeclaration(resolver, property);
      }
   }

}
