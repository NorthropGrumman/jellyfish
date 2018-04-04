package com.ngc.seaside.jellyfish.service.config.api.dto.zeromq;

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

}
