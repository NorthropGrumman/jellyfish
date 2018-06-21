package com.ngc.blocs.guice.module;

import com.google.inject.AbstractModule;
import com.ngc.blocs.service.event.api.IEventService;
import com.ngc.blocs.service.event.impl.common.eventservice.ISubscriberStore;
import com.ngc.blocs.service.notification.api.INotificationService;

public class EventServiceModule extends AbstractModule {

   @Override
   protected void configure() {
      bind(IEventService.class).to(EventServiceDelegate.class);
      bind(ISubscriberStore.class).to(SubscriberStoreDelegate.class);
      bind(INotificationService.class).to(NotificationServiceDelegate.class);
   }

}
