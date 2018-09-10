/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
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
package com.ngc.seaside.jellyfish.service.config.api.dto.zeromq;

import com.google.common.base.Objects;

/**
 * Defines the configuration for a link that should use ZeroMQ's inter-process transport mechanism.
 */
public class ZeroMqIpcTransportConfiguration extends ZeroMqConfiguration {

   private String path;

   /**
    * The file path to use for the buffer. This value should be an absolute file path and it should not begin with
    * 'ipc://'.
    */
   public String getPath() {
      return path;
   }

   public ZeroMqIpcTransportConfiguration setPath(String path) {
      this.path = path;
      return this;
   }

   @Override
   public boolean equals(Object o) {
      if (!(o instanceof ZeroMqIpcTransportConfiguration)) {
         return false;
      }
      ZeroMqIpcTransportConfiguration that = (ZeroMqIpcTransportConfiguration) o;
      return Objects.equal(this.getConnectionType(), that.getConnectionType())
            && Objects.equal(this.getPath(), that.getPath());
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.getConnectionType(), this.getPath());
   }

   @Override
   public String toString() {
      return "ZeroMqIpcTransportConfiguration[connectionType=" + this.getConnectionType()
            + ",groupAddress=" + this.getPath()
            + "]";
   }
}
