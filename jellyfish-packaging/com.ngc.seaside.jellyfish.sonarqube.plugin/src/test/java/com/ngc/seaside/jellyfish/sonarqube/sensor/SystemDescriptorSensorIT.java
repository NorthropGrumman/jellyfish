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
package com.ngc.seaside.jellyfish.sonarqube.sensor;

import com.ngc.seaside.jellyfish.cli.command.analyze.inputsoutputs.InputsOutputsFindingTypes;
import com.ngc.seaside.jellyfish.sonarqube.extension.DefaultJellyfishModuleFactory;
import com.ngc.seaside.jellyfish.sonarqube.language.SystemDescriptorLanguage;
import com.ngc.seaside.jellyfish.sonarqube.properties.SystemDescriptorProperties;
import com.ngc.seaside.jellyfish.sonarqube.rule.SyntaxWarningRule;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.fs.InputFile.Type;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;
import org.sonar.api.batch.sensor.internal.DefaultSensorDescriptor;
import org.sonar.api.batch.sensor.internal.SensorContextTester;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SystemDescriptorSensorIT {

   private static final String SENSOR_NAME = SystemDescriptorSensor.class.getSimpleName();

   private static final Path BASE_DIR = Paths.get("build", "resources", "test");

   private SystemDescriptorSensor sensor;

   private DefaultSensorDescriptor descriptor;
   private SensorContextTester context;
   private Path projectPath;

   @Before
   public void setup() {
      sensor = new SystemDescriptorSensor(new DefaultJellyfishModuleFactory().setIncludeDefaultModules(true));
      descriptor = new DefaultSensorDescriptor();
   }

   @Test
   public void definesCorrectSensorName() {
      sensor.describe(descriptor);
      assertEquals(
            "sensor name is incorrect",
            descriptor.name(),
            SENSOR_NAME
      );
   }

   @Test
   public void definesCorrectLanguage() {
      sensor.describe(descriptor);
      assertEquals(
            "the sensor should be available for only one language!",
            1, descriptor.languages().size()
      );
      assertEquals(
            "the language should be: " + SystemDescriptorLanguage.KEY,
            SystemDescriptorLanguage.KEY,
            descriptor.languages().iterator().next()
      );
   }

   @Test
   public void definesCorrectInputFileType() {
      sensor.describe(descriptor);
      assertEquals(
            "the file type for input files should be: " + Type.MAIN,
            Type.MAIN,
            descriptor.type()
      );
   }

   @Test
   public void doesParseValidProject() {
      projectPath = BASE_DIR.resolve("valid-project");
      context = SensorContextTester.create(projectPath.toFile());
      addProjectInputFiles(context.fileSystem(), projectPath);

      sensor.execute(context);

      assertTrue(
            "a valid project should not have issues!",
            context.allIssues().isEmpty()
      );
   }

   @Test
   public void doesReportWarningsAsIssues() {
      projectPath = BASE_DIR.resolve("valid-project-with-warnings");
      context = SensorContextTester.create(projectPath.toFile());
      addProjectInputFiles(context.fileSystem(), projectPath);

      sensor.execute(context);

      assertFalse(
            "a project with warnings should have issues!",
            context.allIssues().isEmpty()
      );
      assertEquals(
            "a project with warnings should generate a syntax warning rule!",
            SyntaxWarningRule.KEY,
            context.allIssues().iterator().next().ruleKey()
      );
   }

   @Test
   public void doesReportAnalysisFindingsAsIssues() {
      projectPath = BASE_DIR.resolve("valid-project-with-analysis-issues");
      context = SensorContextTester.create(projectPath.toFile());
      context.settings().appendProperty(SystemDescriptorProperties.JELLYFISH_ANALYSIS_KEY, "analyze-inputs-outputs");
      addProjectInputFiles(context.fileSystem(), projectPath);

      sensor.execute(context);

      assertFalse(
            "a project with analysis findings should have issues!",
            context.allIssues().isEmpty()
      );
      assertEquals(
            "a project with analysis findings should use an analysis rule (in this case, inputs with no outputs)!",
            InputsOutputsFindingTypes.INPUTS_WITH_NO_OUTPUTS.getId(),
            context.allIssues().iterator().next().ruleKey().rule()
      );
   }

   private void addProjectInputFiles(DefaultFileSystem fs, Path path) {
      try {
         Files.walk(path)
               .filter(p -> p.toFile().isFile())
               .filter(p -> p.toString().endsWith(".sd"))
               .map(this::createTestInputFile)
               .forEach(fs::add);
      } catch (IOException e) {
         throw new RuntimeException("an error occurred while searching for system descriptor files!", e);
      }
   }

   private DefaultInputFile createTestInputFile(Path file) {
      return new TestInputFileBuilder("", file.toString())
            .setModuleBaseDir(BASE_DIR)
            .initMetadata(readContentsOfTestInputFile(file))
            .build();
   }

   private String readContentsOfTestInputFile(Path file) {
      try {
         return new String(Files.readAllBytes(file));
      } catch (IOException e) {
         throw new RuntimeException("couldn't read file contents: " + file.toString(), e);
      }
   }
}
