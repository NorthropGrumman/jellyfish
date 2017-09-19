package com.ngc.seaside.jellyfish.cli.command.createjavacucumbertests;

import com.ngc.blocs.service.log.api.ILogService;
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
import com.ngc.seaside.jellyfish.cli.command.createjavacucumbertests.dto.CucumberDto;
import com.ngc.seaside.jellyfish.service.codegen.api.IJavaServiceGenerationService;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.jellyfish.service.name.api.IProjectInformation;
import com.ngc.seaside.jellyfish.service.name.api.IProjectNamingService;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import org.apache.commons.io.FileUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.TreeMap;

@Component(service = IJellyFishCommand.class)
public class CreateJavaCucumberTestsCommand implements IJellyFishCommand {

   private static final String NAME = "create-java-cucumber-tests";
   private static final IUsage USAGE = createUsage();

   public static final String GROUP_ID_PROPERTY = "groupId";
   public static final String ARTIFACT_ID_PROPERTY = "artifactId";
   public static final String OUTPUT_DIRECTORY_PROPERTY = "outputDirectory";
   public static final String MODEL_PROPERTY = "model";
   public static final String CLEAN_PROPERTY = "clean";
   public static final String REFRESH_FEATURE_FILES_PROPERTY = "refreshFeatureFiles";

   public static final String MODEL_OBJECT_PROPERTY = "modelObject";

   private ILogService logService;
   private ITemplateService templateService;
   private IProjectNamingService projectNamingService;
   private IPackageNamingService packageNamingService;
   private IJavaServiceGenerationService generationService;

   @Override
   public void run(IJellyFishCommandOptions commandOptions) {

      DefaultParameterCollection parameters = new DefaultParameterCollection();
      parameters.addParameters(commandOptions.getParameters().getAllParameters());

      String modelId = parameters.getParameter(MODEL_PROPERTY).getStringValue();
      final IModel model = commandOptions.getSystemDescriptor()
                                         .findModel(modelId)
                                         .orElseThrow(() -> new CommandException("Unknown model:" + modelId));
      parameters.addParameter(new DefaultParameter<>(MODEL_OBJECT_PROPERTY, model));

      final Path outputDirectory = Paths.get(parameters.getParameter(OUTPUT_DIRECTORY_PROPERTY).getStringValue());
      doCreateDirectories(outputDirectory);

      IProjectInformation info = projectNamingService.getCucumberTestsProjectName(commandOptions, model);

      final String packageName = packageNamingService.getCucumberTestsPackageName(commandOptions, model);
      final String projectName = info.getDirectoryName();
      final boolean clean = evaluateBoolean(commandOptions.getParameters(), CLEAN_PROPERTY);

      if (!evaluateBoolean(commandOptions.getParameters(), REFRESH_FEATURE_FILES_PROPERTY)) {

         CucumberDto dto = new CucumberDto().setProjectName(projectName)
                                            .setPackageName(packageName)
                                            .setClassName(model.getName())
                                            .setTransportTopicsClass(generationService.getTransportTopicsDescription(commandOptions, model).getFullyQualifiedName())
                                            .setDependencies(new LinkedHashSet<>(Arrays.asList(
                                               projectNamingService.getMessageProjectName(commandOptions, model)
                                                                   .getArtifactId(),
                                               projectNamingService.getBaseServiceProjectName(commandOptions, model)
                                                                   .getArtifactId())));

         parameters.addParameter(new DefaultParameter<>("dto", dto));

         templateService.unpack(CreateJavaCucumberTestsCommand.class.getPackage().getName(),
            parameters,
            outputDirectory,
            clean);
         logService.info(CreateJavaCucumberTestsCommand.class, "%s project successfully created", model.getName());
         doAddProject(outputDirectory, info);
      }
      copyFeatureFilesToGeneratedProject(commandOptions, model, outputDirectory.resolve(projectName), clean);
   }

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
    * Sets project naming service.
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeProjectNamingService")
   public void setProjectNamingService(IProjectNamingService ref) {
      this.projectNamingService = ref;
   }

   /**
    * Remove project naming service.
    */
   public void removeProjectNamingService(IProjectNamingService ref) {
      setProjectNamingService(null);
   }
   
   /**
    * Sets package naming service.
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removePackageNamingService")
   public void setPackageNamingService(IPackageNamingService ref) {
      this.packageNamingService = ref;
   }

   /**
    * Remove package naming service.
    */
   public void removePackageNamingService(IPackageNamingService ref) {
      setPackageNamingService(null);
   }
   
   /**
    * Sets java service generation service.
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeJavaServiceGenerationService")
   public void setJavaServiceGenerationService(IJavaServiceGenerationService ref) {
      this.generationService = ref;
   }

   /**
    * Remove java service generation service.
    */
   public void removeJavaServiceGenerationService(IJavaServiceGenerationService ref) {
      setJavaServiceGenerationService(null);
   }

   protected void doAddProject(Path outputDirectory, IProjectInformation projectInformation) {
      DefaultParameterCollection parameters = new DefaultParameterCollection();
      parameters.addParameter(new DefaultParameter<>(OUTPUT_DIRECTORY_PROPERTY, outputDirectory));
      parameters.addParameter(new DefaultParameter<>(GROUP_ID_PROPERTY, projectInformation.getGroupId()));
      parameters.addParameter(new DefaultParameter<>(ARTIFACT_ID_PROPERTY, projectInformation.getArtifactId()));
      try {
         if (!GradleSettingsUtilities.tryAddProject(parameters)) {
            logService.warn(getClass(), "Unable to add the new project to settings.gradle.");
         }
      } catch (FileUtilitiesException e) {
         logService.warn(getClass(), e, "Unable to add the new project to settings.gradle.");
         throw new CommandException(e);
      }
   }

