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
