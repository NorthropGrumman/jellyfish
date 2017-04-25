package com.ngc.seaside.starfish.bootstrap;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Main
{
   private static final String TEMPLATE_FOLDER = "template";
   private static final String TEMPLATE_PROPERTIES = "template.properties";

   /**
    * Run with --help for a description of the script.
    * 
    * @param args command line arguments
    */
   public static void main(String[] args)
   {
      int errorCode = 0;
      Path templateFolder = null;
      try {
         CommandLine cl = CommandLine.parseArgs(args);

         // Unzip template folder and validate for correct format
         templateFolder = TemplateProcessor.unzip(cl.getTemplateFile());
         TemplateProcessor.validateTemplate(templateFolder);

         // Parse template.properties file for each parameter and its default value
         LinkedHashMap<String, String> parametersAndDefaults = TemplateProcessor.parseTemplateProperties(templateFolder.resolve(TEMPLATE_PROPERTIES));

         // For each parameter query the user for its value
         Map<String, String> parametersAndValues = new HashMap<>();
         for (Map.Entry<String, String> entry : parametersAndDefaults.entrySet()) {
            String parameter = entry.getKey();
            String value = CommandLine.queryUser(parameter, entry.getValue(), null);
            parametersAndValues.put(parameter, value);
         }

         // Walk through the unzipped template directory in order to generate the instance of the template
         Files.walkFileTree(templateFolder.resolve(TEMPLATE_FOLDER), new TemplateGenerator(parametersAndValues, templateFolder.resolve(TEMPLATE_FOLDER), cl.getOutputFolder(), cl.isClean()));

      }
      catch (ExitException e) {
         if (e.failed() && !e.getMessage().isEmpty()) {
            System.err.println(e.getMessage());
         }
         errorCode = e.getCode();
      }
      catch (Exception e) {
         System.err.println("An unexpected error occured: ");
         e.printStackTrace(System.err);
         errorCode = 1;
      }
      finally {
         if (templateFolder != null) {
            try {
               TemplateGenerator.deleteRecursive(templateFolder, false);
            }
            catch (Exception e) {
               System.err.println("Unable to delete temporary folder " + templateFolder + ": " + e.getMessage());
            }
         }
      }
      System.exit(errorCode);
   }

}
