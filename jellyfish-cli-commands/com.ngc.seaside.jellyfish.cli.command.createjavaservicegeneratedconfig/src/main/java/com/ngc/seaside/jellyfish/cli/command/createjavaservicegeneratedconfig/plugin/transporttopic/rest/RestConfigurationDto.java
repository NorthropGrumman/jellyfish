package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.rest;

import com.ngc.seaside.jellyfish.service.config.api.dto.RestConfiguration;

public class RestConfigurationDto {

   private final RestConfiguration configuration;
   private final boolean canSend;
   private final boolean canReceive;

   /**
    * Constructor.
    * 
    * @param configuration configuration
    * @param canSend whether or not providers can use this configuration to send messages
    * @param canReceive whether or not providers can use this configuration to receive messages
    */
   public RestConfigurationDto(RestConfiguration configuration, boolean canSend, boolean canReceive) {
      super();
      this.configuration = configuration;
      this.canSend = canSend;
      this.canReceive = canReceive;
   }

   public RestConfiguration getConfiguration() {
      return configuration;
   }

   public boolean canSend() {
      return canSend;
   }

   public boolean canReceive() {
      return canReceive;
   }

}
