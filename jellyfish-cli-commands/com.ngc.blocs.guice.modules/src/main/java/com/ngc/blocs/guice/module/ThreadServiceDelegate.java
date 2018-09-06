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

import com.google.inject.Inject;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.service.resource.api.IResourceService;
import com.ngc.blocs.service.thread.api.ISubmittedLongLivingTask;
import com.ngc.blocs.service.thread.api.ISubmittedTask;
import com.ngc.blocs.service.thread.api.IThreadService;
import com.ngc.blocs.service.thread.api.ThreadServiceException;
import com.ngc.blocs.service.thread.impl.common.ThreadService;
import com.ngc.blocs.time.api.Time;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;

/**
 * This delegate implements the IThreadService interface so that it may be
 * injected with Guice.
 *
 * @author bperkins
 */
public class ThreadServiceDelegate implements IThreadService {

   private final ThreadService delegate;

   @Inject
   public ThreadServiceDelegate(ILogService logService, IResourceService resourceService) {
      delegate = new ThreadService();
      delegate.setLogService(logService);
      delegate.setResourceService(resourceService);
      delegate.activate();
   }

   @Override
   public ISubmittedTask<Void, Future<Void>> submit(String name, Runnable task) throws ThreadServiceException {
      return delegate.submit(name, task);
   }

   @Override
   public <V> ISubmittedTask<V, Future<V>> submit(String name, Callable<V> task) throws ThreadServiceException {
      return delegate.submit(name, task);
   }

   @Override
   public ISubmittedTask<Void, ScheduledFuture<Void>> executeOn(String name, Runnable task, Time startOn)
         throws ThreadServiceException {
      return delegate.executeOn(name, task, startOn);
   }

   @Override
   public ISubmittedTask<Void, ScheduledFuture<Void>> executePeriodically(String name, Runnable task, Time startOn,
                                                                          long millisecondsPeriod)
         throws ThreadServiceException {
      return delegate.executePeriodically(name, task, startOn, millisecondsPeriod);
   }

   @Override
   public ISubmittedTask<Void, ScheduledFuture<Void>> executePeriodically(String name, Runnable task, Time startOn,
                                                                          Time endOn, long millisecondsPeriod)
         throws ThreadServiceException {
      return delegate.executePeriodically(name, task, startOn, endOn, millisecondsPeriod);
   }

   @Override
   public ISubmittedLongLivingTask executeLongLivingTask(String name, Runnable task) throws ThreadServiceException {
      return delegate.executeLongLivingTask(name, task);
   }
}
