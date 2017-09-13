package com.ngc.seaside.jellyfish.cli.command.createjavapubsubconnector;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.service.promptuser.api.IPromptUserService;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateService;
import com.ngc.seaside.bootstrap.utilities.file.FileUtilitiesException;
import com.ngc.seaside.bootstrap.utilities.file.GradleSettingsUtilities;
import com.ngc.seaside.command.api.CommandException;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultParameterCollection;
import com.ngc.seaside.command.api.DefaultUsage;
import com.ngc.seaside.command.api.IParameterCollection;
import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.createjavapubsubconnector.dto.ConnectorDto;
import com.ngc.seaside.jellyfish.service.config.api.ITransportConfigurationService;
import com.ngc.seaside.jellyfish.service.requirements.api.IRequirementsService;
import com.ngc.seaside.jellyfish.service.scenario.api.IPublishSubscribeMessagingFlow;
import com.ngc.seaside.jellyfish.service.scenario.api.IScenarioService;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;

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
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

@Component(service = IJellyFishCommand.class)
public class CreateJavaPubsubConnectorCommand implements IJellyFishCommand {

   private static final String NAME = "create-java-pubsub-connector";
   private static final IUsage USAGE = createUsage();

   public static final String OUTPUT_DIRECTORY_PROPERTY = "outputDirectory";
   public static final String MODEL_PROPERTY = "model";
   public static final String GROUP_ID_PROPERTY = "groupId";
   public static final String ARTIFACT_ID_PROPERTY = "artifactId";

   public static final String MODEL_OBJECT_PROPERTY = "modelObject";
   public static final String PACKAGE_PROPERTY = "package";
   public static final String CLEAN_PROPERTY = "clean";

   private ILogService logService;
   private IPromptUserService promptService;
   private ITemplateService templateService;
   private IScenarioService scenarioService;
   private ITransportConfigurationService transportConfigService;
   private IRequirementsService requirementsService;

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
      DefaultParameterCollection parameters = new DefaultParameterCollection();
      parameters.addParameters(commandOptions.getParameters().getAllParameters());

      if (!parameters.containsParameter(OUTPUT_DIRECTORY_PROPERTY)) {

         String outputDir = promptService.prompt(OUTPUT_DIRECTORY_PROPERTY, null, null);
         parameters.addParameter(new DefaultParameter<>(OUTPUT_DIRECTORY_PROPERTY, outputDir));
      }
      final Path outputDirectory = Paths.get(parameters.getParameter(OUTPUT_DIRECTORY_PROPERTY).getStringValue());

      if (!parameters.containsParameter(MODEL_PROPERTY)) {

         String modelId = promptService.prompt(MODEL_PROPERTY, null, null);
         parameters.addParameter(new DefaultParameter<>(MODEL_PROPERTY, modelId));
      }
      final String modelId = parameters.getParameter(MODEL_PROPERTY).getStringValue();

      if (!parameters.containsParameter(MODEL_OBJECT_PROPERTY)) {

         IModel model = commandOptions.getSystemDescriptor().findModel(modelId).orElseThrow(
            () -> new CommandException("Unknown model:" + modelId));
         parameters.addParameter(new DefaultParameter<>(MODEL_OBJECT_PROPERTY, model));
      }
      final IModel model = commandOptions.getSystemDescriptor().findModel(modelId).orElseThrow(
         () -> new CommandException("Unknown model:" + modelId));

      if (!parameters.containsParameter(GROUP_ID_PROPERTY)) {
         parameters.addParameter(new DefaultParameter<>(GROUP_ID_PROPERTY, model.getParent().getName()));
      }
      final String groupId = parameters.getParameter(GROUP_ID_PROPERTY).getStringValue();

      if (!parameters.containsParameter(ARTIFACT_ID_PROPERTY)) {
         String artifact = model.getName().toLowerCase().concat(".connector");
         parameters.addParameter(new DefaultParameter<String>(ARTIFACT_ID_PROPERTY, artifact));
      }
      final String artifactId = parameters.getParameter(ARTIFACT_ID_PROPERTY).getStringValue();
      final String packageName = groupId + "." + artifactId;
      parameters.addParameter(new DefaultParameter<String>(PACKAGE_PROPERTY, packageName));

