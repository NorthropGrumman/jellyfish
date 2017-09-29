package com.ngc.seaside.jellyfish.api;

import com.ngc.seaside.command.api.CommandException;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.IParameter;
import com.ngc.seaside.command.api.IParameterCollection;

public enum CommonParameters implements IParameter<String> {

   ARTIFACT_ID("artifactId", "The project's artifact ID."),
   CLASSNAME("classname", "The name of the class that will be generated. i.e. MyClass"),
   CLEAN("clean", "If true, recursively deletes the project (if it already exists) before generating it again"),
   GROUP_ID("groupId", "The project's group ID. (default: the package in the model)"),
   MODEL("model", "The fully qualified path to the system descriptor model"),
   MODEL_OBJECT("modelObject", "The fully qualified name of the model to generate connectors for"),
   OUTPUT_DIRECTORY("outputDirectory", "Base directory in which to output the project"),
   PACKAGE("package", "The project's default package"),
   PACKAGE_SUFFIX("packageSuffix", "A string to append to the end of the generated package name"),
   UPDATE_GRADLE_SETTING("updateGradleSettings", "If false, the generated project will not be added to any existing"
                                                 + " settings.gradle file");

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
   public static boolean evaluateBooleanParameter(IParameterCollection parameters, String parameter,
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

   public IParameter<String> required() {
      DefaultParameter<String> parameter = new DefaultParameter<>(name);
      parameter.setDescription(description);
      parameter.setRequired(true);
      return parameter;
   }
}
