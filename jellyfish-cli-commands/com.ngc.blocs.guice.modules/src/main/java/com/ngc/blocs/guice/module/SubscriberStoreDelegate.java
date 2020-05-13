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

import com.ngc.blocs.service.event.api.IEventSubscriber;
import com.ngc.blocs.service.event.api.IEventTopic;
import com.ngc.blocs.service.event.extended.api.IEventSubscriberInterceptor;
import com.ngc.blocs.service.event.impl.common.eventservice.DeclarativeEventSubscriberAdapter;
import com.ngc.blocs.service.event.impl.common.eventservice.ISubscriberStore;
import com.ngc.blocs.service.event.impl.common.eventservice.SubscriberWrapper;
import com.ngc.blocs.service.event.impl.common.eventservice.internal.SubscriberStore;
import com.ngc.blocs.service.notification.api.INotificationService;

import java.util.Collection;

import javax.inject.Inject;

public class SubscriberStoreDelegate implements ISubscriberStore {

   private final SubscriberStore delegate;

   @Inject
   public SubscriberStoreDelegate(INotificationService notificationService) {
      delegate = new SubscriberStore();
      delegate.setNotificationService(notificationService);
   }

   @Override
   public Collection<SubscriberWrapper> getWrappers(IEventTopic<?> topic) {
      return delegate.getWrappers(topic);
   }

   @Override
   public SubscriberWrapper getWrapperBySubscriber(IEventSubscriber<?> subscriber) {
      return delegate.getWrapperBySubscriber(subscriber);
   }

   @Override
   public SubscriberWrapper getOrCreateWrapper(@SuppressWarnings("rawtypes") IEventSubscriber subscriber,
            @SuppressWarnings("rawtypes") IEventSubscriberInterceptor interceptor) {
      return delegate.getOrCreateWrapper(subscriber, interceptor);
   }

   @Override
   public SubscriberWrapper removeWrapperFromTopic(IEventSubscriber<?> subscriber, IEventTopic<?> topic) {
      return delegate.removeWrapperFromTopic(subscriber, topic);
   }

   @Override
   public SubscriberWrapper removeWrapper(IEventSubscriber<?> subscriber) {
      return delegate.removeWrapper(subscriber);
   }

   @Override
   public SubscriberWrapper getWrapperByRawSubscriber(Object obj) {
      return delegate.getWrapperByRawSubscriber(obj);
   }

   @Override
   public DeclarativeEventSubscriberAdapter getAdapter(Object obj) {
      return delegate.getAdapter(obj);
   }

   @Override
   public boolean addAdapter(DeclarativeEventSubscriberAdapter adapter) {
      return delegate.addAdapter(adapter);
   }

   @Override
   public DeclarativeEventSubscriberAdapter removeAdapterFor(Object object) {
      return delegate.removeAdapterFor(object);
   }

}
