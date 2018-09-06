/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
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
