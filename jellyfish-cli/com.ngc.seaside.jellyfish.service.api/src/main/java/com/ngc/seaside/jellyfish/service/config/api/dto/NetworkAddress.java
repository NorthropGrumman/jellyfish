package com.ngc.seaside.jellyfish.service.config.api.dto;

import java.util.Objects;

public class NetworkAddress {
   private String address;

   public String getAddress() {
      return address;
   }

   public NetworkAddress setAddress(String address) {
      this.address = address;
      return this;
   }

   @Override
   public boolean equals(Object o) {
      if (!(o instanceof NetworkAddress)) {
         return false;
      }
      NetworkAddress that = (NetworkAddress) o;
      return Objects.equals(this.address, that.address);
   }

   @Override
   public int hashCode() {
      return Objects.hash(address);
   }

   @Override
   public String toString() {
      return "NetworkAddress[address=" + address + "]";
   }

}
