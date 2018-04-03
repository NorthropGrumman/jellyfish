package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.rest;

import com.ngc.seaside.jellyfish.service.config.api.dto.HttpMethod;

public class RestTopicDto {
   private int port;
   private HttpMethod httpMethod;
   private String path;
   private String contentType;
   private String variableName;
   private String name;

   public int getPort() {
      return port;
   }

   public RestTopicDto setPort(int port) {
      this.port = port;
      return this;
   }

   public HttpMethod getHttpMethod() {
      return httpMethod;
   }

   public RestTopicDto setHttpMethod(HttpMethod httpMethod) {
      this.httpMethod = httpMethod;
      return this;
   }

   public String getPath() {
      return path;
   }

   public RestTopicDto setPath(String path) {
      this.path = path;
      return this;
   }

   public String getContentType() {
      return contentType;
   }

   public RestTopicDto setContentType(String contentType) {
      this.contentType = contentType;
      return this;
   }

   public String getVariableName() {
      return variableName;
   }

   public RestTopicDto setVariableName(String variableName) {
      this.variableName = variableName;
      return this;
   }

   public String getName() {
      return name;
   }

   public RestTopicDto setName(String name) {
      this.name = name;
      return this;
   }
}
