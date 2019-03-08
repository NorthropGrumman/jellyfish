/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
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
