package com.ngc.seaside.systemdescriptor.validation.api;

import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;

public class AbstractSystemDescriptorValidator implements ISystemDescriptorValidator {

   @Override
   public void validate(IValidationContext<?> context) {
      throw new UnsupportedOperationException("not implemented");
   }

   protected void validateDataField(IValidationContext<IDataField> context) {

   }

   protected void validateData(IValidationContext<IData> context) {

   }
}
