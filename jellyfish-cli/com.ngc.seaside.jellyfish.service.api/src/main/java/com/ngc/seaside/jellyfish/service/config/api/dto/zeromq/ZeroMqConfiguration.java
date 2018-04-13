package com.ngc.seaside.jellyfish.service.config.api.dto.zeromq;

/**
 * A base data type for the different ZeroMQ configuration options. This type should not be used directly. Use one
 * of the other configuration types instead.
 */
public abstract class ZeroMqConfiguration {

   private ConnectionType connectionType;

   ZeroMqConfiguration() {}

   /**
    * Defines how the source and target connect to one another and identifies which is the client and which is the server.
    */
   public ConnectionType getConnectionType() {
      return connectionType;
   }

   public ZeroMqConfiguration setConnectionType(ConnectionType connectionType) {
      this.connectionType = connectionType;
      return this;
   }

}
