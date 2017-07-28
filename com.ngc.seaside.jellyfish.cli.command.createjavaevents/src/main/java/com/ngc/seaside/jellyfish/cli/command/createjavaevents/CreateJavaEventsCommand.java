package com.ngc.seaside.jellyfish.cli.command.createjavaevents;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.service.resource.api.IResourceService;
import com.ngc.seaside.bootstrap.service.promptuser.api.IPromptUserService;
import com.ngc.seaside.bootstrap.utilities.resource.ITemporaryFileResource;
import com.ngc.seaside.bootstrap.utilities.resource.TemporaryFileResource;
import com.ngc.seaside.command.api.CommandException;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultUsage;
import com.ngc.seaside.command.api.IParameter;
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

@Component(service = IJellyFishCommand.class)
public class CreateJavaEventsCommand implements IJellyFishCommand {

   private static final IUsage USAGE = createUsage();

   /**
    * The name of the default Velocity template.
    */
   static final String EVENT_SOURCE_VELOCITY_TEMPLATE = "event-source.java.vm";
   /**
    * The name of the default events artifact ID suffix.
    */
   static final String DEFAULT_ARTIFACT_ID_SUFFIX = "events";

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
   private IPromptUserService promptUserService;
   private IJellyFishCommandProvider jellyFishCommandProvider;

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
      String artifactId = evaluateArtifactId(commandOptions, model);
      String eventTemplate = evaluateEventTemplate(commandOptions);
      jellyFishCommandProvider.run(CREATE_DOMAIN_COMMAND_NAME, DefaultJellyFishCommandOptions.mergeWith(
            commandOptions,
            new DefaultParameter<>(CreateDomainCommand.ARTIFACT_ID_PROPERTY, artifactId),
            new DefaultParameter<>(DOMAIN_TEMPLATE_FILE_PROPERTY, eventTemplate),
            new DefaultParameter<>(CreateDomainCommand.BUILD_GRADLE_TEMPLATE_PROPERTY,
                                   CreateJavaEventsCommand.class.getPackage().getName())));
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
         unbind = "removePromptUserService")
   public void setPromptUserService(IPromptUserService ref) {
      this.promptUserService = ref;
   }

   public void removePromptUserService(IPromptUserService ref) {
      setPromptUserService(null);
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
            "Generate a Gradle project that can generate the event sources as Java types.",
            new DefaultParameter<String>(CreateDomainCommand.GROUP_ID_PROPERTY)
                  .setDescription("The project's group ID")
                  .setRequired(false),
            new DefaultParameter<String>(CreateDomainCommand.ARTIFACT_ID_PROPERTY)
                  .setDescription("The project's version")
                  .setRequired(false),
            new DefaultParameter<String>(CreateDomainCommand.PACKAGE_PROPERTY)
                  .setDescription("The project's default package")
                  .setRequired(false),
            new DefaultParameter<String>(CreateDomainCommand.PACKAGE_SUFFIX_PROPERTY)
                  .setDescription("A string to append to the end of the generated package name")
                  .setRequired(false),
            new DefaultParameter<String>(CreateDomainCommand.OUTPUT_DIRECTORY_PROPERTY)
                  .setDescription("Base directory in which to output the project")
                  .setRequired(true),
            new DefaultParameter<String>(CreateDomainCommand.MODEL_PROPERTY)
                  .setDescription("The fully qualified path to the system descriptor model")
                  .setRequired(true),
            new DefaultParameter<String>(CreateDomainCommand.CLEAN_PROPERTY)
                  .setDescription("If true, recursively deletes the events project (if it already exists), before"
                                  + " generating it again")
                  .setRequired(false),
            new DefaultParameter<String>(EVENT_TEMPLATE_FILE_PROPERTY)
                  .setDescription("The velocity template file that will be included in the Gradle project.")
                  .setRequired(false));
   }
}
