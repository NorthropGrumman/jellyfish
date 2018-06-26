package com.ngc.seaside.jellyfish.sonarqube.service.impl;

import com.ngc.blocs.service.log.api.ILogService;

import org.sonar.api.utils.log.Loggers;

/**
 * An adapter of the {@link ILogService} to the Sonarqube logging API.
 */
public class SonarqubeLogService implements ILogService {

   @Override
   public void error(Class<?> clazz, Object message) {
      Loggers.get(clazz).error(message == null ? "" : message.toString());
   }

   @Override
   public void error(Class<?> clazz, String msgFormat, Object... params) {
      Loggers.get(clazz).error(String.format(msgFormat, params));
   }

   @Override
   public void error(Class<?> clazz, Object message, Throwable t) {
      Loggers.get(clazz).error(message == null ? "" : message.toString(), t);
   }

   @Override
   public void error(Class<?> clazz, Throwable t, String msgFormat, Object... params) {
      Loggers.get(clazz).error(String.format(msgFormat, params), t);
   }

   @Override
   public void warn(Class<?> clazz, Object message) {
      Loggers.get(clazz).warn(message == null ? "" : message.toString());
   }

   @Override
   public void warn(Class<?> clazz, Throwable t, String msgFormat, Object... params) {
      Loggers.get(clazz).warn(String.format(msgFormat, params), t);
   }

   @Override
   public void warn(Class<?> clazz, Object message, Throwable t) {
      Loggers.get(clazz).warn(message == null ? "" : message.toString(), t);
   }

   @Override
   public void warn(Class<?> clazz, String msgFormat, Object... params) {
      Loggers.get(clazz).warn(String.format(msgFormat, params));
   }

   @Override
   public void info(Class<?> clazz, Object message) {
      Loggers.get(clazz).info(message == null ? "" : message.toString());
   }

   @Override
   public void info(Class<?> clazz, String msgFormat, Object... params) {
      Loggers.get(clazz).info(String.format(msgFormat, params));
   }

   @Override
   public void info(Class<?> clazz, Object message, Throwable t) {
      Loggers.get(clazz).info(message == null ? "" : message.toString(), t);
   }

   @Override
   public void info(Class<?> clazz, Throwable t, String msgFormat, Object... params) {
      Loggers.get(clazz).info(String.format(msgFormat, params), t);
   }

   @Override
   public void debug(Class<?> clazz, Object message) {
      Loggers.get(clazz).debug(message == null ? "" : message.toString());
   }

   @Override
   public void debug(Class<?> clazz, String msgFormat, Object... params) {
      Loggers.get(clazz).debug(String.format(msgFormat, params));
   }

   @Override
   public void debug(Class<?> clazz, Object message, Throwable t) {
      Loggers.get(clazz).debug(message == null ? "" : message.toString(), t);
   }

   @Override
   public void debug(Class<?> clazz, Throwable t, String msgFormat, Object... params) {
      Loggers.get(clazz).debug(String.format(msgFormat, params), t);
   }

   @Override
   public void trace(Class<?> clazz, Object message) {
      Loggers.get(clazz).trace(message == null ? "" : message.toString());
   }

   @Override
   public void trace(Class<?> clazz, String msgFormat, Object... params) {
      Loggers.get(clazz).trace(String.format(msgFormat, params));
   }

   @Override
   public void trace(Class<?> clazz, Object message, Throwable t) {
      Loggers.get(clazz).trace(message == null ? "" : message.toString(), t);
   }

   @Override
   public void trace(Class<?> clazz, Throwable t, String msgFormat, Object... params) {
      Loggers.get(clazz).trace(String.format(msgFormat, params), t);
   }

   @Override
   public boolean isInfoEnabled(Class<?> clazz) {
      return true;
   }

   @Override
   public boolean isDebugEnabled(Class<?> clazz) {
      return Loggers.get(clazz).isDebugEnabled();
   }

   @Override
   public boolean isTraceEnabled(Class<?> clazz) {
      return Loggers.get(clazz).isTraceEnabled();
   }

   @Override
   public boolean isMdcAvailable() {
      return false;
   }

   @Override
   public Object mdcGet(String key) {
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public void mdcPut(String key, Object o) {
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public void mdcClear(String key) {
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public void mdcRemove(String key) {
      throw new UnsupportedOperationException("not implemented");
   }
}
