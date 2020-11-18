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
package com.ngc.seaside.jellyfish;

import org.apache.logging.log4j.LogManager;

import com.ngc.seaside.systemdescriptor.service.log.api.ILogService;

public class Log4J2LogService implements ILogService {

    @Override
    public void error(Class<?> clazz, Object message) {
        LogManager.getLogger(clazz).error(message);
    }

    @Override
    public void error(Class<?> clazz, String msgFormat, Object... params) {
        LogManager.getLogger(clazz).error(String.format(msgFormat, params));
    }

    @Override
    public void error(Class<?> clazz, Object message, Throwable t) {
        LogManager.getLogger(clazz).error(message, t);
    }

    @Override
    public void error(Class<?> clazz, Throwable t, String msgFormat, Object... params) {
        LogManager.getLogger(clazz).error(String.format(msgFormat, params), t);
    }

    @Override
    public void warn(Class<?> clazz, Object message) {
        LogManager.getLogger(clazz).warn(message);
    }

    @Override
    public void warn(Class<?> clazz, Throwable t, String msgFormat, Object... params) {
        LogManager.getLogger(clazz).warn(String.format(msgFormat, params), t);
    }

    @Override
    public void warn(Class<?> clazz, Object message, Throwable t) {
        LogManager.getLogger(clazz).warn(message, t);
    }

    @Override
    public void warn(Class<?> clazz, String msgFormat, Object... params) {
        LogManager.getLogger(clazz).warn(String.format(msgFormat, params));
    }

    @Override
    public void info(Class<?> clazz, Object message) {
        LogManager.getLogger(clazz).info(message);
    }

    @Override
    public void info(Class<?> clazz, String msgFormat, Object... params) {
        LogManager.getLogger(clazz).info(String.format(msgFormat, params));
    }

    @Override
    public void info(Class<?> clazz, Object message, Throwable t) {
        LogManager.getLogger(clazz).info(message, t);
    }

    @Override
    public void info(Class<?> clazz, Throwable t, String msgFormat, Object... params) {
        LogManager.getLogger(clazz).info(String.format(msgFormat, params), t);
    }

    @Override
    public void debug(Class<?> clazz, Object message) {
        LogManager.getLogger(clazz).debug(message);
    }

    @Override
    public void debug(Class<?> clazz, String msgFormat, Object... params) {
        LogManager.getLogger(clazz).debug(String.format(msgFormat, params));
    }

    @Override
    public void debug(Class<?> clazz, Object message, Throwable t) {
        LogManager.getLogger(clazz).debug(message, t);
    }

    @Override
    public void debug(Class<?> clazz, Throwable t, String msgFormat, Object... params) {
        LogManager.getLogger(clazz).debug(String.format(msgFormat, params), t);
    }

    @Override
    public void trace(Class<?> clazz, Object message) {
        LogManager.getLogger(clazz).trace(message);
    }

    @Override
    public void trace(Class<?> clazz, String msgFormat, Object... params) {
        LogManager.getLogger(clazz).trace(String.format(msgFormat, params));
    }

    @Override
    public void trace(Class<?> clazz, Object message, Throwable t) {
        LogManager.getLogger(clazz).trace(message, t);
    }

    @Override
    public void trace(Class<?> clazz, Throwable t, String msgFormat, Object... params) {
        LogManager.getLogger(clazz).trace(String.format(msgFormat, params), t);
    }

    @Override
    public boolean isInfoEnabled(Class<?> clazz) {
        return LogManager.getLogger(clazz).isInfoEnabled();
    }

    @Override
    public boolean isDebugEnabled(Class<?> clazz) {
        return LogManager.getLogger(clazz).isDebugEnabled();
    }

    @Override
    public boolean isTraceEnabled(Class<?> clazz) {
        return LogManager.getLogger(clazz).isTraceEnabled();
    }

}
