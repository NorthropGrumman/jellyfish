package com.ngc.seaside.jellyfish.cli.command.help;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultParameterCollection;
import com.ngc.seaside.command.api.DefaultUsage;
import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class HelpCommandTests {

   private HelpCommand cmd;

   private ILogService logger = Mockito.mock(ILogService.class);

   private IUsage mockUsage1 = new DefaultUsage("Usage 1", new DefaultParameter("param1", "Description 1.1", true), new DefaultParameter("param2", "Description 2.1", true),
      new DefaultParameter("param3", "Description 3.1", false), new DefaultParameter("param4", "Description 4.1", false));

   private IUsage mockUsage2 = new DefaultUsage("Usage 2 ja;skldg jasd g;kjsgd ;jasd gklj",
      new DefaultParameter("param1asdgasdgawe", "Description 1.2 asdjf iosdj f;klsjad f;jklasd f;klasjg a;weijaweio gj", true),
      new DefaultParameter("param2", "Description 2.2 asdgas;kl gjs;kldj g;klsdgj ;klsdjg ;asdgj aiweg;jaw g;klwej ;ljkwekl jagwklj weagjk awekl jawe", true),
      new DefaultParameter("param3", "Description 3.2 ajga;s kdj;kljsd ga;klsjdg ;klj sg", false), new DefaultParameter("param4", false));

   private PrintStream out;

   @Before
   public void before() {
      cmd = new HelpCommand();
      cmd.setLogService(logger);
      cmd.activate();
      cmd.addCommand(cmd);

      IJellyFishCommand mock1 = Mockito.mock(IJellyFishCommand.class);
      Mockito.when(mock1.getName()).thenReturn("Command1");
      Mockito.when(mock1.getUsage()).thenReturn(mockUsage1);

      IJellyFishCommand mock2 = Mockito.mock(IJellyFishCommand.class);
      Mockito.when(mock2.getName()).thenReturn("sijadgasdggasdg");
      Mockito.when(mock2.getUsage()).thenReturn(mockUsage2);

      cmd.addCommand(mock1);
      cmd.addCommand(mock2);

      out = System.out;
   }

   @Test
   public void testBasicRun() {
      ByteArrayOutputStream stream = new ByteArrayOutputStream();
      System.setOut(new PrintStream(stream));

      IJellyFishCommandOptions options = Mockito.mock(IJellyFishCommandOptions.class);
      DefaultParameterCollection parameters = new DefaultParameterCollection();
      Mockito.when(options.getParameters()).thenReturn(parameters);

      cmd.run(options);

      String output = stream.toString();

      Assert.assertTrue(output.contains("help"));
      Assert.assertTrue(output.contains("Command1"));
      Assert.assertTrue(output.contains("sijadgasdggasdg"));
      Assert.assertTrue(output.contains("Usage 1"));
      Assert.assertTrue(output.contains("Usage 2"));

      Assert.assertFalse(output.contains("param1"));
      Assert.assertFalse(output.contains("param2"));
      Assert.assertFalse(output.contains("param3"));
      Assert.assertFalse(output.contains("param4"));
      Assert.assertFalse(output.contains("Description 1"));
      Assert.assertFalse(output.contains("Description 2"));
      Assert.assertFalse(output.contains("Description 3"));
      Assert.assertFalse(output.contains("Description 4"));
   }

   @Test
   public void testVerboseRun() {
      ByteArrayOutputStream stream = new ByteArrayOutputStream();
      System.setOut(new PrintStream(stream));

      IJellyFishCommandOptions options = Mockito.mock(IJellyFishCommandOptions.class);
      DefaultParameterCollection parameters = new DefaultParameterCollection();
      DefaultParameter parameter = new DefaultParameter("verbose", false);
      parameter.setValue("true");
      parameters.addParameter(parameter);
      Mockito.when(options.getParameters()).thenReturn(parameters);

      cmd.run(options);

      String output = stream.toString();

      Assert.assertTrue(output.contains("help"));
      Assert.assertTrue(output.contains("Command1"));
      Assert.assertTrue(output.contains("sijadgasdggasdg"));
      Assert.assertTrue(output.contains("Usage 1"));
      Assert.assertTrue(output.contains("Usage 2"));

      Assert.assertTrue(output.contains("param1"));
      Assert.assertTrue(output.contains("param2"));
      Assert.assertTrue(output.contains("param3"));
      Assert.assertTrue(output.contains("param4"));
      Assert.assertTrue(output.contains("Description 1"));
      Assert.assertTrue(output.contains("Description 2"));
      Assert.assertTrue(output.contains("Description 3"));
      Assert.assertTrue(output.contains("Description 4"));
   }

   @Test
   public void testCommandRun() {
      ByteArrayOutputStream stream = new ByteArrayOutputStream();
      System.setOut(new PrintStream(stream));

      IJellyFishCommandOptions options = Mockito.mock(IJellyFishCommandOptions.class);
      DefaultParameterCollection parameters = new DefaultParameterCollection();
      DefaultParameter parameter = new DefaultParameter("command", false);
      parameter.setValue("Command1");
      parameters.addParameter(parameter);
      Mockito.when(options.getParameters()).thenReturn(parameters);

      cmd.run(options);

      String output = stream.toString();

      Assert.assertFalse(output.contains("help"));
      Assert.assertTrue(output.contains("Command1"));
      Assert.assertFalse(output.contains("sijadgasdggasdg"));
      Assert.assertTrue(output.contains("Usage 1"));
      Assert.assertFalse(output.contains("Usage 2"));

      Assert.assertTrue(output.contains("param1"));
      Assert.assertTrue(output.contains("param2"));
      Assert.assertTrue(output.contains("param3"));
      Assert.assertTrue(output.contains("param4"));
      Assert.assertTrue(output.contains("Description 1.1"));
      Assert.assertTrue(output.contains("Description 2.1"));
      Assert.assertTrue(output.contains("Description 3.1"));
      Assert.assertTrue(output.contains("Description 4.1"));
      Assert.assertFalse(output.contains("Description 1.2"));
      Assert.assertFalse(output.contains("Description 2.2"));
      Assert.assertFalse(output.contains("Description 3.2"));
   }

   @After
   public void after() {
      cmd.deactivate();
      System.setOut(out);
   }

}
