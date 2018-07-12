package com.ngc.seaside.jellyfish.cli.command.analyzebudget;

import com.ngc.seaside.jellyfish.service.analysis.api.ISystemDescriptorFindingType;
import com.ngc.seaside.jellyfish.utilities.command.AbstractJellyfishAnalysisCommand;

public enum BudgetFindingTypes implements ISystemDescriptorFindingType {

   OUT_OF_BUDGET_ANALYSIS("outOfBudget", "docs/budget.md", Severity.WARNING),
   WITHIN_BUDGET_ANALYSIS("withinBudget", "docs/budget.md", Severity.INFO);

   private final String id;
   private final String description;
   private final Severity severity;

   BudgetFindingTypes(String id, String resource, Severity severity) {
      this.id = id;
      this.description = AbstractJellyfishAnalysisCommand.getResource(resource, BudgetFindingTypes.class);
      this.severity = severity;
   }

   @Override
   public String getDescription() {
      return description;
   }

   @Override
   public String getId() {
      return id;
   }

   @Override
   public Severity getSeverity() {
      return severity;
   }

}
