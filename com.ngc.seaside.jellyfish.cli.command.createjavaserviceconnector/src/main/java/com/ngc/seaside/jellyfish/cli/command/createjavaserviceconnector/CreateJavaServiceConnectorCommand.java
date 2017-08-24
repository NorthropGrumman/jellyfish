package com.ngc.seaside.jellyfish.cli.command.createjavaserviceconnector;

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
import com.ngc.seaside.jellyfish.cli.command.createjavaserviceconnector.dto.ConnectorDto;
import com.ngc.seaside.bootstrap.utilities.file.GradleSettingsUtilities;
import com.ngc.seaside.bootstrap.utilities.file.FileUtilitiesException;
import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.command.api.IParameterCollection;

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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.json.JsonValue;

@Component(service = IJellyFishCommand.class)
public class CreateJavaServiceConnectorCommand implements IJellyFishCommand {

   private static final String NAME = "create-java-service-connector";
   private static final IUsage USAGE = createUsage();

   public static final String OUTPUT_DIRECTORY_PROPERTY = "outputDirectory";
   public static final String MODEL_PROPERTY = "model";
   public static final String GROUP_ID_PROPERTY = "groupId";
   public static final String ARTIFACT_ID_PROPERTY = "artifactId";

   public static final String MODEL_OBJECT_PROPERTY = "modelObject";
   public static final String MODEL_REQUIREMENTS_PROPERTY = "modelRequirements";
   public static final String JAVA_CLASS_NAME_PROPERTY = "javaClassName";
   public static final String PACKAGE_PROPERTY = "package";
   public static final String CLEAN_PROPERTY = "clean";

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

         IModel model = commandOptions.getSystemDescriptor().findModel(modelId).orElseThrow(() -> new CommandException("Unknown model:" + modelId));
         parameters.addParameter(new DefaultParameter<>(MODEL_OBJECT_PROPERTY, model));
      }
      final IModel model = commandOptions.getSystemDescriptor().findModel(modelId).orElseThrow(() -> new CommandException("Unknown model:" + modelId));

      ArrayList<String> requirements = new ArrayList<String>();
      if (!parameters.containsParameter(MODEL_REQUIREMENTS_PROPERTY)) {
         JsonValue value = model.getMetadata().getJson().get("satisfies");
         if (value != null) {
            for (JsonValue req : value.asJsonArray()) {
               requirements.add(req.toString());
            }
         }

         parameters.addParameter(new DefaultParameter<>(MODEL_REQUIREMENTS_PROPERTY, requirements));
      }

      parameters.addParameter(new DefaultParameter<>(JAVA_CLASS_NAME_PROPERTY, model.getName().concat("Connector")));

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
         logService.error(CreateJavaServiceConnectorCommand.class, e);
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

      final Set<IData> allInputData = new LinkedHashSet<>();
      final Set<IData> allOutputData = new LinkedHashSet<>();
      final Set<IEnumeration> allInputEnums = new LinkedHashSet<>();
      final Set<IEnumeration> allOutputEnums = new LinkedHashSet<>();

      //final INamedChildCollection<IModel, IDataReferenceField> inputs = model.getInputs();

      model.getInputs().forEach(eachInput -> {
         allInputData.add(eachInput.getType());
         separateFields(eachInput.getType().getFields(), allInputData, allInputEnums);
      });

      model.getOutputs().forEach(eachOutput -> {
         allOutputData.add(eachOutput.getType());
         separateFields(eachOutput.getType().getFields(), allOutputData, allOutputEnums);
      });

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
      dto.setAllOutputEnums(allOutputEnums);
      dto.setAllOutputData(allOutputData);
      dto.setAllInputEnums(allInputEnums);
      dto.setAllInputData(allInputData);
      dto.setRequirements(requirements);

      parameters.addParameter(new DefaultParameter<>("dto", dto));

      templateService.unpack(CreateJavaServiceConnectorCommand.class.getPackage().getName(), parameters, outputDirectory, clean);
      logService.info(CreateJavaServiceConnectorCommand.class, "%s project successfully created", modelId);
   }


   private void separateFields(final INamedChildCollection<IData, IDataField> fields, final Set<IData> dataSet, final Set<IEnumeration> enumSet) {
      fields.forEach(eachField -> {
         final DataTypes fieldType = eachField.getType();
         switch (fieldType) {
         case DATA:
            dataSet.add(eachField.getReferencedDataType());
            separateFields(eachField.getReferencedDataType().getFields(), dataSet, enumSet);
            break;

         case ENUM:
            enumSet.add(eachField.getReferencedEnumeration());
            break;

         default:
            // TODO what to do here? Anything?
         }
      });
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
    * Create the usage for this command.
    *
    * @return the usage.
    */
   private static IUsage createUsage() {
      return new DefaultUsage("Creates a new JellyFish Service Connector project.",
         new DefaultParameter(OUTPUT_DIRECTORY_PROPERTY).setDescription("The directory to generate the command project").setRequired(false),
         new DefaultParameter(MODEL_OBJECT_PROPERTY).setDescription("The fully qualified name of the model to generate connectors for").setRequired(false),
         new DefaultParameter(GROUP_ID_PROPERTY).setDescription("The groupId. This is usually similar to com.ngc.myprojectname").setRequired(false),
         new DefaultParameter(ARTIFACT_ID_PROPERTY).setDescription("The artifactId, usually the lowercase version of the classname").setRequired(false), new DefaultParameter(CLEAN_PROPERTY)
                  .setDescription("If true, recursively deletes the connector project (if it already exists), before generating the connector project again").setRequired(false));
   }
}
