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

import com.ngc.blocs.notification.api.IDurableQueue;
import com.ngc.blocs.notification.api.INotificationDelegate;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.service.notification.api.INotificationService;
import com.ngc.blocs.service.notification.impl.common.notificationservice.NotificationService;
import com.ngc.blocs.service.thread.api.IThreadService;

import java.util.Map;

import javax.inject.Inject;

public class NotificationServiceDelegate implements INotificationService {

   private final NotificationService delegate;

   @Inject
   public NotificationServiceDelegate(ILogService logService, IThreadService threadService) {
      delegate = new NotificationService();
      delegate.setLogService(logService);
      delegate.setThreadService(threadService);
      delegate.activate();
   }

   @Override
   public <T> INotificationDelegate<T> getNotificationDelegate(T object) {
      return delegate.getNotificationDelegate(object);
   }

   @Override
   public <T> INotificationDelegate<T> getNotificationDelegate(T object, String queue) {
      return delegate.getNotificationDelegate(object, queue);
   }

   @Override
   public <T> Map<String, INotificationDelegate<T>> getAllDelegates(T object) {
      return delegate.getAllDelegates(object);
   }

   @Override
   public void releaseDelegate(INotificationDelegate<?> delegate) {
      this.delegate.releaseDelegate(delegate);
   }

   @Override
   public <T> IDurableQueue<T> newDurableQueue() {
      return delegate.newDurableQueue();
   }

}