   protected void doCreateDirectories(Path outputDirectory) {
      try {
         Files.createDirectories(outputDirectory);
      } catch (IOException e) {
         logService.error(CreateJavaCucumberTestsCommand.class, e);
         throw new CommandException(e);
      }
   }

   /**
    * Copies feature files from a System Descriptor project to a newly generated test project. Only feature files that
    * apply to scenarios in the given model will be copied. Any features files that are already in the test project
    * will be deleted before coping the new files.
    *
    * @param model the model for which the feature files will be copied
    * @param commandOptions the options the command was run with
    * @param generatedProjectDirectory the directory that contains the generated tests project
    * @param clean if true, deletes the features and resources before copying them
    * @throws IOException
    */
   private void copyFeatureFilesToGeneratedProject(IJellyFishCommandOptions commandOptions, IModel model,
            Path generatedProjectDirectory, boolean clean) {

      // First, find the feature files that apply to the model.
      TreeMap<String, FeatureFile> features = new TreeMap<>(Collections.reverseOrder());
      final PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**.feature");
      final Path gherkin = commandOptions.getSystemDescriptorProjectPath()
                                         .resolve(Paths.get("src", "test", "gherkin"))
                                         .toAbsolutePath();
      final Path dataFile = commandOptions.getSystemDescriptorProjectPath()
                                          .resolve(Paths.get("src", "test", "resources", "data"))
                                          .toAbsolutePath();

      String packages = model.getParent().getName();
      Path modelPath = Paths.get(packages.replace('.', File.separatorChar));

      Path featureFilesRoot = gherkin.resolve(modelPath);

      try {
         Files.list(featureFilesRoot)
              .filter(Files::isRegularFile)
              .filter(matcher::matches)
              .filter(f -> f.getFileName().toString().startsWith(model.getName() + '.'))
              .map(Path::toAbsolutePath)
              .forEach(path -> features.put(path.toString(), new FeatureFile(path, gherkin.relativize(path))));
      } catch (IOException e) {
         throw new CommandException(e);
      }

      final Path destination = generatedProjectDirectory.resolve(Paths.get("src", "main", "resources"));
      final Path dataDestination = generatedProjectDirectory.resolve(Paths.get("src", "main", "resources", "data"));

      deleteDir(destination.resolve(model.getParent().getName()).toFile());
      deleteDir(dataDestination.toFile());

      for (FeatureFile feature : features.values()) {
         Path featureDestination = destination.resolve(feature.getRelativePath());
         try {
            Files.createDirectories(featureDestination.getParent());
            Files.copy(feature.getAbsolutePath(), featureDestination, StandardCopyOption.REPLACE_EXISTING);
         } catch (IOException e) {
            throw new CommandException("Failed to copy " + feature.getAbsolutePath() + " to " + featureDestination, e);
         }
      }
      if (Files.isDirectory(dataFile)) {

         try {

            FileUtils.copyDirectory(dataFile.toFile(), dataDestination.toFile());

         } catch (IOException e) {
            throw new CommandException("Failed to copy resoureces  to " + destination, e);
         }
      }
   }

   /**
    * Create the usage for this command.
    *
    * @return the usage.
    */
   @SuppressWarnings("rawtypes")
   private static IUsage createUsage() {
      return new DefaultUsage("Generates the gradle distribution project for a Java application",
         new DefaultParameter(GROUP_ID_PROPERTY)
                                                .setDescription(
                                                   "The project's group ID. (default: the package in the model)")
                                                .setRequired(false),
         new DefaultParameter(ARTIFACT_ID_PROPERTY).setDescription(
            "The project's artifact ID. (default: model name in lowercase + '.distribution')")
                                                   .setRequired(false),
         new DefaultParameter(OUTPUT_DIRECTORY_PROPERTY)
                                                        .setDescription("Base directory in which to output the project")
                                                        .setRequired(true),
         new DefaultParameter(MODEL_PROPERTY)
                                             .setDescription("The fully qualified path to the system descriptor model")
                                             .setRequired(true),
         new DefaultParameter(CLEAN_PROPERTY).setDescription(
            "If true, recursively deletes the domain project (if it already exists), before generating the it again")
                                             .setRequired(false),
         new DefaultParameter(REFRESH_FEATURE_FILES_PROPERTY).setDescription(
            "If true, only copy the feature files and resources from the system descriptor project into src/main/resources.")
                                                             .setRequired(false));
   }

   private static boolean evaluateBoolean(IParameterCollection parameters, String parameter) {
      return parameters.containsParameter(parameter)
         && Boolean.valueOf(parameters.getParameter(parameter).getStringValue().toLowerCase());
   }

   /**
    * Helper method to delete folder/files
    *
    * @param file file/folder to delete
    */
   private static void deleteDir(File file) {
      File[] contents = file.listFiles();
      if (contents != null) {
         for (File f : contents) {
            deleteDir(f);
         }
      }
      file.delete();
   }

}
