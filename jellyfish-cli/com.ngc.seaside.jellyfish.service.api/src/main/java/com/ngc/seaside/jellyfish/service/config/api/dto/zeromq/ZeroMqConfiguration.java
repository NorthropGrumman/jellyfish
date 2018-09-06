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

/**
 * A base data type for the different ZeroMQ configuration options. This type should not be used directly. Use one
 * of the other configuration types instead.
 */
public abstract class ZeroMqConfiguration {

   private ConnectionType connectionType;

   ZeroMqConfiguration() {
   }

   /**
    * Defines how the source and target connect to one another and identifies which is the client and
    * which is the server.
    */
   public ConnectionType getConnectionType() {
      return connectionType;
   }

   public ZeroMqConfiguration setConnectionType(ConnectionType connectionType) {
      this.connectionType = connectionType;
      return this;
   }

}
