package com.ngc.seaside.jellyfish.api;

import com.ngc.seaside.jellyfish.api.CommandException;
import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.IParameter;
import com.ngc.seaside.jellyfish.api.IParameterCollection;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum CommonParameters implements IParameter<String> {
   ARTIFACT_ID("artifactId", "The project's artifact ID."),
   CLASSNAME("classname", "The name of the class that will be generated. i.e. MyClass"),
   CLEAN("clean", "If true, recursively deletes the project (if it already exists) before generating it again"),
   GROUP_ARTIFACT_VERSION("gav", "The identifier of a system descriptor project in the form <groupId>:<artifactId>:<version>"),
   @Deprecated
   GROUP_ARTIFACT_VERSION_EXTENSION("gave", "(Deprecated, use " + GROUP_ARTIFACT_VERSION.name() + ") The identifier of a system descriptor project in the form <groupId>:<artifactId>:<version>@<extension>"),
   GROUP_ID("groupId", "The project's group ID. (default: the package in the model)"),
   INPUT_DIRECTORY("inputDir", "Base directory of the system descriptor project"),
   MODEL("model", "The fully qualified path to the system descriptor model"),
   MODEL_OBJECT("modelObject", "The fully qualified name of the model to generate connectors for"),
   OUTPUT_DIRECTORY("outputDirectory", "Base directory in which to output the project"),
   PACKAGE("package", "The project's default package"),
   PACKAGE_SUFFIX("packageSuffix", "A string to append to the end of the generated package name"),
   UPDATE_GRADLE_SETTING("updateGradleSettings", "If false, the generated project will not be added to any existing"
                                                 + " settings.gradle file"),
   @Deprecated
   REPOSITORY_URL("repositoryUrl", "(Deprecated, no longer used) The url of a system descriptor repository. If specified, " + GROUP_ARTIFACT_VERSION_EXTENSION.getName() + " parameter is required");

   private static final Pattern GAV_REGEX = java.util.regex.Pattern.compile("([^:@\\s]+):([^:@\\s]+):([^:@\\s]+)");
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
    * Parses the given group/artifact/version identifier.  The GAV should be in the format
    * <pre>
    *    groupId:artifactId:version
    * </pre>
    * @param gav the GAV to parse
    * @return an array that contains the parsed group ID, artifact ID, and version in that order
    * @throws IllegalArgumentException if {@code gav} is {@code null}, or does not match the format above
    */
   public static String[] parseGav(String gav) {
      if(gav == null || gav.trim().isEmpty()) {
         throw new IllegalArgumentException("GAV may not be null or empty!");
      }
      Matcher matcher = GAV_REGEX.matcher(gav);
      if (!matcher.matches()) {
         throw new IllegalArgumentException("GAV string must be of the format group:artifact:version, got "
                                            + gav);
      }
      return new String[]{
            matcher.group(1),
            matcher.group(2),
            matcher.group(3)
            };
   }
}
