package com.ngc.seaside.jellyfish.cli.command.createdomain;

import com.google.common.collect.Streams;
import com.ngc.blocs.domain.impl.common.generated.DomainConfiguration;
import com.ngc.blocs.domain.impl.common.generated.ObjectFactory;
import com.ngc.blocs.domain.impl.common.generated.Tdomain;
import com.ngc.blocs.domain.impl.common.generated.Tobject;
import com.ngc.blocs.domain.impl.common.generated.Tproperty;
import com.ngc.blocs.jaxb.impl.common.JAXBUtilities;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.service.resource.api.IResourceService;
import com.ngc.seaside.bootstrap.service.promptuser.api.IPromptUserService;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateService;
import com.ngc.seaside.bootstrap.utilities.file.FileUtilitiesException;
import com.ngc.seaside.bootstrap.utilities.file.GradleSettingsUtilities;
import com.ngc.seaside.bootstrap.utilities.resource.ITemporaryFileResource;
import com.ngc.seaside.bootstrap.utilities.resource.TemporaryFileResource;
import com.ngc.seaside.command.api.CommandException;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultParameterCollection;
import com.ngc.seaside.command.api.DefaultUsage;
import com.ngc.seaside.command.api.IParameterCollection;
import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import org.apache.commons.io.FileUtils;
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
import java.nio.file.StandardCopyOption;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

@Component(service = IJellyFishCommand.class)
public class CreateDomainCommand implements IJellyFishCommand {

   private static final String NAME = "create-domain";
   private static final IUsage USAGE = createUsage();
   private static final String DEFAULT_ARTIFACT_ID_SUFFIX = "domain";
   static final String DEFAULT_DOMAIN_TEMPLATE_FILE = "service-domain.java.vm";

   public static final String GROUP_ID_PROPERTY = "groupId";
   public static final String ARTIFACT_ID_PROPERTY = "artifactId";
   public static final String PACKAGE_PROPERTY = "package";
   public static final String PACKAGE_SUFFIX_PROPERTY = "packageSuffix";
   public static final String EXTENSION_PROPERTY = "extension";
   public static final String BUILD_GRADLE_TEMPLATE_PROPERTY = "buildGradleTemplate";
   public static final String OUTPUT_DIRECTORY_PROPERTY = "outputDirectory";
   public static final String DOMAIN_TEMPLATE_FILE_PROPERTY = "domainTemplateFile";
   public static final String MODEL_PROPERTY = "model";
   public static final String USE_MODEL_STRUCTURE_PROPERTY = "useModelStructure";
   public static final String USE_VERBOSE_IMPORTS_PROPERTY = "useVerboseImports";
   public static final String CLEAN_PROPERTY = "clean";

   private ILogService logService;
   private IPromptUserService promptService;
   private ITemplateService templateService;
   private IResourceService resourceService;

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
      final IModel model = evaluateModelParameter(commandOptions);
      final String groupId = evaluateGroupId(parameters, model);
      final String artifactId = evaluateArtifactId(parameters, model);
      final String pkg = evaluatePackage(parameters, groupId, artifactId);
      final Path domainTemplateFile = evaluateDomainTemplateFile(parameters);
      final boolean clean = evaluateBooleanParameter(parameters, CLEAN_PROPERTY);
      final boolean useModelStructure = evaluateBooleanParameter(parameters, USE_MODEL_STRUCTURE_PROPERTY);
      final boolean useVerboseImports = evaluateBooleanParameter(parameters, USE_VERBOSE_IMPORTS_PROPERTY);

      final Set<IData> data = getDataFromModel(model);
      if (data.isEmpty()) {
         logService.warn(CreateDomainCommand.class, "No input/output data for model " + model.getFullyQualifiedName());
         return;
      }

      // Group data by their sd package
      Map<String, List<IData>> mappedData = data.stream().collect(Collectors.groupingBy(d -> d.getParent().getName()));

      final Set<String> domainPackages;
      if (useModelStructure) {
         domainPackages = mappedData.keySet();
      } else {
         domainPackages = Collections.singleton(pkg);
      }

      final Path outputDir = evaluateOutputDirectory(parameters);
      final Path projectDir = evaluateProjectDirectory(outputDir, groupId, artifactId, clean);
      createGradleBuild(projectDir, commandOptions, domainTemplateFile, domainPackages, clean);
      createDomainTemplate(projectDir, domainTemplateFile);
      
