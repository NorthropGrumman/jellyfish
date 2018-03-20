package com.ngc.seaside.systemdescriptor.model.impl.xtext.model.properties;

import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyEnumerationValue;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtext;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.systemDescriptor.EnumPropertyValue;

public class WrappedEnumerationPropertyValue extends AbstractWrappedXtext<EnumPropertyValue>
      implements IPropertyEnumerationValue {

   public WrappedEnumerationPropertyValue(IWrapperResolver resolver, EnumPropertyValue wrapped) {
      super(resolver, wrapped);
   }

   @Override
   public IEnumeration getReferencedEnumeration() {
      return resolver.getWrapperFor(wrapped.getEnumeration());
   }

   @Override
   public String getValue() {
      return wrapped.getValue();
   }

   @Override
   public DataTypes getType() {
      return DataTypes.ENUM;
   }

   @Override
   public boolean isSet() {
      return true;
   }
}
