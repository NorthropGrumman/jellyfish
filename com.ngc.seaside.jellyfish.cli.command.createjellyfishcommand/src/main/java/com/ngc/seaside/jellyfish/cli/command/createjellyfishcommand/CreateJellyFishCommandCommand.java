package com.ngc.seaside.jellyfish.cli.command.createjellyfishcommand;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.service.promptuser.api.IPromptUserService;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateService;
import com.ngc.seaside.bootstrap.utilities.file.FileUtilitiesException;
import com.ngc.seaside.bootstrap.utilities.file.GradleSettingsUtilities;
import com.ngc.seaside.command.api.CommandException;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultParameterCollection;
import com.ngc.seaside.command.api.DefaultUsage;
import com.ngc.seaside.command.api.IParameterCollection;
import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;

import org.apache.commons.lang3.text.WordUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

/**
 * 
 */
@Component(service = IJellyFishCommand.class)
public class CreateJellyFishCommandCommand implements IJellyFishCommand {

   private static final String NAME = "create-jellyfish-command";
   private static final IUsage USAGE = createUsage();
   
   private static final Pattern JAVA_IDENTIFIER = Pattern.compile("[a-zA-Z$_][a-zA-Z$_0-9]*");
   private static final Pattern JAVA_QUALIFIED_IDENTIFIER = Pattern.compile("[a-zA-Z$_][a-zA-Z$_0-9]*(?:\\.[a-zA-Z$_][a-zA-Z$_0-9]*)*");

   public static final String OUTPUT_DIR_PROPERTY = "outputDirectory";
   public static final String GROUP_ID_PROPERTY = "groupId";
   public static final String ARTIFACT_ID_PROPERTY = "artifactId";
   public static final String PACKAGE_PROPERTY = "package";
   public static final String CLASSNAME_PROPERTY = "classname";
   public static final String COMMAND_NAME_PROPERTY = "commandName";
   public static final String CLEAN_PROPERTY = "clean";

   public static final String DEFAULT_GROUP_ID = "com.ngc.seaside";
   public static final String DEFAULT_ARTIFACT_ID_FORMAT = "jellyfish.cli.command.%s";

   private ILogService logService;
   private IPromptUserService promptService;
   public ITemplateService templateService;

   @Activate
   public void activate() {
      logService.trace(getClass(), "Activated");
   }

   @Deactivate
   public void deactivate() {
      logService.trace(getClass(), "Deactivated");
   }

   @Override
   public String getName() {
      return NAME;
   }

   @Override
   public IUsage getUsage() {
      return USAGE;
   }

