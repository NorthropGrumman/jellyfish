package com.ngc.seaside.jellyfish.api;

import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultParameterCollection;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class DefaultJellyFishCommandOptionsTest {

   private DefaultParameterCollection parameters;

   private DefaultJellyFishCommandOptions options;

   @Mock
   private ISystemDescriptor systemDescriptor;

   @Before
   public void setup() throws Throwable {
      parameters = new DefaultParameterCollection();
      parameters.addParameter(new DefaultParameter("param1").setValue("foo"));

      options = new DefaultJellyFishCommandOptions();
      options.setSystemDescriptor(systemDescriptor);
      options.setParameters(parameters);
   }

   @Test
   public void testDoesMergeOptions() throws Throwable {
      IJellyFishCommandOptions merged = DefaultJellyFishCommandOptions.mergeWith(
            options,
            new DefaultParameter("param2").setValue("bar"));
      assertEquals("system descriptor not set!",
                   systemDescriptor,
                   merged.getSystemDescriptor());
      assertEquals("did not include existing param",
                   "foo",
                   merged.getParameters().getParameter("param1").getValue());
      assertEquals("did not add param",
                   "bar",
                   merged.getParameters().getParameter("param2").getValue());
   }

   @Test
   public void testDoesMergeAndOverrideOptions() throws Throwable {
      IJellyFishCommandOptions merged = DefaultJellyFishCommandOptions.mergeWith(
            options,
            new DefaultParameter("param1").setValue("bar"));
      assertEquals("did not replace existing param",
                   "bar",
                   merged.getParameters().getParameter("param1").getValue());
   }
}
