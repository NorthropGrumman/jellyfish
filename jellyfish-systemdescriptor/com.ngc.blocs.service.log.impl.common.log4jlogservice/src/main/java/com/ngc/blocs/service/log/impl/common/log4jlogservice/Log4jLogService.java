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
