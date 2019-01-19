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
