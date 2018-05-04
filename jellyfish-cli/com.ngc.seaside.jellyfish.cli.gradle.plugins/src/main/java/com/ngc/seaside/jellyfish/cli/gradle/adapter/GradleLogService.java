package com.ngc.seaside.jellyfish.cli.gradle.adapter;

import com.google.inject.AbstractModule;
import com.google.inject.Module;

import com.ngc.blocs.service.log.api.ILogService;

import org.gradle.api.logging.Logging;

public class GradleLogService implements ILogService {

   @Override
   public void error(Class<?> aClass, Object o) {
      Logging.getLogger(aClass).error(s(o));
   }

   @Override
   public void error(Class<?> aClass, String s, Object... objects) {
      Logging.getLogger(aClass).error(f(s, objects));
   }

   @Override
   public void error(Class<?> aClass, Object o, Throwable throwable) {
      Logging.getLogger(aClass).error(s(o), throwable);
   }

   @Override
   public void error(Class<?> aClass, Throwable throwable, String s, Object... objects) {
      Logging.getLogger(aClass).error(f(s, objects), throwable);
   }

   @Override
   public void warn(Class<?> aClass, Object o) {
      Logging.getLogger(aClass).warn(s(o));
   }

   @Override
   public void warn(Class<?> aClass, Throwable throwable, String s, Object... objects) {
      Logging.getLogger(aClass).warn(f(s, objects), throwable);
   }

   @Override
   public void warn(Class<?> aClass, Object o, Throwable throwable) {
      Logging.getLogger(aClass).warn(s(o), throwable);
   }

   @Override
   public void warn(Class<?> aClass, String s, Object... objects) {
      Logging.getLogger(aClass).warn(f(s, objects));
   }

   @Override
   public void info(Class<?> aClass, Object o) {
      Logging.getLogger(aClass).info(s(o));
   }

   @Override
   public void info(Class<?> aClass, String s, Object... objects) {
      Logging.getLogger(aClass).info(f(s, objects));
   }

   @Override
   public void info(Class<?> aClass, Object o, Throwable throwable) {
      Logging.getLogger(aClass).info(s(o), throwable);
   }

   @Override
   public void info(Class<?> aClass, Throwable throwable, String s, Object... objects) {
      Logging.getLogger(aClass).info(f(s, objects), throwable);
   }

   @Override
   public void debug(Class<?> aClass, Object o) {
      Logging.getLogger(aClass).debug(s(o));
   }

   @Override
   public void debug(Class<?> aClass, String s, Object... objects) {
      Logging.getLogger(aClass).debug(f(s, objects));
   }

   @Override
   public void debug(Class<?> aClass, Object o, Throwable throwable) {
      Logging.getLogger(aClass).debug(s(o), throwable);
   }

   @Override
   public void debug(Class<?> aClass, Throwable throwable, String s, Object... objects) {
      Logging.getLogger(aClass).debug(f(s, objects), throwable);
   }

   @Override
   public void trace(Class<?> aClass, Object o) {
      Logging.getLogger(aClass).trace(s(o));
   }

   @Override
   public void trace(Class<?> aClass, String s, Object... objects) {
      Logging.getLogger(aClass).trace(f(s, objects));
   }

   @Override
   public void trace(Class<?> aClass, Object o, Throwable throwable) {
      Logging.getLogger(aClass).trace(s(o), throwable);
   }

   @Override
   public void trace(Class<?> aClass, Throwable throwable, String s, Object... objects) {
      Logging.getLogger(aClass).trace(f(s, objects), throwable);
   }

   @Override
   public boolean isInfoEnabled(Class<?> aClass) {
      return Logging.getLogger(aClass).isInfoEnabled();
   }

   @Override
   public boolean isDebugEnabled(Class<?> aClass) {
      return Logging.getLogger(aClass).isDebugEnabled();
   }

   @Override
   public boolean isTraceEnabled(Class<?> aClass) {
      return Logging.getLogger(aClass).isTraceEnabled();
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

   private static String s(Object o) {
      return o == null ? "null" : o.toString();
   }

   private static String f(String s, Object... o) {
      return String.format(s, o);
   }
}
