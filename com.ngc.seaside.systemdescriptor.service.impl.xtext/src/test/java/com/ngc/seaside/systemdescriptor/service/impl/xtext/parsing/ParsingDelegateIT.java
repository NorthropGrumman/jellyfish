package com.ngc.seaside.systemdescriptor.service.impl.xtext.parsing;

import com.google.inject.Injector;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.systemdescriptor.SystemDescriptorStandaloneSetup;
import com.ngc.seaside.systemdescriptor.service.api.IParsingIssue;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;

import org.eclipse.xtext.parser.IParser;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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

   @Mock
   private ILogService logService;

   @Before
   public void setup() throws Throwable {
      Injector injector = new SystemDescriptorStandaloneSetup().createInjectorAndDoEMFRegistration();
      delegate = new ParsingDelegate(injector.getInstance(IParser.class),
                                     injector.getInstance(XtextResourceSet.class),
                                     logService);
   }

   @Test
   public void testDoesParseSingleFile() throws Throwable {
      Path time = pathTo("valid-project", "clocks", "datatypes", "Time.sd");
      IParsingResult result = delegate.parseFiles(Collections.singletonList(time));
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
      IParsingResult result = delegate.parseFiles(paths);
      assertTrue("parsing should be successful!",
                 result.isSuccessful());
      assertNotNull("system descriptor not set!",
                    result.getSystemDescriptor());
      //result.getSystemDescriptor().traverse(Traversals.SYSTEM_OUT_PRINTING_VISITOR);
   }

   @Test
   public void testDoesParseProjectDirectory() throws Throwable {
      IParsingResult result = delegate.parseProject(Paths.get("build", "resources", "test", "valid-project"));
      assertTrue("parsing should be successful!",
                 result.isSuccessful());
      assertNotNull("system descriptor not set!",
                    result.getSystemDescriptor());
   }

   @Test
   public void testDoesReturnParsingErrors() throws Throwable {
      Path time = pathTo("invalid-project", "clocks", "datatypes", "Time.sd");

      IParsingResult result = delegate.parseFiles(Collections.singletonList(time));
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
