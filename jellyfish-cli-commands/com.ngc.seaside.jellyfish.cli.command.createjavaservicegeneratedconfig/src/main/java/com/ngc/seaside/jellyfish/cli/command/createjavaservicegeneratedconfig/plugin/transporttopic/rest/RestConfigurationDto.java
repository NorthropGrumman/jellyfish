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
