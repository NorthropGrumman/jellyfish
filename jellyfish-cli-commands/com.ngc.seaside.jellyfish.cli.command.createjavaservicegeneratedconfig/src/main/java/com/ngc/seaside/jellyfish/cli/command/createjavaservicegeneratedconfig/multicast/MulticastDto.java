package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.multicast;

import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.dto.GeneratedServiceConfigDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MulticastDto {

   private GeneratedServiceConfigDto baseDto;
   private String topicsImport;
   private List<MulticastTopicDto> topics = new ArrayList<>();
   private String classname;

   public GeneratedServiceConfigDto getBaseDto() {
      return baseDto;
   }

   public MulticastDto setBaseDto(GeneratedServiceConfigDto baseDto) {
      this.baseDto = baseDto;
      return this;
   }

   public String getTopicsImport() {
      return topicsImport;
   }

   public MulticastDto setTopicsImport(String topicsImport) {
      this.topicsImport = topicsImport;
      return this;
   }

   public List<MulticastTopicDto> getTopics() {
      return topics;
   }

   /**
    *
    * @return a map with the send topic name as the key and a value of MulticastTopicDto
    */
   public Map<String, List<MulticastTopicDto>> getSendTopics() {
      return topics.stream()
            .filter(MulticastTopicDto::isSend)
            .collect(Collectors.groupingBy(MulticastTopicDto::getName));
   }

   /**
    *
    * @return a map with the receive topic name as the key and a value of MulticastTopicDto
    */
   public Map<String, List<MulticastTopicDto>> getReceiveTopics() {
      return topics.stream()
            .filter(topic -> !topic.isSend())
            .collect(Collectors.groupingBy(MulticastTopicDto::getName));
   }

   public MulticastDto addTopic(MulticastTopicDto topic) {
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

   public MulticastDto setClassname(String classname) {
      this.classname = classname;
      return this;
   }

}
