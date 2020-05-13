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
