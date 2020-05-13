/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.ngc.seaside.jellyfish.cli.command.createdomain;

import com.ngc.blocs.domain.impl.common.generated.DomainConfiguration;
import com.ngc.blocs.domain.impl.common.generated.ObjectFactory;
import com.ngc.blocs.domain.impl.common.generated.Tdomain;
import com.ngc.blocs.domain.impl.common.generated.Tobject;
import com.ngc.blocs.domain.impl.common.generated.Tproperty;
import com.ngc.blocs.jaxb.impl.common.JAXBUtilities;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.service.resource.api.IResourceService;
import com.ngc.seaside.jellyfish.api.CommandException;
import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.DefaultUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IParameterCollection;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.jellyfish.service.name.api.IProjectInformation;
import com.ngc.seaside.jellyfish.service.name.api.IProjectNamingService;
import com.ngc.seaside.jellyfish.service.template.api.ITemplateService;
import com.ngc.seaside.jellyfish.utilities.command.AbstractJellyfishCommand;
import com.ngc.seaside.jellyfish.utilities.resource.ITemporaryFileResource;
import com.ngc.seaside.jellyfish.utilities.resource.TemporaryFileResource;
import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;
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
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component(service = IJellyFishCommand.class)
public class CreateDomainCommand extends AbstractJellyfishCommand {

   private static final String NAME = "create-domain";
   private static final String DEFAULT_DOMAIN_TEMPLATE_FILE = "service-domain.java.vm";
   static final String BLOCS_PLUGINS_DEPENDENCY = "com.ngc.blocs:gradle.plugin";

   public static final String OUTPUT_DIRECTORY_PROPERTY = CommonParameters.OUTPUT_DIRECTORY.getName();
   public static final String CLEAN_PROPERTY = CommonParameters.CLEAN.getName();

   public static final String EXTENSION_PROPERTY = "extension";
   public static final String DOMAIN_TEMPLATE_FILE_PROPERTY = "domainTemplateFile";
   public static final String USE_VERBOSE_IMPORTS_PROPERTY = "useVerboseImports";

   // Note these parameters can only be used programmatically. Thus, we don't include them the usage object that
   // the user sees.
   public static final String PACKAGE_GENERATOR_PROPERTY = "packageGenerator";
   public static final String PROJECT_NAMER_PROPERTY = "projectNamer";
   public static final String GENERATED_OBJECT_PREDICATE_PROPERTY = "generatedObjectPredicate";

   static final Path DOMAIN_PATH = Paths.get("src", "main", "resources", "domain");

   private IResourceService resourceService;

