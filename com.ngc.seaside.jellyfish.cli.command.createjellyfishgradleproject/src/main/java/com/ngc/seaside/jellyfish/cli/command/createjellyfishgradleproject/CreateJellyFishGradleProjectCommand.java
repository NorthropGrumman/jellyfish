package com.ngc.seaside.jellyfish.cli.command.createjellyfishgradleproject;

import com.google.common.base.Preconditions;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateService;
import com.ngc.seaside.command.api.CommandException;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultParameterCollection;
import com.ngc.seaside.command.api.DefaultUsage;
import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
@Component(service = IJellyFishCommand.class)
public class CreateJellyFishGradleProjectCommand implements IJellyFishCommand {

   private static final String NAME = "create-jellyfish-gradle-project";
   private static final IUsage USAGE = createUsage();

   public static final String OUTPUT_DIR_PROPERTY = CommonParameters.OUTPUT_DIRECTORY.getName();
   public static final String GROUP_ID_PROPERTY = CommonParameters.GROUP_ID.getName();
   public static final String SYSTEM_DESCRIPTOR_GAVE_PROPERTY =
         CommonParameters.GROUP_ARTIFACT_VERSION_EXTENSION.getName();
   public static final String MODEL_NAME_PROPERTY = CommonParameters.MODEL.getName();

   public static final String PROJECT_NAME_PROPERTY = "projectName";
   public static final String VERSION_PROPERTY = "version";
   public static final String DEFAULT_GROUP_ID = "com.ngc.seaside";
   static final String SYSTEM_DESCRIPTOR_ARTIFACT_ID_PROPERTY = "systemDescriptorArtifactId";
   static final String SYSTEM_DESCRIPTOR_VERSION_PROPERTY = "systemDescriptorVersion";
   static final Pattern GAVE_REGEX = Pattern.compile("(.+):(.+):(.+)@(.+)");

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
   public void run(IJellyFishCommandOptions commandOptions) {
      DefaultParameterCollection collection = new DefaultParameterCollection();
      collection.addParameters(commandOptions.getParameters().getAllParameters());

      // Ensure OUTPUT_DIR_PROPERTY parameter is set
      if (!collection.containsParameter(OUTPUT_DIR_PROPERTY)) {
         collection.addParameter(new DefaultParameter<>(OUTPUT_DIR_PROPERTY)
                                       .setValue(
                                             Paths.get(".")
                                                   .toAbsolutePath()
                                                   .toString()));
      }

      final String projectName = collection.getParameter(PROJECT_NAME_PROPERTY).getStringValue();

      // Create project directorythreatevaluation
      final Path outputDirectory = Paths.get(collection.getParameter(OUTPUT_DIR_PROPERTY).getStringValue());
      final Path projectDirectory = outputDirectory.resolve(projectName);
      try {
         Files.createDirectories(projectDirectory);
      } catch (IOException e) {
         logService.error(CreateJellyFishGradleProjectCommand.class, e);
         throw new CommandException(e);
      }

      // Ensure GROUP_ID_PROPERTY parameter is set
      if (!collection.containsParameter(GROUP_ID_PROPERTY)) {
         collection.addParameter(new DefaultParameter<>(GROUP_ID_PROPERTY).setValue(DEFAULT_GROUP_ID));
      }

      // parse the parameters to get the system descriptor artifact id
      String gaveStr = collection.getParameter(SYSTEM_DESCRIPTOR_GAVE_PROPERTY).getStringValue();
      Matcher matcher = GAVE_REGEX.matcher(gaveStr);
      Preconditions.checkArgument(matcher.matches(),
                                  "GAVE string must be of the format group:artifact:version@extension, got %s!",
                                  gaveStr);
      collection.addParameter(new DefaultParameter<>(SYSTEM_DESCRIPTOR_ARTIFACT_ID_PROPERTY, matcher.group(2)));
      collection.addParameter(new DefaultParameter<>(SYSTEM_DESCRIPTOR_VERSION_PROPERTY, matcher.group(3)));

      boolean clean = CommonParameters.evaluateBooleanParameter(collection, CommonParameters.CLEAN.getName(), false);
      String templateName = CreateJellyFishGradleProjectCommand.class.getPackage().getName();
      templateService.unpack(templateName, collection, projectDirectory, clean);

      logService.info(getClass(), "%s project successfully created", projectName);
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
    * Create the usage for this command.
    *
    * @return the usage.
    */
   private static IUsage createUsage() {
      return new DefaultUsage(
            "Creates a new JellyFish Gradle project. This requires that a settings.gradle file be present in the output directory. It also requires that the jellyfishAPIVersion be set in the parent build.gradle.",
            CommonParameters.OUTPUT_DIRECTORY.required(),
            new DefaultParameter<>(PROJECT_NAME_PROPERTY)
                  .setDescription(
                        "The name of the Gradle project. This should use hyphens and lower case letters. i.e.  my-project")
                  .setRequired(false),
            CommonParameters.GROUP_ID,
            new DefaultParameter<>(VERSION_PROPERTY)
                  .setDescription("The version to use for the Gradle project")
                  .setRequired(true),
            CommonParameters.GROUP_ARTIFACT_VERSION_EXTENSION.required(),
            CommonParameters.MODEL.required(),
            CommonParameters.CLEAN);
   }

}
