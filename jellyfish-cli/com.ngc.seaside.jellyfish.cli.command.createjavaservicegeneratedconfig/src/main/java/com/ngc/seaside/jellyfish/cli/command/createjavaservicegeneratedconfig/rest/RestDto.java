package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.rest;

import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.dto.GeneratedServiceConfigDto;
import com.ngc.seaside.jellyfish.service.config.api.dto.HttpMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RestDto {
   private GeneratedServiceConfigDto baseDto;
   private String topicsImport;
   private List<RestTopicDto> topics = new ArrayList<>();

   public GeneratedServiceConfigDto getBaseDto() {
      return baseDto;
   }

   public RestDto setBaseDto(
         GeneratedServiceConfigDto baseDto) {
      this.baseDto = baseDto;
      return this;
   }

   public String getTopicsImport() {
      return topicsImport;
   }

   public RestDto setTopicsImport(String topicsImport) {
      this.topicsImport = topicsImport;
      return this;
   }

   public List<RestTopicDto> getTopics() {
      return topics;
   }

   public Map<String, List<RestTopicDto>> getSendTopics() {
      return topics.stream()
                   .filter(topic -> topic.getHttpMethod() == HttpMethod.GET)
                   .collect(Collectors.groupingBy(RestTopicDto::getName));
   }

   public Map<String, List<RestTopicDto>> getReceiveTopics() {
      return topics.stream()
                   .filter(topic -> topic.getHttpMethod() != HttpMethod.GET)
                   .collect(Collectors.groupingBy(RestTopicDto::getName));
   }

   public RestDto addTopic(RestTopicDto topic) {
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
}
