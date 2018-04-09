package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.rest;

import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.dto.GeneratedServiceConfigDto;
import com.ngc.seaside.jellyfish.service.config.api.dto.HttpMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SparkDto {
   private GeneratedServiceConfigDto baseDto;
   private String topicsImport;
   private List<SparkTopicDto> topics = new ArrayList<>();

   public GeneratedServiceConfigDto getBaseDto() {
      return baseDto;
   }

   public SparkDto setBaseDto(
         GeneratedServiceConfigDto baseDto) {
      this.baseDto = baseDto;
      return this;
   }

   public String getTopicsImport() {
      return topicsImport;
   }

   public SparkDto setTopicsImport(String topicsImport) {
      this.topicsImport = topicsImport;
      return this;
   }

   public List<SparkTopicDto> getTopics() {
      return topics;
   }

   public Map<String, List<SparkTopicDto>> getSendTopics() {
      return topics.stream()
                   .filter(topic -> topic.getHttpMethod() == HttpMethod.GET)
                   .collect(Collectors.groupingBy(SparkTopicDto::getName));
   }

   public Map<String, List<SparkTopicDto>> getReceiveTopics() {
      return topics.stream()
                   .filter(topic -> topic.getHttpMethod() != HttpMethod.GET)
                   .collect(Collectors.groupingBy(SparkTopicDto::getName));
   }

   public SparkDto addTopic(SparkTopicDto topic) {
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
