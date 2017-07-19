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
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;

@Component(service = IJellyFishCommand.class)
public class CreateDomainCommand implements IJellyFishCommand {

   private static final String NAME = "create-domain";
   private static final IUsage USAGE = createUsage();
   private static final String METADATA_STEREOTYPES = "stereotypes";

   public static final String GROUP_ID_PROPERTY = "groupId";
   public static final String ARTIFACT_ID_PROPERTY = "artifactId";
   public static final String VERSION_PROPERTY = "version";
   public static final String PACKAGE_PROPERTY = "package";
   public static final String PACKAGE_SUFFIX_PROPERTY = "packageSuffix";
   public static final String OUTPUT_DIRECTORY_PROPERTY = "outputDirectory";
   public static final String DOMAIN_TEMPLATE_FILE_PROPERTY = "domainTemplateFile";
   public static final String MODEL_PROPERTY = "model";
   public static final String IGNORE_STEREOTYPES_PROPERTY = "ignoreStereoTypes";
   public static final String STEREOTYPES_PROPERTY = "stereoTypes";

   private ILogService logService;

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
         IParameterCollection parameters = commandOptions.getParameters();

         final Set<IModel> models = getModels(commandOptions);
         final Predicate<IModel> modelFilter = getModelFilter(parameters);

         models.removeIf(modelFilter.negate());
         if (models.isEmpty()) {
            logService.error(CreateDomainCommand.class, "No model files were found");
            throw new CommandException("No model files were found");
         }
         models.removeIf(model -> model.getInputs().isEmpty() && model.getOutputs().isEmpty());
         if (models.isEmpty()) {
            logService.error(CreateDomainCommand.class, "No model files with inputs/outputs were found");
            throw new CommandException("No model files with inputs/outputs were found");
         }

         final String groupIdFormat;
         if (parameters.containsParameter(GROUP_ID_PROPERTY)) {
            groupIdFormat = parameters.getParameter(GROUP_ID_PROPERTY).getValue();
         } else {
            groupIdFormat = "%s";
         }

         final String artifactIdFormat;
         if (parameters.containsParameter(ARTIFACT_ID_PROPERTY)) {
            artifactIdFormat = parameters.getParameter(ARTIFACT_ID_PROPERTY).getValue();
         } else {
            artifactIdFormat = "%s";
         }

         final String packageFormat;
         if (parameters.containsParameter(PACKAGE_PROPERTY)) {
            if (parameters.containsParameter(PACKAGE_SUFFIX_PROPERTY)) {
               throw new CommandException("Invalid parameter: " + PACKAGE_SUFFIX_PROPERTY + " cannot be set if "
                  + PACKAGE_PROPERTY + " is set");
            }
            packageFormat = parameters.getParameter(PACKAGE_PROPERTY).getValue();
         } else {
            if (parameters.containsParameter(PACKAGE_SUFFIX_PROPERTY)) {
               String suffix = parameters.getParameter(PACKAGE_SUFFIX_PROPERTY).getValue().trim();
               if (suffix.isEmpty() || suffix.startsWith(".")) {
                  packageFormat = "%s.%s" + suffix;
               } else {
                  packageFormat = "%s.%s." + suffix;
               }
            } else {
               packageFormat = "%s.%s.domainmodel";
            }
         }

         final Path domainTemplateFile = Paths.get(parameters.getParameter(DOMAIN_TEMPLATE_FILE_PROPERTY).getValue());
         if (!Files.isRegularFile(domainTemplateFile)) {
            throw new CommandException(domainTemplateFile + " is invalid");
         }

         final Path outputDir = Paths.get(parameters.getParameter(OUTPUT_DIRECTORY_PROPERTY).getValue());
         try {
            Files.createDirectories(outputDir);
         } catch (IOException e) {
            logService.error(CreateDomainCommand.class, e);
            throw new CommandException(e);
         }

