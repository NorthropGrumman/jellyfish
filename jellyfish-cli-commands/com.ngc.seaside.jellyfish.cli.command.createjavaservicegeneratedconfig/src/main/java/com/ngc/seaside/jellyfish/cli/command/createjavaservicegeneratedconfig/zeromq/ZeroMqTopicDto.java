package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.zeromq;

public class ZeroMqTopicDto {

   private String connectionType;
   private String socketType;
   private String networkInterface;
   private int port;
   private String variableName;
   private String name;
   private boolean isSend;

   public String getVariableName() {
      return variableName;
   }

   public ZeroMqTopicDto setVariableName(String variableName) {
      this.variableName = variableName;
      return this;
   }

   public String getConnectionType() {
      return connectionType;
   }

   public ZeroMqTopicDto setConnectionType(String connectionType) {
      this.connectionType = connectionType;
      return this;
   }

   public String getSocketType() {
      return socketType;
   }

   public ZeroMqTopicDto setSocketType(String socketType) {
      this.socketType = socketType;
      return this;
   }

   public String getNetworkInterface() {
      return networkInterface;
   }

   public ZeroMqTopicDto setNetworkInterface(String networkInterface) {
      this.networkInterface = networkInterface;
      return this;
   }

   public int getPort() {
      return port;
   }

   public ZeroMqTopicDto setPort(int port) {
      this.port = port;
      return this;
   }

   public String getName() {
      return name;
   }

   public ZeroMqTopicDto setName(String name) {
      this.name = name;
      return this;
   }

   public boolean isSend() {
      return isSend;
   }

   public ZeroMqTopicDto setSend(boolean isSend) {
      this.isSend = isSend;
      return this;
   }

}
