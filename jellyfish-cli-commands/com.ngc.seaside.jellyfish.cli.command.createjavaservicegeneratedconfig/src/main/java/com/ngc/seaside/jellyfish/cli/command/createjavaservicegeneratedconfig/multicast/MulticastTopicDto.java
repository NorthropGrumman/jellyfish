package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.multicast;

public class MulticastTopicDto {

   private String variableName;
   private String groupAddress;
   private int port;
   private String name;
   private String bindAddress;
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

   public String getBindAddress() {
      return bindAddress;
   }

   public MulticastTopicDto setBindAddress(String bindAddress) {
      this.bindAddress = bindAddress;
      return this;
   }

}
