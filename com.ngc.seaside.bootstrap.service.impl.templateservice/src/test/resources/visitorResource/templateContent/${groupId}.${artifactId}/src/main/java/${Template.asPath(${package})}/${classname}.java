package ${package};

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.api.IBootstrapCommand;
import com.ngc.seaside.bootstrap.api.IBootstrapCommandOptions;
import com.ngc.seaside.bootstrap.utilites.file.FileUtilitiesException;
import com.ngc.seaside.bootstrap.utilites.file.GradleSettingsUtilities;
import com.ngc.seaside.command.api.CommandException;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultUsage;
import com.ngc.seaside.command.api.IParameterCollection;
import com.ngc.seaside.command.api.IUsage;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 *
 */
@Component(service = IBootstrapCommand.class)
public class ${classname} implements IBootstrapCommand {

   private static final String NAME = "${commandName}";
   private static final IUsage USAGE = createUsage();

   private static final String OUTPUT_DIR_PROPERTY = "outputDirectory";
   private static final String GROUP_ID_PROPERTY = "groupId";
   private static final String ARTIFACT_ID_PROPERTY = "artifactId";
   private static final String PACKAGE_PROPERTY = "package";
   private static final String CLASSNAME_PROPERTY = "classname";

   private ILogService logService;

   @Activate
   public void activate() {
      logService.info(getClass(), "activated");
   }

   @Deactivate
   public void deactivate() {
      logService.info(getClass(), "deactivated");
   }

   @Reference(
       unbind="removeLogService",
       cardinality = ReferenceCardinality.MANDATORY,
       policy = ReferencePolicy.STATIC)
   public void setLogService(ILogService ref) {
      this.logService = ref;
   }

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
   public void run(IBootstrapCommandOptions commandOptions) {
      IParameterCollection parameters = commandOptions.getParameters();

      try {
         //adds the new project to the settings.gradle file located in the outputDirectory
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
         //TODO Fix the description
         return new DefaultUsage(
         "The ${commandName} ... fill in the blank.",
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
               .setDescription("The default package for the classname to reside. This is usually a combination of the groupId.artifactId")
               .setRequired(true),
            new DefaultParameter(OUTPUT_DIR_PROPERTY)
               .setDescription("The directory in which the bundle has been created.")
               .setRequired(true));
   }
}
