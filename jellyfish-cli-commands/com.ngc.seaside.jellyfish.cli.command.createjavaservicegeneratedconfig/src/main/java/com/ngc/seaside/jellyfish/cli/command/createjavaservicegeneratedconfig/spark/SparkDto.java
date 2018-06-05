package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.spark;

import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.dto.GeneratedServiceConfigDto;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class SparkDto {
   private GeneratedServiceConfigDto baseDto;
   private Set<String> imports = new LinkedHashSet<>();
   private List<SparkTopicDto> topics = new ArrayList<>();
   private String classname;

   public GeneratedServiceConfigDto getBaseDto() {
      return baseDto;
   }

   public SparkDto setBaseDto(
         GeneratedServiceConfigDto baseDto) {
      this.baseDto = baseDto;
      return this;
   }

   public Set<String> getImports() {
      return imports;
   }

   public SparkDto addImport(String imp) {
      this.imports.add(imp);
      return this;
   }

   public List<SparkTopicDto> getTopics() {
      return topics;
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

   public String getClassname() {
      return this.classname;
   }

   public SparkDto setClassname(String classname) {
      this.classname = classname;
      return this;
   }
}
