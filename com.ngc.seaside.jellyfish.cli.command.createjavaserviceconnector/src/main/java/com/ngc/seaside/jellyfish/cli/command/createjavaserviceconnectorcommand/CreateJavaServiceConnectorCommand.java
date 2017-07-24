package com.ngc.seaside.jellyfish.cli.command.createjavaserviceconnectorcommand;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.service.promptuser.api.IPromptUserService;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateService;
import com.ngc.seaside.command.api.CommandException;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultParameterCollection;
import com.ngc.seaside.command.api.DefaultUsage;
import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.JellyFishCommandConfiguration;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;

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

@JellyFishCommandConfiguration(autoTemplateProcessing = false)
@Component(service = IJellyFishCommand.class)
public class CreateJavaServiceConnectorCommand implements IJellyFishCommand {

   private static final String NAME = "create-java-service-connector";
   private static final IUsage USAGE = createUsage();

   public static final String OUTPUT_DIRECTORY_PROPERTY = "outputDirectory";
   public static final String MODEL_PROPERTY = "model";
   public static final String CLASS_NAME_PROPERTY = "className";
   public static final String GROUP_ID_PROPERTY = "groupId";
   public static final String ARTIFACT_ID_PROPERTY = "artifactId";
   public static final String PACKAGE_PROPERTY = "package";
   public static final String CLEAN_PROPERTY = "clean";
   private static final Pattern JAVA_QUALIFIED_IDENTIFIER = Pattern
         .compile("[a-zA-Z$_][a-zA-Z$_0-9]*(?:\\.[a-zA-Z$_][a-zA-Z$_0-9]*)*");

   private ILogService logService;
   private IPromptUserService promptService;
   private ITemplateService templateService;

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

      if (!collection.containsParameter(OUTPUT_DIRECTORY_PROPERTY)) {

         String outputDir = promptService.prompt(OUTPUT_DIRECTORY_PROPERTY, "", null);
         collection.addParameter(new DefaultParameter<>(OUTPUT_DIRECTORY_PROPERTY, outputDir));
      }
      final Path outputDirectory = Paths.get(collection.getParameter(OUTPUT_DIRECTORY_PROPERTY).getStringValue());
      System.out.println("Output dir :"  + outputDirectory);
      if (!collection.containsParameter(MODEL_PROPERTY)) {

         String modelId = promptService.prompt(MODEL_PROPERTY, "", null);
         collection.addParameter(new DefaultParameter<>(MODEL_PROPERTY, modelId));
      }

      ISystemDescriptor systemDescriptor = commandOptions.getSystemDescriptor();
      String modelId = collection.getParameter(MODEL_PROPERTY).getStringValue();
      final IModel model = systemDescriptor.findModel(modelId)
            .orElseThrow(() -> new CommandException("Unknown model:" + modelId));
      collection.addParameter(new DefaultParameter<>(CLASS_NAME_PROPERTY, model.getName().concat("Connector")));
      System.out.println("Model Name :"  + model.getName().concat("Connector"));

      if (!collection.containsParameter(GROUP_ID_PROPERTY)) {
         collection.addParameter(new DefaultParameter<>(GROUP_ID_PROPERTY, model.getParent().getName()));
      }
      final String groupId = collection.getParameter(GROUP_ID_PROPERTY).getStringValue();
      System.out.println("Group Id :"  + groupId);
      if (!collection.containsParameter(ARTIFACT_ID_PROPERTY)) {
         String artifact = model.getName().toLowerCase().concat(".connector");
         collection.addParameter(new DefaultParameter<>(ARTIFACT_ID_PROPERTY, artifact));
      }
      final String artifactId = collection.getParameter(ARTIFACT_ID_PROPERTY).getStringValue();
      System.out.println("Artifact Id :"  + artifactId);
      collection.addParameter(new DefaultParameter<>(PACKAGE_PROPERTY, groupId + "." + artifactId));

      try {
         Files.createDirectories(outputDirectory);
      } catch (IOException e) {
         logService.error(CreateJavaServiceConnectorCommand.class, e);
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

      templateService.unpack("JellyFishJavaServiceConnector", collection, outputDirectory, clean);
      logService.info(CreateJavaServiceConnectorCommand.class, "%s project successfully created", modelId);
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
    * Sets prompt user service.
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removePromptService")
   public void setPromptService(IPromptUserService ref) {
      this.promptService = ref;
   }

   /**
    * Remove prompt user service.
    */
   public void removePromptService(IPromptUserService ref) {
      setPromptService(null);
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
      return new DefaultUsage(
            "Creates a new JellyFish Service Connector project.",
            new DefaultParameter(OUTPUT_DIRECTORY_PROPERTY).setDescription("The directory to generate the command project")
                  .setRequired(false),
            new DefaultParameter(MODEL_PROPERTY).setDescription("The fully qualified name of the model to generate connectors for")
                  .setRequired(false),
            new DefaultParameter(GROUP_ID_PROPERTY)
                  .setDescription("The groupId. This is usually similar to com.ngc.myprojectname").setRequired(false),
            new DefaultParameter(ARTIFACT_ID_PROPERTY)
                  .setDescription("The artifactId, usually the lowercase version of the classname").setRequired(false),
            new DefaultParameter(CLEAN_PROPERTY)
                  .setDescription(
                        "If true, recursively deletes the connector project (if it already exists), before generating the connector project again")
                  .setRequired(false));
   }
}
