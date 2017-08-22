package com.ngc.seaside.jellyfish.cli.command.createjavacucumbertests;

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
import com.ngc.seaside.jellyfish.cli.command.createjavacucumbertests.dto.CucumberDto;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

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
import java.util.Collections;
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
   private static final String OUTPUT_PROPERTY = "output";
   static final String DEFAULT_OUTPUT_DIRECTORY = ".";

   private ILogService logService;
   private IPromptUserService promptService;
   private ITemplateService templateService;

   @Override
   public void run(IJellyFishCommandOptions commandOptions) {

      DefaultParameterCollection parameters = new DefaultParameterCollection();
      parameters.addParameters(commandOptions.getParameters().getAllParameters());

      if (!parameters.containsParameter(MODEL_PROPERTY)) {
         String modelId = promptService.prompt(MODEL_PROPERTY, null, null);
         parameters.addParameter(new DefaultParameter<>(MODEL_PROPERTY, modelId));
      }

      ISystemDescriptor systemDescriptor = commandOptions.getSystemDescriptor();
      String modelId = parameters.getParameter(MODEL_PROPERTY).getStringValue();
      final IModel model = systemDescriptor.findModel(modelId)
                                           .orElseThrow(() -> new CommandException("Unknown model:" + modelId));
      parameters.addParameter(new DefaultParameter<>(MODEL_OBJECT_PROPERTY, model));

      if (!parameters.containsParameter(OUTPUT_DIRECTORY_PROPERTY)) {
         String input = promptService.prompt(OUTPUT_DIRECTORY_PROPERTY, null, null);
         parameters.addParameter(new DefaultParameter<>(OUTPUT_DIRECTORY_PROPERTY, input));
      }

      final Path outputDirectory = Paths.get(parameters.getParameter(OUTPUT_DIRECTORY_PROPERTY).getStringValue());
      doCreateDirectories(outputDirectory);

      if (!parameters.containsParameter(GROUP_ID_PROPERTY)) {
         parameters.addParameter(new DefaultParameter<>(GROUP_ID_PROPERTY, model.getParent().getName()));
      }
      final String groupId = parameters.getParameter(GROUP_ID_PROPERTY).getStringValue();

      final String baseArtifact;
      final String artifactId;
      if (!parameters.containsParameter(ARTIFACT_ID_PROPERTY)) {
         baseArtifact = model.getName().toLowerCase();
         artifactId = baseArtifact + ".tests";
      } else {
         baseArtifact = parameters.getParameter(ARTIFACT_ID_PROPERTY).getStringValue();
         artifactId = baseArtifact;
      }
      parameters.addParameter(new DefaultParameter<String>(ARTIFACT_ID_PROPERTY, artifactId));

      final String basePackage = evaluatePackage(commandOptions, groupId, baseArtifact);
      final String packageName = evaluatePackage(commandOptions, groupId, artifactId);
      final String projectName = packageName;

      // If the REFRESH_FEATURE_FILES_PROPERTY is set, then do not invoke the template service.
      if (!evaluateBoolean(commandOptions.getParameters(), REFRESH_FEATURE_FILES_PROPERTY)) {

         CucumberDto dto = new CucumberDto().setArtifactId(artifactId)
                                            .setGroupId(groupId)
                                            .setProjectName(projectName)
                                            .setBasePackage(basePackage)
                                            .setPackageName(packageName)
                                            .setClassName(model.getName());

         parameters.addParameter(new DefaultParameter<>("dto", dto));

         final boolean clean = evaluateBoolean(commandOptions.getParameters(), CLEAN_PROPERTY);
         templateService.unpack(CreateJavaCucumberTestsCommand.class.getPackage().getName(),
            parameters,
            outputDirectory,
            clean);
         logService.info(CreateJavaCucumberTestsCommand.class, "%s project successfully created", model.getName());
         doAddProject(parameters);
      }
      copyFeatureFilesToGeneratedProject(commandOptions, model, outputDirectory.resolve(projectName));
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

   protected void doCreateDirectories(Path outputDirectory) {
      try {
         Files.createDirectories(outputDirectory);
      } catch (IOException e) {
         logService.error(CreateJavaCucumberTestsCommand.class, e);
         throw new CommandException(e);
      }
   }

   private static String evaluatePackage(IJellyFishCommandOptions commandOptions, String groupId, String artifactId) {
      return String.format("%s.%s", groupId, artifactId);
   }

   /**
    * Retrieve the output property value based on user input. Default is standard output
    *
    * @param commandOptions Jellyfish command options containing user params
    */
   protected Path evaluateOutput(IJellyFishCommandOptions commandOptions) {
      Path output;
      if (commandOptions.getParameters().containsParameter(REFRESH_FEATURE_FILES_PROPERTY)) {
         String outputUri = commandOptions.getParameters().getParameter(OUTPUT_PROPERTY).getStringValue();
         output = Paths.get(outputUri);

         if (!output.isAbsolute()) {
            output = commandOptions.getSystemDescriptorProjectPath().toAbsolutePath().resolve(outputUri);
         }

         return output.toAbsolutePath();
      } else {
         return null;
      }
   }

   /**
    * Copies feature files from a System Descriptor project to a newly generated test project. Only feature files that
    * apply to scenarios in the given model will be copied. Any features files that are already in the test project
    * will be deleted before coping the new files.
    *
    * @param models the model for which the feature files will be copied
    * @param commandOptions the options the command was run with
    * @param generatedProjectDirectory the directory that contains the generated tests project
    * @throws IOException
    */
   private void copyFeatureFilesToGeneratedProject(IJellyFishCommandOptions commandOptions, IModel model,
            Path generatedProjectDirectory) {
      removeOldFeatureFiles(generatedProjectDirectory, model);

      // First, find the feature files that apply to the model.
      TreeMap<String, FeatureFile> features = new TreeMap<>(Collections.reverseOrder());
      final PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**.feature");
      final Path gherkin = commandOptions.getSystemDescriptorProjectPath()
                                         .resolve(Paths.get("src", "test", "gherkin"))
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

      // Second, copy the files to the generated project.
      final Path destination = generatedProjectDirectory.resolve(Paths.get("src", "main", "resources"));
      for (FeatureFile feature : features.values()) {
         Path featureDestination = destination.resolve(feature.getRelativePath());
         try {
            Files.createDirectories(featureDestination.getParent());
            Files.copy(feature.getAbsolutePath(), featureDestination);
         } catch (IOException e) {
            throw new CommandException("Failed to copy " + feature.getAbsolutePath() + " to " + featureDestination, e);
         }
      }
   }

   private void removeOldFeatureFiles(Path testsProjectDirectory, IModel model) {
      deleteDir(testsProjectDirectory.resolve(model.getParent().getName()).toFile());
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
            "If false, copy the feature files and resources from the system descriptor project into src/main/resources.")
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
