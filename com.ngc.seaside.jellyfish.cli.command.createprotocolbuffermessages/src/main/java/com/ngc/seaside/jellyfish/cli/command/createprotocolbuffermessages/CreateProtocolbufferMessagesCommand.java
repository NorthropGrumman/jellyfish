package com.ngc.seaside.jellyfish.cli.command.createprotocolbuffermessages;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.service.resource.api.IResourceService;
import com.ngc.seaside.bootstrap.service.promptuser.api.IPromptUserService;
import com.ngc.seaside.bootstrap.utilities.resource.ITemporaryFileResource;
import com.ngc.seaside.bootstrap.utilities.resource.TemporaryFileResource;
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
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 * This command generates the message IDL and gradle project structure that will
 * produce the protocol buffer message bundle.
 *
 * @author bperkins
 */
@Component(service = IJellyFishCommand.class)
public class CreateProtocolbufferMessagesCommand implements IJellyFishCommand {

   static final String NAME = "create-protocolbuffer-messages";
   static final String CREATE_DOMAIN_COMMAND = "create-domain";
   static final String TEMPLATE_FILE = "proto-messages.vm";
   static final String DEFAULT_ARTIFACT_ID_SUFFIX = "messages";
   static final String DEFAULT_EXT_PROPERTY = "proto";
   static final IUsage USAGE = createUsage();

   private ILogService logService;
   private IJellyFishCommandProvider jellyfishCommandProvider;
   private IResourceService resourceService;
   private IPromptUserService promptUserService;

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
      final IModel model = evaluateModelParameter(commandOptions);
      final String artifactId = evaluateArtifactId(commandOptions, model);

      // Unpack the velocity template to a temporary directory.
      final ITemporaryFileResource velocityTemplate = TemporaryFileResource.forClasspathResource(
         CreateProtocolbufferMessagesCommand.class,
         TEMPLATE_FILE);
      resourceService.readResource(velocityTemplate);
      final String domainTemplate = velocityTemplate.getTemporaryFile().toAbsolutePath().toString();

      jellyfishCommandProvider.run(CREATE_DOMAIN_COMMAND,
         DefaultJellyFishCommandOptions.mergeWith(commandOptions,
            new DefaultParameter<>(CreateDomainCommand.DOMAIN_TEMPLATE_FILE_PROPERTY, domainTemplate),
            new DefaultParameter<>(CreateDomainCommand.USE_MODEL_STRUCTURE_PROPERTY, "true"),
            new DefaultParameter<>(CreateDomainCommand.ARTIFACT_ID_PROPERTY, artifactId),
            new DefaultParameter<>(CreateDomainCommand.EXTENSION_PROPERTY, DEFAULT_EXT_PROPERTY),
            new DefaultParameter<>(CreateDomainCommand.BUILD_GRADLE_TEMPLATE_PROPERTY,
               CreateProtocolbufferMessagesCommand.class.getPackage().getName()),
            new DefaultParameter<>(CreateDomainCommand.USE_VERBOSE_IMPORTS_PROPERTY, true)));
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
    * Sets the resource service
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeResourceService")
   public void setResourceService(IResourceService ref) {
      this.resourceService = ref;
   }

   /**
    * Remove the resource service
    */
   public void removeResourceService(IResourceService ref) {
      setResourceService(null);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removePromptUserService")
   public void setPromptUserService(IPromptUserService ref) {
      this.promptUserService = ref;
   }

   public void removePromptUserService(IPromptUserService ref) {
      setPromptUserService(null);
   }

   private IModel evaluateModelParameter(IJellyFishCommandOptions commandOptions) {
      ISystemDescriptor sd = commandOptions.getSystemDescriptor();
      IParameterCollection parameters = commandOptions.getParameters();
      final String modelName;
      if (parameters.containsParameter(CreateDomainCommand.MODEL_PROPERTY)) {
         modelName = parameters.getParameter(CreateDomainCommand.MODEL_PROPERTY).getStringValue();
      } else {
         modelName = promptUserService.prompt(CreateDomainCommand.MODEL_PROPERTY,
            null,
            m -> commandOptions.getSystemDescriptor().findModel(m).isPresent());
      }
      return sd.findModel(modelName).orElseThrow(() -> new CommandException("Unknown model: " + modelName));
   }

   private String evaluateArtifactId(IJellyFishCommandOptions options, IModel model) {
      final String artifactId;
      if (options.getParameters().containsParameter(CreateDomainCommand.ARTIFACT_ID_PROPERTY)) {
         artifactId = options.getParameters().getParameter(CreateDomainCommand.ARTIFACT_ID_PROPERTY).getStringValue();
      } else {
         artifactId = model.getName().toLowerCase() + "." + DEFAULT_ARTIFACT_ID_SUFFIX;
      }
      return artifactId;
   }

   /**
    * Create the usage for this command.
    *
    * @return the usage.
    */
   private static IUsage createUsage() {
      return new DefaultUsage(
         "Generate the message IDL and gradle project structure that can generate the protocol buffer message bundle.",
         new DefaultParameter<String>(CreateDomainCommand.GROUP_ID_PROPERTY).setDescription("The project's group ID")
                                                                            .setRequired(false),
         new DefaultParameter<String>(CreateDomainCommand.ARTIFACT_ID_PROPERTY).setDescription("The project's version")
                                                                               .setRequired(false),
         new DefaultParameter<String>(CreateDomainCommand.PACKAGE_PROPERTY)
                                                                           .setDescription(
                                                                              "The project's default package")
                                                                           .setRequired(false),
         new DefaultParameter<String>(CreateDomainCommand.PACKAGE_SUFFIX_PROPERTY)
                                                                                  .setDescription(
                                                                                     "A string to append to the end of the generated package name")
                                                                                  .setRequired(false),
         new DefaultParameter<String>(CreateDomainCommand.OUTPUT_DIRECTORY_PROPERTY)
                                                                                    .setDescription(
                                                                                       "Base directory in which to output the project")
                                                                                    .setRequired(true),
         new DefaultParameter<String>(CreateDomainCommand.MODEL_PROPERTY)
                                                                         .setDescription(
                                                                            "The fully qualified path to the system descriptor model")
                                                                         .setRequired(true),
         new DefaultParameter<String>(CreateDomainCommand.CLEAN_PROPERTY)
                                                                         .setDescription(
                                                                            "If true, recursively deletes the domain project (if it already exists), before generating the it again")
                                                                         .setRequired(false));
   }
}
