package com.ngc.seaside.systemdescriptor.model.impl.xtext.model.properties;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperty;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyEnumerationValue;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyValues;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.util.ConversionUtil;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Enumeration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.ReferencedPropertyFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorFactory;

public class WrappedEnumerationProperty extends AbstractWrappedProperty<ReferencedPropertyFieldDeclaration> {

   private final IEnumeration referencedType;

   public WrappedEnumerationProperty(IWrapperResolver resolver, ReferencedPropertyFieldDeclaration wrapped) {
      super(resolver, wrapped);
      if (!(wrapped.getDataModel() instanceof Enumeration)) {
         throw new IllegalArgumentException("Expected reference property field declaration to be an enumeration");
      }
      this.referencedType = resolver.getWrapperFor((Enumeration) wrapped.getDataModel());
   }

   @Override
   public IEnumeration getReferencedEnumeration() {
      return referencedType;
   }

   @Override
   public IPropertyEnumerationValue getEnumeration() {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public IPropertyValues<IPropertyEnumerationValue> getEnumerations() {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public DataTypes getType() {
      return DataTypes.ENUM;
   }

   public static ReferencedPropertyFieldDeclaration toXtextReferencedPropertyFieldDeclaration(IWrapperResolver resolver,
                                                                                              IProperty property) {
      Preconditions.checkNotNull(resolver, "resolver must not be null!");
      Preconditions.checkNotNull(property, "property must not be null!");
      if (property.getType() != DataTypes.ENUM) {
         throw new IllegalArgumentException("property is not of type enumeration");
      }
      ReferencedPropertyFieldDeclaration declaration =
            SystemDescriptorFactory.eINSTANCE.createReferencedPropertyFieldDeclaration();
      declaration.setName(property.getName());
      declaration.setCardinality(ConversionUtil.convertCardinalityToXtext(property.getCardinality()));
      IEnumeration enumeration = property.getReferencedEnumeration();
      declaration.setDataModel(resolver.findXTextEnum(enumeration.getName(), enumeration.getParent().getName()).get());
      return declaration;
   }

}
