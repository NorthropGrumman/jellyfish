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

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.service.log.impl.common.LogService;

/**
 * Adapts the log service implementation so it can work in Guice.
 */
public class LogServiceDelegate implements ILogService {

   private final LogService logService;

   public LogServiceDelegate() {
      this.logService = new LogService();
      this.logService.activate();
   }

   @Override
   public void error(Class<?> clazz, Object message) {
      logService.error(clazz, message);
   }

   @Override
   public void error(Class<?> clazz, String msgFormat, Object... params) {
      logService.error(clazz, msgFormat, params);
   }

   @Override
   public void error(Class<?> clazz, Object message, Throwable t) {
      logService.error(clazz, message, t);
   }

   @Override
   public void error(Class<?> clazz, Throwable t, String msgFormat, Object... params) {
      logService.error(clazz, t, msgFormat, params);
   }

   @Override
   public void warn(Class<?> clazz, Object message) {
      logService.warn(clazz, message);
   }

   @Override
   public void warn(Class<?> clazz, String msgFormat, Object... params) {
      logService.warn(clazz, msgFormat, params);
   }

   @Override
   public void warn(Class<?> clazz, Object message, Throwable t) {
      logService.warn(clazz, message, t);
   }

   @Override
   public void warn(Class<?> clazz, Throwable t, String msgFormat, Object... params) {
      logService.warn(clazz, t, msgFormat, params);
   }

   @Override
   public void info(Class<?> clazz, Object message) {
      logService.info(clazz, message);
   }

   @Override
   public void info(Class<?> clazz, String msgFormat, Object... params) {
      logService.info(clazz, msgFormat, params);
   }

   @Override
   public void info(Class<?> clazz, Object message, Throwable t) {
      logService.info(clazz, message, t);
   }

   @Override
   public void info(Class<?> clazz, Throwable t, String msgFormat, Object... params) {
      logService.info(clazz, t, msgFormat, params);
   }

   @Override
   public void debug(Class<?> clazz, Object message) {
      logService.debug(clazz, message);
   }

   @Override
   public void debug(Class<?> clazz, String msgFormat, Object... params) {
      logService.debug(clazz, msgFormat, params);
   }

   @Override
   public void debug(Class<?> clazz, Object message, Throwable t) {
      logService.debug(clazz, message, t);
   }

   @Override
   public void debug(Class<?> clazz, Throwable t, String msgFormat, Object... params) {
      logService.debug(clazz, t, msgFormat, params);
   }

   @Override
   public void trace(Class<?> clazz, Object message) {
      logService.trace(clazz, message);
   }

   @Override
   public void trace(Class<?> clazz, String msgFormat, Object... params) {
      logService.trace(clazz, msgFormat, params);
   }

   @Override
   public void trace(Class<?> clazz, Object message, Throwable t) {
      logService.trace(clazz, message, t);
   }

   @Override
   public void trace(Class<?> clazz, Throwable t, String msgFormat, Object... params) {
      logService.trace(clazz, t, msgFormat, params);
   }

   @Override
   public boolean isInfoEnabled(Class<?> clazz) {
      return logService.isInfoEnabled(clazz);
   }

   @Override
   public boolean isDebugEnabled(Class<?> clazz) {
      return logService.isDebugEnabled(clazz);
   }

   @Override
   public boolean isTraceEnabled(Class<?> clazz) {
      return logService.isTraceEnabled(clazz);
   }

   @Override
   public boolean isMdcAvailable() {
      return logService.isMdcAvailable();
   }

   @Override
   public Object mdcGet(String key) {
      return logService.mdcGet(key);
   }

   @Override
   public void mdcPut(String key, Object o) {
      logService.mdcPut(key, o);
   }

   @Override
   public void mdcClear(String key) {
      logService.mdcClear(key);
   }

   @Override
   public void mdcRemove(String key) {
      logService.mdcRemove(key);
   }
}
