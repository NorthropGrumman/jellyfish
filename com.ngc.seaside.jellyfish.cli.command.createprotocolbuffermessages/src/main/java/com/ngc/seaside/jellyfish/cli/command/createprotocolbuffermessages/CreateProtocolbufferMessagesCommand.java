package com.ngc.seaside.jellyfish.cli.command.createprotocolbuffermessages;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.command.api.CommandException;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultUsage;
import com.ngc.seaside.command.api.IParameterCollection;
import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.DefaultJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandProvider;
import com.ngc.seaside.jellyfish.cli.command.createdomain.CreateDomainCommand;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component(service = IJellyFishCommand.class)
public class CreateProtocolbufferMessagesCommand implements IJellyFishCommand {

   private static final String NAME = "create-protocolbuffer-messages";
   private static final String CREATE_DOMAIN_COMMAND = "create-domain";
   private static final String TEMPLATE_FILE = "proto-messages.vm";
   private static final String DEFAULT_PACKAGE_SUFFIX = "messages";
   private static final String DEFAULT_EXT_PROPERTY = "proto";
   private static final IUsage USAGE = createUsage();

   private ILogService logService;
   private IJellyFishCommandProvider jellyfishCommandProvider;

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
      final IParameterCollection parameters = commandOptions.getParameters();
      final String pkgSuffix = evaluatePackageSuffix(parameters);

      jellyfishCommandProvider.run(CREATE_DOMAIN_COMMAND,
         DefaultJellyFishCommandOptions.mergeWith(commandOptions,
            new DefaultParameter<String>(CreateDomainCommand.DOMAIN_TEMPLATE_FILE_PROPERTY, TEMPLATE_FILE),
            new DefaultParameter<String>(CreateDomainCommand.USE_MODEL_STRUCTURE_PROPERTY, "true"),
            new DefaultParameter<String>(CreateDomainCommand.PACKAGE_SUFFIX_PROPERTY).setValue(pkgSuffix),
            new DefaultParameter<String>(CreateDomainCommand.EXTENSION_PROPERTY).setValue(DEFAULT_EXT_PROPERTY),
            new DefaultParameter<String>(CreateDomainCommand.BUILD_GRADLE_TEMPLATE_PROPERTY)
                     .setValue(CreateProtocolbufferMessagesCommand.class.getPackage().getName())));
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
    * Sets the JellyFish Command Provider
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeJellyFishCommandProvider")
   public void setJellyFishCommandProvider(IJellyFishCommandProvider ref) {
      this.jellyfishCommandProvider = ref;
   }

   /**
    * Remove the JellyFish Command Provider
    */
   public void removeJellyFishCommandProvider(IJellyFishCommandProvider ref) {
      setJellyFishCommandProvider(null);
   }

   /**
    * Returns the package for the domain project.
    * 
    * @param parameters command parameters
    * @param groupId domain groupId
    * @param artifactId domain artifactId
    * @return the package for the domain project
    */
   private static String evaluatePackageSuffix(IParameterCollection parameters) {
      String pkgSuffix = DEFAULT_PACKAGE_SUFFIX;
      if (parameters.containsParameter(CreateDomainCommand.PACKAGE_SUFFIX_PROPERTY)) {
         pkgSuffix = parameters.getParameter(CreateDomainCommand.PACKAGE_SUFFIX_PROPERTY).getStringValue().trim();
      }
      return pkgSuffix;
   }

   /**
    * Create the usage for this command.
    *
    * @return the usage.
    */
   private static IUsage createUsage() {
      return new DefaultUsage("Generate a gradle project that can generate the protocol buffer message bundle.",
         new DefaultParameter<String>(CreateDomainCommand.GROUP_ID_PROPERTY).setDescription("The project's group ID")
                  .setRequired(false),
         new DefaultParameter<String>(CreateDomainCommand.ARTIFACT_ID_PROPERTY).setDescription("The project's version")
                  .setRequired(false),
         new DefaultParameter<String>(CreateDomainCommand.PACKAGE_PROPERTY)
                  .setDescription("The project's default package").setRequired(false),
         new DefaultParameter<String>(CreateDomainCommand.PACKAGE_SUFFIX_PROPERTY)
                  .setDescription("A string to append to the end of the generated package name").setRequired(false),
         new DefaultParameter<String>(CreateDomainCommand.OUTPUT_DIRECTORY_PROPERTY)
                  .setDescription("Base directory in which to output the project").setRequired(true),
         new DefaultParameter<String>(CreateDomainCommand.MODEL_PROPERTY)
                  .setDescription("The fully qualified path to the system descriptor model").setRequired(true),
         new DefaultParameter<String>(CreateDomainCommand.CLEAN_PROPERTY)
                  .setDescription(
                     "If true, recursively deletes the domain project (if it already exists), before generating the it again")
                  .setRequired(false),
         new DefaultParameter<String>(CreateDomainCommand.BUILD_GRADLE_TEMPLATE_PROPERTY)
                  .setDescription("Name of template used to generate the domain projects build.gradle")
                  .setRequired(false));
   }
}
