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
