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
