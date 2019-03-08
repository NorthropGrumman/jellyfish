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
