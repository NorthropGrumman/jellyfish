package com.ngc.seaside.systemdescriptor.service.impl.xtext;

import com.ngc.seaside.systemdescriptor.model.api.traversal.Traversals;
import com.ngc.seaside.systemdescriptor.service.api.IParsingIssue;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;

import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class XTextSystemDescriptorServiceIT {

   private XTextSystemDescriptorService service;

   @Before
   public void setup() throws Throwable {
      service = new XTextSystemDescriptorService();
   }

   @Test
   public void testDoesParseSingleFile() throws Throwable {
      Path time = pathTo("valid-project", "clocks", "datatypes", "Time.sd");
      IParsingResult result = service.parseFiles(Collections.singletonList(time));
      assertTrue("parsing should be successful!",
                 result.isSuccessful());
      assertNotNull("system descriptor not set!",
                    result.getSystemDescriptor());
   }

   @Test
   public void testDoesParseMultipleFiles() throws Throwable {
      Collection<Path> paths = Arrays.asList(
            pathTo("valid-project", "clocks", "datatypes", "Time.sd"),
            pathTo("valid-project", "clocks", "models", "Alarm.sd"),
            pathTo("valid-project", "clocks", "models", "ClockDisplay.sd"),
            pathTo("valid-project", "clocks", "models", "Speaker.sd"),
            pathTo("valid-project", "clocks", "models", "Timer.sd"),
            pathTo("valid-project", "clocks", "AlarmClock.sd"));
      IParsingResult result = service.parseFiles(paths);
      assertTrue("parsing should be successful!",
                 result.isSuccessful());
      assertNotNull("system descriptor not set!",
                    result.getSystemDescriptor());
      result.getSystemDescriptor().traverse(Traversals.SYSTEM_OUT_PRINTING_VISITOR);
   }

   @Test
   public void testDoesParseProjectDirectory() throws Throwable {
      IParsingResult result = service.parseProject(Paths.get("build", "resources", "test", "valid-project"));
      assertTrue("parsing should be successful!",
                 result.isSuccessful());
      assertNotNull("system descriptor not set!",
                    result.getSystemDescriptor());
   }

   @Test
   public void testDoesReturnParsingErrors() throws Throwable {
      Path time = pathTo("invalid-project", "clocks", "datatypes", "Time.sd");

      IParsingResult result = service.parseFiles(Collections.singletonList(time));
      assertFalse("parsing should not be successful!",
                  result.isSuccessful());

      IParsingIssue issue = result.getIssues().iterator().next();
      assertEquals("wrong line number!",
                   9,
                   issue.getLineNumber());
      assertEquals("wrong column number!",
                   3,
                   issue.getColumn());
      assertEquals("wrong message!",
                   "mismatched input 'MISSING_TYPE' expecting '}'",
                   issue.getMessage());
      assertEquals("file not correct!",
                   time.toAbsolutePath(),
                   issue.getOffendingFile());
   }

   private static Path pathTo(String project, String... packagesAndFile) {
      Collection<String> parts = new ArrayList<>();
      parts.add("resources");
      parts.add("test");
      parts.add(project);
      parts.add("src");
      parts.add("main");
      parts.add("sd");
      parts.addAll(Arrays.asList(packagesAndFile));
      return Paths.get("build", parts.toArray(new String[parts.size()]));
   }
}
