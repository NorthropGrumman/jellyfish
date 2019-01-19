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
