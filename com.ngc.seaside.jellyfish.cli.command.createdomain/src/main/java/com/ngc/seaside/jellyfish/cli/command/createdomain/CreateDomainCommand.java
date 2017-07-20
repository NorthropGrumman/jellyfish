package com.ngc.seaside.jellyfish.cli.command.createdomain;

import com.google.common.collect.Streams;
import com.ngc.blocs.domain.impl.common.generated.ObjectFactory;
import com.ngc.blocs.domain.impl.common.generated.Tdomain;
import com.ngc.blocs.domain.impl.common.generated.Tobject;
import com.ngc.blocs.domain.impl.common.generated.Tproperty;
import com.ngc.blocs.jaxb.impl.common.JAXBUtilities;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.command.api.CommandException;
import com.ngc.seaside.command.api.DefaultParameter;
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
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayDeque;
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

   public static final String GROUP_ID_PROPERTY = "groupId";
   public static final String ARTIFACT_ID_PROPERTY = "artifactId";
   public static final String VERSION_PROPERTY = "version";
   public static final String PACKAGE_PROPERTY = "package";
   public static final String PACKAGE_SUFFIX_PROPERTY = "packageSuffix";
   public static final String OUTPUT_DIRECTORY_PROPERTY = "outputDirectory";
   public static final String DOMAIN_TEMPLATE_FILE_PROPERTY = "domainTemplateFile";
   public static final String MODEL_PROPERTY = "model";
   public static final String CLEAN_PROPERTY = "clean";

   private ILogService logService;

   @Override
   public String getName() {
      return NAME;
   }

   @Override
   public IUsage getUsage() {
      return USAGE;
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

   @Override
   public void run(IJellyFishCommandOptions commandOptions) {
      final IParameterCollection parameters = commandOptions.getParameters();
      final IModel model = evaluateModelParameter(commandOptions);
      final String groupId = evaluateGroupId(parameters, model);
      final String artifactId = evaluateArtifactId(parameters, model);
      final String pkg = evaluatePackage(parameters, groupId, artifactId);
      final Path domainTemplateFile = evaluteDomainTemplateFile(parameters);
      final boolean clean = evaluateCleanParameter(parameters);

      final Set<IData> data = getDataFromModel(model);
      if (data.isEmpty()) {
         logService.warn(CreateDomainCommand.class, "No input/output data for model " + model.getFullyQualifiedName());
         return;
      }

      final Path projectDir = evaluteProjectDirectory(parameters, pkg, clean);

      createGradleBuild(projectDir, domainTemplateFile, Collections.singleton(pkg));
      createDomainTemplate(projectDir, domainTemplateFile);

      // Group data by their sd package
      Map<String, List<IData>> mappedData = data.stream().collect(Collectors.groupingBy(d -> d.getParent().getName()));

      mappedData.forEach((sdPackage, dataList) -> {
         Path xmlFile = projectDir.resolve(Paths.get("src", "main", "resources", "domain", sdPackage + ".xml"));
         generateDomain(xmlFile, dataList, pkg);
      });
      logService.info(CreateDomainCommand.class, "Domain project successfully created");
   }

   /**
    * Returns the value of the {@link #CLEAN_PROPERTY} if it was set, false otherwise.
    * 
    * @param parameters command parameters
    * @return the value of the {@link #CLEAN_PROPERTY}
    * @throws CommandException if the value is invalid
    */
   private static boolean evaluateCleanParameter(IParameterCollection parameters) {
      final boolean clean;
      if (parameters.containsParameter(CLEAN_PROPERTY)) {
         String value = parameters.getParameter(CLEAN_PROPERTY).getValue();
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
      return clean;
   }

   /**
    * Returns the {@link IModel} associated with the value of the {@link #MODEL_PROPERTY}.
    * 
    * @param commandOptions command options
    * @return the {@link IModel}
    * @throws CommandException if the model name is invalid or missing
    */
   private static IModel evaluateModelParameter(IJellyFishCommandOptions commandOptions) {
      ISystemDescriptor sd = commandOptions.getSystemDescriptor();
      IParameterCollection parameters = commandOptions.getParameters();
      if (parameters.containsParameter(MODEL_PROPERTY)) {
         String modelName = parameters.getParameter(MODEL_PROPERTY).getValue();
         return sd.findModel(modelName).orElseThrow(() -> new CommandException("Unknown model: " + modelName));
      } else {
         throw new CommandException("Missing required parameter: " + MODEL_PROPERTY);
      }
   }

   /**
    * Returns the groupId for the domain project.
    * 
    * @param parameters command parameters
    * @param model domain model
    * @return the groupId for the domain project
    */
   private static String evaluateGroupId(IParameterCollection parameters, IModel model) {
      final String groupId;
      if (parameters.containsParameter(GROUP_ID_PROPERTY)) {
         groupId = parameters.getParameter(GROUP_ID_PROPERTY).getValue();
      } else {
         groupId = model.getParent().getName();
      }
      return groupId;
   }

   /**
    * Returns the artifactId for the domain project.
    * 
    * @param parameters command parameters
    * @param model domain model
    * @return the artifactId for the domain project
    */
   private static String evaluateArtifactId(IParameterCollection parameters, IModel model) {
      final String artifactId;
      if (parameters.containsParameter(ARTIFACT_ID_PROPERTY)) {
         artifactId = parameters.getParameter(ARTIFACT_ID_PROPERTY).getValue();
      } else {
         artifactId = model.getName().toLowerCase();
      }
      return artifactId;
   }

   /**
    * Returns the package for the domain project.
    * 
    * @param parameters command parameters
    * @param groupId domain groupId
    * @param artifactId domain artifactId
    * @return the package for the domain project
    */
   private static String evaluatePackage(IParameterCollection parameters, String groupId, String artifactId) {
      final String pkg;
      if (parameters.containsParameter(PACKAGE_PROPERTY)) {
         if (parameters.containsParameter(PACKAGE_SUFFIX_PROPERTY)) {
            throw new CommandException(
               "Invalid parameter: " + PACKAGE_SUFFIX_PROPERTY + " cannot be set if " + PACKAGE_PROPERTY + " is set");
         }
         pkg = parameters.getParameter(PACKAGE_PROPERTY).getValue();
      } else {
         if (parameters.containsParameter(PACKAGE_SUFFIX_PROPERTY)) {
            String suffix = parameters.getParameter(PACKAGE_SUFFIX_PROPERTY).getValue().trim();
            if (suffix.isEmpty() || suffix.startsWith(".")) {
               pkg = groupId + '.' + artifactId + suffix;
            } else {
               pkg = groupId + '.' + artifactId + '.' + suffix;
            }
         } else {
            pkg = groupId + '.' + artifactId + ".domainmodel";
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
   private static Path evaluteDomainTemplateFile(IParameterCollection parameters) {
      if (!parameters.containsParameter(DOMAIN_TEMPLATE_FILE_PROPERTY)) {
         throw new CommandException("Missing required parameter: " + DOMAIN_TEMPLATE_FILE_PROPERTY);
      }
      final Path domainTemplateFile = Paths.get(parameters.getParameter(DOMAIN_TEMPLATE_FILE_PROPERTY).getValue());
      if (!Files.isRegularFile(domainTemplateFile)) {
         throw new CommandException(domainTemplateFile + " is invalid");
      }
      return domainTemplateFile;
   }

   /**
    * Creates and returns the path to the domain project directory.
    * 
    * @param parameters command parameters
    * @param pkg domain package
    * @param clean whether or not to delete the contents of the directory
    * @return the path to the domain project directory
    * @throws CommandException if an error occurred in creating the project directory
    */
   private static Path evaluteProjectDirectory(IParameterCollection parameters, String pkg, boolean clean) {
      if (!parameters.containsParameter(OUTPUT_DIRECTORY_PROPERTY)) {
         throw new CommandException("Missing required parameter: " + OUTPUT_DIRECTORY_PROPERTY);
      }
      final Path outputDir = Paths.get(parameters.getParameter(OUTPUT_DIRECTORY_PROPERTY).getValue());
      final Path projectDir = outputDir.resolve(pkg);
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
    * Returns all IData associated with the given model. This includes inputs, outputs, and any nested IData objects in IData fields.
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
    * @param projectDir path project directory
    * @param domainTemplateFile path to domain template file
    * @param packages collection of packages to export
    * @throws CommandException if an error occurred generating the build.gradle file
    */
   private static void createGradleBuild(Path projectDir, Path domainTemplateFile, Collection<String> packages) {
      VelocityEngine engine = new VelocityEngine();
      engine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
      engine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
      engine.setProperty("runtime.references.strict", true);
      engine.init();
      Template template = engine.getTemplate("templates/build.gradle.vm");
      VelocityContext context = new VelocityContext();
      context.put("velocityFile", domainTemplateFile.getFileName());
      context.put("packages", packages);
      try (OutputStream out = Files.newOutputStream(projectDir.resolve("build.gradle"));
               OutputStreamWriter writer = new OutputStreamWriter(out)) {
         template.merge(context, writer);
      } catch (IOException e) {
         throw new CommandException(e);
      }
   }

   /**
    * Copies the domain template file to the output velocity folder.
    * 
    * @param projectDir directory of project
    * @param domainTemplateFile domain template file
    * @return the path to the new file
    * @throws CommandException if an error occurred when copying the template file
    */
   private Path createDomainTemplate(Path projectDir, Path domainTemplateFile) {
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
   private static void generateDomain(Path xmlFile, Collection<IData> data, String pkg) {
      Tdomain domain = new Tdomain();

      data.forEach(d -> domain.getObject().add(convert(d, pkg)));

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
   private static Tobject convert(IData data, String pkg) {
      Tobject object = new Tobject();
      object.setClazz(pkg + '.' + data.getName());
      data.getFields().forEach(field -> object.getProperty().add(convert(field, pkg)));
      return object;
   }

   /**
    * Converts the IDataField to a Tproperty.
    * 
    * @param field IDataField
    * @param pkg package of domain class
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
         new DefaultParameter(GROUP_ID_PROPERTY).setDescription("The project's group ID").setRequired(false),
         new DefaultParameter(ARTIFACT_ID_PROPERTY).setDescription("The project's version").setRequired(false),
         new DefaultParameter(VERSION_PROPERTY).setDescription("The project's artifactId").setRequired(false),
         new DefaultParameter(PACKAGE_PROPERTY).setDescription("The project's default package").setRequired(false),
         new DefaultParameter(PACKAGE_SUFFIX_PROPERTY)
                  .setDescription("A string to append to the end of the generated package name").setRequired(false),
         new DefaultParameter(OUTPUT_DIRECTORY_PROPERTY).setDescription("Base directory in which to output the project")
                  .setRequired(true),
         new DefaultParameter(DOMAIN_TEMPLATE_FILE_PROPERTY).setDescription("The velocity template file")
                  .setRequired(true),
         new DefaultParameter(MODEL_PROPERTY).setDescription("The fully qualified path to the system descriptor model")
                  .setRequired(true),
         new DefaultParameter(CLEAN_PROPERTY)
                  .setDescription(
                     "If true, recursively deletes the domain project (if it already exists), before generating the it again")
                  .setRequired(false));
   }

}
