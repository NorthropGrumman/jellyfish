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

import com.ngc.seaside.systemdescriptor.service.log.api.ILogService;

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
}
