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
package com.ngc.seaside.systemdescriptor.service.log.api;

public interface ILogService {

    void error(Class<?> clazz, Object message);

    void error(Class<?> clazz, String msgFormat, Object... params);

    void error(Class<?> clazz, Object message, Throwable t);

    void error(Class<?> clazz, Throwable t, String msgFormat, Object... params);

    void warn(Class<?> clazz, Object message);

    void warn(Class<?> clazz, Throwable t, String msgFormat, Object... params);

    void warn(Class<?> clazz, Object message, Throwable t);

    void warn(Class<?> clazz, String msgFormat, Object... params);

    void info(Class<?> clazz, Object message);

    void info(Class<?> clazz, String msgFormat, Object... params);

    void info(Class<?> clazz, Object message, Throwable t);

    void info(Class<?> clazz, Throwable t, String msgFormat, Object... params);

    void debug(Class<?> clazz, Object message);

    void debug(Class<?> clazz, String msgFormat, Object... params);

    void debug(Class<?> clazz, Object message, Throwable t);

    void debug(Class<?> clazz, Throwable t, String msgFormat, Object... params);

    void trace(Class<?> clazz, Object message);

    void trace(Class<?> clazz, String msgFormat, Object... params);

    void trace(Class<?> clazz, Object message, Throwable t);

    void trace(Class<?> clazz, Throwable t, String msgFormat, Object... params);

    boolean isInfoEnabled(Class<?> clazz);

    boolean isDebugEnabled(Class<?> clazz);

    boolean isTraceEnabled(Class<?> clazz);
}
