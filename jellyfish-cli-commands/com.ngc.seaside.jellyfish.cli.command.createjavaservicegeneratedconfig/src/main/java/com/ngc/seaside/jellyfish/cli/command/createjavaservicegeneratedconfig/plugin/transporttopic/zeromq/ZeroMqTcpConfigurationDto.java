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
package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.zeromq;

import com.ngc.seaside.jellyfish.service.config.api.dto.zeromq.ZeroMqTcpTransportConfiguration;

public class ZeroMqTcpConfigurationDto {

   private final ZeroMqTcpTransportConfiguration configuration;
   private final boolean canPublish;
   private final boolean canSubscribe;
   private final boolean canRequest;
   private final boolean canRespond;

   /**
    * Constructor.
    * 
    * @param configuration configuration
    * @param canPublish whether or not providers can use this configuration to publish messages
    * @param canSubscribe whether or not providers can use this configuration to subscribe to messages
    * @param canRequest whether or not providers can use this configuration to request messages
    * @param canRespond whether or not providers can use this configuration to respond to messages
    */
   public ZeroMqTcpConfigurationDto(ZeroMqTcpTransportConfiguration configuration, boolean canPublish,
                                    boolean canSubscribe, boolean canRequest, boolean canRespond) {
      super();
      this.configuration = configuration;
      this.canPublish = canPublish;
      this.canSubscribe = canSubscribe;
      this.canRequest = canRequest;
      this.canRespond = canRespond;
   }

   public ZeroMqTcpTransportConfiguration getConfiguration() {
      return configuration;
   }

   public boolean canPublish() {
      return canPublish;
   }

   public boolean canSubscribe() {
      return canSubscribe;
   }

   public boolean canRequest() {
      return canRequest;
   }

   public boolean canRespond() {
      return canRespond;
   }

}
