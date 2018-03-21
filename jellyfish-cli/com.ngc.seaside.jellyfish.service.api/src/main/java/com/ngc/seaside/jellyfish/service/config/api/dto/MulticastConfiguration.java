package com.ngc.seaside.jellyfish.service.config.api.dto;

public class MulticastConfiguration {

   private String address;
   private int port;

   public String getAddress() {
      return address;
   }

   public void setAddress(String address) {
      this.address = address;
   }

   public int getPort() {
      return port;
   }

   public void setPort(int port) {
      this.port = port;
   }

}
