package com.ngc.seaside.systemdescriptor.service.impl.xtext;

import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.validation.api.AbstractSystemDescriptorValidator;
import com.ngc.seaside.systemdescriptor.validation.api.IValidationContext;
import com.ngc.seaside.systemdescriptor.validation.api.Severity;

public class FakeValidator extends AbstractSystemDescriptorValidator {

   @Override
   protected void validateModel(IValidationContext<IModel> context) {
      IModel model = context.getObject();
      if("Watermelon".equals(model.getName())) {
         context.declare(Severity.ERROR, "Jelly and watermelon don't go together.", model).getName();
      }
   }
}
