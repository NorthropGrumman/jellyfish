/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
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
