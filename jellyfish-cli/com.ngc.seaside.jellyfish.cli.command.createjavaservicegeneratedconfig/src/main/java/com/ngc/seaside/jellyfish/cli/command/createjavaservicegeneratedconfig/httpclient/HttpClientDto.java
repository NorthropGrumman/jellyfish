package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.httpclient;

import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.dto.GeneratedServiceConfigDto;

import java.util.ArrayList;
import java.util.List;

public class HttpClientDto {
   private GeneratedServiceConfigDto baseDto;
   private String topicsImport;
   private List<HttpClientTopicDto> topics = new ArrayList<>();
   private String classname;

   public GeneratedServiceConfigDto getBaseDto() {
      return baseDto;
   }

   public HttpClientDto setBaseDto(
         GeneratedServiceConfigDto baseDto) {
      this.baseDto = baseDto;
      return this;
   }

   public String getTopicsImport() {
      return topicsImport;
   }

   public HttpClientDto setTopicsImport(String topicsImport) {
      this.topicsImport = topicsImport;
      return this;
   }

   public List<HttpClientTopicDto> getTopics() {
      return topics;
   }

   public HttpClientDto addTopic(HttpClientTopicDto topic) {
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

   public HttpClientDto setClassname(String classname) {
      this.classname = classname;
      return this;
   }
}
