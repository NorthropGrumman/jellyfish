package ${dto.packageName}.main;

#set ($ignore = $dto.imports.add("com.ngc.blocs.guice.module.EventServiceModule"))
#set ($ignore = $dto.imports.add("com.ngc.blocs.guice.module.LogServiceModule"))
#set ($ignore = $dto.imports.add("com.ngc.blocs.guice.module.ResourceServiceModule"))
#set ($ignore = $dto.imports.add("com.ngc.blocs.guice.module.ThreadServiceModule"))
#set ($ignore = $dto.imports.add("com.ngc.seaside.cucumber.runner.api.CucumberRunnerBuilder"))
#set ($ignore = $dto.imports.add("com.ngc.seaside.service.fault.impl.faultloggingservice.module.LoggingFaultManagementServiceModule"))
#set ($ignore = $dto.imports.add("com.ngc.seaside.service.telemetry.api.ITelemetryService"))
#set ($ignore = $dto.imports.add("com.ngc.seaside.service.telemetry.impl.jsontelemetryservice.module.JsonTelemetryServiceModule"))
#set ($ignore = $dto.imports.add("${dto.packageName}.di.${dto.className}TestModule"))
#set ($ignore = $dto.imports.add("${dto.packageName}.steps.${dto.className}Steps"))
#if ($dto.isConfigGenerated())
#set ($ignore = $dto.imports.add("${dto.configModulePackage}.${dto.configModuleType}"))
#end
#foreach ($i in $dto.imports)
import ${i};
#end

/**
 * This application runs the {@link ${dto.packageName}.steps.${dto.className}Steps ${dto.className}Steps}.
 */
public class ${dto.className}TestMain {

   public static final String APP_HOME_SYS_PROPERTY = "appHome";

   public static void main(String[] args) throws Throwable {
      int returnStatus;
      try {
         returnStatus = CucumberRunnerBuilder.withAppHomeFromSystemProperty(APP_HOME_SYS_PROPERTY, "build/runtime")
            .useAppHomeForBlocsHome()
            .setStepPackage(${dto.className}Steps.class.getPackage())
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
#if ($dto.isConfigGenerated())
                        new ${dto.configModuleType}(),
#end
                        new ${dto.className}TestModule())
#foreach ($remoteService in $dto.remoteServices)
            .addRequiredRemoteService($remoteService)
#end
            .build()
            .execute();
      } catch(Throwable e) {
         e.printStackTrace(System.err);
         returnStatus = 1;
      }
      System.exit(returnStatus);
   }
}
