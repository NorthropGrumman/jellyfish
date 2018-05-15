package com.ngc.seaside.jellyfish;

import com.google.inject.AbstractModule;

import com.ngc.seaside.jellyfish.api.ICommand;
import com.ngc.seaside.jellyfish.api.ICommandOptions;
import com.ngc.seaside.jellyfish.api.ICommandProvider;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandProvider;
import com.ngc.seaside.jellyfish.service.execution.api.IJellyfishExecution;
import com.ngc.seaside.jellyfish.service.execution.api.JellyfishExecutionException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.nio.file.Paths;
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

   @Mock
   private IJellyFishCommand mockedJfCommand;

   @Mock
   private ICommandProvider<ICommandOptions, ICommand<ICommandOptions>, ICommandOptions> defaultProvider;

   @Mock
   private ICommandOptions defaultCommandOptions;

   @Before
   public void setup() {
      service = new JellyfishService();
   }

   @Test
   public void testDoesRunJellyfishWithCustomModules() {
      String command = "foo";
      String expectedHomeProperty = Paths.get(System.getProperty("user.dir")).toAbsolutePath().toString();
      Collection<String> args = Arrays.asList("a=b", "c=d");
      when(provider.getCommand(command)).thenReturn(mockedJfCommand);
      when(provider.run(aryEq(new String[]{command, "a=b", "c=d"})))
            .thenAnswer(new SystemPropertyAssertingAnswer<>(commandOptions,
                                                            JellyfishService.BLOCS_HOME_SYSTEM_PROPERTY,
                                                            expectedHomeProperty));

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
      when(provider.getCommand(command)).thenReturn(mockedJfCommand);
      when(provider.run(aryEq(new String[]{command, "a=b", "c=d"}))).thenReturn(commandOptions);

      IJellyfishExecution result = service.run(command, args, Collections.singleton(new MockRegisteringModule()));
      assertEquals("result not correct!",
                   commandOptions,
                   result.getOptions());
   }

   @Test
   public void testDoesRunDefaultCommand() {
      String command = "foo";
      String expectedHomeProperty = Paths.get(System.getProperty("user.dir")).toAbsolutePath().toString();
      Collection<String> args = Arrays.asList("a=b", "c=d");
      when(provider.getCommand(command)).thenReturn(null);
      when(defaultProvider.run(aryEq(new String[]{command, "a=b", "c=d"})))
            .thenAnswer(new SystemPropertyAssertingAnswer<>(defaultCommandOptions,
                                                            JellyfishService.BLOCS_HOME_SYSTEM_PROPERTY,
                                                            expectedHomeProperty));

      IJellyfishExecution result = service.run(command, args, Collections.singleton(new MockRegisteringModule()));
      assertEquals("result not correct!",
                   defaultCommandOptions.getParameters(),
                   result.getOptions().getParameters());
   }

   @Test
   public void testDoesNotOverwriteAlreadySetBlocsHomeProperty() {
      String command = "foo";
      String expectedHomeProperty = "blah/blah/blah";
      Collection<String> args = Arrays.asList("a=b", "c=d");
      when(provider.getCommand(command)).thenReturn(mockedJfCommand);
      when(provider.run(aryEq(new String[]{command, "a=b", "c=d"})))
            .thenAnswer(new SystemPropertyAssertingAnswer<>(commandOptions,
                                                            JellyfishService.BLOCS_HOME_SYSTEM_PROPERTY,
                                                            expectedHomeProperty));

      System.setProperty(JellyfishService.BLOCS_HOME_SYSTEM_PROPERTY, expectedHomeProperty);
      IJellyfishExecution result = service.run(command, args, Collections.singleton(new MockRegisteringModule()));
      assertEquals("result not correct!",
                   commandOptions,
                   result.getOptions());
      assertEquals("did not leave system property alone!",
                   expectedHomeProperty,
                   System.getProperty(JellyfishService.BLOCS_HOME_SYSTEM_PROPERTY));
   }

   @Test(expected = JellyfishExecutionException.class)
   public void testDoesWrapExceptions() {
      String command = "foo";
      Collection<String> args = Arrays.asList("a=b", "c=d");
      when(provider.getCommand(command)).thenReturn(mockedJfCommand);
      when(provider.run(aryEq(new String[]{command, "a=b", "c=d"})))
            .thenThrow(new RuntimeException("testing error handling"));

      service.run(command, args, Collections.singleton(new MockRegisteringModule()));
   }

   @After
   public void after() {
      System.clearProperty(JellyfishService.BLOCS_HOME_SYSTEM_PROPERTY);
   }

   private class MockRegisteringModule extends AbstractModule {

      @Override
      protected void configure() {
         bind(IJellyFishCommandProvider.class).toInstance(provider);
         bind(ICommandProvider.class).toInstance(defaultProvider);
      }
   }

   private static class SystemPropertyAssertingAnswer<T> implements Answer<T> {

      private final T returnValue;
      private final String systemPropertyName;
      private final String systemPropertyValue;

      SystemPropertyAssertingAnswer(T returnValue, String systemPropertyName, String systemPropertyValue) {
         this.returnValue = returnValue;
         this.systemPropertyName = systemPropertyName;
         this.systemPropertyValue = systemPropertyValue;
      }

      @Override
      public T answer(InvocationOnMock invocation) {
         assertEquals("system property " + systemPropertyName + " not correct!",
                      systemPropertyValue,
                      System.getProperty(systemPropertyName));
         return returnValue;
      }
   }
}