   public CreateDomainCommand() {
      super(NAME);
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
   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         unbind = "removeLogService")
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

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC)
   public void setBuildManagementService(IBuildManagementService ref) {
      this.buildManagementService = ref;
   }

   public void removeBuildManagementService(IBuildManagementService ref) {
      setBuildManagementService(null);
   }

   @Override
   protected IUsage createUsage() {
      return new DefaultUsage("Generate a BLoCS domain model gradle project.",
                              CommonParameters.GROUP_ID.advanced(),
                              CommonParameters.ARTIFACT_ID.advanced(),
                              CommonParameters.OUTPUT_DIRECTORY.required(),
                              CommonParameters.MODEL.required(),
                              CommonParameters.CLEAN.optional(),
                              CommonParameters.UPDATE_GRADLE_SETTING.advanced(),
                              CommonParameters.HEADER_FILE.advanced(),
                              new DefaultParameter<String>(DOMAIN_TEMPLATE_FILE_PROPERTY)
                                    .setDescription("The velocity template file")
                                    .advanced(),
                              new DefaultParameter<String>(USE_VERBOSE_IMPORTS_PROPERTY)
                                    .setDescription(
                                          "If true, imports from the same package will be included for "
                                          + "generated domains")
                                    .advanced(),
                              new DefaultParameter<String>(EXTENSION_PROPERTY)
                                    .setDescription("The extension of the generated domain files")
                                    .advanced());
   }

   @Override
   protected void doRun() {
      final IParameterCollection parameters = getOptions().getParameters();
      final IModel model = getModel();
      final Supplier<IProjectInformation> projectInfoSupplier = evaluateProjectNamerParameter(getOptions(), model);

      IProjectInformation projectInfo = projectInfoSupplier.get();
      final Path domainTemplateFile = evaluateDomainTemplateFile(parameters);
      final boolean clean = CommonParameters.evaluateBooleanParameter(parameters, CLEAN_PROPERTY);
      final boolean useVerboseImports = CommonParameters.evaluateBooleanParameter(parameters,
                                                                                  USE_VERBOSE_IMPORTS_PROPERTY);
      final Function<INamedChild<IPackage>, String> packageGenerator =
            evaluatePackageGeneratorParameter(getOptions());
      final Predicate<INamedChild<IPackage>> generatedObjectPredicate = evaluateObjectPredicate(getOptions());

      final Path outputDir = evaluateOutputDirectory(parameters);
      final Path projectDir = evaluateProjectDirectory(outputDir, projectInfo.getDirectoryName(), clean);

      final Path xmlFile = projectDir.resolve(DOMAIN_PATH).resolve(model.getFullyQualifiedName() + ".xml");
      final Set<String> exportPackages = generateDomainXml(xmlFile,
                                                           model,
                                                           generatedObjectPredicate,
                                                           packageGenerator,
                                                           useVerboseImports);

      createGradleBuild(projectDir, getOptions(), domainTemplateFile, exportPackages, clean);
      createDomainTemplate(projectDir, domainTemplateFile);
      if (CommonParameters.evaluateBooleanParameter(getOptions().getParameters(),
                                                    CommonParameters.UPDATE_GRADLE_SETTING.getName(),
                                                    true)) {
         buildManagementService.registerProject(getOptions(), projectInfo);
      }

      // Register blocs plugins as a required dependency.
      buildManagementService.registerDependency(getOptions(), BLOCS_PLUGINS_DEPENDENCY);
   }

   @SuppressWarnings("unchecked")
   private Predicate<INamedChild<IPackage>> evaluateObjectPredicate(IJellyFishCommandOptions options) {
      Predicate<INamedChild<IPackage>> predicate = __ -> true;
      if (options.getParameters().containsParameter(GENERATED_OBJECT_PREDICATE_PROPERTY)) {
         predicate = (Predicate<INamedChild<IPackage>>) options.getParameters()
               .getParameter(GENERATED_OBJECT_PREDICATE_PROPERTY)
               .getValue();
      }
      return predicate;
   }

   @SuppressWarnings("unchecked")
   private Function<INamedChild<IPackage>, String> evaluatePackageGeneratorParameter(IJellyFishCommandOptions options) {
      Function<INamedChild<IPackage>, String> generator = null;
      if (options.getParameters().containsParameter(PACKAGE_GENERATOR_PROPERTY)) {
         generator = (Function<INamedChild<IPackage>, String>) options.getParameters()
               .getParameter(PACKAGE_GENERATOR_PROPERTY)
               .getValue();
      }
      if (generator == null) {
         generator = value -> packageNamingService.getDomainPackageName(options, value);
      }
      return generator;
   }

   @SuppressWarnings("unchecked")
   private Supplier<IProjectInformation> evaluateProjectNamerParameter(IJellyFishCommandOptions options, IModel model) {
      Supplier<IProjectInformation> namer;
      if (options.getParameters().containsParameter(PROJECT_NAMER_PROPERTY)) {
         namer = (Supplier<IProjectInformation>) options.getParameters()
               .getParameter(PROJECT_NAMER_PROPERTY)
               .getValue();
      } else {
         // If no supplier is given, just default.
         namer = () -> projectNamingService.getDomainProjectName(options, model);
      }
      return namer;
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
      final Path outputDir = Paths.get(parameters.getParameter(OUTPUT_DIRECTORY_PROPERTY).getStringValue());

      return outputDir;
   }

   /**
    * Creates and returns the path to the domain project directory.
    *
    * @param outputDir   output directory
    * @param projDirName project directory name
    * @param clean       whether or not to delete the contents of the directory
    * @return the path to the domain project directory
    * @throws CommandException if an error occurred in creating the project directory
    */
   private Path evaluateProjectDirectory(Path outputDir, String projDirName, boolean clean) {
      final Path projectDir = outputDir.resolve(Paths.get(projDirName));
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
    * Creates the build.gradle file for the domain project
    *
    * @param projectDir         path project directory
    * @param commandOptions     the command options used to run the command
    * @param domainTemplateFile path to domain template file
    * @param packages           set of packages to export
    * @throws CommandException if an error occurred generating the build.gradle file
    */
   private void createGradleBuild(Path projectDir,
                                  IJellyFishCommandOptions commandOptions,
                                  Path domainTemplateFile,
                                  Set<String> packages,
                                  boolean clean) {
      String extension = "java";
      if (commandOptions.getParameters().containsParameter(EXTENSION_PROPERTY)) {
         extension = commandOptions.getParameters().getParameter(EXTENSION_PROPERTY).getStringValue();
      }

      DefaultParameterCollection parameters = new DefaultParameterCollection(commandOptions.getParameters());
      parameters.addParameter(new DefaultParameter<>("options", commandOptions));
      parameters.addParameter(new DefaultParameter<>("velocityFile", domainTemplateFile.getFileName()));
      parameters.addParameter(new DefaultParameter<>("packages", packages));
      parameters.addParameter(new DefaultParameter<>(EXTENSION_PROPERTY, extension));

      unpackDefaultTemplate(parameters, projectDir, clean);
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

   private Set<String> generateDomainXml(Path xmlFile, IModel model,
                                         Predicate<INamedChild<IPackage>> generatedObjectPredicate,
                                         Function<INamedChild<IPackage>, String> packageGenerator,
                                         boolean useVerboseImports) {

      try {
         Files.createDirectories(xmlFile.getParent());
      } catch (IOException e) {
         throw new CommandException(e);
      }

      Set<String> packages = new TreeSet<>();
      Set<IData> dataTypes = new LinkedHashSet<>();
      Set<IData> superDataTypes = new LinkedHashSet<>();
      Set<IEnumeration> enumerations = new LinkedHashSet<>();
      collectData(model, dataTypes, superDataTypes, enumerations);
      if (dataTypes.isEmpty()) {
         logService.warn(CreateDomainCommand.class, "No input/output data for model " + model.getFullyQualifiedName());
         return Collections.emptySet();
      }

      Tdomain domain = new Tdomain();
      DomainConfiguration config = new DomainConfiguration();
      config.setUseVerboseImports(useVerboseImports);
      domain.setConfig(config);

      for (IData data : dataTypes) {
         domain.getObject().add(convert(data, false, generatedObjectPredicate, packageGenerator, packages));
      }
      for (IData data : superDataTypes) {
         domain.getObject().add(convert(data, true, generatedObjectPredicate, packageGenerator, packages));
      }
      for (IEnumeration enumeration : enumerations) {
         domain.getObject().add(convertEnum(enumeration, generatedObjectPredicate, packageGenerator, packages));
      }

      ObjectFactory factory = new ObjectFactory();
      try {
         JAXBUtilities.write(xmlFile.toFile(), factory.createDomain(domain));
      } catch (IOException e) {
         throw new CommandException(e);
      }

      return packages;
   }

   /**
    * Adds all of the inputs and outputs, their corresponding data objects that they extend, and the nested data of
    * their fields to the given sets.
    *
    * @param model          model to search
    * @param dataTypes      data types used as input, outputs or fields
    * @param superDataTypes data types that are extends from any of the data in dataTypes
    * @param enumerations   enumeration fields used from dataTyeps or superDataTypes
    */
   private void collectData(IModel model, Set<IData> dataTypes, Set<IData> superDataTypes,
                            Set<IEnumeration> enumerations) {
      Queue<IData> queue = new ArrayDeque<>();
      model.getInputs().forEach(field -> queue.add(field.getType()));
      model.getOutputs().forEach(field -> queue.add(field.getType()));
      dataTypes.addAll(queue);
      while (!queue.isEmpty()) {
         IData next = queue.poll();
         IData parent = next.getExtendedDataType().orElse(null);
         while (parent != null) {
            if (superDataTypes.add(parent)) {
               queue.add(parent);
               parent = parent.getExtendedDataType().orElse(null);
            } else {
               break;
            }
         }
         for (IDataField field : next.getFields()) {
            switch (field.getType()) {
               case DATA:
                  IData dataType = field.getReferencedDataType();
                  if (dataTypes.add(dataType)) {
                     queue.add(dataType);
                  }
                  break;
               case ENUM:
                  enumerations.add(field.getReferencedEnumeration());
                  break;
               default:
                  break;
            }
         }
      }
      superDataTypes.removeAll(dataTypes);
   }

   /**
    * Converts the IData to a Tobject.
    *
    * @param data             IData
    * @param isAbstract       if this data is extended from another data object
    * @param packageGenerator package generating function
    * @param packages         set of packages that this method should add to
    * @return domain object
    */
   private static Tobject convert(IData data, boolean isAbstract,
                                  Predicate<INamedChild<IPackage>> generatedObjectPredicate,
                                  Function<INamedChild<IPackage>, String> packageGenerator, Set<String> packages) {
      final String pkg = packageGenerator.apply(data);
      final boolean generated = generatedObjectPredicate.test(data);
      if (generated) {
         packages.add(pkg);
      }

      Tobject object = new Tobject();
      object.setAbstract(isAbstract);
      data.getExtendedDataType().ifPresent(superDataType -> {
         String superPkg = packageGenerator.apply(superDataType);
         object.setExtends(superPkg + '.' + superDataType.getName());
      });
      object.setGenerated(generated);
      object.setClazz(pkg + '.' + data.getName());
      data.getFields().forEach(field -> object.getProperty().add(convert(field, packageGenerator)));
      return object;
   }

   /**
    * Converts the IDataField to a Tproperty.
    *
    * @param field            IDataField
    * @param packageGenerator package generating function
    * @return domain property
    */
   private static Tproperty convert(IDataField field, Function<INamedChild<IPackage>, String> packageGenerator) {
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
      String pkg;
      switch (field.getType()) {
         case BOOLEAN:
            property.setType("boolean");
            break;
         case DATA:
            IData dataRef = field.getReferencedDataType();
            pkg = packageGenerator.apply(dataRef);
            property.setType(pkg + '.' + dataRef.getName());
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
            IEnumeration enumRef = field.getReferencedEnumeration();
            pkg = packageGenerator.apply(enumRef);
            property.setType(pkg + "." + enumRef.getName());
            break;
         default:
            throw new IllegalStateException("Unknown field type: " + field.getType());
      }
      return property;
   }

   /**
    * Converts the IEnumeration to a Tobject.
    *
    * @param packageGenerator package generating function
    * @param packages         set of packages that this method should add to
    * @return domain object
    */
   private Tobject convertEnum(IEnumeration enumVal, Predicate<INamedChild<IPackage>> generatedObjectPredicate,
                               Function<INamedChild<IPackage>, String> packageGenerator,
                               Set<String> packages) {
      final String pkg = packageGenerator.apply(enumVal);
      final boolean generated = generatedObjectPredicate.test(enumVal);
      if (generated) {
         packages.add(pkg);
      }

      Tobject object = new Tobject();
      object.setClazz(pkg + '.' + enumVal.getName());
      object.setType("enum");
      object.setAbstract(false);
      object.setGenerated(generated);
      object.setEnumValues(enumVal.getValues().stream().collect(Collectors.joining(" ")));
      return object;
   }
}
