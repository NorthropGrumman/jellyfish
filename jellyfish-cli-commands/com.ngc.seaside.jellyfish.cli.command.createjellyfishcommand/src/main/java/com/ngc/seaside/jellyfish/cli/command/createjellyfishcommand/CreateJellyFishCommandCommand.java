package com.ngc.seaside.jellyfish.cli.command.createjellyfishcommand;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.CommandException;
import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.DefaultUsage;
import com.ngc.seaside.jellyfish.api.ICommand;
import com.ngc.seaside.jellyfish.api.ICommandOptions;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.service.template.api.ITemplateService;
import com.ngc.seaside.jellyfish.utilities.file.FileUtilitiesException;
import com.ngc.seaside.jellyfish.utilities.file.GradleSettingsUtilities;

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
@Component(service = ICommand.class)
public class CreateJellyFishCommandCommand implements ICommand<ICommandOptions> {

   private static final String NAME = "create-jellyfish-command";
   private static final IUsage USAGE = createUsage();

   private static final Pattern JAVA_IDENTIFIER = Pattern.compile("[a-zA-Z$_][a-zA-Z$_0-9]*");
   private static final Pattern JAVA_QUALIFIED_IDENTIFIER = Pattern
         .compile("[a-zA-Z$_][a-zA-Z$_0-9]*(?:\\.[a-zA-Z$_][a-zA-Z$_0-9]*)*");

   public static final String OUTPUT_DIR_PROPERTY = CommonParameters.OUTPUT_DIRECTORY.getName();
   public static final String GROUP_ID_PROPERTY = CommonParameters.GROUP_ID.getName();
   public static final String ARTIFACT_ID_PROPERTY = CommonParameters.ARTIFACT_ID.getName();
   public static final String PACKAGE_PROPERTY = CommonParameters.PACKAGE.getName();
   public static final String CLASSNAME_PROPERTY = CommonParameters.CLASSNAME.getName();
   public static final String COMMAND_NAME_PROPERTY = "commandName";
   public static final String CLEAN_PROPERTY = CommonParameters.CLEAN.getName();

   static final String DEFAULT_GROUP_ID = "com.ngc.seaside";
   static final String DEFAULT_ARTIFACT_ID_FORMAT = "jellyfish.cli.command.%s";

   private ILogService logService;
   private ITemplateService templateService;

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
   public void run(ICommandOptions commandOptions) {
      DefaultParameterCollection collection = new DefaultParameterCollection();
      collection.addParameters(commandOptions.getParameters().getAllParameters());

      final String commandName = collection.getParameter(COMMAND_NAME_PROPERTY).getStringValue();

      if (!collection.containsParameter(OUTPUT_DIR_PROPERTY)) {
         collection.addParameter(
               new DefaultParameter<>(OUTPUT_DIR_PROPERTY, Paths.get(".").toAbsolutePath().toString()));
      }
      final Path outputDirectory = Paths.get(collection.getParameter(OUTPUT_DIR_PROPERTY).getStringValue());
      try {
         Files.createDirectories(outputDirectory);
      } catch (IOException e) {
         logService.error(CreateJellyFishCommandCommand.class, e);
         throw new CommandException(e);
      }

      if (Files.isDirectory(outputDirectory.resolve("com.ngc.seaside.jellyfish.cli.command.testutils"))) {
         collection.addParameter(new DefaultParameter<>("withCliCommands", "true"));
      } else {
         collection.addParameter(new DefaultParameter<>("withCliCommands", "false"));
      }

      if (!collection.containsParameter(GROUP_ID_PROPERTY)) {
         collection.addParameter(new DefaultParameter<>(GROUP_ID_PROPERTY, DEFAULT_GROUP_ID));
      }
      final String group = collection.getParameter(GROUP_ID_PROPERTY).getStringValue();

      if (!collection.containsParameter(ARTIFACT_ID_PROPERTY)) {
         String artifact = collection.getParameter(COMMAND_NAME_PROPERTY)
               .getStringValue()
               .replace("-", "").toLowerCase();
         collection.addParameter(
               new DefaultParameter<>(ARTIFACT_ID_PROPERTY, String.format(DEFAULT_ARTIFACT_ID_FORMAT, artifact)));
      }
      final String artifact = collection.getParameter(ARTIFACT_ID_PROPERTY).getStringValue();

      if (!collection.containsParameter(PACKAGE_PROPERTY)) {
         collection.addParameter(new DefaultParameter<>(PACKAGE_PROPERTY, group + '.' + artifact));
      }
      String pkg = collection.getParameter(PACKAGE_PROPERTY).getStringValue();
      if (!JAVA_QUALIFIED_IDENTIFIER.matcher(pkg).matches()) {
         throw new CommandException("Invalid package name: " + pkg);
      }

      if (!collection.containsParameter(CLASSNAME_PROPERTY)) {
         String classname = WordUtils.capitalize(commandName, '-').replaceAll("[^a-zA-Z0-9_$]", "") + "Command";
         collection.addParameter(new DefaultParameter<>(CLASSNAME_PROPERTY, classname));
      }
      final String classname = collection.getParameter(CLASSNAME_PROPERTY).getStringValue();
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
      if (collection.containsParameter(CLEAN_PROPERTY)) {
         String value = collection.getParameter(CLEAN_PROPERTY).getStringValue();
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

      templateService.unpack(CreateJellyFishCommandCommand.class.getPackage().getName(),
                             collection,
                             outputDirectory,
                             clean);
   }

   /**
    * Sets log service.
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         unbind = "removeLogService")
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
    * Sets template service.
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         unbind = "removeTemplateService")
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
    * Create the usage for this command.
    *
    * @return the usage.
    */
   @SuppressWarnings("rawtypes")
   private static IUsage createUsage() {
      return new DefaultUsage(
            "Creates a Gradle project for a new JellyFish command. This requires that a settings.gradle file be "
            + "present in the output directory. It also requires that the jellyfishAPIVersion be set in the parent "
            + "build.gradle.",
            CommonParameters.CLASSNAME,
            new DefaultParameter(COMMAND_NAME_PROPERTY)
                  .setDescription(
                        "The name of the command. This should use hyphens and lower case letters. i.e.  my-class")
                  .setRequired(true),
            CommonParameters.GROUP_ID,
            CommonParameters.ARTIFACT_ID,
            CommonParameters.PACKAGE,
            CommonParameters.OUTPUT_DIRECTORY,
            CommonParameters.CLEAN);
   }

}
