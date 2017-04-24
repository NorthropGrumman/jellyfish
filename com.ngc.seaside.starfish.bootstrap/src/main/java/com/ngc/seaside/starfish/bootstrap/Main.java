package com.ngc.seaside.starfish.bootstrap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Main
{

   public static void main(String[] args) throws IOException
   {
      CommandLine cl = CommandLine.parseArgs(args);
      Path templateFolder = TemplateProcessor.unzip(cl.getTemplateFile());
      TemplateProcessor.validateTemplate(templateFolder);

      LinkedHashMap<String, String> parametersAndDefaults = TemplateProcessor.parseTemplateProperties(templateFolder.resolve("template.properties"));
      Map<String, String> parametersAndValues = new HashMap<>();
      for (Map.Entry<String, String> entry : parametersAndDefaults.entrySet()) {
         String parameter = entry.getKey();
         String value = CommandLine.queryUser(parameter, entry.getValue(), null);
         parametersAndValues.put(parameter, value);
      }

      Files.walkFileTree(templateFolder.resolve("template"), new TemplateGenerator(parametersAndValues, cl.getOutputFolder(), cl.isClean()));
   }

}
