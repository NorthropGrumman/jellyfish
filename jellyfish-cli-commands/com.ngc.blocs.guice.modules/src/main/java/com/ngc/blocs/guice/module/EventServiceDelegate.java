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
package com.ngc.blocs.guice.module;

import com.ngc.blocs.service.event.api.IEventService;
import com.ngc.blocs.service.event.api.IEventSubscriber;
import com.ngc.blocs.service.event.api.IEventTopic;
import com.ngc.blocs.service.event.impl.common.eventservice.EventService;
import com.ngc.blocs.service.event.impl.common.eventservice.ISubscriberStore;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.service.notification.api.INotificationService;

import java.util.Collection;

import javax.inject.Inject;

public class EventServiceDelegate implements IEventService {

   private final EventService delegate;

   @Inject
   public EventServiceDelegate(ILogService logService, INotificationService notificationService,
                               ISubscriberStore store) {
      delegate = new EventService();
      delegate.setLogService(logService);
      delegate.setNotificationService(notificationService);
      delegate.setSubscriberStore(store);
      delegate.activate();
   }

   @Override
   public <T> void publish(T eventSource, IEventTopic<T> topic) {
      delegate.publish(eventSource, topic);
   }

   @Override
   public <T> void publishPersistently(T eventSource, IEventTopic<T> topic, Object key) {
      delegate.publishPersistently(eventSource, topic, key);
   }

   @Override
   public void revoke(IEventTopic<?> topic, Object key) {
      delegate.revoke(topic, key);
   }

   @Override
   public boolean addSubscriber(Object obj) {
      return delegate.addSubscriber(obj);
   }

   @SuppressWarnings("unchecked")
   @Override
   public <T> boolean addSubscriber(IEventSubscriber<T> subscriber, IEventTopic<? extends T> topic,
            IEventTopic<? extends T>... optionalTopics) {
      return delegate.addSubscriber(subscriber, topic, optionalTopics);
   }

   @Override
   public <T> boolean addSubscriber(IEventSubscriber<T> subscriber, Collection<IEventTopic<? extends T>> topics) {
      return delegate.addSubscriber(subscriber, topics);
   }

   @Override
   public boolean removeSubscriber(Object obj) {
      return delegate.removeSubscriber(obj);
   }

   @SuppressWarnings("unchecked")
   @Override
   public <T> boolean removeSubscriber(IEventSubscriber<T> subscriber, IEventTopic<? extends T> topic,
            IEventTopic<? extends T>... optionalTopics) {
      return delegate.removeSubscriber(subscriber, topic, optionalTopics);
   }

   @Override
   public <T> boolean removeSubscriber(IEventSubscriber<T> subscriber, Collection<IEventTopic<? extends T>> topics) {
      return delegate.removeSubscriber(subscriber, topics);
   }

}
