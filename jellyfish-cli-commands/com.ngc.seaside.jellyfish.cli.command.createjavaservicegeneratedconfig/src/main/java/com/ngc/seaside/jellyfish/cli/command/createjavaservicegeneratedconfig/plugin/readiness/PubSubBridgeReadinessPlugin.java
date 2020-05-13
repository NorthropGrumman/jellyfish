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
package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.readiness;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto.BaseServiceDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto.BasicPubSubDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto.CorrelationDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto.IBaseServiceDtoFactory;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto.InputDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.ConfigurationContext;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.util.List;

import javax.inject.Inject;

public class PubSubBridgeReadinessPlugin implements IReadinessPlugin {

   private static final String SUBSCRIBER_SUFFIX = "Subscriber";

   private IBaseServiceDtoFactory baseServiceDtoFactory;
   private IPackageNamingService packageNamingService;

   @Inject
   public PubSubBridgeReadinessPlugin(IBaseServiceDtoFactory baseServiceDtoFactory,
                                      IPackageNamingService packageNamingService) {
      this.baseServiceDtoFactory = baseServiceDtoFactory;
      this.packageNamingService = packageNamingService;
   }

   @Override
   public void configure(ReadinessTemplateDto dto) {
      ConfigurationContext context = dto.getContext();
      IJellyFishCommandOptions options = context.getOptions();
      IModel model = context.getModel();

      BaseServiceDto baseServiceDto = baseServiceDtoFactory.newDto(options, model);
      String packageName = packageNamingService.getPubSubBridgePackageName(options, model);

      List<CorrelationDto> correlationMethodDtos = baseServiceDto.getCorrelationMethods();
      List<BasicPubSubDto> pubSubMethods = baseServiceDto.getBasicPubSubMethods();

      for (CorrelationDto correlationMethodDto : correlationMethodDtos) {
         for (InputDto inputDto : correlationMethodDto.getInputs()) {
            dto.addEventSubscriber(
                     String.format("%s.%s%s", packageName, inputDto.getType(), SUBSCRIBER_SUFFIX));
         }
      }

      for (BasicPubSubDto pubSubMethod : pubSubMethods) {
         dto.addEventSubscriber(
                  String.format("%s.%s%s", packageName, pubSubMethod.getInput().getType(), SUBSCRIBER_SUFFIX));
      }

   }

}
