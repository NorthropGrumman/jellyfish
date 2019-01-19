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
package com.ngc.seaside.jellyfish.sonarqube.service.impl;

import com.ngc.blocs.service.log.api.ILogService;

/**
 * An implementation of {@code ILogService} that suppresses all logging.  This is useful when running Jellyfish just to
 * get an instance of the injector and we don't actually want to see any output in logs.
 */
public class NoOpLogService implements ILogService {

   @Override
   public void error(Class<?> clazz, Object o) {
   }

   @Override
   public void error(Class<?> clazz, String s, Object... objects) {
   }

   @Override
   public void error(Class<?> clazz, Object o, Throwable throwable) {
   }

   @Override
   public void error(Class<?> clazz, Throwable throwable, String s, Object... objects) {
   }

   @Override
   public void warn(Class<?> clazz, Object o) {
   }

   @Override
   public void warn(Class<?> clazz, Throwable throwable, String s, Object... objects) {
   }

   @Override
   public void warn(Class<?> clazz, Object o, Throwable throwable) {
   }

   @Override
   public void warn(Class<?> clazz, String s, Object... objects) {
   }

   @Override
   public void info(Class<?> clazz, Object o) {
   }

   @Override
   public void info(Class<?> clazz, String s, Object... objects) {
   }

   @Override
   public void info(Class<?> clazz, Object o, Throwable throwable) {
   }

   @Override
   public void info(Class<?> clazz, Throwable throwable, String s, Object... objects) {
   }

   @Override
   public void debug(Class<?> clazz, Object o) {
   }

   @Override
   public void debug(Class<?> clazz, String s, Object... objects) {
   }

   @Override
   public void debug(Class<?> clazz, Object o, Throwable throwable) {
   }

   @Override
   public void debug(Class<?> clazz, Throwable throwable, String s, Object... objects) {
   }

   @Override
   public void trace(Class<?> clazz, Object o) {
   }

   @Override
   public void trace(Class<?> clazz, String s, Object... objects) {
   }

   @Override
   public void trace(Class<?> clazz, Object o, Throwable throwable) {
   }

   @Override
   public void trace(Class<?> clazz, Throwable throwable, String s, Object... objects) {
   }

   @Override
   public boolean isInfoEnabled(Class<?> clazz) {
      return false;
   }

   @Override
   public boolean isDebugEnabled(Class<?> clazz) {
      return false;
   }

   @Override
   public boolean isTraceEnabled(Class<?> clazz) {
      return false;
   }

   @Override
   public boolean isMdcAvailable() {
      return false;
   }

   @Override
   public Object mdcGet(String s) {
      return null;
   }

   @Override
   public void mdcPut(String s, Object o) {
   }

   @Override
   public void mdcClear(String s) {
   }

   @Override
   public void mdcRemove(String s) {
   }
}
