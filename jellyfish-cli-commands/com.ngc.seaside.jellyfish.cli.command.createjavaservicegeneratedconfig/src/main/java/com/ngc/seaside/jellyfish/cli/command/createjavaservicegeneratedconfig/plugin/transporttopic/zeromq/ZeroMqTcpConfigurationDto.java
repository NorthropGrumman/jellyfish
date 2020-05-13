/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
