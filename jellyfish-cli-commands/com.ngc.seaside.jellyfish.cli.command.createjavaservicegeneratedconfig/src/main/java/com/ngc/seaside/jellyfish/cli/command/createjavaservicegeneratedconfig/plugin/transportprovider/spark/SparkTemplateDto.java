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
