package com.ngc.seaside.starfish.bootstrap;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

public class CommandLine
{

   private Path templateFile;
   private Path outputFolder;
   private boolean clean;
   private static Scanner sc;

   public CommandLine()
   {

   }

   public Path getTemplateFile()
   {
      return templateFile;
   }

   public Path getOutputFolder()
   {
      return outputFolder;
   }

   public boolean isClean()
   {
      return clean;
   }

   public static CommandLine parseArgs(String... args)
   {
      CommandLine cl = new CommandLine();
      List<String> list = new ArrayList<>(Arrays.asList(args));

      if (list.indexOf("-h") >= 0 || list.indexOf("--help") >= 0) {
         printHelp();
         throw new ExitException();
      }

      int outputFolderIndex = list.indexOf("-o");
      if (outputFolderIndex >= 0) {
         if (outputFolderIndex + 1 < list.size()) {
            cl.outputFolder = Paths.get(list.get(outputFolderIndex + 1));
            list.subList(outputFolderIndex, outputFolderIndex + 2).clear();
         }
         else {
            throw new ExitException("Expected a folder after the option -o");
         }
      }
      else {
         cl.outputFolder = Paths.get(".").toAbsolutePath().normalize();
      }

      int cleanIndex = list.indexOf("--clean");
      if (cleanIndex >= 0) {
         cl.clean = true;
         list.remove(cleanIndex);
      }
      else {
         cl.clean = false;
      }

      if (list.size() != 1) {
         for (String arg : list) {
            if (arg.startsWith("-")) {
               throw new ExitException("Unknown argument " + arg);
            }
         }
         throw new ExitException("Too many arguments. Try running with --help");
      }

      cl.templateFile = Paths.get(list.get(0));
      if (!Files.exists(cl.templateFile)) {
         throw new ExitException("File " + list.get(0) + " does not exist");
      }
      if (!Files.isRegularFile(cl.templateFile)) {
         throw new ExitException("Expected a template file, not a directory: " + cl.templateFile.toAbsolutePath());
      }
      return cl;
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
      System.out.println("  -o output-file  output directory for template generation");
      System.out.println("  --clean         remove files from previous template generation");
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
      if (sc == null) {
         sc = new Scanner(System.in);
      }
      if (validator == null) {
         validator = __ -> true;
      }
      final String defaultString = defaultValue == null ? "" : " (" + defaultValue + ")";
      while (true) {
         System.out.print("Enter value for " + parameter + defaultString + ": ");
         String line;
         line = sc.nextLine();
         if (line.isEmpty() && defaultValue != null) {
            return defaultValue;
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
