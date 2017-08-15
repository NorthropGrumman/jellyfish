package ${dto.packageName}.config;

import com.google.inject.Inject;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.service.resource.api.IResourceService;
import com.ngc.seaside.service.transport.api.ITransportProvider;
import com.ngc.seaside.service.transport.api.TransportConfiguration;
import com.ngc.seaside.service.transport.impl.defaultz.module.DefaultTransportServiceDelegate;
import com.ngc.seaside.service.transport.impl.topic.multicast.MulticastTopic;
import ${model}.transport.topic.${dto.className}TransportTopics;

import java.net.UnknownHostException;

public class ${dto.className}TestConfiguration {

   @SuppressWarnings("unused")
   private final ILogService logService;

   @SuppressWarnings("unused")
   private final IResourceService resourceService;

   private final DefaultTransportServiceDelegate transportService;

   private final ITransportProvider<MulticastTopic> multicastProvider;

   @Inject
   public ${dto.className}TestConfiguration(ILogService logService,
                                                          IResourceService resourceService,
                                                          DefaultTransportServiceDelegate transportService,
                                                          ITransportProvider<MulticastTopic> multicastProvider) {
      this.logService = logService;
      this.resourceService = resourceService;
      this.transportService = transportService;
      this.multicastProvider = multicastProvider;
      activate();
   }

   private void activate() {
      try {
         MulticastTopic trackPriorityTopic = new MulticastTopic("224.5.6.7", 61001);
         MulticastTopic trackEngagementStatusTopic = new MulticastTopic("224.5.6.8", 61002);

         TransportConfiguration config = new TransportConfiguration();
         config.whenReceiving()
               .withTransportProvider(multicastProvider)
               .onPhysicalTopic(trackPriorityTopic)
               .enableResponses(false)
               .useApplicationTopic(t -> ${dto.className}TransportTopics.TRACK_PRIORITY);

         config.whenSending()
               .toApplicationTopic(${dto.className}TransportTopics.TRACK_ENGAGEMENT_STATUS)
               .useTransportProvider(multicastProvider)
               .onPhysicalTopic(t -> trackEngagementStatusTopic);

         transportService.addTransportProvider(multicastProvider);
         transportService.registerConfiguration(config);
      } catch (UnknownHostException e) {
         throw new RuntimeException(e.getMessage(), e);
      }
   }
}
