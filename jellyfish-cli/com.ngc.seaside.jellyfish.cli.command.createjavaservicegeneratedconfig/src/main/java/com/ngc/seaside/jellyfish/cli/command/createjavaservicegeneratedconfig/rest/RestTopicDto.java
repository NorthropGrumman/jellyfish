package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.rest;

import com.ngc.seaside.jellyfish.service.config.api.dto.HttpMethod;
import com.ngc.seaside.jellyfish.service.config.api.dto.NetworkAddress;
import com.ngc.seaside.jellyfish.service.config.api.dto.NetworkInterface;

public class RestTopicDto {
   private NetworkAddress networkAddress;
   private NetworkInterface networkInterface;
   private int port;
   private HttpMethod httpMethod;
   private String path;
   private String contentType;
   private String variableName;
   private String name;

   public NetworkAddress getNetworkAddress() {
      return networkAddress;
   }

   public RestTopicDto setNetworkAddress(NetworkAddress networkAddress) {
      this.networkAddress = networkAddress;
      return this;
   }

   public NetworkInterface getNetworkInterface() {
      return networkInterface;
   }

   public RestTopicDto setNetworkInterface(NetworkInterface networkInterface) {
      this.networkInterface = networkInterface;
      return this;
   }

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

   @Override
   public String toString() {
      return "RestTopicDto["
             + "\n\tnetworkAddress=" + networkAddress.getAddress()
             + "\n\tnetworkInterface=" + networkInterface.getName()
             + "\n\tport=" + port
             + "\n\thttpMethod=" + httpMethod
             + "\n\tpath=" + path
             + "\n\tcontentType=" + contentType
             + "\n\tvariableName=" + variableName
             + "\n\tname=" + name
             + "\n]";
   }
}
