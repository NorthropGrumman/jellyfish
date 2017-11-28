package com.ngc.seaside.jellyfish.api;

import com.ngc.seaside.command.api.CommandException;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.IParameter;
import com.ngc.seaside.command.api.IParameterCollection;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum CommonParameters implements IParameter<String> {
   ARTIFACT_ID("artifactId", "The project's artifact ID."),
   CLASSNAME("classname", "The name of the class that will be generated. i.e. MyClass"),
   CLEAN("clean", "If true, recursively deletes the project (if it already exists) before generating it again"),
   GROUP_ARTIFACT_VERSION_EXTENSION("gave", "The Group/Artifact/Version/Extension of a system descriptor project"),
   GROUP_ID("groupId", "The project's group ID. (default: the package in the model)"),
   INPUT_DIRECTORY("inputDir", "Base directory of the system descriptor project"),
   MODEL("model", "The fully qualified path to the system descriptor model"),
   MODEL_OBJECT("modelObject", "The fully qualified name of the model to generate connectors for"),
   OUTPUT_DIRECTORY("outputDirectory", "Base directory in which to output the project"),
   PACKAGE("package", "The project's default package"),
   PACKAGE_SUFFIX("packageSuffix", "A string to append to the end of the generated package name"),
   UPDATE_GRADLE_SETTING("updateGradleSettings", "If false, the generated project will not be added to any existing"
                                                 + " settings.gradle file"),
   REPOSITORY_URL("repositoryUrl", "The url of a system descriptor repository. If specified, " + GROUP_ARTIFACT_VERSION_EXTENSION.getName() + " parameter is required");

   private static final Pattern GAVE_REGEX = java.util.regex.Pattern.compile("(.+):(.+):(.+)@(.+)");
   private final String description;
   private final String name;

   CommonParameters(String name, String description) {
      this.description = description;
      this.name = name;
   }

   @Override
   public String getDescription() {
      return description;
   }

   @Override
   public String getName() {
      return name;
   }

   @Override
   public String getValue() {
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public String getStringValue() {
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public boolean isRequired() {
      return false;
   }

   /**
    * Returns a copy of the current parameter whose required value is true.
    *
    * @return a required version of the current parameter
    */
   public IParameter<String> required() {
      DefaultParameter<String> parameter = new DefaultParameter<>(name);
      parameter.setDescription(description);
      parameter.setRequired(true);
      return parameter;
   }

   /**
    * Returns the boolean value of the given parameter if it was set, false otherwise.
    *
    * @param parameters command parameters
    * @param parameter  name of parameter
    * @return the boolean value of the parameter
    * @throws CommandException if the value is neither 'true' nor 'false'
    */
   public static boolean evaluateBooleanParameter(IParameterCollection parameters, String parameter) {
      return evaluateBooleanParameter(parameters, parameter, false);
   }

   /**
    * Returns the boolean value of the given parameter if it was set, the given default otherwise.
    *
    * @param parameters command parameters
    * @param parameter  name of parameter
    * @return the boolean value of the parameter
    * @throws CommandException if the value is neither 'true' nor 'false'
    */
   public static boolean evaluateBooleanParameter(IParameterCollection parameters,
                                                  String parameter,
                                                  boolean defaultValue) {
      if (parameters.containsParameter(parameter)) {
         String value = parameters.getParameter(parameter).getStringValue().toLowerCase();
         if (!value.equals("true") && !value.equals("false")) {
            throw new CommandException(
                  "Invalid value for " + parameter + ": " + value + ". Expected either true or false.");
         } else {
            return Boolean.valueOf(value);
         }
      } else {
         return defaultValue;
      }
   }

   /**
    * Parses the given group/artifact/version/extension identifier.  The GAVE should be in the format
    * <pre>
    *    groupId:artifactId:version@extension
    * </pre>
    * If the GAVE is not in this format, an {@code IllegalArgumentException} is thrown.
    * @param gave the GAVE to parse
    * @return an array that contains the parsed group ID, artifact ID, version, and extension in that order
    * @throws IllegalArgumentException {@code gave} is {@code null}, empty, or does not match the format above
    */
   public static String[] parseGave(String gave) {
      if(gave == null || gave.trim().isEmpty()) {
         throw new IllegalArgumentException("GAVE may not be null or empty!");
      }
      Matcher matcher = GAVE_REGEX.matcher(gave);
      if (!matcher.matches()) {
         throw new IllegalArgumentException("GAVE string must be of the format group:artifact:version@extension, got "
                                            + gave
                                            + "!");
      }
      return new String[]{
            matcher.group(1),
            matcher.group(2),
            matcher.group(3),
            matcher.group(4),
            };
   }
}
