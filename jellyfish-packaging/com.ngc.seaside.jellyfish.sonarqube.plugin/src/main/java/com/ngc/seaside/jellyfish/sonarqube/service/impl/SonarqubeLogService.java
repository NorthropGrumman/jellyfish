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
package com.ngc.seaside.jellyfish.sonarqube.service.impl;

import org.sonar.api.utils.log.Loggers;

import com.ngc.seaside.systemdescriptor.service.log.api.ILogService;

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
}
