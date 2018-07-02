package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transportprovider.spark;

import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.ConfigurationContext;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transportprovider.AbstractTransportProviderConfigurationDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.ITransportTopicConfigurationDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.ITransportTopicConfigurationDto.TransportTopicDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.rest.RestConfigurationDto;

import java.util.LinkedHashSet;
import java.util.Set;

public class SparkTemplateDto extends AbstractTransportProviderConfigurationDto {

   private static final String SPARK_TOPIC =
            "com.ngc.seaside.service.transport.impl.topic.spark.SparkTopic";
   private static final String CONFIGURATION_TYPE_SUFFIX = "SparkConfiguration";
   private static final String SPARK_PROVIDER_TARGET =
            "(component.name=com.ngc.seaside.service.transport.impl.provider.spark.SparkTransportProvider)";
   private static final String SPARK_MODULE =
            "com.ngc.seaside.service.transport.impl.provider.spark.module.SparkTransportProviderModule";

   private Set<SparkTemplateTopicDto> topicDtos = new LinkedHashSet<>();

   public SparkTemplateDto(ConfigurationContext context) {
      super(context, SPARK_TOPIC, CONFIGURATION_TYPE_SUFFIX, SPARK_PROVIDER_TARGET, SPARK_MODULE);
   }

   public String getPackageName() {
      return getBasePackage();
   }

   /**
    * Adds the given topic configuration dto.
    * 
    * @param topicDto topic configuration dto
    */
   public void addTopic(ITransportTopicConfigurationDto<RestConfigurationDto> topicDto) {
      if (topicDto.getValue().canReceive()) {
         topicDtos.add(new SparkTemplateTopicDto(topicDto));
         for (TransportTopicDto transportTopicDto : topicDto.getTransportTopics()) {
            getImports().add(transportTopicDto.getType());
         }
      }
   }

   public Set<SparkTemplateTopicDto> getTopics() {
      return topicDtos;
   }

}
