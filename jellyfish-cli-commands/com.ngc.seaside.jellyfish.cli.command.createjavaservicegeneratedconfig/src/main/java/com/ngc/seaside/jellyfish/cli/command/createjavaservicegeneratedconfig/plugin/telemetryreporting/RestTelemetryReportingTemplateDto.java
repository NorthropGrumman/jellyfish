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
package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.telemetryreporting;

import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.BaseConfigurationDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.ConfigurationContext;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.ITransportTopicConfigurationDto.TransportTopicDto;

public class RestTelemetryReportingTemplateDto extends BaseConfigurationDto {

   private String className;
   private TransportTopicDto topic;
   private int rateInMilliseconds;

   /**
    * Telemetry reporting template dto.
    * 
    * @param context context
    * @param className name of generated class
    * @param topicType fully-qualified name of class containing the topic
    * @param topicValue static variable name of topic
    * @param rateInMilliseconds rate in seconds
    */
   public RestTelemetryReportingTemplateDto(ConfigurationContext context, String className, String topicType,
                                            String topicValue, int rateInMilliseconds) {
      super(context);
      this.className = className;
      this.topic = new TransportTopicDto(topicType, topicValue);
      this.rateInMilliseconds = rateInMilliseconds;
   }

   public String getPackageName() {
      return getBasePackage();
   }

   public String getClassName() {
      return className;
   }

   public String getFullyQualifiedName() {
      return getPackageName() + '.' + getClassName();
   }

   public int getRateInMilliseconds() {
      return rateInMilliseconds;
   }

   public TransportTopicDto getTopic() {
      return topic;
   }

}
