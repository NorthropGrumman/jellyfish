package com.ngc.seaside.bootstrap.utilites.file;

import com.ngc.seaside.command.api.IParameterCollection;

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

   /**
    * Add a gradle project to the settings.gradle file. The file should be in the output directory. The output directory
    * should be located in the input parameters to this method.
    *
    * @param parameters the parameters to use. these parameters must contain the groupId and artifactId.
    * @throws FileUtilitiesException this error is thrown anytime an error occurs when trying to write to the
    *                                settings file or if the parameters don't contain the necessary properties.
    */
   public static void addProject(IParameterCollection parameters) throws FileUtilitiesException {
      if (parameters.containsParameter(OUTPUT_DIR_PROPERTY)) {
         Path outputDirectory = Paths.get(parameters.getParameter(OUTPUT_DIR_PROPERTY).getValue());
         Path settings = Paths.get(outputDirectory.normalize().toString(), SETTINGS_FILE_NAME);

         String bundleName = String.format("%s.%s",
                                           parameters.getParameter(GROUP_ID_PROPERTY).getValue(),
                                           parameters.getParameter(ARTIFACT_ID_PROPERTY).getValue());
         List<String> lines = new ArrayList<>();
         lines.add(String.format("%ninclude '%s'", bundleName));
         lines.add(String.format("%nproject(':%s').name = '%s'%n",
                                 bundleName,
                                 parameters.getParameter(ARTIFACT_ID_PROPERTY).getValue()));

         FileUtilities.addLinesToFile(settings, lines);
      } else {
         throw new FileUtilitiesException("The outputDirectory is required in order to find the settings.gradle file.");
      }

   }

}
