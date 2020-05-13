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