         for (IModel model : models) {

            final String groupId = String.format(groupIdFormat, model.getParent().getName());
            final String artifactId = String.format(artifactIdFormat, model.getName().toLowerCase());

            final String pkg = String.format(packageFormat, groupId, artifactId);
            final Path projectDir = outputDir.resolve(pkg);
            final Set<IData> data = getDataFromModel(model);

            try {
               Files.createDirectories(projectDir);
               createGradleBuild(projectDir);
               createDomainTemplate(projectDir, domainTemplateFile);
            } catch (IOException e) {
               logService.error(CreateDomainCommand.class, e);
               throw new CommandException(e);
            }

            // Group data by their sd package
            Map<String, List<IData>> map = data.stream().collect(Collectors.groupingBy(d -> d.getParent().getName()));

            map.forEach((sdPackage, dataList) -> {
               Path xmlFile = projectDir.resolve(Paths.get("src", "main", "resources", "domain", sdPackage + ".xml"));
               generateDomain(xmlFile, dataList, pkg);
            });
         }
   }
   
   /**
    * Returns a predicate for filtering models based on the {@link #STEREOTYPES_PROPERTY} and {@link #IGNORE_STEREOTYPES_PROPERTY}.
    * 
    * @param parameters command parameters
    * @return a predicate for filtering models
    */
   private static Predicate<IModel> getModelFilter(IParameterCollection parameters) {
      final Predicate<IModel> stereotypeFilter;
      if (parameters.containsParameter(STEREOTYPES_PROPERTY)) {
         List<String> stereotypes = Arrays
                  .asList(parameters.getParameter(STEREOTYPES_PROPERTY).getValue().split("\\s*,\\s*"));
         stereotypeFilter = CreateDomainCommand.withAnyStereotype(stereotypes);
      } else {
         stereotypeFilter = __ -> true;
      }
      final Predicate<IModel> ignoreFilter;
      if (parameters.containsParameter(IGNORE_STEREOTYPES_PROPERTY)) {
         List<String> ignored = Arrays
                  .asList(parameters.getParameter(IGNORE_STEREOTYPES_PROPERTY).getValue().split("\\s*,\\s*"));
         ignoreFilter = CreateDomainCommand.withAnyStereotype(ignored).negate();
      } else {
         ignoreFilter = __ -> true;
      }
      return stereotypeFilter.and(ignoreFilter);
   }

   /**
    * Returns the models from the system descriptor in the command options. If the {@link #MODEL_PROPERTY} is set, then this will return just that model.
    * 
    * @param commandOptions command options
    * @return the models in the system descriptor
    */
   private static Set<IModel> getModels(IJellyFishCommandOptions commandOptions) {
      ISystemDescriptor sd = commandOptions.getSystemDescriptor();
      IParameterCollection parameters = commandOptions.getParameters();

      final Set<IModel> models = new HashSet<>();
      if (parameters.containsParameter(MODEL_PROPERTY)) {
         String modelName = parameters.getParameter(MODEL_PROPERTY).getValue();
         IModel model = sd.findModel(modelName).orElseThrow(() -> new CommandException("Unknown model: " + modelName));
         models.add(model);
      } else {
         if (parameters.containsParameter(ARTIFACT_ID_PROPERTY)) {
            throw new CommandException(
               "Invalid parameter: " + MODEL_PROPERTY + " must be set when using " + ARTIFACT_ID_PROPERTY);
         }
         if (parameters.containsParameter(PACKAGE_PROPERTY)) {
            throw new CommandException(
               "Invalid parameter: " + MODEL_PROPERTY + " must be set when using " + PACKAGE_PROPERTY);
         }
         sd.getPackages().stream().flatMap(pkg -> pkg.getModels().stream()).forEach(models::add);
      }
      return models;
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

   private void createGradleBuild(Path projectDir) throws IOException {
      Files.createDirectories(projectDir);
      try {
         Files.createFile(projectDir.resolve("build.gradle"));
      } catch (FileAlreadyExistsException e) {
         // ignore
      }
   }

   /**
    * Copies the domain template to the output velocity folder
    * 
    * @param projectDir directory of project
    * @param domainTemplateFile domain template file
    * @throws IOException
    */
   private void createDomainTemplate(Path projectDir, Path domainTemplateFile) throws IOException {
      Path velocityDir = projectDir.resolve(Paths.get("src", "main", "resources", "velocity"));
      Files.createDirectories(velocityDir);
      Files.copy(domainTemplateFile, velocityDir.resolve(domainTemplateFile.getFileName()));
   }

   /**
    * Generates the Blocs domain xml file with the given data and package.
    * 
    * @param xmlFile output xml file
    * @param data collection of data
    * @param pkg package of domain data classes
    */
   private void generateDomain(Path xmlFile, Collection<IData> data, String pkg) {
      Tdomain domain = new Tdomain();

      data.forEach(d -> domain.getObject().add(convert(d, pkg)));

      ObjectFactory factory = new ObjectFactory();
      try {
         Files.createDirectories(xmlFile.getParent());
         JAXBUtilities.write(xmlFile.toFile(), factory.createDomain(domain));
      } catch (IOException e) {
         logService.error(CreateDomainCommand.class, e);
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
    * Create the usage for this command.
    *
    * @return the usage.
    */
   private static IUsage createUsage() {
      return new DefaultUsage("Generate a BLoCS domain model gradle project.",
         new DefaultParameter(GROUP_ID_PROPERTY).setDescription("The project's group ID").setRequired(false),
         new DefaultParameter(ARTIFACT_ID_PROPERTY).setDescription("The project's version").setRequired(false),
         new DefaultParameter(VERSION_PROPERTY)
                  .setDescription(
                     "The project's artifactId (this argument may only be used if the model argument is set)")
                  .setRequired(false),
         new DefaultParameter(PACKAGE_PROPERTY)
                  .setDescription(
                     "The project's default package (this argument may only be used if the model argument is set)")
                  .setRequired(false),
         new DefaultParameter(PACKAGE_SUFFIX_PROPERTY)
                  .setDescription("A string to append to the end of the generated package name").setRequired(false),
         new DefaultParameter(OUTPUT_DIRECTORY_PROPERTY).setDescription("Base directory in which to output the file(s)")
                  .setRequired(true),
         new DefaultParameter(DOMAIN_TEMPLATE_FILE_PROPERTY).setDescription("The velocity template file")
                  .setRequired(true),
         new DefaultParameter(MODEL_PROPERTY)
                  .setDescription(
                     "The fully qualified path to the system descriptor model. If set it would only generate the input and output data for that model")
                  .setRequired(false),
         new DefaultParameter(IGNORE_STEREOTYPES_PROPERTY)
                  .setDescription(
                     "Comma-separated string of the stereotypes to ignore when generating the domain model (e.g. \"virtual, system\")")
                  .setRequired(false),
         new DefaultParameter(STEREOTYPES_PROPERTY)
                  .setDescription(
                     "Comma-separated string of the stereotypes in which the domain should only be generated")
                  .setRequired(false));
   }

   private static Predicate<IModel> withAnyStereotype(Collection<String> stereotypes) {
      // TODO replace with ModelPredicates.withAnyStereotype when implemented
      return model -> {
         IMetadata metadata = model.getMetadata();
         if (metadata != null) {
            JsonObject json = metadata.getJson();
            if (json != null) {
               JsonValue value = json.get(METADATA_STEREOTYPES);
               if (value instanceof JsonString) {
                  return stereotypes.contains(((JsonString) value).getString());
               } else if (value instanceof JsonArray) {
                  Object[] values = ((JsonArray) value).toArray();
                  for (Object val : values) {
                     if (val instanceof JsonString) {
                        if (stereotypes.contains(((JsonString) val).getString())) {
                           return true;
                        }
                     }
                  }
               }
            }
         }
         return false;
      };
   }

}
