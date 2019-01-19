/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
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
