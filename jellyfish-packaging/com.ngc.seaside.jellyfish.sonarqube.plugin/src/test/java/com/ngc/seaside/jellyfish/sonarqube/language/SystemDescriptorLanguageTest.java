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
package com.ngc.seaside.jellyfish.sonarqube.language;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;


public class SystemDescriptorLanguageTest {

   private static final String NAME = "System Descriptor";
   private static final String KEY = "systemdescriptor";
   private static final String[] FILE_SUFFIXES = {"sd"};

   private SystemDescriptorLanguage language;

   @Before
   public void beforeTests() {
      language = new SystemDescriptorLanguage();
   }

   @Test
   public void providesCorrectFileSuffixes() {
      assertArrayEquals(language.getFileSuffixes(), FILE_SUFFIXES);
   }

   @Test
   public void definesCorrectName() {
      assertEquals(language.getName(), NAME);
   }

   @Test
   public void definesCorrectKey() {
      assertEquals(language.getKey(), KEY);
   }
}
