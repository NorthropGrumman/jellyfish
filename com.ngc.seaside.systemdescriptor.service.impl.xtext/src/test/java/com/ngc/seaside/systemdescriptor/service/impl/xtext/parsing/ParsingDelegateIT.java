package com.ngc.seaside.systemdescriptor.service.impl.xtext.parsing;

import com.ngc.seaside.systemdescriptor.SystemDescriptorStandaloneSetup;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.service.api.IParsingIssue;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.testutil.InjectorTestFactory;

import org.eclipse.xtext.common.TerminalsStandaloneSetup;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

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

@RunWith(MockitoJUnitRunner.class)
public class ParsingDelegateIT {

   private ParsingDelegate delegate;

   @Before
   public void setup() throws Throwable {
      TerminalsStandaloneSetup.doSetup();
      new SystemDescriptorStandaloneSetup().register(InjectorTestFactory.getSharedInstance());
      delegate = InjectorTestFactory.getSharedInstance().getInstance(ParsingDelegate.class);
   }

   @Test
   public void testDoesParseSingleFile() throws Throwable {
      Path time = pathTo("valid-project", "clocks", "datatypes", "Time.sd");
      IParsingResult result = delegate.parseFiles(Collections.singletonList(time));
      assertTrue("parsing should be successful!", result.isSuccessful());
      assertNotNull("system descriptor not set!", result.getSystemDescriptor());
   }

   @Test
   public void testDoesParseMultipleFiles() throws Throwable {
      Collection<Path> paths = Arrays.asList(
            pathTo("valid-project", "clocks", "datatypes", "Time.sd"),
            pathTo("valid-project", "clocks", "datatypes", "TimeZone.sd"),
            pathTo("valid-project", "clocks", "datatypes", "ComplexTime.sd"),
            pathTo("valid-project", "clocks", "models", "Alarm.sd"),
            pathTo("valid-project", "clocks", "models", "ClockDisplay.sd"),
            pathTo("valid-project", "clocks", "models", "Speaker.sd"),
            pathTo("valid-project", "clocks", "models", "Timer.sd"),
            pathTo("valid-project", "clocks", "AlarmClock.sd"));
      IParsingResult result = delegate.parseFiles(paths);
      assertTrue("parsing should be successful!", result.isSuccessful());
      assertNotNull("system descriptor not set!", result.getSystemDescriptor());
      // result.getSystemDescriptor().traverse(Traversals.SYSTEM_OUT_PRINTING_VISITOR);
   }

   @Test
   public void testDoesParseProjectDirectory() throws Throwable {
      IParsingResult result = delegate.parseProject(Paths.get("build", "resources", "test", "valid-project"));
      assertTrue("parsing should be successful!", result.isSuccessful());
      assertNotNull("system descriptor not set!", result.getSystemDescriptor());
   }

   @Test
   public void testDoesHandleDataInheritance() throws Throwable {
      IParsingResult result = delegate.parseProject(Paths.get("build", "resources", "test", "valid-project"));
      assertTrue("parsing should be successful!", result.isSuccessful());
      assertNotNull("system descriptor not set!", result.getSystemDescriptor());
      IData bestTime = result.getSystemDescriptor().findData("clocks.datatypes.BestTime").get();
      assertTrue("data does not have base type!",
                 bestTime.getSuperDataType().isPresent());
      assertEquals("base data type name not correct!",
                   "BaseTime",
                   bestTime.getSuperDataType().get().getName());
   }

   @Test
   public void testDoesReturnParsingErrors() throws Throwable {
      Path time = pathTo("invalid-project", "clocks", "datatypes", "Time.sd");

      IParsingResult result = delegate.parseFiles(Collections.singletonList(time));
      assertFalse("parsing should not be successful!", result.isSuccessful());

      IParsingIssue issue = result.getIssues().iterator().next();
      assertEquals("wrong line number!", 9, issue.getLineNumber());
      assertEquals("wrong column number!", 3, issue.getColumn());
      assertNotNull("error message should not be null!", issue.getMessage());
      assertEquals("file not correct!", time.toAbsolutePath(), issue.getOffendingFile());
   }

   public static Path pathTo(String project, String... packagesAndFile) {
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