      try {
         Files.createDirectories(outputDirectory);
      } catch (IOException e) {
         logService.error(CreateJavaPubsubConnectorCommand.class, e);
         throw new CommandException(e);
      }

      final boolean clean;
      if (parameters.containsParameter(CLEAN_PROPERTY)) {
         String value = parameters.getParameter(CLEAN_PROPERTY).getStringValue();
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
         clean = true;
      }

      doAddProject(parameters);

      ConnectorDto dto = new ConnectorDto();

      final String projectName = packageName;
      final String basePackageName = stripTrailingString(packageName, ".connector");
      final String baseArtifactId = stripTrailingString(artifactId, ".connector");

      dto.setProjectName(projectName);
      dto.setPackageName(packageName);
      dto.setModel(model);
      dto.setGroupId(groupId);
      dto.setArtifactId(artifactId);
      dto.setBasePackage(basePackageName);
      dto.setBaseArtifactId(baseArtifactId);

      evaluateIO(commandOptions, dto);

      parameters.addParameter(new DefaultParameter<>("dto", dto));

      templateService.unpack(CreateJavaPubsubConnectorCommand.class.getPackage().getName(),
         parameters,
         outputDirectory,
         clean);
      logService.info(CreateJavaPubsubConnectorCommand.class, "%s project successfully created", modelId);
   }

   private void evaluateIO(IJellyFishCommandOptions options, ConnectorDto dto) {

      IModel model = dto.getModel();

      Set<IData> inputs = new LinkedHashSet<>();
      Set<IEnumeration> inputEnums = new LinkedHashSet<>();
      Set<IData> outputs = new LinkedHashSet<>();
      Set<IEnumeration> outputEnums = new LinkedHashSet<>();

      Map<String, IData> inputTopics = new TreeMap<>();
      Map<String, IData> outputTopics = new TreeMap<>();
      Map<String, Set<String>> topicRequirements = new TreeMap<>();

      final Set<String> modelRequirements = requirementsService.getRequirements(options, model);
      for (IScenario scenario : model.getScenarios()) {

         final Set<String> scenarioRequirements = requirementsService.getRequirements(options, scenario);

         for (IPublishSubscribeMessagingFlow flow : scenarioService.getPubSubMessagingFlows(options, scenario)) {

            for (IDataReferenceField input : flow.getInputs()) {
               final IData type = input.getType();
               inputs.add(type);

               final String topic = transportConfigService.getTransportTopicName(flow, input);
               IData previous = inputTopics.put(topic, type);
               if (previous != null && !previous.equals(type)) {
                  throw new IllegalStateException(String.format("Conflicting data types for topic <%s>: %s and %s",
                     topic,
                     previous.getClass(),
                     type.getClass()));
               }

               final Set<String> requirements = topicRequirements.computeIfAbsent(topic, t -> new TreeSet<>());
               requirements.addAll(requirementsService.getRequirements(options, input));
               requirements.addAll(requirementsService.getRequirements(options, type));
               requirements.addAll(scenarioRequirements);
               requirements.addAll(modelRequirements);

            }

            for (IDataReferenceField output : flow.getOutputs()) {
               final IData type = output.getType();
               outputs.add(type);

               final String topic = transportConfigService.getTransportTopicName(flow, output);
               IData previous = outputTopics.put(topic, type);
               if (previous != null && !previous.equals(type)) {
                  throw new IllegalStateException(String.format("Conflicting data types for topic <%s>: %s and %s",
                     topic,
                     previous.getClass(),
                     type.getClass()));
               }

               final Set<String> requirements = topicRequirements.computeIfAbsent(topic, t -> new TreeSet<>());
               requirements.addAll(requirementsService.getRequirements(options, output));
               requirements.addAll(requirementsService.getRequirements(options, type));
               requirements.addAll(scenarioRequirements);
               requirements.addAll(modelRequirements);
            }

         }

      }

      addNestedData(inputs, inputEnums);
      addNestedData(outputs, outputEnums);

      dto.setAllInputData(inputs);
      dto.setAllInputEnums(inputEnums);
      dto.setAllOutputData(outputs);
      dto.setAllOutputEnums(outputEnums);
      dto.setInputTopics(inputTopics);
      dto.setOutputTopics(outputTopics);
      dto.setTopicRequirements(topicRequirements);
   }

   private static void addNestedData(Set<IData> data, Set<IEnumeration> enums) {
      Queue<IData> queue = new ArrayDeque<>(data);
      Set<IData> superTypes = new HashSet<>();
      while (!queue.isEmpty()) {
         IData datum = queue.poll();
         for (IDataField field : datum.getFields()) {
            switch (field.getType()) {
            case DATA:
               if (data.add(field.getReferencedDataType())) {
                  queue.add(field.getReferencedDataType());
               }
               break;
            case ENUM:
               enums.add(field.getReferencedEnumeration());
               break;
            default:
               // Ignore primitive field types
               break;
            }
         }
         
         while (datum.getSuperDataType().isPresent()) {
            datum = datum.getSuperDataType().get();
            if (superTypes.add(datum)) {
               queue.add(datum);
            }
         }
      }
   }

   private String stripTrailingString(final String sourceStr, final String trailingStringToRemove) {
      String retStr = sourceStr;

      if (sourceStr.endsWith(trailingStringToRemove)) {
         final int sourceStrLen = sourceStr.length();
         final int removeStringLen = trailingStringToRemove.length();
         final int trimmedStrLen = sourceStrLen - removeStringLen;

         retStr = sourceStr.substring(0, trimmedStrLen);
      }

      return retStr;
   }

   protected void doAddProject(IParameterCollection parameters) {
      try {
         if (!GradleSettingsUtilities.tryAddProject(parameters)) {
            logService.warn(getClass(), "Unable to add the new project to settings.gradle.");
         }
      } catch (FileUtilitiesException e) {
         logService.warn(getClass(), e, "Unable to add the new project to settings.gradle.");
         throw new CommandException(e);
      }
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
    * Sets scenario service.
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeScenarioService")
   public void setScenarioService(IScenarioService ref) {
      this.scenarioService = ref;
   }

   /**
    * Remove scenario service.
    */
   public void removeScenarioService(IScenarioService ref) {
      setLogService(null);
   }

   /**
    * Sets transport configuration service.
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeTransportConfigurationService")
   public void setTransportConfigurationService(ITransportConfigurationService ref) {
      this.transportConfigService = ref;
   }

   /**
    * Remove transport configuration service.
    */
   public void removeTransportConfigurationService(ITransportConfigurationService ref) {
      setTransportConfigurationService(null);
   }

   /**
    * Sets requirements service.
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeRequirementsService")
   public void setRequirementsService(IRequirementsService ref) {
      this.requirementsService = ref;
   }

   /**
    * Remove requirements service.
    */
   public void removeRequirementsService(IRequirementsService ref) {
      setRequirementsService(null);
   }

   /**
    * Create the usage for this command.
    *
    * @return the usage.
    */
   private static IUsage createUsage() {
      return new DefaultUsage("Creates a new JellyFish Pub/Sub Connector project.",
         new DefaultParameter<>(OUTPUT_DIRECTORY_PROPERTY).setDescription("The directory to generate the command project")
                                                        .setRequired(false),
         new DefaultParameter<>(MODEL_OBJECT_PROPERTY).setDescription(
            "The fully qualified name of the model to generate connectors for").setRequired(false),
         new DefaultParameter<>(GROUP_ID_PROPERTY)
                                                .setDescription(
                                                   "The groupId. This is usually similar to com.ngc.myprojectname")
                                                .setRequired(false),
         new DefaultParameter<>(ARTIFACT_ID_PROPERTY)
                                                   .setDescription(
                                                      "The artifactId, usually the lowercase version of the classname")
                                                   .setRequired(false),
         new DefaultParameter<>(CLEAN_PROPERTY)
                                             .setDescription(
                                                "If true, recursively deletes the connector project (if it already exists), before generating the connector project again")
                                             .setRequired(false));
   }
}
