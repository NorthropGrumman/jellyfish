package com.ngc.seaside.bootstrap;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Tests for the CommandLine class.
 */
public class CommandLineTests {
   private static PrintStream SYSTEM_OUT;
   private static InputStream SYSTEM_IN;

   @BeforeClass
   public static void setupClass() {
//      SYSTEM_OUT = System.out;
//      SYSTEM_IN = System.in;
   }

//   @Test
//   public void testCommandLine() throws IOException {
//      System.setOut(new PrintStream(new ByteArrayOutputStream()));
//      try {
//         CommandLine.parseArgs("--help");
//         Assert.fail();
//      } catch (ExitException e) {
//         Assert.assertFalse(e.failed());
//      }
//
//      try {
//         CommandLine.parseArgs("test.zip", "-o", "output");
//         Assert.fail();
//      } catch (ExitException e) {
//         Assert.assertTrue(e.failed());
//      }
//
//      Path p = Files.createTempFile(null, "test.zip");
//      Path o = Files.createTempDirectory(null);
//      p.toFile().deleteOnExit();
//      o.toFile().deleteOnExit();
//
//      CommandLine cl = CommandLine.parseArgs(p.toAbsolutePath().toString(), "-o", o.toAbsolutePath().toString());
//      Assert.assertFalse(cl.isClean());
//      Assert.assertEquals(p, cl.getTemplateFile());
//      Assert.assertEquals(o, cl.getOutputFolder());
//   }

//   @Test
//   public void testDoesDefaultToResourcesConfigForTemplates() throws Throwable {
//      System.setOut(new PrintStream(new ByteArrayOutputStream()));
//
//      Path tempDir = Files.createTempDirectory(null);
//      Path templateDir = tempDir.resolve(Paths.get("resources", "config", "test"));
//      Path templateFile = templateDir.resolve("TemplateTest.zip");
//      Files.createDirectories(templateDir);
//      Files.createFile(templateFile);
//      Path o = Files.createTempDirectory(null);
//
//      tempDir.toFile().deleteOnExit();
//      o.toFile().deleteOnExit();
//
//      System.setProperty(CommandLine.APP_HOME_SYS_PROPERTY, tempDir.toFile().getAbsolutePath());
//
//      CommandLine cl = CommandLine.parseArgs("test", "-o", o.toAbsolutePath().toString());
//      Assert.assertEquals("did not search default resources for template file!",
//                          templateFile,
//                          cl.getTemplateFile());
//   }

//   @Test
//   public void testQuery() throws Exception {
//      final String STR1 = "asdf isdf";
//      final String STR2 = "aisdgoogji;aiasjga;lklkejag;lkasdgjsa;lksejasgioaw;jeg;akdj jwiq;j  asdfij ;owaef";
//      final String STR3 = "£asdf├ƒ|`~[]\\\"\'ô╣☻╚Ä\\n\\\\";
//      final String STR4 = "";
//      final String STR5 = "asdf";
//      final String STR6 = "ASDF";
//
//      final String input = STR1 + "\n" + STR2 + "\r" + STR3 + "\r\n" + STR4 + "\n" + STR5 + "\n" + STR6 + "\n";
//      InputStream in = new ByteArrayInputStream(input.getBytes());
//      System.setIn(in);
//      System.setOut(new PrintStream(new ByteArrayOutputStream()));
//      Field f = CommandLine.class.getDeclaredField("sc");
//      f.setAccessible(true);
//      f.set(null, null);
//
//      Assert.assertEquals(STR1, CommandLine.queryUser("Test1", "default1", null));
//      Assert.assertEquals(STR2, CommandLine.queryUser("Test2", "default2", null));
//      Assert.assertEquals(STR3, CommandLine.queryUser("Test3", "default3", null));
//      Assert.assertEquals("default4", CommandLine.queryUser("Test4", "default4", null));
//      Assert.assertEquals(STR6, CommandLine.queryUser("Test5", "default5", i -> Character.isUpperCase(i.charAt(0))));
//   }

//   @AfterClass
//   public static void cleanUpClass() {
//      System.setOut(SYSTEM_OUT);
//      System.setIn(SYSTEM_IN);
//      System.clearProperty(CommandLine.APP_HOME_SYS_PROPERTY);
//   }

}
