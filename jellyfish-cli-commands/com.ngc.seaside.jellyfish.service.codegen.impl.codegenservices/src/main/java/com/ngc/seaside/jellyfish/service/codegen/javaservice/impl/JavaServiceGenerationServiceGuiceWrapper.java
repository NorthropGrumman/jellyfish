package com.ngc.seaside.jellyfish.service.codegen.javaservice.impl;

import com.google.inject.Inject;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.codegen.api.IJavaServiceGenerationService;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.ClassDto;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.EnumDto;
import com.ngc.seaside.jellyfish.service.config.api.ITelemetryConfigurationService;
import com.ngc.seaside.jellyfish.service.config.api.ITelemetryReportingConfigurationService;
import com.ngc.seaside.jellyfish.service.config.api.ITransportConfigurationService;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.jellyfish.service.scenario.api.IScenarioService;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

public class JavaServiceGenerationServiceGuiceWrapper implements IJavaServiceGenerationService {

   private final JavaServiceGenerationService javaServiceGenerationService;

   @Inject
   public JavaServiceGenerationServiceGuiceWrapper(
         ILogService logService,
         IScenarioService scenarioService,
         IPackageNamingService packageNamingService,
         ITransportConfigurationService transportConfigService,
         ITelemetryConfigurationService telemetryConfigService,
         ITelemetryReportingConfigurationService telemetryReportingConfigService) {
      javaServiceGenerationService = new JavaServiceGenerationService();
      javaServiceGenerationService.setLogService(logService);
      javaServiceGenerationService.setScenarioService(scenarioService);
      javaServiceGenerationService.setPackageNamingService(packageNamingService);
      javaServiceGenerationService.setTransportConfigurationService(transportConfigService);
      javaServiceGenerationService.setTelemetryConfigurationService(telemetryConfigService);
      javaServiceGenerationService.setTelemetryReportingConfigurationService(telemetryReportingConfigService);
      javaServiceGenerationService.activate();
   }

   @Override
   public ClassDto getServiceInterfaceDescription(IJellyFishCommandOptions options, IModel model) {
      return javaServiceGenerationService.getServiceInterfaceDescription(options, model);
   }

   @Override
   public ClassDto getBaseServiceDescription(IJellyFishCommandOptions options,
            IModel model) {
      return javaServiceGenerationService.getBaseServiceDescription(options, model);
   }

   @Override
   public EnumDto getTransportTopicsDescription(IJellyFishCommandOptions options, IModel model) {
      return javaServiceGenerationService.getTransportTopicsDescription(options, model);
   }
}
