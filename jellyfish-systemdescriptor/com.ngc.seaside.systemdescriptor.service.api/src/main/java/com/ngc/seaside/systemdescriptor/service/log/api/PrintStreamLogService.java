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

import java.io.PrintStream;

public class PrintStreamLogService implements ILogService {

    private final PrintStream stream;

    /**
     * Creates a new {@code PrintStreamLogService} that prints to
     * {@code System.out}.
     */
    public PrintStreamLogService() {
        this(System.out);
    }

    /**
     * Creates a new {@code PrintStreamLogService} that prints to the given
     * {@code PrintStream}.
     */
    public PrintStreamLogService(PrintStream stream) {
        this.stream = stream;
    }

    @Override
    public void error(Class<?> clazz, Object message) {
        print(clazz, message);
    }

    @Override
    public void error(Class<?> clazz, String msgFormat, Object... params) {
        print(clazz, String.format(msgFormat, params));
    }

    @Override
    public void error(Class<?> clazz, Object message, Throwable t) {
        print(clazz, message);
        print(t);
    }

    @Override
    public void error(Class<?> clazz, Throwable t, String msgFormat, Object... params) {
        error(clazz, String.format(msgFormat, params), t);
    }

    @Override
    public void warn(Class<?> clazz, Object message) {
        print(clazz, message);
    }

    @Override
    public void warn(Class<?> clazz, Throwable t, String msgFormat, Object... params) {
        warn(clazz, String.format(msgFormat, params), t);
    }

    @Override
    public void warn(Class<?> clazz, Object message, Throwable t) {
        print(clazz, message);
        print(t);
    }

    @Override
    public void warn(Class<?> clazz, String msgFormat, Object... params) {
        print(clazz, String.format(msgFormat, params));
    }

    @Override
    public void info(Class<?> clazz, Object message) {
        print(clazz, message);
    }

    @Override
    public void info(Class<?> clazz, String msgFormat, Object... params) {
        print(clazz, String.format(msgFormat, params));
    }

    @Override
    public void info(Class<?> clazz, Object message, Throwable t) {
        print(clazz, message);
        print(t);
    }

    @Override
    public void info(Class<?> clazz, Throwable t, String msgFormat, Object... params) {
        info(clazz, String.format(msgFormat, params), t);
    }

    @Override
    public void debug(Class<?> clazz, Object message) {
        print(clazz, message);
    }

    @Override
    public void debug(Class<?> clazz, String msgFormat, Object... params) {
        print(clazz, String.format(msgFormat, params));
    }

    @Override
    public void debug(Class<?> clazz, Object message, Throwable t) {
        print(clazz, message);
        print(t);
    }

    @Override
    public void debug(Class<?> clazz, Throwable t, String msgFormat, Object... params) {
        debug(clazz, String.format(msgFormat, params), t);
    }

    @Override
    public void trace(Class<?> clazz, Object message) {
        print(clazz, message);
    }

    @Override
    public void trace(Class<?> clazz, String msgFormat, Object... params) {
        print(clazz, String.format(msgFormat, params));
    }

    @Override
    public void trace(Class<?> clazz, Object message, Throwable t) {
        print(clazz, message);
        print(t);
    }

    @Override
    public void trace(Class<?> clazz, Throwable t, String msgFormat, Object... params) {
        trace(clazz, String.format(msgFormat, params), t);
    }

    @Override
    public boolean isInfoEnabled(Class<?> clazz) {
        return true;
    }

    @Override
    public boolean isDebugEnabled(Class<?> clazz) {
        return true;
    }

    @Override
    public boolean isTraceEnabled(Class<?> clazz) {
        return true;
    }

    protected void print(Class<?> clazz, Object message) {
        stream.println(clazz.getName() + ": " + message);
    }

    protected void print(Throwable t) {
        t.printStackTrace(stream);
    }
}
