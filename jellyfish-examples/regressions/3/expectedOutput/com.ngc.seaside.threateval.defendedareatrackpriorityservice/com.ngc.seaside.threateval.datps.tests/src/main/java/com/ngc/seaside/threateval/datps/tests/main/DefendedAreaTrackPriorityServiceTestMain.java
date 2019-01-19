
package com.ngc.seaside.threateval.datps.tests.main;

import com.ngc.seaside.service.telemetry.api.ITelemetryService;
import com.ngc.blocs.guice.module.EventServiceModule;
import com.ngc.blocs.guice.module.LogServiceModule;
import com.ngc.blocs.guice.module.ResourceServiceModule;
import com.ngc.blocs.guice.module.ThreadServiceModule;
import com.ngc.seaside.cucumber.runner.api.CucumberRunnerBuilder;
import com.ngc.seaside.service.fault.impl.faultloggingservice.module.LoggingFaultManagementServiceModule;
import com.ngc.seaside.service.telemetry.impl.jsontelemetryservice.module.JsonTelemetryServiceModule;
import com.ngc.seaside.threateval.datps.tests.di.DefendedAreaTrackPriorityServiceTestModule;
import com.ngc.seaside.threateval.datps.tests.steps.DefendedAreaTrackPriorityServiceSteps;
import com.ngc.seaside.threateval.datps.testsconfig.DefendedAreaTrackPriorityServiceTestConfigurationModule;

/**
 * This application runs the {@link com.ngc.seaside.threateval.datps.tests.steps.DefendedAreaTrackPriorityServiceSteps DefendedAreaTrackPriorityServiceSteps}.
 */
public class DefendedAreaTrackPriorityServiceTestMain {

   public static final String APP_HOME_SYS_PROPERTY = "appHome";

   public static void main(String[] args) throws Throwable {
      int returnStatus;
      try {
         returnStatus = CucumberRunnerBuilder.withAppHomeFromSystemProperty(APP_HOME_SYS_PROPERTY, "build/runtime")
            .useAppHomeForBlocsHome()
            .setStepPackage(DefendedAreaTrackPriorityServiceSteps.class.getPackage())
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
                        new DefendedAreaTrackPriorityServiceTestConfigurationModule(),
                        new DefendedAreaTrackPriorityServiceTestModule())
            .addRequiredRemoteService(ITelemetryService.TELEMETRY_REQUEST_TRANSPORT_TOPIC)
            .build()
            .execute();
      } catch(Throwable e) {
         e.printStackTrace(System.err);
         returnStatus = 1;
      }
      System.exit(returnStatus);
   }
}
