/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
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
package com.ngc.seaside.jellyfish.sonarqube.language;

import org.sonar.api.resources.AbstractLanguage;

/**
 * Makes Sonarqube aware of the System Descriptor language.  Sources files of the System Descriptor language use the
 * {@code .sd} file extension.
 */
public final class SystemDescriptorLanguage extends AbstractLanguage {

   /**
    * A user friendly name of the System Descriptor language.  Do <i>not</i> reference this value when associating
    * components with the SD language via the Sonarqube API.  Use {@link #KEY} for that.
    */
   public static final String NAME = "System Descriptor";

   /**
    * The unique key of the System Descriptor language.  Code that registers components that are associated with the
    * SD language should use this value and <i>not</i> {@link #NAME}.
    */
   public static final String KEY = "systemdescriptor";

   private static final String[] FILE_SUFFIXES = {"sd"};

   /**
    * Creates a System Descriptor language.
    */
   public SystemDescriptorLanguage() {
      super(KEY, NAME);
   }

   @Override
   public String[] getFileSuffixes() {
      return FILE_SUFFIXES;
   }
}
