package com.ngc.seaside.starfish.bootstrap;

import org.apache.velocity.app.VelocityEngine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;

public class Main
{

   private static Path templateFile;
   private static Path outputFolder;
   private static boolean clean;

   public static void main(String[] args) throws IOException
   {
      parseArgs(args);
      Path templateFolder = unzip(templateFile);
      validateTemplate(templateFolder);

      LinkedHashMap<String, String> parametersAndDefaults = parseTemplateProperties(templateFolder.resolve("template.properties"));
      Map<String, String> parametersAndValues = new HashMap<>();
      VelocityEngine engine = new VelocityEngine();
      for (Map.Entry<String, String> entry : parametersAndDefaults.entrySet()) {
         String parameter = entry.getKey();
         String value = queryUser(parameter, entry.getValue(), null);
         parametersAndValues.put(parameter, value);
         engine.setProperty(parameter, value);
      }

      Files.walkFileTree(templateFolder.resolve("template"), new TemplateGenerator(parametersAndValues));

   }

   private static void parseArgs(String[] args)
   {
      List<String> list = new ArrayList<>(Arrays.asList(args));

      if (list.indexOf("-h") >= 0 || list.indexOf("--help") >= 0) {
         printHelp();
         System.exit(0);
      }

      int outputFolderIndex = list.indexOf("-o");
      if (outputFolderIndex >= 0) {
         if (outputFolderIndex + 1 < list.size()) {
            outputFolder = Paths.get(list.get(outputFolderIndex + 1));
            list.subList(outputFolderIndex, outputFolderIndex + 2);
         }
         else {
            System.err.println("Expected a folder after the option -o");
            System.exit(1);
         }
      }
      else {
         outputFolder = Paths.get(".").toAbsolutePath().normalize();
      }

      int cleanIndex = list.indexOf("--clean");
      if (cleanIndex >= 0) {
         clean = true;
         list.remove(cleanIndex);
      }
      else {
         clean = false;
      }

      if (list.size() != 1) {
         for (String arg : list) {
            if (arg.startsWith("-")) {
               System.err.println("Unknown argument " + arg);
               System.exit(1);
            }
         }
         System.err.println("Too many arguments. Try running with --help");
         System.exit(1);
      }

      templateFile = Paths.get(list.get(0));
      if (!Files.exists(templateFile)) {
         System.err.println("File " + list.get(0) + " does not exist");
         System.exit(1);
      }
      if (!Files.isRegularFile(templateFile)) {
         System.err.println("Expected a template file, not a directory: " + templateFile.toAbsolutePath());
         System.exit(1);
      }

   }

   private static void printHelp()
   {
      System.out.println("usage: java -jar [-h] [-o output-file] [--clean] template-file");
      System.out.println();
      System.out.println("positional arguments:");
      System.out.println("  template-file  template zip file");
      System.out.println();
      System.out.println("optional arguments:");
      System.out.println("  -h, --help      show this help message and exit");
      System.out.println("  -o output-file  Output directory for template generation");
      System.out.println("  --clean         Remove files from previous template generation");
   }

   /**
    * Unzips the template and returns the path to the folder of the unzipped contents.
    * 
    * @implNote the unzipped contents are put into the users temp folder.
    * 
    * @param template template zip file
    * @return folder of unzipped contents
    * @throws IOException if an exception occurred while unzipping or storing the files
    */
   private static Path unzip(Path template) throws IOException
   {
      Path folder = Files.createTempDirectory(null);
      throw new AssertionError("Not Implemented");
   }


   /**
    * Determines if the unzipped contents of the template are valid. Reports errors and exits the system if any occured.
    * 
    * @param templateFolder path to unzipped template folder
    */
   private static void validateTemplate(Path templateFolder)
   {
      if (!Files.exists(templateFolder.resolve("template.properties"))) {
         System.err.println("Invalid template: template.properties not found");
         System.exit(1);
      }
      if (!Files.isRegularFile(templateFolder.resolve("template.properties"))) {
         System.err.println("Invalid template: template.properties must be a file");
      }
      if (!Files.exists(templateFolder.resolve("template"))) {
         System.err.println("Invalid template: template folder not found");
         System.exit(1);
      }
      if (!Files.isDirectory(templateFolder.resolve("template"))) {
         System.err.println("Invalid template: template must be a folder");
         System.exit(1);
      }
   }

   /**
    * Parses the properties file a returns a map of parameters and their default values in the properties file,
    * ordered by encounter in the file.
    * 
    * @param propertiesFile path to properties file
    * @return map of parameters and their default values
    * @throws IOException if an exception occured while reading or parsing the properties file
    */
   private static LinkedHashMap<String, String> parseTemplateProperties(Path propertiesFile) throws IOException
   {
      LinkedHashMap<String, String> properties = new LinkedHashMap<>();

      for (String line : Files.readAllLines(propertiesFile)) {
         String trimmed = line.trim();
         if (trimmed.isEmpty() || trimmed.startsWith("#")) {
            continue;
         }
         String[] values = line.split("=", 2);
         if (values.length != 2) {
            throw new IllegalArgumentException("Invalid syntax in properties file: " + line);
         }
         properties.put(values[0].trim(), values[1].trim());
      }
      return properties;
   }

   /**
    * Queries the user to enter a value for the given parameter.
    *
    * @param parameter name of the parameter
    * @param defaultValue default value for the parameter, or null
    * @param validator function to determine whether a value is valid or not (can be null)
    * @return a valid value for the parameter
    */
   public static String queryUser(String parameter, String defaultValue, Predicate<String> validator)
   {
      if (validator == null) {
         validator = __ -> true;
      }
      final String defaultString = defaultValue == null ? "" : " (" + defaultValue + ")";
      while (true) {
         System.out.print("Enter value for " + parameter + defaultValue + ": ");
         final String line;
         try (Scanner sc = new Scanner(System.in)) {
            line = sc.nextLine();
         }

         if (validator.test(line)) {
            return line;
         }
         else {
            System.out.println("Invalid value, please try again");
         }
      }
   }

}
