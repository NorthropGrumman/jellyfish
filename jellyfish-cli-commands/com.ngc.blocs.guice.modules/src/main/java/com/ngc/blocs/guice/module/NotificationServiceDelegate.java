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
