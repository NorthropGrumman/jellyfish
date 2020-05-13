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
package com.ngc.seaside.systemdescriptor.service.impl.gherkin.model;

import com.google.common.base.Preconditions;

import gherkin.ast.Node;

import java.nio.file.Path;

/**
 * Base class for wrapping Gherkin {@code Node}s.
 *
 * @param <T> the type of Gherkin node being wrapped
 */
public abstract class AbstractWrappedGherkin<T extends Node> implements IGherkinUnwrappable<T> {

   /**
    * The wrapped object.
    */
   protected final T wrapped;

   /**
    * The path to the wrapped object.
    */
   protected final Path path;

   /**
    * Creates a new wrapped Gherkin node.
    *
    * @param wrapped the object to wrap
    * @param path    the path to the source of the wrapped object
    */
   protected AbstractWrappedGherkin(T wrapped, Path path) {
      this.wrapped = Preconditions.checkNotNull(wrapped, "wrapped may not be null!");
      this.path = Preconditions.checkNotNull(path, "path may not be null!");
   }

   @Override
   public T unwrap() {
      return wrapped;
   }

   @Override
   public int getLineNumber() {
      return wrapped.getLocation() != null ? wrapped.getLocation().getLine() : -1;
   }

   @Override
   public int getColumn() {
      return wrapped.getLocation() != null ? wrapped.getLocation().getColumn() : -1;
   }

   @Override
   public Path getPath() {
      return path;
   }
}
