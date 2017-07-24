package com.ngc.seaside.jellyfish.cli.command.samplecommand;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.utilities.file.FileUtilitiesException;
import com.ngc.seaside.bootstrap.utilities.file.GradleSettingsUtilities;
import com.ngc.seaside.command.api.CommandException;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultUsage;
import com.ngc.seaside.command.api.IParameterCollection;
import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 * The default java bundle built using Gradle. This command does have a template and should be modified
 * to support any structural changes to the project.
 */
@Component(service = IJellyFishCommand.class)
public class SampleCommand implements IJellyFishCommand {

   private static final String NAME = "sample";
   private static final IUsage USAGE = createUsage();

   private static final String OUTPUT_DIR_PROPERTY = "outputDirectory";
   private static final String GROUP_ID_PROPERTY = "groupId";
   private static final String ARTIFACT_ID_PROPERTY = "artifactId";
   private static final String PACKAGE_PROPERTY = "package";
   private static final String CLASSNAME_PROPERTY = "classname";

   private ILogService logService;

   @Activate
   public void activate() {
      logService.trace(getClass(), "Activated");
   }

   @Deactivate
   public void deactivate() {
      logService.trace(getClass(), "Deactivated");
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

      try {
         GradleSettingsUtilities.addProject(parameters);
      } catch (FileUtilitiesException e) {
         logService.warn(getClass(), e, "Unable to add the new project to settings.gradle.");
         throw new CommandException(e);
      }
   }

   /**
    * Create the usage for this command. Some of these parameters are provided in the template.
    *
    * @return the usage.
    */
   private static IUsage createUsage() {
      return new DefaultUsage(
            "Create a new Java bundle. This requires that a settings.gradle file be present in the output directory. See the init-java-gradle-repo command.",
            new DefaultParameter(CLASSNAME_PROPERTY)
                  .setDescription("The name of the class that will be generated. i.e. MyClass")
                  .setRequired(true),
            new DefaultParameter(GROUP_ID_PROPERTY)
                  .setDescription("The groupId. This is usually similar to com.ngc.myprojectname.")
                  .setRequired(true),
            new DefaultParameter(ARTIFACT_ID_PROPERTY)
                  .setDescription("The artifactId. This is usually the lowercase version of the classname.")
                  .setRequired(true),
            new DefaultParameter(PACKAGE_PROPERTY)
                  .setDescription(
                        "The default package for the classname to reside. This is usually a combination of the groupId.artifactId")
                  .setRequired(true),
            new DefaultParameter(OUTPUT_DIR_PROPERTY)
                  .setDescription("The directory in which the bundle has been created.")
                  .setRequired(true));
   }
}
