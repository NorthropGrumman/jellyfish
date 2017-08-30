package com.ngc.seaside.bootstrap.utilities.file;

import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultParameterCollection;
import com.ngc.seaside.command.api.IParameterCollection;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class GradleSettingsUtilities {

   public static final String SETTINGS_FILE_NAME = "settings.gradle";

   private static final String OUTPUT_DIR_PROPERTY = "outputDirectory";
   private static final String GROUP_ID_PROPERTY = "groupId";
   private static final String ARTIFACT_ID_PROPERTY = "artifactId";

   private GradleSettingsUtilities() {
      //used as a static class
   }

   /**
    * Attempt to add a Gradle project to the settings.gradle file if the file actually exists in the output directory, otherwise to the output directory's parent settings.gradle.
    * If neither file exists, it does nothing.
    *
    * @return whether or not the project was added to a settings.gradle
    * @see #addProject(IParameterCollection)
    */
   public static boolean tryAddProject(IParameterCollection parameters) throws FileUtilitiesException {
      if (!parameters.containsParameter(OUTPUT_DIR_PROPERTY) ||
          !parameters.containsParameter(GROUP_ID_PROPERTY) ||
          !parameters.containsParameter(ARTIFACT_ID_PROPERTY)) {
         throw new FileUtilitiesException(
               String.format("The %s, %s and %s properties are required in order to find the settings.gradle file.",
                             OUTPUT_DIR_PROPERTY, GROUP_ID_PROPERTY, ARTIFACT_ID_PROPERTY));
      }

      Path outputDirectory = Paths.get(parameters.getParameter(OUTPUT_DIR_PROPERTY).getStringValue()).normalize().toAbsolutePath();
      Path settings = outputDirectory.resolve(SETTINGS_FILE_NAME);
      if (settings.toFile().isFile()) {
         addProject(parameters, settings);
         return true;
      } else {
         settings = outputDirectory.getParent().resolve(SETTINGS_FILE_NAME);
         if (settings.toFile().isFile()) {
            DefaultParameterCollection newParameters = new DefaultParameterCollection();
            newParameters.addParameter(new DefaultParameter<>(GROUP_ID_PROPERTY,
               outputDirectory.getFileName() + "/" + parameters.getParameter(GROUP_ID_PROPERTY).getStringValue()));
            newParameters.addParameter(parameters.getParameter(ARTIFACT_ID_PROPERTY));
            addProject(newParameters, settings);
            return true;
         }
         return false;
      }
   }

   /**
    * Add a gradle project to the settings.gradle file. The file should be in the output directory. The output directory
    * should be located in the input parameters to this method.
    *
    * @param parameters the parameters to use. these parameters must contain the groupId and artifactId.
    * @throws FileUtilitiesException thrown anytime an error occurs when trying to write to the settings file or if the
    *                                parameters don't contain the necessary properties.
    */
   public static void addProject(IParameterCollection parameters) throws FileUtilitiesException {
      if (!parameters.containsParameter(OUTPUT_DIR_PROPERTY) ||
          !parameters.containsParameter(GROUP_ID_PROPERTY) ||
          !parameters.containsParameter(ARTIFACT_ID_PROPERTY)) {
         throw new FileUtilitiesException(
               String.format("The %s, %s and %s properties are required in order to find the settings.gradle file.",
                             OUTPUT_DIR_PROPERTY, GROUP_ID_PROPERTY, ARTIFACT_ID_PROPERTY));
      }

      Path outputDirectory = Paths.get(parameters.getParameter(OUTPUT_DIR_PROPERTY).getStringValue());
      Path settings = Paths.get(outputDirectory.normalize().toString(), SETTINGS_FILE_NAME);
      addProject(parameters, settings);
   }

   /**
    * Add a gradle project to the settings.gradle file at the given location.
    *
    * @param parameters the parameters which must contain the groupId and artifactId.
    * @throws FileUtilitiesException thrown anytime an error occurs when trying to write to the settings file or if the
    *                                parameters don't contain the necessary properties
    */
   public static void addProject(IParameterCollection parameters, Path settingsGradleFile)
         throws FileUtilitiesException {
      if (!parameters.containsParameter(GROUP_ID_PROPERTY) ||
          !parameters.containsParameter(ARTIFACT_ID_PROPERTY)) {
         throw new FileUtilitiesException(
               String.format("The %s and %s properties are required in order to find the settings.gradle file.",
                             GROUP_ID_PROPERTY, ARTIFACT_ID_PROPERTY));
      }

      String bundleName = String.format("%s.%s",
                                        parameters.getParameter(GROUP_ID_PROPERTY).getValue(),
                                        parameters.getParameter(ARTIFACT_ID_PROPERTY).getValue());

      List<String> lines = new ArrayList<>();
      lines.add(""); //add a line to separate the bundles
      lines.add(String.format("include '%s'", bundleName));
      lines.add(String.format("project(':%s').name = '%s'",
                              bundleName,
                              parameters.getParameter(ARTIFACT_ID_PROPERTY).getValue()));

      boolean areLinesAlreadyInFile = false;
      try {
         String contents = new String(Files.readAllBytes(settingsGradleFile));
         areLinesAlreadyInFile = lines.stream().allMatch(contents::contains);
      } catch (IOException e) {
         // Ignore exception, add project if file can't be read
      }

      if (!areLinesAlreadyInFile) {
         FileUtilities.addLinesToFile(settingsGradleFile, lines);
      }
   }
}
