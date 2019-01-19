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
package com.ngc.seaside.jellyfish.cli.gradle.adapter;

import com.google.inject.AbstractModule;
import com.google.inject.Module;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.cli.command.report.console.ConsoleAnalysisReportCommand;

import org.gradle.api.logging.Logging;

public class GradleLogService implements ILogService {

   @Override
   public void error(Class<?> clazz, Object o) {
      Logging.getLogger(clazz).error(nullSafe(o));
   }

   @Override
   public void error(Class<?> error, String s, Object... objects) {
      Logging.getLogger(error).error(format(s, objects));
   }

   @Override
   public void error(Class<?> clazz, Object o, Throwable throwable) {
      Logging.getLogger(clazz).error(nullSafe(o), throwable);
   }

   @Override
   public void error(Class<?> clazz, Throwable throwable, String s, Object... objects) {
      Logging.getLogger(clazz).error(format(s, objects), throwable);
   }

   @Override
   public void warn(Class<?> clazz, Object o) {
      Logging.getLogger(clazz).warn(nullSafe(o));
   }

   @Override
   public void warn(Class<?> clazz, Throwable throwable, String s, Object... objects) {
      Logging.getLogger(clazz).warn(format(s, objects), throwable);
   }

   @Override
   public void warn(Class<?> clazz, Object o, Throwable throwable) {
      Logging.getLogger(clazz).warn(nullSafe(o), throwable);
   }

   @Override
   public void warn(Class<?> clazz, String s, Object... objects) {
      Logging.getLogger(clazz).warn(format(s, objects));
   }

   @Override
   public void info(Class<?> clazz, Object o) {
      if (shouldLoggingBePromoted(clazz)) {
         Logging.getLogger(clazz).lifecycle(nullSafe(o));
      } else {
         Logging.getLogger(clazz).info(nullSafe(o));
      }
   }

   @Override
   public void info(Class<?> clazz, String s, Object... objects) {
      if (shouldLoggingBePromoted(clazz)) {
         Logging.getLogger(clazz).lifecycle(format(s, objects));
      } else {
         Logging.getLogger(clazz).info(format(s, objects));
      }
   }

   @Override
   public void info(Class<?> clazz, Object o, Throwable throwable) {
      if (shouldLoggingBePromoted(clazz)) {
         Logging.getLogger(clazz).lifecycle(nullSafe(o), throwable);
      } else {
         Logging.getLogger(clazz).info(nullSafe(o), throwable);
      }
   }

   @Override
   public void info(Class<?> clazz, Throwable throwable, String s, Object... objects) {
      if (shouldLoggingBePromoted(clazz)) {
         Logging.getLogger(clazz).lifecycle(format(s, objects), throwable);
      } else {
         Logging.getLogger(clazz).info(format(s, objects), throwable);
      }
   }

   @Override
   public void debug(Class<?> clazz, Object o) {
      Logging.getLogger(clazz).debug(nullSafe(o));
   }

   @Override
   public void debug(Class<?> clazz, String s, Object... objects) {
      Logging.getLogger(clazz).debug(format(s, objects));
   }

   @Override
   public void debug(Class<?> clazz, Object o, Throwable throwable) {
      Logging.getLogger(clazz).debug(nullSafe(o), throwable);
   }

   @Override
   public void debug(Class<?> clazz, Throwable throwable, String s, Object... objects) {
      Logging.getLogger(clazz).debug(format(s, objects), throwable);
   }

   @Override
   public void trace(Class<?> clazz, Object o) {
      Logging.getLogger(clazz).trace(nullSafe(o));
   }

   @Override
   public void trace(Class<?> clazz, String s, Object... objects) {
      Logging.getLogger(clazz).trace(format(s, objects));
   }

   @Override
   public void trace(Class<?> clazz, Object o, Throwable throwable) {
      Logging.getLogger(clazz).trace(nullSafe(o), throwable);
   }

   @Override
   public void trace(Class<?> clazz, Throwable throwable, String s, Object... objects) {
      Logging.getLogger(clazz).trace(format(s, objects), throwable);
   }

   @Override
   public boolean isInfoEnabled(Class<?> clazz) {
      return Logging.getLogger(clazz).isInfoEnabled();
   }

   @Override
   public boolean isDebugEnabled(Class<?> clazz) {
      return Logging.getLogger(clazz).isDebugEnabled();
   }

   @Override
   public boolean isTraceEnabled(Class<?> clazz) {
      return Logging.getLogger(clazz).isTraceEnabled();
   }

   @Override
   public boolean isMdcAvailable() {
      return false;
   }

   @Override
   public Object mdcGet(String s) {
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public void mdcPut(String s, Object o) {
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public void mdcClear(String s) {
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public void mdcRemove(String s) {
      throw new UnsupportedOperationException("not implemented");
   }

   public static final Module MODULE = new AbstractModule() {

      @Override
      protected void configure() {
         bind(ILogService.class).to(GradleLogService.class);
      }
   };

   private static String nullSafe(Object o) {
      return o == null ? "null" : o.toString();
   }

   private static String format(String s, Object... o) {
      return String.format(s, o);
   }

   private static boolean shouldLoggingBePromoted(Class<?> clazz) {
      // The console reporter logs at the info level, so we don't see the results in the Gradle output.  Therefore,
      // if the console reporter logs anything, we promote it to the lifecycle level so we can actually see the
      // output.
      return ConsoleAnalysisReportCommand.class.equals(clazz);
   }
}
