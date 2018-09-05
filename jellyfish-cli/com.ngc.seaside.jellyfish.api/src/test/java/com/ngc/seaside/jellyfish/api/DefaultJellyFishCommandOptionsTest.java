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
package com.ngc.seaside.jellyfish.api;

import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultJellyFishCommandOptionsTest {

   private DefaultParameterCollection parameters;

   private DefaultJellyFishCommandOptions options;

   @Mock
   private IParsingResult parsingResult;

   @Mock
   private ISystemDescriptor systemDescriptor;

   @Before
   public void setup() throws Throwable {
      when(parsingResult.getSystemDescriptor()).thenReturn(systemDescriptor);

      parameters = new DefaultParameterCollection();
      parameters.addParameter(new DefaultParameter<>("param1", "foo"));

      options = new DefaultJellyFishCommandOptions();
      options.setParsingResult(parsingResult);
      options.setParameters(parameters);
   }

   @Test
   public void testDoesMergeOptions() throws Throwable {
      IJellyFishCommandOptions merged = DefaultJellyFishCommandOptions.mergeWith(
            options,
            new DefaultParameter<>("param2", "bar"));
      assertEquals("system descriptor not set!",
                   systemDescriptor,
                   merged.getSystemDescriptor());
      assertEquals("parsing result not set!",
                   parsingResult,
                   merged.getParsingResult());
      assertEquals("did not include existing param!",
                   "foo",
                   merged.getParameters().getParameter("param1").getValue());
      assertEquals("did not add param!",
                   "bar",
                   merged.getParameters().getParameter("param2").getValue());
   }

   @Test
   public void testDoesMergeAndOverrideOptions() throws Throwable {
      IJellyFishCommandOptions merged = DefaultJellyFishCommandOptions.mergeWith(
            options,
            new DefaultParameter<>("param1", "bar"));
      assertEquals("did not replace existing param!",
                   "bar",
                   merged.getParameters().getParameter("param1").getValue());
   }
}
