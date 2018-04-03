package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.multicast;

import com.ngc.seaside.jellyfish.service.config.api.dto.MulticastConfiguration;

public class MulticastTopicDto {

   private String variableName;
   private String groupAddress;
   private int port;
   private String sourceAddress;
   private String targetAddress;
   private String name;
   private String bindName;
   private boolean isSend;

   public String getVariableName() {
      return variableName;
   }

   public MulticastTopicDto setVariableName(String variableName) {
      this.variableName = variableName;
      return this;
   }

   public String getGroupAddress() {
      return groupAddress;
   }

   public MulticastTopicDto setGroupAddress(String groupAddress) {
      this.groupAddress = groupAddress;
      return this;
   }

   public String getSourceAddress() {
      return sourceAddress;
   }

   public MulticastTopicDto setSourceAddress(String sourceAddress) {
      this.sourceAddress = sourceAddress;
      return this;
   }

   public String getTargetAddress() {
      return targetAddress;
   }

   public MulticastTopicDto setTargetAddress(String targetAddress) {
      this.targetAddress = targetAddress;
      return this;
   }

   public int getPort() {
      return port;
   }

   public MulticastTopicDto setPort(int port) {
      this.port = port;
      return this;
   }

   public String getName() {
      return name;
   }

   public MulticastTopicDto setName(String name) {
      this.name = name;
      return this;
   }

   public boolean isSend() {
      return isSend;
   }

   public MulticastTopicDto setSend(boolean isSend) {
      this.isSend = isSend;
      return this;
   }

   public String getBindName() {
      String bindName = sourceAddress;
      if (isSend){
         bindName = targetAddress;
      }
      return bindName;
   }

   public MulticastTopicDto setBindName(String bindName) {
      this.bindName = bindName;
      return this;
   }
}
