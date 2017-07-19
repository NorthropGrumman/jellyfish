package com.ngc.seaside.jellyfish.cli.command.createdomain;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultUsage;
import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component(service = IJellyFishCommand.class)
public class CreateDomainCommand implements IJellyFishCommand {

   private static final String NAME = "create-domain";
   private static final IUsage USAGE = createUsage();

   public static final String EXAMPLE_PROPERTY = "example";

   public static final String GROUP_ID_PROPERTY = "groupId";
   public static final String ARTIFACT_ID_PROPERTY = "artifactId";
   public static final String VERSION_PROPERTY = "version";
   public static final String PACKAGE_PROPERTY = "package";
   public static final String PACKAGE_SUFFIX_PROPERTY = "packageSuffix";
   public static final String OUTPUT_DIRECTORY_PROPERTY = "outputDirectory";
   public static final String DOMAIN_TEMPLATE_FILE_PROPERTY = "domainTemplateFile";
   public static final String MODEL_PROPERTY = "model";
   public static final String USE_MODEL_STRUCTURE_PROPERTY = "useModelStructure";
   public static final String IGNORE_STEREOTYPES_PROPERTY = "ignoreStereoTypes";
   public static final String STEREOTYPES_PROPERTY = "stereoTypes";

   private ILogService logService;

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
      // TODO Auto-generated method stub
   }

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
    * Create the usage for this command.
    *
    * @return the usage.
    */
   private static IUsage createUsage() {
      return new DefaultUsage("Generate a BLoCS domain model gradle project.",
         new DefaultParameter(GROUP_ID_PROPERTY).setDescription("The project's group ID").setRequired(false),
         new DefaultParameter(ARTIFACT_ID_PROPERTY).setDescription("The project's version").setRequired(false),
         new DefaultParameter(VERSION_PROPERTY)
                  .setDescription(
                     "The project's artifactId (this argument may only be used if the model argument is set)")
                  .setRequired(false),
         new DefaultParameter(PACKAGE_PROPERTY)
                  .setDescription(
                     "The project's default package (this argument may only be used if the model argument is set)")
                  .setRequired(false),
         new DefaultParameter(PACKAGE_SUFFIX_PROPERTY)
                  .setDescription("A string to append to the end of the generated package name").setRequired(false),
         new DefaultParameter(OUTPUT_DIRECTORY_PROPERTY).setDescription("Base directory in which to output the file(s)")
                  .setRequired(true),
         new DefaultParameter(DOMAIN_TEMPLATE_FILE_PROPERTY).setDescription("The velocity template file")
                  .setRequired(true),
         new DefaultParameter(MODEL_PROPERTY)
                  .setDescription(
                     "The fully qualified path to the system descriptor model. If set it would only generate the input and output data for that model")
                  .setRequired(false),
         new DefaultParameter(USE_MODEL_STRUCTURE_PROPERTY)
                  .setDescription(
                     "If true, the system descriptor model's structural decomposition is used when outputting the domain model project")
                  .setRequired(false),
         new DefaultParameter(IGNORE_STEREOTYPES_PROPERTY)
                  .setDescription(
                     "Comma-separated string of the stereotypes to ignore when generating the domain model (e.g. \"virtual, system\")")
                  .setRequired(false),
         new DefaultParameter(STEREOTYPES_PROPERTY)
                  .setDescription(
                     "Comma-separated string of the stereotypes in which the domain should only be generated")
                  .setRequired(false));
   }

}
