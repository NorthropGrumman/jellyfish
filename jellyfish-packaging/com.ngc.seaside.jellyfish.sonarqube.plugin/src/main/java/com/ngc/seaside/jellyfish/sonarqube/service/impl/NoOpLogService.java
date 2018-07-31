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
