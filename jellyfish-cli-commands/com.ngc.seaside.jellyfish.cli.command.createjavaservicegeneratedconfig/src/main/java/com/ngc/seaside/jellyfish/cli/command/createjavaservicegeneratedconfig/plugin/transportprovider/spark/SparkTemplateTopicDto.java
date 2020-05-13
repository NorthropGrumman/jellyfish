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

import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.ITransportTopicConfigurationDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.ITransportTopicConfigurationDto.TransportTopicDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.rest.RestConfigurationDto;
import com.ngc.seaside.jellyfish.service.config.api.dto.HttpMethod;
import com.ngc.seaside.jellyfish.service.config.api.dto.RestConfiguration;

import java.util.Set;

public class SparkTemplateTopicDto {

   private final String topicVariableName;
   private final RestConfiguration configuration;
   private final Set<TransportTopicDto> topics;

   /**
    * Constructor.
    * 
    * @param transportTopicConfigurationDto transport topic configuration dto
    */
   public SparkTemplateTopicDto(ITransportTopicConfigurationDto<
            RestConfigurationDto> transportTopicConfigurationDto) {
      this.topicVariableName = transportTopicConfigurationDto.getTopicVariableName();
      this.configuration = transportTopicConfigurationDto.getValue().getConfiguration();
      this.topics = transportTopicConfigurationDto.getTransportTopics();
   }

   public Set<TransportTopicDto> getTransportTopics() {
      return topics;
   }

   public String getVariableName() {
      return topicVariableName;
   }

   public String getNetworkInterface() {
      return configuration.getNetworkInterface().getName();
   }

   public String getPath() {
      return configuration.getPath();
   }

   public String getContentType() {
      return configuration.getContentType();
   }

   public HttpMethod getHttpMethod() {
      return configuration.getHttpMethod();
   }

   public int getPort() {
      return configuration.getPort();
   }
}
