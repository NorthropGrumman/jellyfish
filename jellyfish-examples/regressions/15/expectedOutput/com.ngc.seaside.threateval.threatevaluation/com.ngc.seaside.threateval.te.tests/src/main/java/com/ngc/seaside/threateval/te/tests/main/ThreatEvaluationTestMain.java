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
package com.ngc.seaside.threateval.te.tests.main;

import com.ngc.seaside.threateval.te.transport.topic.ThreatEvaluationTransportTopics;
import com.ngc.blocs.guice.module.EventServiceModule;
import com.ngc.blocs.guice.module.LogServiceModule;
import com.ngc.blocs.guice.module.ResourceServiceModule;
import com.ngc.blocs.guice.module.ThreadServiceModule;
import com.ngc.seaside.cucumber.runner.api.CucumberRunnerBuilder;
import com.ngc.seaside.service.fault.impl.faultloggingservice.module.LoggingFaultManagementServiceModule;
import com.ngc.seaside.service.telemetry.impl.jsontelemetryservice.module.JsonTelemetryServiceModule;
import com.ngc.seaside.threateval.te.tests.di.ThreatEvaluationTestModule;
import com.ngc.seaside.threateval.te.tests.steps.ThreatEvaluationSteps;
import com.ngc.seaside.threateval.te.testsconfig.ThreatEvaluationTestConfigurationModule;

/**
 * This application runs the {@link com.ngc.seaside.threateval.te.tests.steps.ThreatEvaluationSteps ThreatEvaluationSteps}.
 */
public class ThreatEvaluationTestMain {

   public static final String APP_HOME_SYS_PROPERTY = "appHome";

   public static void main(String[] args) throws Throwable {
      int returnStatus;
      try {
         returnStatus = CucumberRunnerBuilder.withAppHomeFromSystemProperty(APP_HOME_SYS_PROPERTY, "build/runtime")
            .useAppHomeForBlocsHome()
            .setStepPackage(ThreatEvaluationSteps.class.getPackage())
            .setReportsDirectory("reports/cucumber")
            .setFeaturePath("resources")
            .enablePrettyConsoleOutput()
            .enableHtmlReports()
            .enableJsonReports()
            .enableJunitReports()
            .addModules(new LogServiceModule(),
                        new ResourceServiceModule(),
                        new ThreadServiceModule(),
                        new EventServiceModule(),
                        new LoggingFaultManagementServiceModule(),
                        new JsonTelemetryServiceModule(),
                        new LoggingFaultManagementServiceModule(),
                        new ThreatEvaluationTestConfigurationModule(),
                        new ThreatEvaluationTestModule())
            .addRequiredRemoteService(ThreatEvaluationTransportTopics.DEFENDED_AREA_TRACK_PRIORITY_SERVICE_TELEMETRY)
            .addRequiredRemoteService(ThreatEvaluationTransportTopics.CLASSIFICATION_TRACK_PRIORITY_SERVICE_TELEMETRY)
            .addRequiredRemoteService(ThreatEvaluationTransportTopics.ENGAGEMENT_TRACK_PRIORITY_SERVICE_TELEMETRY)
            .addRequiredRemoteService(ThreatEvaluationTransportTopics.TRACK_PRIORITY_SERVICE_TELEMETRY)
            .build()
            .execute();
      } catch(Throwable e) {
         e.printStackTrace(System.err);
         returnStatus = 1;
      }
      System.exit(returnStatus);
   }
}
