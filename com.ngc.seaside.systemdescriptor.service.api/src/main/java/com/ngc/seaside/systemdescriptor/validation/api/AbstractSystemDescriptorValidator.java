package com.ngc.seaside.systemdescriptor.validation.api;

import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.link.IModelLink;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;

public class AbstractSystemDescriptorValidator implements ISystemDescriptorValidator {

   @SuppressWarnings("unchecked")
   @Override
   public void validate(IValidationContext<?> context) {
      Object o = context.getObject();
      // All casts are safe because of the instance of checks.
      if (o instanceof IPackage) {
         validatePackage((IValidationContext<IPackage>) context);
      } else if (o instanceof IData) {
         validateData((IValidationContext<IData>) context);
      } else if (o instanceof IDataField) {
         validateDataField((IValidationContext<IDataField>) context);
      } else if (o instanceof IModel) {
         validateModel((IValidationContext<IModel>) context);
      } else if (o instanceof IDataReferenceField) {
         validateDataReferenceField((IValidationContext<IDataReferenceField>) context);
      } else if (o instanceof IModelReferenceField) {
         validateModelReferenceField((IValidationContext<IModelReferenceField>) context);
      } else if (o instanceof IModelLink) {
         validateLink((IValidationContext<IModelLink<?>>) context);
      } else if (o instanceof IScenario) {
         validateScenario((IValidationContext<IScenario>) context);
      } else if (o instanceof IScenarioStep) {
         validateStep((IValidationContext<IScenarioStep>) context);
      }
   }

   protected void validateDataField(IValidationContext<IDataField> context) {
   }

   protected void validateData(IValidationContext<IData> context) {
   }

   protected void validateModel(IValidationContext<IModel> context) {
   }

   protected void validateDataReferenceField(IValidationContext<IDataReferenceField> context) {
   }

   protected void validateModelReferenceField(IValidationContext<IModelReferenceField> context) {
   }

   protected void validateLink(IValidationContext<IModelLink<?>> context) {
   }

   protected void validateScenario(IValidationContext<IScenario> context) {
   }

   protected void validateStep(IValidationContext<IScenarioStep> context) {
   }

   protected void validatePackage(IValidationContext<IPackage> context) {
   }
}
