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
package com.ngc.blocs.service.log.impl.common.log4jlogservice;

import com.ngc.blocs.service.log.api.ILogService;

import org.apache.log4j.Logger;

/**
 * An implementation of the BLoCS {@code ILogService} that is intented to be used with Eclipse when Eclipse is
 * configured with JellyFish.  It just delegates to log4j which is what the rest of XText does.  Unlike the default
 * BLoCS implementation this service does not attempt to actually configure log4j.  Log4j is configured by the Eclipse
 * platform itself.
 *
 * <p> This service is intended to be used in a Guice module and is not an OSGi DS managed component.
 */
public class Log4jLogService implements ILogService {

   @Override
   public void debug(Class<?> arg0, Object arg1) {
      Logger.getLogger(arg0).debug(arg1);
   }

   @Override
   public void debug(Class<?> arg0, String arg1, Object... arg2) {
      Logger.getLogger(arg0).debug(String.format(arg1, arg2));
   }

   @Override
   public void debug(Class<?> arg0, Object arg1, Throwable arg2) {
      Logger.getLogger(arg0).debug(arg1, arg2);
   }

   @Override
   public void debug(Class<?> arg0, Throwable arg1, String arg2, Object... arg3) {
      Logger.getLogger(arg0).debug(String.format(arg2, arg3), arg1);
   }

   @Override
   public void error(Class<?> arg0, Object arg1) {
      Logger.getLogger(arg0).error(arg1);
   }

   @Override
   public void error(Class<?> arg0, String arg1, Object... arg2) {
      Logger.getLogger(arg0).error(String.format(arg1, arg2));
   }

   @Override
   public void error(Class<?> arg0, Object arg1, Throwable arg2) {
      Logger.getLogger(arg0).error(arg1, arg2);
   }

   @Override
   public void error(Class<?> arg0, Throwable arg1, String arg2, Object... arg3) {
      Logger.getLogger(arg0).error(String.format(arg2, arg3), arg1);
   }

   @Override
   public void info(Class<?> arg0, Object arg1) {
      Logger.getLogger(arg0).info(arg1);
   }

   @Override
   public void info(Class<?> arg0, String arg1, Object... arg2) {
      Logger.getLogger(arg0).info(String.format(arg1, arg2));
   }

   @Override
   public void info(Class<?> arg0, Object arg1, Throwable arg2) {
      Logger.getLogger(arg0).info(arg1, arg2);
   }

   @Override
   public void info(Class<?> arg0, Throwable arg1, String arg2, Object... arg3) {
      Logger.getLogger(arg0).info(String.format(arg2, arg3), arg1);
   }

   @Override
   public boolean isDebugEnabled(Class<?> arg0) {
      return Logger.getLogger(arg0).isDebugEnabled();
   }

   @Override
   public boolean isInfoEnabled(Class<?> arg0) {
      return Logger.getLogger(arg0).isInfoEnabled();
   }

   @Override
   public boolean isMdcAvailable() {
      return false;
   }

   @Override
   public boolean isTraceEnabled(Class<?> arg0) {
      return Logger.getLogger(arg0).isTraceEnabled();
   }

   @Override
   public void mdcClear(String arg0) {
   }

   @Override
   public Object mdcGet(String arg0) {
      return null;
   }

   @Override
   public void mdcPut(String arg0, Object arg1) {
   }

   @Override
   public void mdcRemove(String arg0) {
   }

   @Override
   public void trace(Class<?> arg0, Object arg1) {
      Logger.getLogger(arg0).trace(arg1);
   }

   @Override
   public void trace(Class<?> arg0, String arg1, Object... arg2) {
      Logger.getLogger(arg0).trace(String.format(arg1, arg2));
   }

   @Override
   public void trace(Class<?> arg0, Object arg1, Throwable arg2) {
      Logger.getLogger(arg0).trace(arg1, arg2);
   }

   @Override
   public void trace(Class<?> arg0, Throwable arg1, String arg2, Object... arg3) {
      Logger.getLogger(arg0).trace(String.format(arg2, arg3), arg1);
   }

   @Override
   public void warn(Class<?> arg0, Object arg1) {
      Logger.getLogger(arg0).warn(arg1);
   }

   @Override
   public void warn(Class<?> arg0, Object arg1, Throwable arg2) {
      Logger.getLogger(arg0).warn(arg1, arg2);
   }

   @Override
   public void warn(Class<?> arg0, String arg1, Object... arg2) {
      Logger.getLogger(arg0).warn(String.format(arg1, arg2));
   }

   @Override
   public void warn(Class<?> arg0, Throwable arg1, String arg2, Object... arg3) {
      Logger.getLogger(arg0).warn(String.format(arg2, arg3), arg1);
   }
}
