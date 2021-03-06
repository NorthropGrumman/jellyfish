/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.ngc.seaside.systemdescriptor.service.impl.xtext.parsing;

import com.ngc.seaside.systemdescriptor.SystemDescriptorStandaloneSetup;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.service.api.IParsingIssue;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.testutil.InjectorTestFactory;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocation;

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

@RunWith(MockitoJUnitRunner.Silent.class)
public class ParsingDelegateIT {

   private ParsingDelegate delegate;

   @Before
   public void setup() {
      TerminalsStandaloneSetup.doSetup();
      new SystemDescriptorStandaloneSetup().register(InjectorTestFactory.getSharedInstance());
      delegate = InjectorTestFactory.getSharedInstance().getInstance(ParsingDelegate.class);
   }

   @Test
   public void testDoesParseSingleFile() {
      Path time = pathTo("valid-project", "clocks", "datatypes", "Time.sd");
      IParsingResult result = delegate.parseFiles(Collections.singletonList(time));
      assertTrue("parsing should be successful!", result.isSuccessful());
      assertNotNull("system descriptor not set!", result.getSystemDescriptor());
   }

   @Test
   public void testDoesParseMultipleFiles() {
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
   public void testDoesParseProjectDirectory() {
      IParsingResult result = delegate.parseProject(Paths.get("build", "resources", "test", "valid-project"));
      assertTrue("parsing should be successful!", result.isSuccessful());
      assertNotNull("system descriptor not set!", result.getSystemDescriptor());
   }

   @Test
   public void testDoesHandleDataInheritance() {
      IParsingResult result = delegate.parseProject(Paths.get("build", "resources", "test", "valid-project"));
      assertTrue("parsing should be successful!", result.isSuccessful());
      assertNotNull("system descriptor not set!", result.getSystemDescriptor());
      IData bestTime = result.getSystemDescriptor().findData("clocks.datatypes.BestTime").get();
      assertTrue("data does not have base type!",
                 bestTime.getExtendedDataType().isPresent());
      assertEquals("base data type name not correct!",
                   "BaseTime",
                   bestTime.getExtendedDataType().get().getName());
   }

   @Test
   public void testDoesReturnParsingErrors() {
      Path time = pathTo("invalid-project", "clocks", "datatypes", "Time.sd");

      IParsingResult result = delegate.parseFiles(Collections.singletonList(time));
      assertFalse("parsing should not be successful!", result.isSuccessful());

      IParsingIssue issue = result.getIssues().iterator().next();
      ISourceLocation location = issue.getLocation();
      assertEquals("wrong line number!", 31, location.getLineNumber());
      assertEquals("wrong column number!", 3, location.getColumn());
      assertNotNull("error message should not be null!", issue.getMessage());
      assertEquals("file not correct!", time.toAbsolutePath(), location.getPath());
   }

   /**
    * Creates a path to the given location.
    */
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
