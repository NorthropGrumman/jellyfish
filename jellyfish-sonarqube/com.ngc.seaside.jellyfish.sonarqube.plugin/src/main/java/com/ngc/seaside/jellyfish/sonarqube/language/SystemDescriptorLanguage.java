package com.ngc.seaside.jellyfish.sonarqube.language;

import org.sonar.api.resources.AbstractLanguage;

/**
 * Makes Sonarqube aware of the System Descriptor language.  Sources files of the System Descriptor language use the
 * {@code .sd} file extension.
 */
public final class SystemDescriptorLanguage extends AbstractLanguage {

   /**
    * A user friendly name of the System Descriptor language.
    */
   public static final String NAME = "System Descriptor";

   /**
    * The unique key of the System Descriptor language.
    */
   public static final String KEY = "systemdescriptor";

   private static final String[] FILE_SUFFIXES = {"sd"};

   public SystemDescriptorLanguage() {
      super(KEY, NAME);
   }

   @Override
   public String[] getFileSuffixes() {
      return FILE_SUFFIXES;
   }
}
