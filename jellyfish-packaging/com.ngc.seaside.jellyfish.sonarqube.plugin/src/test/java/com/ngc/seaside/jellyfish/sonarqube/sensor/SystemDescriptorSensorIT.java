package com.ngc.seaside.jellyfish.sonarqube.sensor;

import com.ngc.seaside.jellyfish.sonarqube.language.SystemDescriptorLanguage;

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
      sensor = new SystemDescriptorSensor();
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
            descriptor.languages().toArray()[0]
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
   public void doesParseValidProject() throws IOException {
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
   public void doesFailWithInvalidProject() throws IOException {
      projectPath = BASE_DIR.resolve("invalid-project1");
      context = SensorContextTester.create(projectPath.toFile());
      addProjectInputFiles(context.fileSystem(), projectPath);

      sensor.execute(context);

      assertFalse(
            "an invalid file should have issues!",
            context.allIssues().isEmpty()
      );
   }

   @Test
   public void doesReportWarningsAsFindings() throws IOException {
      projectPath = BASE_DIR.resolve("valid-project-with-warnings");
      context = SensorContextTester.create(projectPath.toFile());
      addProjectInputFiles(context.fileSystem(), projectPath);

      sensor.execute(context);

      assertFalse(
            "a project with only warnings should have issues!",
            context.allIssues().isEmpty()
      );
   }

   private void addProjectInputFiles(DefaultFileSystem fs, Path path) throws IOException {
      Files.walk(path)
            .filter(p -> p.toFile().isFile())
            .filter(p -> p.toString().endsWith(".sd"))
            .map(this::createTestInputFile)
            .forEach(fs::add);
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
         System.out.println("couldn't read file contents: " + file.toString());
         return "";
      }
   }
}
