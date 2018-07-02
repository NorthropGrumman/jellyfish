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
    * @param rateInSeconds rate in seconds
    */
   public RestTelemetryReportingTemplateDto(ConfigurationContext context, String className, String topicType,
                                            String topicValue, int rateInSeconds) {
      super(context);
      this.className = className;
      this.topic = new TransportTopicDto(topicType, topicValue);
      this.rateInMilliseconds = 1000 * rateInSeconds;
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
