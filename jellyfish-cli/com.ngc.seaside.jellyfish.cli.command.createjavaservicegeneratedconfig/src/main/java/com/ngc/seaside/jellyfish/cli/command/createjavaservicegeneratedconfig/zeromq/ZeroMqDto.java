package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.zeromq;

import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.dto.GeneratedServiceConfigDto;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ZeroMqDto {
   private GeneratedServiceConfigDto baseDto;
   private Set<String> imports = new LinkedHashSet<>();
   private List<ZeroMqTopicDto> topics = new ArrayList<>();
   private String classname;

   public GeneratedServiceConfigDto getBaseDto() {
      return baseDto;
   }

   public ZeroMqDto setBaseDto(GeneratedServiceConfigDto baseDto) {
      this.baseDto = baseDto;
      return this;
   }

   public Set<String> getImports() {
      return imports;
   }

   public ZeroMqDto addImport(String imp) {
      this.imports.add(imp);
      return this;
   }

   public List<ZeroMqTopicDto> getTopics() {
      return topics;
   }

   public Map<String, List<ZeroMqTopicDto>> getSendTopics() {
      return topics.stream()
                   .filter(ZeroMqTopicDto::isSend)
                   .collect(Collectors.groupingBy(ZeroMqTopicDto::getName));
   }

   public Map<String, List<ZeroMqTopicDto>> getReceiveTopics() {
      return topics.stream()
                   .filter(topic -> !topic.isSend())
                   .collect(Collectors.groupingBy(ZeroMqTopicDto::getName));
   }

   public ZeroMqDto addTopic(ZeroMqTopicDto topic) {
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

   public ZeroMqDto setClassname(String classname) {
      this.classname = classname;
      return this;
   }

}
