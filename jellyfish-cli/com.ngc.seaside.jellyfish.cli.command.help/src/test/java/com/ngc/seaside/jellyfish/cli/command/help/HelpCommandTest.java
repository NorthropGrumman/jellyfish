package com.ngc.seaside.jellyfish.cli.command.help;

import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;
import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.DefaultUsage;
import com.ngc.seaside.jellyfish.api.ICommandOptions;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandProvider;
import com.ngc.seaside.jellyfish.api.IUsage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HelpCommandTest {

   private static final IUsage USAGE_1 = new DefaultUsage(
         "Usage 1",
         new DefaultParameter<>("param1_1")
               .setDescription("Description 1_1")
               .setRequired(true),
         new DefaultParameter<>("param1_2")
               .setDescription("Description 1_2")
               .setRequired(false));

   private static final IUsage USAGE_2 = new DefaultUsage(
         "Usage 2",
         new DefaultParameter<>("param2_1")
               .setDescription("Description 2_1")
               .setRequired(true),
         new DefaultParameter<>("param2_2")
               .setDescription("Description 2_2")
               .setRequired(false));

   private HelpCommand cmd;

   @Mock
   private ICommandOptions options;
   
   @Mock(answer = Answers.RETURNS_DEEP_STUBS)
   private IJellyFishCommandProvider jellyfishProvider;

   private DefaultParameterCollection parameters = new DefaultParameterCollection();

   private ByteArrayOutputStream stream = new ByteArrayOutputStream();

   @Before
   public void before() {
      when(jellyfishProvider.getUsage().getAllParameters())
         .thenReturn(new DefaultParameterCollection().getAllParameters());

      cmd = new HelpCommand();
      cmd.setLogService(new PrintStreamLogService(new PrintStream(stream)));
      cmd.addCommand(cmd);
      cmd.setJellyfishProvider(jellyfishProvider);
      cmd.activate();

      IJellyFishCommand mock1 = mock(IJellyFishCommand.class);
      when(mock1.getName()).thenReturn("Command1");
      when(mock1.getUsage()).thenReturn(USAGE_1);

      IJellyFishCommand mock2 = mock(IJellyFishCommand.class);
      when(mock2.getName()).thenReturn("Command2");
      when(mock2.getUsage()).thenReturn(USAGE_2);

      cmd.addCommand(mock1);
      cmd.addCommand(mock2);

      when(options.getParameters()).thenReturn(parameters);
   }

   @Test
   public void testBasicRun() {
      cmd.run(options);

      String output = stream.toString();

      assertTrue(output.contains("help"));
      assertTrue(output.contains("Command1"));
      assertTrue(output.contains("Command2"));
      assertTrue(output.contains("Usage 1"));
      assertTrue(output.contains("Usage 2"));

      assertFalse(output.contains("param"));
      assertFalse(output.contains("Description"));
   }

   @Test
   public void testVerboseRun() {
      parameters.addParameter(new DefaultParameter<>("verbose", "true"));

      cmd.run(options);

      String output = stream.toString();

      assertTrue(output.contains("help"));
      assertTrue(output.contains("Command1"));
      assertTrue(output.contains("Command2"));
      assertTrue(output.contains("Usage 1"));
      assertTrue(output.contains("Usage 2"));

      assertTrue(output.contains("param1_1"));
      assertTrue(output.contains("param1_2"));
      assertTrue(output.contains("param2_1"));
      assertTrue(output.contains("param2_2"));
      assertTrue(output.contains("Description 1_1"));
      assertTrue(output.contains("Description 1_2"));
      assertTrue(output.contains("Description 2_1"));
      assertTrue(output.contains("Description 2_2"));
   }

   @Test
   public void testCommandRun() {
      parameters.addParameter(new DefaultParameter<>("command", "Command1"));

      cmd.run(options);

      String output = stream.toString();

      assertTrue(output.contains("Command1"));
      assertFalse(output.contains("Command2"));
      assertTrue(output.contains("Usage 1"));
      assertFalse(output.contains("Usage 2"));

      assertTrue(output.contains("param1_1"));
      assertTrue(output.contains("param1_2"));
      assertTrue(output.contains("Description 1_1"));
      assertTrue(output.contains("Description 1_2"));
      assertFalse(output.contains("Description 2_1"));
      assertFalse(output.contains("Description 2_2"));
   }

   @After
   public void after() {
      cmd.deactivate();
   }

}
