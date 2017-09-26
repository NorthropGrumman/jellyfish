package com.ngc.seaside.jellyfish.cli.command.createprotocolbuffermessages;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.service.resource.api.IResourceService;
import com.ngc.seaside.bootstrap.utilities.resource.ITemporaryFileResource;
import com.ngc.seaside.bootstrap.utilities.resource.TemporaryFileResource;
import com.ngc.seaside.command.api.CommandException;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultUsage;
import com.ngc.seaside.command.api.IParameterCollection;
import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.DefaultJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandProvider;
import com.ngc.seaside.jellyfish.cli.command.createdomain.CreateDomainCommand;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.jellyfish.service.name.api.IProjectInformation;
import com.ngc.seaside.jellyfish.service.name.api.IProjectNamingService;
import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.util.function.Function;
import java.util.function.Supplier;

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
   static final String DEFAULT_EXT_PROPERTY = "proto";
   static final IUsage USAGE = createUsage();

   private ILogService logService;
   private IJellyFishCommandProvider jellyfishCommandProvider;
   private IResourceService resourceService;
   private IPackageNamingService packageNamingService;
   private IProjectNamingService projectNamingService;

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
      IProjectInformation messageProjectName = projectNamingService.getMessageProjectName(commandOptions, model);
      String artifactId = messageProjectName.getArtifactId();
      
      // Unpack the velocity template to a temporary directory.
      final ITemporaryFileResource velocityTemplate = TemporaryFileResource.forClasspathResource(
         CreateProtocolbufferMessagesCommand.class,
         TEMPLATE_FILE);
      resourceService.readResource(velocityTemplate);
      final String domainTemplate = velocityTemplate.getTemporaryFile().toAbsolutePath().toString();

      Function<INamedChild<IPackage>, String> packageGenerator =
            (d) -> packageNamingService.getMessagePackageName(commandOptions, d);
      Supplier<IProjectInformation> messageProjectNamer = () -> messageProjectName;
      
      jellyfishCommandProvider.run(CREATE_DOMAIN_COMMAND,
         DefaultJellyFishCommandOptions.mergeWith(commandOptions,
            new DefaultParameter<>(CreateDomainCommand.DOMAIN_TEMPLATE_FILE_PROPERTY, domainTemplate),
            new DefaultParameter<>(CreateDomainCommand.ARTIFACT_ID_PROPERTY, artifactId),
            new DefaultParameter<>(CreateDomainCommand.PACKAGE_GENERATOR_PROPERTY, packageGenerator),
            new DefaultParameter<>(CreateDomainCommand.EXTENSION_PROPERTY, DEFAULT_EXT_PROPERTY),
            new DefaultParameter<>(CreateDomainCommand.BUILD_GRADLE_TEMPLATE_PROPERTY,
               CreateProtocolbufferMessagesCommand.class.getPackage().getName()),
            new DefaultParameter<>(CreateDomainCommand.USE_VERBOSE_IMPORTS_PROPERTY, true),
            new DefaultParameter<>(CreateDomainCommand.PROJECT_NAMER_PROPERTY, messageProjectNamer)));
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

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.STATIC,
            unbind = "removePackageNamingService")
   public void setPackageNamingService(IPackageNamingService ref) {
         this.packageNamingService = ref;
   }
   

   public void removePackageNamingService(IPackageNamingService ref) {
      setPackageNamingService(null);
   }
   
   @Reference(cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.STATIC,
            unbind = "removeProjectNamingService")
   public void setProjectNamingService(IProjectNamingService ref) {
         this.projectNamingService = ref;
   }
   
   public void removeProjectNamingService(IProjectNamingService ref) {
      setProjectNamingService(null);
   }

   private IModel evaluateModelParameter(IJellyFishCommandOptions commandOptions) {
      ISystemDescriptor sd = commandOptions.getSystemDescriptor();
      IParameterCollection parameters = commandOptions.getParameters();
      final String modelName = parameters.getParameter(CreateDomainCommand.MODEL_PROPERTY).getStringValue();
      return sd.findModel(modelName).orElseThrow(() -> new CommandException("Unknown model: " + modelName));
   }

   /**
    * Create the usage for this command.
    *
    * @return the usage.
    */
   private static IUsage createUsage() {
      return new DefaultUsage("Generate the message IDL and gradle project structure that can generate the protocol buffer message bundle.",
         CommonParameters.GROUP_ID,
         CommonParameters.ARTIFACT_ID,
         CommonParameters.PACKAGE,
         CommonParameters.PACKAGE_SUFFIX,
         CommonParameters.OUTPUT_DIRECTORY.required(),
         CommonParameters.MODEL.required(),
         CommonParameters.CLEAN);
   }
}
