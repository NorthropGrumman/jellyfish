package com.ngc.seaside.jellyfish.cli.command.analyzebudget.budget;

import com.ngc.seaside.systemdescriptor.model.api.FieldCardinality;
import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperty;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyDataValue;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyPrimitiveValue;
import com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService;

import systems.uom.unicode.CLDR;
import tec.uom.se.format.SimpleUnitFormat;
import tec.uom.se.quantity.Quantities;
import tec.uom.se.unit.MetricPrefix;

import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;
import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.format.UnitFormat;

public class SdBudgetAdapter {

   public static final String BUDGET_QUALIFIED_NAME = "com.ngc.seaside.systemdescriptor.budget.Budget";
   public static final String BUDGET_MINIMUM_FIELD_NAME = "min";
   public static final String BUDGET_MAXIMUM_FIELD_NAME = "max";
   public static final String BUDGET_GIVEN_BY_FIELD_NAME = "givenBy";

   private ISystemDescriptorService sdService;
   private final UnitFormat unitFormat;

   /**
    * Constructor.
    */
   public SdBudgetAdapter() {
      unitFormat = SimpleUnitFormat.getInstance();
      unitFormat.label(CLDR.BYTE, "B");
      unitFormat.label(MetricPrefix.KILO(CLDR.BYTE), "KB");
      unitFormat.label(MetricPrefix.MEGA(CLDR.BYTE), "MB");
      unitFormat.label(MetricPrefix.GIGA(CLDR.BYTE), "GB");
      unitFormat.label(MetricPrefix.TERA(CLDR.BYTE), "TB");
   }

   @Inject
   public void setSdService(ISystemDescriptorService ref) {
      this.sdService = ref;
   }

   /**
    * Returns the budgets associated with the given model.
    * 
    * @param model model
    * @return the budgets associated with the given model
    */
   public Set<Budget<? extends Quantity<?>>> getBudgets(IModel model) {

      IModel aggregatedModel = sdService.getAggregatedView(model);

      Set<Budget<? extends Quantity<?>>> budgets = new LinkedHashSet<>();
      for (IProperty property : aggregatedModel.getProperties()) {
         if (property.getType() != DataTypes.DATA || property.getCardinality() != FieldCardinality.SINGLE
                  || !BUDGET_QUALIFIED_NAME.equals(property.getReferencedDataType().getFullyQualifiedName())) {
            continue;
         }
         Object source = getSource(model, property.getName());
         budgets.add(getBudget(aggregatedModel, property, source));
      }

      return budgets;
   }

   @SuppressWarnings({ "rawtypes", "unchecked" })
   public <T extends Quantity<T>> Optional<Quantity<T>> getBudgetValue(IModel model, Budget<T> budget) {
      model = sdService.getAggregatedView(model);
      Optional<IProperty> optional = model.getProperties().getByName(budget.getProperty());
      if (!optional.isPresent()) {
         return Optional.empty();
      }
      IProperty property = optional.get();
      if (property.getType() == DataTypes.DATA
               && BUDGET_QUALIFIED_NAME.equals(property.getReferencedDataType().getFullyQualifiedName())) {
         return Optional.empty();
      }
      if (property.getType() != DataTypes.STRING || property.getCardinality() != FieldCardinality.SINGLE) {
         throw new IllegalStateException("Invalid budget property: " + property.getName());
      }
      IPropertyPrimitiveValue primitive = property.getPrimitive();
      if (primitive.isSet()) {
         String value = property.getPrimitive().getString();
         Quantity q = parse(model, primitive, value);
         return Optional.of(q);
      } else {
         return Optional.empty();
      }
   }

   @SuppressWarnings({ "unchecked", "rawtypes" })
   Budget<?> getBudget(IModel model, IProperty property, Object source) {
      IPropertyDataValue value = property.getData();
      String minimum = getStringField(model, value, BUDGET_MINIMUM_FIELD_NAME);
      String maximum = getStringField(model, value, BUDGET_MAXIMUM_FIELD_NAME);
      String givenBy = getStringField(model, value, BUDGET_GIVEN_BY_FIELD_NAME);

      Quantity maximumQuantity = null;
      Quantity minimumQuantity = null;

      boolean zeroMinimum = "0".equals(minimum);
      boolean zeroMaximum = "0".equals(maximum);

      if (zeroMinimum && zeroMaximum) {
         throw new BudgetValidationException("Budget cannot have a 0 minimum and maximum", null, source);
      }

      if (!zeroMinimum) {
         minimumQuantity = parse(model, source, minimum);
      }
      if (!zeroMaximum) {
         maximumQuantity = parse(model, source, maximum);
      }

      if (zeroMinimum) {
         minimumQuantity = Quantities.getQuantity(0, maximumQuantity.getUnit());
      }

      if (zeroMaximum) {
         maximumQuantity = Quantities.getQuantity(0, minimumQuantity.getUnit());
      }

      return new Budget<>(minimumQuantity, maximumQuantity, givenBy, source);
   }

   private String getStringField(IModel model, IPropertyDataValue value, String fieldName) {
      IDataField field = value.getFieldByName(fieldName).orElseThrow(
               () -> new IllegalArgumentException("Model " + model.getFullyQualifiedName() + " missing " + fieldName));

      return value.getPrimitive(field).getString();
   }

   Quantity<?> parse(IModel model, Object source, String value) {
      ParsePosition position = new ParsePosition(0);
      Number number;
      try {
         number = DecimalFormat.getInstance().parse(value, position);
      } catch (Exception e) {
         throw new BudgetValidationException(
                  "Invalid value for budget property in model " + model.getFullyQualifiedName() + ": " + value, e,
                  source, "field must start with a number");
      }

      Unit<?> unit;
      try {
         unit = unitFormat.parse(value.substring(position.getIndex()));
      } catch (Exception e) {
         throw new BudgetValidationException(
                  "Invalid unit for budget property in model " + model.getFullyQualifiedName() + ": " + value, e,
                  source, "Invalid/Unknown unit");
      }
      return Quantities.getQuantity(number, unit);
   }

   /**
    * Returns the source of the property.
    * 
    * @param model unaggregated model
    * @param propertyName property name
    * @return the source of the property
    */
   Object getSource(IModel model, String propertyName) {
      IModel m = model;
      while (m != null) {
         IProperty property = m.getProperties().getByName(propertyName).orElse(null);
         if (property != null) {
            return property;
         }
         m = m.getRefinedModel().orElse(null);
      }
      throw new IllegalStateException(
               "Unable to get property " + propertyName + " for model " + model.getFullyQualifiedName());
   }
}
