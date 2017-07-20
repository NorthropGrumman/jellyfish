package com.ngc.seaside.bootstrap.utilities.file;

import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.IParameter;
import com.ngc.seaside.command.api.IParameterCollection;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 */
public class GradleSettingsUtilitiesTest {

   @Rule
   public TemporaryFolder testFolder = new TemporaryFolder();

   @Test
   public void doesAddProject() throws IOException, FileUtilitiesException {
      IParameterCollection collection = mock(IParameterCollection.class);
      when(collection.containsParameter("outputDirectory")).thenReturn(true);
      when(collection.containsParameter("groupId")).thenReturn(true);
      when(collection.containsParameter("artifactId")).thenReturn(true);

      File outputDirectory = testFolder.newFolder("output");
      Path settingsGradle = Paths.get(outputDirectory.toString(), "settings.gradle");
      Files.createFile(settingsGradle);

      when(collection.getParameter("outputDirectory"))
               .thenReturn(createParameter("outputDirectory", outputDirectory.getAbsolutePath()));
      when(collection.getParameter("groupId")).thenReturn(createParameter("groupId", "groupIdValue"));
      when(collection.getParameter("artifactId")).thenReturn(createParameter("artifactId", "artifactIdValue"));

      GradleSettingsUtilities.addProject(collection);

      List<String> lines = Files.readAllLines(settingsGradle);

      assertEquals(3, lines.size());

      assertTrue(lines.contains(""));
      assertTrue(lines.contains("include 'groupIdValue.artifactIdValue'"));
      assertTrue(lines.contains("project(':groupIdValue.artifactIdValue').name = 'artifactIdValue'"));
   }

   @Test
   public void testAddDuplicateProject() throws IOException, FileUtilitiesException {
      IParameterCollection collection = mock(IParameterCollection.class);
      when(collection.containsParameter("outputDirectory")).thenReturn(true);
      when(collection.containsParameter("groupId")).thenReturn(true);
      when(collection.containsParameter("artifactId")).thenReturn(true);

      File outputDirectory = testFolder.newFolder("output");
      Path settingsGradle = Paths.get(outputDirectory.toString(), "settings.gradle");
      Files.createFile(settingsGradle);

      when(collection.getParameter("outputDirectory"))
               .thenReturn(createParameter("outputDirectory", outputDirectory.getAbsolutePath()));
      when(collection.getParameter("groupId")).thenReturn(createParameter("groupId", "groupIdValue"));
      when(collection.getParameter("artifactId")).thenReturn(createParameter("artifactId", "artifactIdValue"));

      GradleSettingsUtilities.addProject(collection);
      GradleSettingsUtilities.addProject(collection);

      List<String> lines = Files.readAllLines(settingsGradle);

      assertEquals(3, lines.size());

      assertTrue(lines.contains(""));
      assertTrue(lines.contains("include 'groupIdValue.artifactIdValue'"));
      assertTrue(lines.contains("project(':groupIdValue.artifactIdValue').name = 'artifactIdValue'"));
   }

   private IParameter createParameter(String name, String value) {
      return new DefaultParameter(name).setValue(value);
   }

}
