package com.ngc.seaside.systemdescriptor.model.impl.xtext.model.properties;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyEnumerationValue;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtext;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.systemDescriptor.EnumerationValueDeclaration;

public class WrappedEnumerationPropertyValue extends AbstractWrappedXtext<EnumerationValueDeclaration>
      implements IPropertyEnumerationValue {
   public WrappedEnumerationPropertyValue(IWrapperResolver resolver, EnumerationValueDeclaration wrapped) {
      super(resolver, wrapped);
   }

   @Override
   public IEnumeration getReferencedEnumeration() {
      return null;
   }

   @Override
   public String getValue() {
      Preconditions.checkState(isSet(), "property is not set!");
      return wrapped.getValue();
   }

   @Override
   public DataTypes getType() {
      return DataTypes.ENUM;
   }

   @Override
   public boolean isSet() {
      return wrapped.getValue() != null;
   }
}
