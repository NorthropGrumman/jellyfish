package com.ngc.seaside.jellyfish.service.config.api.dto;

import java.util.Objects;

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

   @Override
   public boolean equals(Object o) {
      if (!(o instanceof MulticastConfiguration)) {
         return false;
      }
      MulticastConfiguration that = (MulticastConfiguration) o;
      return Objects.equals(this.address, that.address) && this.port == that.port;
   }

   @Override
   public int hashCode() {
      return Objects.hash(address, port);
   }

   @Override
   public String toString() {
      return "MulticastConfiguration[address=" + address + ", port=" + port + "]";
   }
}
