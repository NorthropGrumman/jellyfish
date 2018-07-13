package com.ngc.seaside.jellyfish.cli.command.analyzebudget.budget;

import com.ngc.seaside.systemdescriptor.model.api.FieldCardinality;
import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperty;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyPrimitiveValue;
import com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService;
import com.ngc.seaside.systemdescriptor.validation.api.AbstractSystemDescriptorValidator;
import com.ngc.seaside.systemdescriptor.validation.api.IValidationContext;
import com.ngc.seaside.systemdescriptor.validation.api.Severity;

import javax.inject.Inject;
import javax.measure.Quantity;

import static com.ngc.seaside.jellyfish.cli.command.analyzebudget.budget.SdBudgetAdapter.BUDGET_QUALIFIED_NAME;

public class BudgetValidator extends AbstractSystemDescriptorValidator {

   private SdBudgetAdapter adapter;
   private ISystemDescriptorService sdService;

   @Inject
   public BudgetValidator(SdBudgetAdapter adapter, ISystemDescriptorService sdService) {
      this.adapter = adapter;
      this.sdService = sdService;
   }

   @Override
   protected void validateModel(IValidationContext<IModel> context) {
      IModel model = context.getObject();

      IModel aggregatedModel = sdService.getAggregatedView(model);
      for (IProperty property : aggregatedModel.getProperties()) {
         if (property.getType() != DataTypes.DATA || property.getCardinality() != FieldCardinality.SINGLE
                  || !BUDGET_QUALIFIED_NAME.equals(property.getReferencedDataType().getFullyQualifiedName())) {
            continue;
         }
         Object source = adapter.getSource(model, property.getName());
         Budget<?> budget;
         try {
            budget = adapter.getBudget(aggregatedModel, property, source);
         } catch (BudgetValidationException e) {
            Object errorSource = context.declare(Severity.ERROR, e.getSimpleMessage(), e.getSource());
            callOffendingError(errorSource);
            continue;
         }
         checkParts(context, model, budget);
      }
   }

   private void checkParts(IValidationContext<IModel> context, IModel model, Budget<?> budget) {
      IModel aggregatedModel = sdService.getAggregatedView(model);
      IProperty property = aggregatedModel.getProperties().getByName(budget.getProperty()).orElse(null);
      if (property != null && property.getType() == DataTypes.STRING
               && property.getCardinality() == FieldCardinality.SINGLE && property.getPrimitive().isSet()) {
         Object source = adapter.getSource(model, budget.getProperty()).getPrimitive();

         String value = property.getPrimitive().getString();
         try {
            Quantity<?> quantity = adapter.parse(aggregatedModel, source, value);
            if (!quantity.getUnit().isCompatible(budget.getUnit())) {
               throw new BudgetValidationException("Invalid unit. Expected " + budget.getUnit(), null, source);
            }
         } catch (BudgetValidationException e) {
            Object errorSource = context.declare(Severity.ERROR, e.getSimpleMessage(), e.getSource());
            callOffendingError(errorSource);
         }
      }

      for (IModelReferenceField part : model.getParts()) {
         checkParts(context, part.getType(), budget);
      }
   }

   private void callOffendingError(Object errorSource) {
      if (errorSource instanceof IProperty) {
         ((IProperty) errorSource).getName();
      } else if (errorSource instanceof IPropertyPrimitiveValue) {
         ((IPropertyPrimitiveValue) errorSource).getString();
      }
   }
   
}
