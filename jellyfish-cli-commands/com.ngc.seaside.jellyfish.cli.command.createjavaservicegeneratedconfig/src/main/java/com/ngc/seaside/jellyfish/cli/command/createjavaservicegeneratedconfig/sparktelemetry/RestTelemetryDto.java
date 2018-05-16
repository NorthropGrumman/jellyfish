package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.sparktelemetry;

import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.dto.GeneratedServiceConfigDto;

import java.util.ArrayList;
import java.util.List;

public class RestTelemetryDto {
   private GeneratedServiceConfigDto baseDto;
   private String topicsImport;
   private List<RestTelemetryTopicDto> topics = new ArrayList<>();
   private String classname;

   public GeneratedServiceConfigDto getBaseDto() {
      return baseDto;
   }

   public RestTelemetryDto setBaseDto(
         GeneratedServiceConfigDto baseDto) {
      this.baseDto = baseDto;
      return this;
   }

   public String getTopicsImport() {
      return topicsImport;
   }

   public RestTelemetryDto setTopicsImport(String topicsImport) {
      this.topicsImport = topicsImport;
      return this;
   }

   public List<RestTelemetryTopicDto> getTopics() {
      return topics;
   }

   public RestTelemetryDto addTopic(RestTelemetryTopicDto topic) {
      this.topics.add(topic);
      return this;
   }

   public String getProjectDirectoryName() {
      return this.baseDto.getProjectDirectoryName();
   }

   public String getPackageName() {
      return this.baseDto.getPackageName();
   }

   public String getModelName() {
      return this.baseDto.getModelName();
   }

   public String getClassname() {
      return this.classname;
   }

   public RestTelemetryDto setClassname(String classname) {
      this.classname = classname;
      return this;
   }
}
