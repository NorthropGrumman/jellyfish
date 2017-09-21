package com.ngc.seaside.jellyfish.cli.command.createjavaevents;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.service.resource.api.IResourceService;
import com.ngc.seaside.bootstrap.utilities.resource.ITemporaryFileResource;
import com.ngc.seaside.bootstrap.utilities.resource.TemporaryFileResource;
import com.ngc.seaside.command.api.CommandException;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultUsage;
import com.ngc.seaside.command.api.IParameter;
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

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;
import java.util.function.Supplier;

@Component(service = IJellyFishCommand.class)
public class CreateJavaEventsCommand implements IJellyFishCommand {

   private static final IUsage USAGE = createUsage();

   /**
    * The name of the default Velocity template.
    */
   static final String EVENT_SOURCE_VELOCITY_TEMPLATE = "event-source.java.vm";

   /**
    * The property used by the create-domain command for the Velocity template file.
    */
   static final String DOMAIN_TEMPLATE_FILE_PROPERTY = "domainTemplateFile";
   /**
    * The name of the command that is invoked to create the actual project.
    */
   static final String CREATE_DOMAIN_COMMAND_NAME = "create-domain";

   /**
    * The name of the command.
    */
   public static final String NAME = "create-java-events";
   /**
    * The name of the property that defines the event template file.
    */
   public static final String EVENT_TEMPLATE_FILE_PROPERTY = "eventTemplateFile";

   private ILogService logService;
   private IResourceService resourceService;
   private IJellyFishCommandProvider jellyFishCommandProvider;
   private IProjectNamingService projectNamingService;
   private IPackageNamingService packageNamingService;

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
      issueUsageWarnings(commandOptions);
      IModel model = evaluateModelParameter(commandOptions);
      IProjectInformation eventsProjectName = projectNamingService.getEventsProjectName(commandOptions, model);
      String artifactId = eventsProjectName.getArtifactId();
      Function<INamedChild<IPackage>, String> packageGenerator =
            (d) -> packageNamingService.getEventPackageName(commandOptions, d);
      Supplier<IProjectInformation> eventsProjectNamer = () -> eventsProjectName;
      String eventTemplate = evaluateEventTemplate(commandOptions);

      jellyFishCommandProvider.run(CREATE_DOMAIN_COMMAND_NAME, DefaultJellyFishCommandOptions.mergeWith(
            commandOptions,
            new DefaultParameter<>(CommonParameters.ARTIFACT_ID.getName(), artifactId),
            new DefaultParameter<>(DOMAIN_TEMPLATE_FILE_PROPERTY, eventTemplate),
            new DefaultParameter<>(CreateDomainCommand.BUILD_GRADLE_TEMPLATE_PROPERTY,
                                   CreateJavaEventsCommand.class.getPackage().getName()),
            new DefaultParameter<>(CreateDomainCommand.PACKAGE_GENERATOR_PROPERTY, packageGenerator),
            new DefaultParameter<>(CreateDomainCommand.PROJECT_NAMER_PROPERTY, eventsProjectNamer)));
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
    * @param ref the log service
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         unbind = "removeLogService")
   public void setLogService(ILogService ref) {
      this.logService = ref;
   }

   /**
    * Removes log service.
    */
   public void removeLogService(ILogService ref) {
      setLogService(null);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         unbind = "removeResourceService")
   public void setResourceService(IResourceService ref) {
      this.resourceService = ref;
   }

   public void removeResourceService(IResourceService ref) {
      setResourceService(null);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         unbind = "removeJellyFishCommandProvider")
   public void setJellyFishCommandProvider(IJellyFishCommandProvider ref) {
      this.jellyFishCommandProvider = ref;
   }

   public void removeJellyFishCommandProvider(IJellyFishCommandProvider ref) {
      setJellyFishCommandProvider(null);
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

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         unbind = "removePackageNamingService")
   public void setPackageNamingService(IPackageNamingService ref) {
      this.packageNamingService = ref;

   }

   public void removePackageNamingService(IPackageNamingService ref) {
      setPackageNamingService(null);
   }

   private void issueUsageWarnings(IJellyFishCommandOptions commandOptions) {
      // This command will set the value of the domainFile parameter itself, thereby overriding the value provided
      // by the user.  We issue this warning in case the user gets confused.
      if (commandOptions.getParameters().containsParameter(DOMAIN_TEMPLATE_FILE_PROPERTY)) {
         logService.warn(CreateJavaEventsCommand.class,
                         "The parameter '%s' has been set but it will be ignored; did you mean to use '%s' instead?",
                         DOMAIN_TEMPLATE_FILE_PROPERTY,
                         EVENT_TEMPLATE_FILE_PROPERTY);
      }
   }

   private IModel evaluateModelParameter(IJellyFishCommandOptions commandOptions) {
      ISystemDescriptor sd = commandOptions.getSystemDescriptor();
      IParameterCollection parameters = commandOptions.getParameters();
      final String modelName = parameters.getParameter(CommonParameters.MODEL.getName()).getStringValue();
      return sd.findModel(modelName).orElseThrow(() -> new CommandException("Unknown model: " + modelName));
   }

   private String evaluateEventTemplate(IJellyFishCommandOptions options) {
      IParameter<?> eventTemplateParameter = options.getParameters().getParameter(EVENT_TEMPLATE_FILE_PROPERTY);
      String eventTemplate = eventTemplateParameter == null ? null : eventTemplateParameter.getStringValue();
      if (eventTemplate == null) {
         // Unpack the velocity template to a temporary directory.
         ITemporaryFileResource velocityTemplate = TemporaryFileResource.forClasspathResource(
               CreateJavaEventsCommand.class,
               EVENT_SOURCE_VELOCITY_TEMPLATE);
         resourceService.readResource(velocityTemplate);
         eventTemplate = velocityTemplate.getTemporaryFile().toAbsolutePath().toString();
      }
      return eventTemplate;
   }

   /**
    * Create the usage for this command.
    *
    * @return the usage.
    */
   private static IUsage createUsage() {
      return new DefaultUsage(
            "Generate a Gradle project that can generate the event sources as Java types.",
            CommonParameters.GROUP_ID,
            CommonParameters.ARTIFACT_ID,
            CommonParameters.PACKAGE,
            CommonParameters.PACKAGE_SUFFIX,
            CommonParameters.OUTPUT_DIRECTORY.required(),
            CommonParameters.MODEL.required(),
            CommonParameters.CLEAN,

            new DefaultParameter<String>(EVENT_TEMPLATE_FILE_PROPERTY)
                  .setDescription("The velocity template file that will be included in the Gradle project.")
                  .setRequired(false));
   }
}