   @Override
   public void run(IJellyFishCommandOptions commandOptions) {
      IParameterCollection parameters = commandOptions.getParameters();

      DefaultParameterCollection collection = new DefaultParameterCollection();
      collection.addParameters(parameters.getAllParameters());

      final String commandName;
      if (!parameters.containsParameter(COMMAND_NAME_PROPERTY)) {
         commandName = promptService.prompt(COMMAND_NAME_PROPERTY, "", null);
         collection.addParameter(new DefaultParameter(COMMAND_NAME_PROPERTY).setValue(commandName));
      } else {
         commandName = parameters.getParameter(COMMAND_NAME_PROPERTY).getValue();
      }
      
      if (!parameters.containsParameter(OUTPUT_DIR_PROPERTY)) {
         collection.addParameter(new DefaultParameter(OUTPUT_DIR_PROPERTY).setValue(Paths.get(".").toAbsolutePath().toString()));
      }
      final Path outputDirectory = Paths.get(collection.getParameter(OUTPUT_DIR_PROPERTY).getValue());
      try {
         Files.createDirectories(outputDirectory);
      } catch (IOException e) {
         logService.error(CreateJellyFishCommandCommand.class, e);
         throw new CommandException(e);
      }
      
      if (Files.isDirectory(outputDirectory.resolve("com.ngc.seaside.jellyfish.api"))) {
         collection.addParameter(new DefaultParameter("withApi").setValue("true"));
      } else {
         collection.addParameter(new DefaultParameter("withApi").setValue("false"));
      }

      
      if (!parameters.containsParameter(GROUP_ID_PROPERTY)) {
         collection.addParameter(new DefaultParameter(GROUP_ID_PROPERTY).setValue(DEFAULT_GROUP_ID));
      }
      final String group = collection.getParameter(GROUP_ID_PROPERTY).getValue();

      if (!parameters.containsParameter(ARTIFACT_ID_PROPERTY)) {
         String artifact = collection.getParameter(COMMAND_NAME_PROPERTY).getValue().replace("-", "").toLowerCase();
         collection.addParameter(new DefaultParameter(ARTIFACT_ID_PROPERTY).setValue(String.format(DEFAULT_ARTIFACT_ID_FORMAT, artifact)));
      }
      final String artifact = collection.getParameter(ARTIFACT_ID_PROPERTY).getValue();
      
      if (!parameters.containsParameter(PACKAGE_PROPERTY)) {
         collection.addParameter(new DefaultParameter(PACKAGE_PROPERTY).setValue(group + '.' + artifact));
      }
      String pkg = collection.getParameter(PACKAGE_PROPERTY).getValue();
      if (!JAVA_QUALIFIED_IDENTIFIER.matcher(pkg).matches()) {
         throw new CommandException("Invalid package name: " + pkg);
      }
      
      if (!parameters.containsParameter(CLASSNAME_PROPERTY)) {
         String classname = WordUtils.capitalize(commandName).replaceAll("[^a-zA-Z0-9_$]", "") + "Command";
         collection.addParameter(new DefaultParameter(CLASSNAME_PROPERTY).setValue(classname));
      }
      final String classname = collection.getParameter(CLASSNAME_PROPERTY).getValue();
      if (!JAVA_IDENTIFIER.matcher(classname).matches()) {
         throw new CommandException("Invalid classname for command " + commandName + ": " + classname);
      }

      try {
         GradleSettingsUtilities.addProject(collection);
      } catch (FileUtilitiesException e) {
         logService.warn(getClass(), e, "Unable to add the new project to settings.gradle.");
         throw new CommandException(e);
      }

      final boolean clean;
      if (parameters.containsParameter(CLEAN_PROPERTY)) {
         String value = parameters.getParameter(CLEAN_PROPERTY).getValue();
         switch (value.toLowerCase()) {
         case "true":
            clean = true;
            break;
         case "false":
            clean = false;
            break;
         default:
            throw new CommandException("Invalid value for clean: " + value + ". Expected either true or false.");
         }
      } else {
         clean = false;
      }

      templateService.unpack("JellyFishCommand", collection, outputDirectory, clean);
      logService.info(CreateJellyFishCommandCommand.class, "%s project successfully created", commandName);
   }

   /**
    * Sets log service.
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeLogService")
   public void setLogService(ILogService ref) {
      this.logService = ref;
   }

   /**
    * Remove log service.
    */
   public void removeLogService(ILogService ref) {
      setLogService(null);
   }

   /**
    * Sets prompt user service.
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removePromptService")
   public void setPromptService(IPromptUserService ref) {
      this.promptService = ref;
   }

   /**
    * Remove prompt user service.
    */
   public void removePromptService(IPromptUserService ref) {
      setPromptService(null);
   }

   /**
    * Sets template service.
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeTemplateService")
   public void setTemplateService(ITemplateService ref) {
      this.templateService = ref;
   }

   /**
    * Remove template service.
    */
   public void removeTemplateService(ITemplateService ref) {
      setTemplateService(null);
   }

   /**
    * Create the usage for this command. Some of these parameters are provided in the template.
    *
    * @return the usage.
    */
   private static IUsage createUsage() {
      return new DefaultUsage(
         "Creates a new JellyFish Command project. This requires that a settings.gradle file be present in the output directory. It also requires that the jellyfishAPIVersion be set in the parent build.gradle.",
         new DefaultParameter(CLASSNAME_PROPERTY).setDescription("The name of the class that will be generated. i.e. MyClass").setRequired(false),
         new DefaultParameter(COMMAND_NAME_PROPERTY).setDescription("The name of the command. This should use hyphens and lower case letters. i.e.  my-class").setRequired(false),
         new DefaultParameter(GROUP_ID_PROPERTY).setDescription("The groupId. This is usually similar to com.ngc.myprojectname").setRequired(false),
         new DefaultParameter(ARTIFACT_ID_PROPERTY).setDescription("The artifactId, usually the lowercase version of the classname").setRequired(false),
         new DefaultParameter(PACKAGE_PROPERTY).setDescription("The default package for the classname to reside, usually a combination of the groupId.artifactId").setRequired(false),
         new DefaultParameter(OUTPUT_DIR_PROPERTY).setDescription("The directory to generate the command project").setRequired(false), new DefaultParameter(CLEAN_PROPERTY)
                  .setDescription("If true, recursively deletes the command project (if it already exists), before generating the command project again").setRequired(false));
   }

}