      mappedData.forEach((sdPackage, dataList) -> {
         final Path xmlFile = projectDir.resolve(Paths.get("src", "main", "resources", "domain", sdPackage + ".xml"));
         final String domainPackage = useModelStructure ? sdPackage : pkg;
         generateDomainXml(xmlFile, dataList, domainPackage, useVerboseImports);
      });

      updateGradleDotSettings(outputDir, groupId, artifactId, commandOptions.getParameters());

      logService.info(CreateDomainCommand.class, "Domain project successfully created");
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
    * Sets prompt user service.
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         unbind = "removePromptService")
   public void setPromptService(IPromptUserService ref) {
      this.promptService = ref;
   }

   /**
    * Remove prompt service.
    */
   public void removePromptService(IPromptUserService ref) {
      setPromptService(null);
   }

   /**
    * Sets template service.
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         unbind = "removeTemplateService")
   public void setTemplateService(ITemplateService ref) {
      this.templateService = ref;
   }

   /**
    * Remove template service.
    */
   public void removeTemplateService(ITemplateService ref) {
      setTemplateService(null);
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

   private void updateGradleDotSettings(Path outputDir,
                                        String groupId,
                                        String artifactId,
                                        IParameterCollection originalParameters) {
      DefaultParameterCollection updatedParameters = new DefaultParameterCollection();
      updatedParameters.addParameter(new DefaultParameter<>(OUTPUT_DIRECTORY_PROPERTY, outputDir.toString()));
      updatedParameters.addParameter(new DefaultParameter<>(GROUP_ID_PROPERTY, groupId));
      updatedParameters.addParameter(new DefaultParameter<>(ARTIFACT_ID_PROPERTY, artifactId));
      try {
         if (!GradleSettingsUtilities.tryAddProject(updatedParameters)) {
            logService.warn(getClass(), "Unable to add the new project to settings.gradle.");
         }
      } catch (FileUtilitiesException e) {
         throw new CommandException("failed to update settings.gradle!", e);
      }
   }

   /**
    * Returns the boolean value of the given parameter if it was set, false otherwise.
    *
    * @param parameters command parameters
    * @param parameter  name of parameter
    * @return the boolean value of the parameter
    * @throws CommandException if the value is invalid
    */
   private static boolean evaluateBooleanParameter(IParameterCollection parameters, String parameter) {
      final boolean booleanValue;
      if (parameters.containsParameter(parameter)) {
         String value = parameters.getParameter(parameter).getStringValue();
         switch (value.toLowerCase()) {
            case "true":
               booleanValue = true;
               break;
            case "false":
               booleanValue = false;
               break;
            default:
               throw new CommandException(
                     "Invalid value for " + parameter + ": " + value + ". Expected either true or false.");
         }
      } else {
         booleanValue = false;
      }
      return booleanValue;
   }

   /**
    * Returns the {@link IModel} associated with the value of the {@link #MODEL_PROPERTY}.
    *
    * @param commandOptions command options
    * @return the {@link IModel}
    * @throws CommandException if the model name is invalid or missing
    */
   private IModel evaluateModelParameter(IJellyFishCommandOptions commandOptions) {
      ISystemDescriptor sd = commandOptions.getSystemDescriptor();
      IParameterCollection parameters = commandOptions.getParameters();
      final String modelName;
      if (parameters.containsParameter(MODEL_PROPERTY)) {
         modelName = parameters.getParameter(MODEL_PROPERTY).getStringValue();
      } else {
         modelName = promptService.prompt(MODEL_PROPERTY, null, null);
      }
      return sd.findModel(modelName).orElseThrow(() -> new CommandException("Unknown model: " + modelName));
   }

   /**
    * Returns the groupId for the domain project.
    *
    * @param parameters command parameters
    * @param model      domain model
    * @return the groupId for the domain project
    */
   private static String evaluateGroupId(IParameterCollection parameters, IModel model) {
      final String groupId;
      if (parameters.containsParameter(GROUP_ID_PROPERTY)) {
         groupId = parameters.getParameter(GROUP_ID_PROPERTY).getStringValue();
      } else {
         groupId = model.getParent().getName();
      }
      return groupId;
   }

   /**
    * Returns the artifactId for the domain project.
    *
    * @param parameters command parameters
    * @param model      domain model
    * @return the artifactId for the domain project
    */
   private static String evaluateArtifactId(IParameterCollection parameters, IModel model) {
      final String artifactId;
      if (parameters.containsParameter(ARTIFACT_ID_PROPERTY)) {
         artifactId = parameters.getParameter(ARTIFACT_ID_PROPERTY).getStringValue();
      } else {
         artifactId = model.getName().toLowerCase() + "." + DEFAULT_ARTIFACT_ID_SUFFIX;
      }
      return artifactId;
   }

   /**
    * Returns the package for the domain project.
    *
    * @param parameters command parameters
    * @param groupId    domain groupId
    * @param artifactId domain artifactId
    * @return the package for the domain project
    */
   private static String evaluatePackage(IParameterCollection parameters, String groupId, String artifactId) {
      final String pkg;
      if (parameters.containsParameter(PACKAGE_PROPERTY)) {
         if (parameters.containsParameter(PACKAGE_SUFFIX_PROPERTY)) {
            throw new CommandException(
                  "Invalid parameter: " + PACKAGE_SUFFIX_PROPERTY + " cannot be set if " + PACKAGE_PROPERTY
                  + " is set");
         }
         pkg = parameters.getParameter(PACKAGE_PROPERTY).getStringValue();
      } else {
         if (parameters.containsParameter(PACKAGE_SUFFIX_PROPERTY)) {
            String suffix = parameters.getParameter(PACKAGE_SUFFIX_PROPERTY).getStringValue().trim();
            if (suffix.isEmpty() || suffix.startsWith(".")) {
               pkg = groupId + '.' + artifactId + suffix;
            } else {
               pkg = groupId + '.' + artifactId + '.' + suffix;
            }
         } else {
            pkg = groupId + '.' + artifactId;
         }
      }
      return pkg;
   }

   /**
    * Returns the path to the domain template file.
    *
    * @param parameters command parameters
    * @return the path to the domain template file
    * @throws CommandException if the file does not exist
    */
   private Path evaluateDomainTemplateFile(IParameterCollection parameters) {
      final Path domainTemplateFile;
      final String templateFilename;
      if (parameters.containsParameter(DOMAIN_TEMPLATE_FILE_PROPERTY)) {
         templateFilename = parameters.getParameter(DOMAIN_TEMPLATE_FILE_PROPERTY).getStringValue();
      } else {
         // Unpack the default velocity template to a temporary directory.
         final ITemporaryFileResource velocityTemplate = TemporaryFileResource.forClasspathResource(
               CreateDomainCommand.class,
               DEFAULT_DOMAIN_TEMPLATE_FILE);
         resourceService.readResource(velocityTemplate);
         templateFilename = velocityTemplate.getTemporaryFile().toAbsolutePath().toString();
      }
      domainTemplateFile = Paths.get(templateFilename);

      if (!Files.isRegularFile(domainTemplateFile)) {
         throw new CommandException(templateFilename + " is invalid");
      }
      return domainTemplateFile;
   }

   /**
    * Creates and returns the path to the output directory.
    *
    * @param parameters command parameters
    * @return the path to the output directory
    * @throws CommandException if an error occurred in creating the project directory
    */
   private Path evaluateOutputDirectory(IParameterCollection parameters) {
      final Path outputDir;

      if (parameters.containsParameter(OUTPUT_DIRECTORY_PROPERTY)) {
         outputDir = Paths.get(parameters.getParameter(OUTPUT_DIRECTORY_PROPERTY).getStringValue());
      } else {
         String input = promptService.prompt(OUTPUT_DIRECTORY_PROPERTY, null, null);
         outputDir = Paths.get(input);
      }
      return outputDir;
   }

   /**
    * Creates and returns the path to the domain project directory.
    *
    * @param outputDir  output directory
    * @param groupId    the groupId
    * @param artifactId the artifactId
    * @param clean      whether or not to delete the contents of the directory
    * @return the path to the domain project directory
    * @throws CommandException if an error occurred in creating the project directory
    */
   private Path evaluateProjectDirectory(Path outputDir, String groupId, String artifactId, boolean clean) {
      final Path projectDir = outputDir.resolve(Paths.get(groupId + "." + artifactId));
      try {
         Files.createDirectories(outputDir);
         if (clean) {
            FileUtils.deleteQuietly(projectDir.toFile());
         }
         Files.createDirectories(projectDir);
      } catch (IOException e) {
         throw new CommandException(e);
      }
      return projectDir;
   }

   /**
    * Returns all IData associated with the given model. This includes inputs, outputs, and any nested IData objects in
    * IData fields.
    *
    * @param model model
    * @return all IData associated with the model inputs and outputs
    */
   private static Set<IData> getDataFromModel(IModel model) {
      Set<IData> data = new HashSet<>();
      Queue<IData> newData = new ArrayDeque<>();
      Streams.concat(model.getInputs().stream().map(IDataReferenceField::getType),
                     model.getOutputs().stream().map(IDataReferenceField::getType)).forEach(newData::add);
      while (!newData.isEmpty()) {
         IData d = newData.poll();
         if (!data.add(d)) {
            continue;
         }
         for (IDataField field : d.getFields()) {
            if (field.getType() == DataTypes.DATA) {
               newData.add(field.getReferencedDataType());
            }
         }
      }
      return data;
   }

   /**
    * Creates the build.gradle file for the domain project
    *
    * @param projectDir         path project directory
    * @param commandOptions     the command options used to run the command
    * @param domainTemplateFile path to domain template file
    * @param packages           collection of packages to export
    * @throws CommandException if an error occurred generating the build.gradle file
    */
   private void createGradleBuild(Path projectDir, IJellyFishCommandOptions commandOptions, Path domainTemplateFile,
                                  Set<String> packages, boolean clean) {

      String extension = "java";
      if (commandOptions.getParameters().containsParameter(EXTENSION_PROPERTY)) {
         extension = commandOptions.getParameters().getParameter(EXTENSION_PROPERTY).getStringValue();
      }

      String template = CreateDomainCommand.class.getPackage().getName();
      if (commandOptions.getParameters().containsParameter(BUILD_GRADLE_TEMPLATE_PROPERTY)) {
         template = commandOptions.getParameters().getParameter(BUILD_GRADLE_TEMPLATE_PROPERTY).getStringValue();
      }

      DefaultParameterCollection parameters = new DefaultParameterCollection();
      parameters.addParameter(new DefaultParameter<>("options", commandOptions));
      parameters.addParameter(new DefaultParameter<>("velocityFile", domainTemplateFile.getFileName()));
      parameters.addParameter(new DefaultParameter<>("packages", packages));
      parameters.addParameter(new DefaultParameter<>(EXTENSION_PROPERTY, extension));

      templateService.unpack(template, parameters, projectDir, clean);
   }

   /**
    * Copies the domain template file to the output velocity folder.
    *
    * @param projectDir         directory of project
    * @param domainTemplateFile domain template file
    * @return the path to the new file
    * @throws CommandException if an error occurred when copying the template file
    */
   private static Path createDomainTemplate(Path projectDir, Path domainTemplateFile) {
      Path velocityDir = projectDir.resolve(Paths.get("src", "main", "resources", "velocity"));
      try {
         Files.createDirectories(velocityDir);
         Path newFile = velocityDir.resolve(domainTemplateFile.getFileName());
         Files.copy(domainTemplateFile, newFile, StandardCopyOption.REPLACE_EXISTING);
         return newFile;
      } catch (IOException e) {
         throw new CommandException(e);
      }
   }

   /**
    * Generates the Blocs domain xml file with the given data and package.
    *
    * @param xmlFile output xml file
    * @param data collection of data
    * @param pkg package of domain data classes
    * @throws CommandException if an error occurred when creating the xml file
    */
   private static void generateDomainXml(Path xmlFile, Collection<IData> data, String pkg, boolean useVerboseImports) {
      Tdomain domain = new Tdomain();
      
      DomainConfiguration config = new DomainConfiguration();
      config.setUseVerboseImports(useVerboseImports);
      domain.setConfig(config);
      
      ArrayList<Tobject> enumObjList = new ArrayList<Tobject>();
      for (IData d : data) {
         domain.getObject().add(convert(d, pkg, useVerboseImports));

         for (IDataField dField : d.getFields()) {
            if (dField.getReferencedEnumeration() != null) {
               // Handle enumerations
               enumObjList.add(convertEnum(dField, pkg));
            }
         }
      }
      domain.getObject().addAll(enumObjList);

      ObjectFactory factory = new ObjectFactory();
      try {
         Files.createDirectories(xmlFile.getParent());
         JAXBUtilities.write(xmlFile.toFile(), factory.createDomain(domain));
      } catch (IOException e) {
         throw new CommandException(e);
      }
   }

   /**
    * Converts the IData to a Tobject.
    *
    * @param data IData
    * @param pkg package of domain classes
    * @return domain object
    */
   private static Tobject convert(IData data, String pkg, boolean useVerboseImports) {
      Tobject object = new Tobject();
      object.setClazz(pkg + '.' + data.getName());
      data.getFields().forEach(field -> object.getProperty().add(convert(field, pkg)));
      return object;
   }
   
   /**
    * Converts the IData enumeration to a Tobject.
    *
    * @param dField IData enumeration
    * @param pkg package of domain classes
    * @return domain object
    */
   private static Tobject convertEnum(IDataField dField, String pkg) {
      String enumValString = "";
      Tobject object = new Tobject();
      object.setClazz(dField.getReferencedEnumeration().getFullyQualifiedName());
      object.setType("enum");

      for (String enumVal : dField.getReferencedEnumeration().getValues()) {
         enumValString += enumVal + " ";
      }
      enumValString = enumValString.trim();
      object.setEnumValues(enumValString);
      return object;
   }


   /**
    * Converts the IDataField to a Tproperty.
    *
    * @param field IDataField
    * @param pkg   package of domain class
    * @return domain property
    */
   private static Tproperty convert(IDataField field, String pkg) {
      Tproperty property = new Tproperty();
      property.setAbstract(false);
      property.setName(field.getName());
      switch (field.getCardinality()) {
         case MANY:
            property.setMultiple(true);
            break;
         case SINGLE:
            property.setMultiple(false);
            break;
         default:
            throw new IllegalStateException("Unknown cardinality: " + field.getCardinality());
      }
      switch (field.getType()) {
         case BOOLEAN:
            property.setType("boolean");
            break;
         case DATA:
            IData ref = field.getReferencedDataType();
            property.setType(pkg + '.' + ref.getName());
            break;
         case FLOAT:
            property.setType("float");
            break;
         case INT:
            property.setType("int");
            break;
         case STRING:
            property.setType("String");
            break;
         case ENUM:
            property.setType(field.getReferencedEnumeration().getFullyQualifiedName());
            break;
         default:
            throw new IllegalStateException("Unknown field type: " + field.getType());
      }
      return property;
   }

   /**
    * Create the usage for this command.
    *
    * @return the usage.
    */
   private static IUsage createUsage() {
      return new DefaultUsage("Generate a BLoCS domain model gradle project.",
                              new DefaultParameter<String>(GROUP_ID_PROPERTY).setDescription("The project's group ID")
                                    .setRequired(false),
                              new DefaultParameter<String>(ARTIFACT_ID_PROPERTY).setDescription("The project's version")
                                    .setRequired(false),
                              new DefaultParameter<String>(PACKAGE_PROPERTY)
                                    .setDescription("The project's default package")
                                    .setRequired(false),
                              new DefaultParameter<String>(PACKAGE_SUFFIX_PROPERTY)
                                    .setDescription("A string to append to the end of the generated package name")
                                    .setRequired(false),
                              new DefaultParameter<String>(OUTPUT_DIRECTORY_PROPERTY)
                                    .setDescription("Base directory in which to output the project").setRequired(true),
                              new DefaultParameter<String>(DOMAIN_TEMPLATE_FILE_PROPERTY)
                                    .setDescription("The velocity template file")
                                    .setRequired(false),
                              new DefaultParameter<String>(MODEL_PROPERTY)
                                    .setDescription("The fully qualified path to the system descriptor model")
                                    .setRequired(true),
                              new DefaultParameter<String>(CLEAN_PROPERTY)
                                    .setDescription(
                                          "If true, recursively deletes the domain project (if it already exists), before generating the it again")
                                    .setRequired(false),
                              new DefaultParameter<String>(USE_VERBOSE_IMPORTS_PROPERTY)
                                    .setDescription("If true, imports from the same package will be included for generated domains")
                                    .setRequired(false),
                              new DefaultParameter<String>(USE_MODEL_STRUCTURE_PROPERTY)
                                    .setDescription(
                                          "If true, uses the System Descriptor package structure for the generated domain package structure")
                                    .setRequired(false),
                              new DefaultParameter<String>(EXTENSION_PROPERTY)
                                    .setDescription("The extension of the generated domain files")
                                    .setRequired(false),
                              new DefaultParameter<String>(BUILD_GRADLE_TEMPLATE_PROPERTY)
                                    .setDescription(
                                          "Name of template used to generate the domain projects build.gradle")
                                    .setRequired(false));
   }

}
