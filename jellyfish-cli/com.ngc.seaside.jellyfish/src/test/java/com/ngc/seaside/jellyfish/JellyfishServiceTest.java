package com.ngc.seaside.jellyfish;

import com.google.inject.AbstractModule;

import com.ngc.seaside.jellyfish.JellyfishService;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandProvider;
import com.ngc.seaside.jellyfish.service.execution.api.IJellyfishExecution;
import com.ngc.seaside.jellyfish.service.execution.api.JellyfishExecutionException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalMatchers.aryEq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JellyfishServiceTest {

   private JellyfishService service;

   @Mock
   private IJellyFishCommandProvider provider;

   @Mock
   private IJellyFishCommandOptions commandOptions;

   @Before
   public void setup() {
      service = new JellyfishService();
   }

   @Test
   public void testDoesRunJellyfishWithCustomModules() {
      String command = "foo";
      Collection<String> args = Arrays.asList("a=b", "c=d");
      when(provider.run(aryEq(new String[]{command, "a=b", "c=d"}))).thenReturn(commandOptions);

      IJellyfishExecution result = service.run(command, args, Collections.singleton(new MockRegisteringModule()));
      assertEquals("result not correct!",
                   commandOptions,
                   result.getOptions());
   }

   @Test
   public void testDoesRunJellyfishWithArgsAsMap() {
      String command = "foo";
      Map<String, String> args = new LinkedHashMap<>();
      args.put("a", "b");
      args.put("c", "d");
      when(provider.run(aryEq(new String[]{command, "a=b", "c=d"}))).thenReturn(commandOptions);

      IJellyfishExecution result = service.run(command, args, Collections.singleton(new MockRegisteringModule()));
      assertEquals("result not correct!",
                   commandOptions,
                   result.getOptions());
   }

   @Test(expected = JellyfishExecutionException.class)
   public void testDoesWrapExceptions() {
      String command = "foo";
      Collection<String> args = Arrays.asList("a=b", "c=d");
      when(provider.run(aryEq(new String[]{command, "a=b", "c=d"})))
            .thenThrow(new RuntimeException("testing error handling"));

      service.run(command, args, Collections.singleton(new MockRegisteringModule()));
   }

   private class MockRegisteringModule extends AbstractModule {

      @Override
      protected void configure() {
         bind(IJellyFishCommandProvider.class).toInstance(provider);
      }
   }
}
