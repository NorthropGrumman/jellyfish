package com.ngc.seaside.systemdescriptor.validation.api;

import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.link.IModelLink;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperty;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;

/**
 * A default implementation of {@code ISystemDescriptorValidator}.  Most implementations should extend this class.
 */
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
      } else if (o instanceof IEnumeration) {
         validateEnumeration((IValidationContext<IEnumeration>) context);
      } else if (o instanceof IProperty) {
         validateProperty((IValidationContext<IProperty>) context);
      }
   }

   /**
    * Invoked to validate an {@code IEnumeration}.  Default implementation does nothing.
    */
   private void validateEnumeration(IValidationContext<IEnumeration> context) {
   }

   /**
    * Invoked to validate an {@code IValidationContext}.  Default implementation does nothing.
    */
   protected void validateDataField(IValidationContext<IDataField> context) {
   }

   /**
    * Invoked to validate an {@code IValidationContext}.  Default implementation does nothing.
    */
   protected void validateData(IValidationContext<IData> context) {
   }

   /**
    * Invoked to validate an {@code IValidationContext}.  Default implementation does nothing.
    */
   protected void validateModel(IValidationContext<IModel> context) {
   }

   /**
    * Invoked to validate an {@code IDataReferenceField}.  Default implementation does nothing.
    */
   protected void validateDataReferenceField(IValidationContext<IDataReferenceField> context) {
   }

   /**
    * Invoked to validate an {@code IModelReferenceField}.  Default implementation does nothing.
    */
   protected void validateModelReferenceField(IValidationContext<IModelReferenceField> context) {
   }

   /**
    * Invoked to validate an {@code IModelLink}.  Default implementation does nothing.
    */
   protected void validateLink(IValidationContext<IModelLink<?>> context) {
   }

   /**
    * Invoked to validate an {@code IScenario}.  Default implementation does nothing.
    */
   protected void validateScenario(IValidationContext<IScenario> context) {
   }

   /**
    * Invoked to validate an {@code IScenarioStep}.  Default implementation does nothing.
    */
   protected void validateStep(IValidationContext<IScenarioStep> context) {
   }

   /**
    * Invoked to validate an {@code IPackage}.  Default implementation does nothing.
    */
   protected void validatePackage(IValidationContext<IPackage> context) {
   }

   /**
    * Invoked to validate an {@code IProperty}.  Default implementation does nothing.
    */
   protected void validateProperty(IValidationContext<IProperty> context) {
   }
}
