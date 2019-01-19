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
package com.ngc.seaside.jellyfish.sonarqube.rule;

import com.google.common.base.Preconditions;

import com.ngc.seaside.jellyfish.service.analysis.api.IReportingOutputService;
import com.ngc.seaside.jellyfish.service.analysis.api.ISystemDescriptorFindingType;

import org.sonar.api.rule.RuleKey;
import org.sonar.api.rule.RuleStatus;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.rule.RulesDefinition;

/**
 * Adapts instances of Jellyfish {@code ISystemDescriptorFindingType} to a Sonarqube {@code AbstractRule}.
 */
public class FindingTypeRuleAdapter extends AbstractRule {

   /**
    * The finding type that is being adapted.
    */
   private final ISystemDescriptorFindingType findingType;

   private IReportingOutputService reportingOutputService;

   /**
    * Creates a new adapter for the given finding type.
    *
    * @param findingType the finding type to adapt
    */
   public FindingTypeRuleAdapter(ISystemDescriptorFindingType findingType) {
      super(RuleKey.of(SystemDescriptorRulesDefinition.REPOSITORY_KEY, findingType.getId()));
      this.findingType = findingType;
   }

   /**
    * Gets the finding type that is being adapted.
    *
    * @return the finding type begin adapted
    */
   public ISystemDescriptorFindingType getFindingType() {
      return findingType;
   }

   public void setReportingOutputService(IReportingOutputService reportingOutputService) {
      Preconditions.checkNotNull(reportingOutputService, "reportingOutputService may not be null!");
      this.reportingOutputService = reportingOutputService;
   }

   @Override
   protected void configure(RulesDefinition.NewRule rule) {
      Preconditions.checkNotNull(reportingOutputService,
                                 "reportingOutputService is null! Did you call setReportingOutputService()?");

      // Do the actual mapping between the Jellyfish API and Sonarqube here.
      rule.setName(findingType.getId())
            .setHtmlDescription(reportingOutputService.convert(findingType.getDescription()))
            .setSeverity(convert(findingType.getSeverity()))
            // Make all Jellyfish rules ready.
            .setStatus(RuleStatus.READY)
            // By default, we make all Jellyfish rules code smells.
            .setType(RuleType.CODE_SMELL)
            // Use a default linear dept function with a "gap" 1 hour.  When we create each issue
            // we'll use the complexity of the SystemDescriptorFinding as the "gap" value on the Sonarqube issue.
            // This means that complexity values in SystemDescriptorFinding are effectively units of hours.
            .setDebtRemediationFunction(rule.debtRemediationFunctions().linear("1 h"));
   }

   private static String convert(ISystemDescriptorFindingType.Severity severity) {
      switch (severity) {
         case ERROR:
            return Severity.MAJOR;
         case WARNING:
            return Severity.MINOR;
         case INFO:
            // Intentionally fall-through
         default:
            return Severity.INFO;
      }
   }
}
