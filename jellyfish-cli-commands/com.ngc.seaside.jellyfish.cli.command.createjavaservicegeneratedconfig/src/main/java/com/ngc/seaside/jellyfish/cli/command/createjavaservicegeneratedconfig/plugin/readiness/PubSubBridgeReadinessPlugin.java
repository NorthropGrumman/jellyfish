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
