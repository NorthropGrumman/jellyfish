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
package com.ngc.seaside.jellyfish.impl.provider;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.CommandException;
import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.DefaultUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IParameterCollection;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.api.ParameterCategory;
import com.ngc.seaside.jellyfish.service.parameter.api.IParameterService;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;
import com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.file.Path;
import java.util.Collections;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JellyfishCommandProviderTest {

   private JellyfishCommandProvider provider;

   @Mock
   private ILogService logService;

   @Mock
   private IParameterService parameterService;

   @Mock
   private ISystemDescriptorService systemDescriptorService;

   @Mock
   private IParsingResult parsingResult;

   @Before
   public void setup() {
      provider = new JellyfishCommandProvider();
      provider.setLogService(logService);
      provider.setParameterService(parameterService);
      provider.setSystemDescriptorService(systemDescriptorService);
      provider.activate();
   }

   @Test
   public void testDoesGetUsage() {
      IUsage usage = provider.getUsage();
      assertNotNull("usage is null!",
                    usage);
      assertNotNull("description not set!",
                    usage.getDescription());
      assertTrue("missing parameter input directory!",
                 usage.getAllParameters().contains(CommonParameters.INPUT_DIRECTORY));
      assertTrue("missing parameter GAV!",
                 usage.getAllParameters().contains(CommonParameters.GROUP_ARTIFACT_VERSION));
   }

   @Test
   public void testDoesRunCommandWithValidProject() {
      IJellyFishCommand command = mockedCommand("foo-command");
      when(parameterService.parseParameters(anyList())).thenReturn(mockedParams());
      when(systemDescriptorService.parseProject(any(Path.class))).thenReturn(parsingResult);
      when(parsingResult.isSuccessful()).thenReturn(true);

      provider.addCommand(command);
      provider.run(new String[]{command.getName()});

      verify(command).run(any(IJellyFishCommandOptions.class));
   }

   @Test(expected = CommandException.class)
   public void testDoesRunNotCommandWithInvalidProject() {
      IJellyFishCommand command = mockedCommand("foo-command");
      when(parameterService.parseParameters(anyList())).thenReturn(mockedParams());
      when(systemDescriptorService.parseProject(any(Path.class))).thenReturn(parsingResult);
      when(parsingResult.isSuccessful()).thenReturn(false);

      provider.addCommand(command);
      provider.run(new String[]{command.getName()});
   }

   @Test(expected = CommandException.class)
   public void testDoesCheckRequiredParametersBeforeRunningCommand() {
      IUsage usage = new DefaultUsage("", new DefaultParameter<>("y").setParameterCategory(ParameterCategory.REQUIRED));

      IJellyFishCommand command = mockedCommand("foo-command");
      when(command.getUsage()).thenReturn(usage);
      when(parameterService.parseParameters(anyList())).thenReturn(mockedParams("x"));

      provider.addCommand(command);
      provider.run(new String[]{command.getName()});
   }

   @Test(expected = IllegalArgumentException.class)
   public void testDoesFailIfCommandNotFound() {
      when(parameterService.parseParameters(anyList())).thenReturn(mockedParams());
      provider.run(new String[]{"command-does-not-exist"});
   }

   @After
   public void after() {
      provider.removeLogService(logService);
      provider.removeParameterService(parameterService);
      provider.removeSystemDescriptorService(systemDescriptorService);
      provider.deactivate();
   }

   private static IJellyFishCommand mockedCommand(String name) {
      IJellyFishCommand command = mock(IJellyFishCommand.class);
      when(command.getName()).thenReturn(name);
      when(command.getUsage()).thenReturn(new DefaultUsage("", Collections.emptyList()));
      return command;
   }

   private static IParameterCollection mockedParams(String... paramNames) {
      DefaultParameterCollection collection = new DefaultParameterCollection();
      for (String name : paramNames) {
         collection.addParameter(new DefaultParameter<>(name, name));
      }
      return collection;
   }
}
