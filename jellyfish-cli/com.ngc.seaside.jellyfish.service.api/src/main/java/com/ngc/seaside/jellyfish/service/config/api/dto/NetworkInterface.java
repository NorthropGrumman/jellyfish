package com.ngc.seaside.jellyfish.service.config.api.dto;

import java.util.Objects;

public class NetworkInterface {

   private String name;

   public NetworkInterface(String name) {
      this.name = name;
   }

   public NetworkInterface() {
   }

   public String getName() {
      return name;
   }

   public NetworkInterface setName(String name) {
      this.name = name;
      return this;
   }

   @Override
   public boolean equals(Object o) {
      if (!(o instanceof NetworkInterface)) {
         return false;
      }
      NetworkInterface that = (NetworkInterface) o;
      return Objects.equals(this.name, that.name);
   }

   @Override
   public int hashCode() {
      return Objects.hash(name);
   }

   @Override
   public String toString() {
      return "NetworkInterface[name=" + name + "]";
   }

}
