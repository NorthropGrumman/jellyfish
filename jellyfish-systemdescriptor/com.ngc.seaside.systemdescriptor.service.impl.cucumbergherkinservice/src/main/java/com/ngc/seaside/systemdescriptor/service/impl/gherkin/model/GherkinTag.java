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

import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IGherkinTag;

import gherkin.ast.Tag;

import java.nio.file.Path;

/**
 * Wraps a Gherkin tag.
 */
public class GherkinTag extends AbstractWrappedGherkin<Tag> implements IGherkinTag {

   private final String name;

   /**
    * Creates a new tag.
    *
    * @param wrapped the tag to wrap
    * @param path    the source file that contains the tag
    */
   public GherkinTag(Tag wrapped, Path path) {
      super(wrapped, path);
      // Skip the leading '@' character.
      this.name = wrapped.getName().substring(1);
   }

   @Override
   public String getName() {
      return name;
   }
}
