package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.multicast;

public class MulticastTopicDto {

   private String variableName;
   private String address;
   private int port;
   private String localAddress = "127.0.0.1";
   private String name;
   private boolean isSend;

   public String getVariableName() {
      return variableName;
   }

   public MulticastTopicDto setVariableName(String variableName) {
      this.variableName = variableName;
      return this;
   }

   public String getAddress() {
      return address;
   }

   public MulticastTopicDto setAddress(String address) {
      this.address = address;
      return this;
   }

   public int getPort() {
      return port;
   }

   public MulticastTopicDto setPort(int port) {
      this.port = port;
      return this;
   }

   public String getLocalAddress() {
      return localAddress;
   }

   public MulticastTopicDto setLocalAddress(String localAddress) {
      this.localAddress = localAddress;
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

}
