package com.ngc.seaside.jellyfish.impl.provider;

import com.google.common.base.Preconditions;
import com.ngc.blocs.component.impl.common.DeferredDynamicReference;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.service.parameter.api.IParameterService;
import com.ngc.seaside.bootstrap.service.promptuser.api.IPromptUserService;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateOutput;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateService;
import com.ngc.seaside.bootstrap.service.template.api.TemplateServiceException;
import com.ngc.seaside.command.api.CommandException;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultParameterCollection;
import com.ngc.seaside.command.api.DefaultUsage;
import com.ngc.seaside.command.api.IParameter;
import com.ngc.seaside.command.api.IParameterCollection;
import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.DefaultJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandProvider;
import com.ngc.seaside.jellyfish.api.JellyFishCommandConfiguration;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;
import com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService;
import com.ngc.seaside.systemdescriptor.service.api.ParsingException;
import com.ngc.seaside.systemdescriptor.validation.api.Severity;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Default implementation of the IJellyFishCommandProvider interface.
 */
@Component(service = IJellyFishCommandProvider.class)
public class JellyFishCommandProvider implements IJellyFishCommandProvider {

   private final Map<String, IJellyFishCommand> commandMap = new ConcurrentHashMap<>();
   private ILogService logService;
   private IParameterService parameterService;
   private ISystemDescriptorService sdService;
   private ITemplateService templateService;
   private IPromptUserService promptService;

   /**
    * Ensure the dynamic references are added only after the activation of this Component.
    */
   private DeferredDynamicReference<IJellyFishCommand> commands = new DeferredDynamicReference<IJellyFishCommand>() {
      @Override
      protected void addPostActivate(IJellyFishCommand command) {
         doAddCommand(command);
      }

      @Override
      protected void removePostActivate(IJellyFishCommand command) {
         doRemoveCommand(command);
      }
   };

   @Activate
   public void activate() {
      commands.markActivated();
   }

   @Deactivate
   public void deactivate() {

   }

   @Override
   public IUsage getUsage() {
      List<IParameter<?>> parameters = new ArrayList<>();
      parameters.add(new DefaultParameter<>("inputDir").setRequired(false));
      parameters.add(CommonParameters.GROUP_ARTIFACT_VERSION_EXTENSION.required());
      return new DefaultUsage("JellyFish Description", parameters);
   }

   @Override
   public IJellyFishCommand getCommand(String commandName) {
      IJellyFishCommand command = commandMap.get(commandName);
      if (command == null) {
         logService.error(getClass(), "Unable to find command '%s'", commandName);
         return null;
      } else {
         return command;
      }
   }

   @Override
   public void addCommand(IJellyFishCommand command) {
      commands.add(command);
   }

   @Override
   public void removeCommand(IJellyFishCommand command) {
      commands.remove(command);
   }

   private IParameterCollection addMissingRequiredParameters(IParameterCollection parameters,
            IJellyFishCommand command) {
      List<IParameter<?>> requiredParams = command.getUsage().getRequiredParameters();

      DefaultParameterCollection newParamCollection = new DefaultParameterCollection();
      newParamCollection.addParameters(parameters.getAllParameters());
      for (IParameter<?> requiredParam : requiredParams) {
         if (!parameters.containsParameter(requiredParam.getName())) {
            IParameter<String> newParam = promptForParameter(requiredParam);
            newParamCollection.addParameter(newParam);
         }
      }

      return newParamCollection;
   }

   private IParameter<String> promptForParameter(IParameter<?> paramDesired) {
      String paramValue = promptService.prompt(paramDesired.getName(), null, null);
      return new DefaultParameter<>(paramDesired.getName(), paramValue);
   }

   @Override
   public void run(String[] arguments) {
      Preconditions.checkNotNull(arguments, "Arguments must not be null.");

      String[] validatedArgs;

      // If no input directory is provided, look in working directory
      if (arguments.length == 0) {
         throw new IllegalArgumentException("No command provided");
      }

      validatedArgs = arguments;

      String commandName = validatedArgs[0];
      logService.trace(getClass(), "Running command '%s'", commandName);

      IJellyFishCommand command = lookupCommand(validatedArgs[0]);
      if (command == null) {
         logService.error(getClass(), "Unable to find command '%s'", commandName);
         return;
      }

      IParameterCollection cmdLineParameters = parameterService.parseParameters(
         Arrays.asList(validatedArgs).subList(1, validatedArgs.length));

      cmdLineParameters = resolveImportantParameters(cmdLineParameters, command);
      
      cmdLineParameters = addMissingRequiredParameters(cmdLineParameters, command);

      IParameterCollection templateParameters = null;
      if (isCommandConfiguredForTemplateService(command)) {
         templateParameters = unpackTemplate(command, cmdLineParameters);
      }

      IJellyFishCommandOptions jellyFishCommandOptions = createCommandOptions(cmdLineParameters,
         templateParameters,
         command);
      command.run(jellyFishCommandOptions);
   }

   @Override
   public void run(String command, IJellyFishCommandOptions commandOptions) {
      Preconditions.checkNotNull(command, "command may not be null!");
      Preconditions.checkArgument(!command.trim().isEmpty(), "command may not be empty!");
      Preconditions.checkNotNull(commandOptions, "commandOptions may not be null!");
      IJellyFishCommand c = lookupCommand(command);
      Preconditions.checkArgument(c != null, "could not find command named %s!", command);
      c.run(commandOptions);
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
    * Sets log service.
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeTemplateService")
   public void setTemplateService(ITemplateService ref) {
      this.templateService = ref;
   }

   /**
    * Remove log service.
    */
   public void removeTemplateService(ITemplateService ref) {
      setTemplateService(null);
   }

   /**
    * Sets prompt service.
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removePromptService")
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
    * Set the IParameterService.
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeParameterService")
   public void setParameterService(IParameterService ref) {
      this.parameterService = ref;
   }

   /**
    * Remove paramater service.
    */
   public void removeParameterService(IParameterService ref) {
      setParameterService(null);
   }

   /**
    * Set the ISystemDescriptorService.
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeSystemDescriptorService")
   public void setSystemDescriptorService(ISystemDescriptorService ref) {
      this.sdService = ref;
   }

   /**
    * Remove the system descriptor service.
    */
   public void removeSystemDescriptorService(ISystemDescriptorService ref) {
      setSystemDescriptorService(null);
   }

   /**
    * Add the command.
    *
    * @param command the command to add.
    */
   protected void doAddCommand(IJellyFishCommand command) {
      Preconditions.checkNotNull(command, "Command is null");
      Preconditions.checkNotNull(command.getName(), "Command name is null %s", command);
      Preconditions.checkArgument(!command.getName().isEmpty(), "Command Name is empty %s", command);
      logService.trace(getClass(), "Adding command '%s'", command.getName());
      commandMap.put(command.getName(), command);
   }

   /**
    * Remove the command.
    *
    * @param command the command to remove.
    */
   protected void doRemoveCommand(IJellyFishCommand command) {
      Preconditions.checkNotNull(command, "Command is null");
      Preconditions.checkNotNull(command.getName(), "Command name is null %s", command);
      logService.trace(getClass(), "Removing command '%s'", command.getName());
      commandMap.remove(command.getName());
   }

   /**
    * This method is required due to an issue when BND tries to resolve the dependencies and the IBootstrapCommand
    * extends an interface that is typed.
    */
   @Reference(unbind = "removeCommandOSGi", service = IJellyFishCommand.class, cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
   protected void addCommandOSGi(IJellyFishCommand command) {
      addCommand(command);
   }

   protected void removeCommandOSGi(IJellyFishCommand command) {
      removeCommand(command);
   }

   /**
    * Unpack the templateContent if it exists. If not, just return an empty collection of parameters.
    *
    * @param command the command.
    * @param userSuppliedParameters the parameters the user passed in. These should overwrite any properties that exists
    *           in the templateContent.properties. meaning, if they pass in these parameters they
    *           should not be prompted!
    * @return the parameters that were required to be input for usage within the templateContent.
    */
   protected IParameterCollection unpackTemplate(IJellyFishCommand command,
            IParameterCollection userSuppliedParameters) {
      String templatePrefix = getCommandTemplatePrefix(command);
      /**
       * Unpack the templateContent
       */
      if (templateService.templateExists(templatePrefix)) {
         try {
            Path outputPath = Paths.get(".");
            if (userSuppliedParameters.containsParameter(CommonParameters.OUTPUT_DIRECTORY.getName())) {
               outputPath = Paths.get(userSuppliedParameters.getParameter(CommonParameters.OUTPUT_DIRECTORY.getName()).getStringValue());
            }

            logService.trace(getClass(),
               "Unpacking templateContent for '%s' to '%s'",
               userSuppliedParameters,
               outputPath);
            ITemplateOutput templateOutput = templateService.unpack(templatePrefix,
               userSuppliedParameters,
               outputPath,
               false);

            return convertParameters(templateOutput, outputPath);
         } catch (TemplateServiceException e) {
            logService.error(getClass(),
               e,
               "Unable to unpack the templateContent for command'%s'. Aborting",
               command);
         }
      }
      return null;
   }

   /**
    * Convert the templateContent output to a parameter collection. This includes the templateFinalOutputDir.
    *
    * @param output the templateContent service's output
    * @return the collection of parameters.
    */
   protected IParameterCollection convertParameters(ITemplateOutput output, Path outputPath) {
      IParameterCollection templateParameters = parameterService.parseParameters(output.getProperties());

      DefaultParameterCollection collection = new DefaultParameterCollection();
      DefaultParameter<?> outputDir = new DefaultParameter<>(CommonParameters.OUTPUT_DIRECTORY.getName(), outputPath.toString());
      DefaultParameter<?> templateOutputDir = new DefaultParameter<>("templateFinalOutputDirectory",
         output.getOutputPath().toString());
      collection.addParameter(outputDir);
      collection.addParameter(templateOutputDir);

      for (IParameter<?> templateParameter : templateParameters.getAllParameters()) {
         collection.addParameter(templateParameter);
      }

      return collection;
   }

   /**
    * Return the prefix used in order to look the command's templateContent up within the templates resource directory.
    * Currently this assumes that the naming convention for the command's package includes the same name used for
    * creating the templateContent zip. This is actually done for us using the correct build tools.
    *
    * @param command the command in which to create the prefix
    * @return the String representation of the command's package.
    */
   protected String getCommandTemplatePrefix(IJellyFishCommand command) {
      return command.getClass().getPackage().getName();
   }

   /**
    * Tries to resolve the inputDir and gave parameters
    * @param userInputParameters
    * @param command
    * @return
    */
   private IParameterCollection resolveImportantParameters(IParameterCollection userInputParameters, IJellyFishCommand command) {
      String inputDirName = "inputDir";
      String repositoryUrlName = CommonParameters.REPOSITORY_URL.getName();
      String gaveName = CommonParameters.GROUP_ARTIFACT_VERSION_EXTENSION.getName();
      
      IParameter<?> inputDir = userInputParameters.getParameter(inputDirName);
      IParameter<?> urlParameter = userInputParameters.getParameter(repositoryUrlName);
      IParameter<?> gaveParameter = userInputParameters.getParameter(gaveName);
      if (inputDir == null && urlParameter == null) {
         inputDir = new DefaultParameter<>(inputDirName, ".");
      }

      if (inputDir != null) {
         String dir = inputDir.getStringValue();
         if (gaveParameter == null) {
            Path projectInfo = Paths.get(dir, "build", "resources", "main", "project-info.properties");
            if (Files.isRegularFile(projectInfo)) {
               Properties properties = new Properties();
               try {
                  properties.load(Files.newInputStream(projectInfo));
                  if (properties.containsKey("gave")) {
                     String gaveValue = properties.getProperty("gave");
                     CommonParameters.parseGave(gaveValue);
                     gaveParameter = new DefaultParameter<>(gaveName, gaveValue);
                  }
               } catch (IOException e) {
               }
            }
         }
         if (gaveParameter != null) {
            CommonParameters.parseGave(gaveParameter.getStringValue());
         }
      } else if (urlParameter != null) {
         String gaveValue;
         if (gaveParameter == null) {
            gaveValue = promptService.prompt(gaveName, null, null);
            gaveParameter = new DefaultParameter<>(gaveName, gaveValue);
         } else {
            gaveValue = gaveParameter.getStringValue();
         }
         gaveValue = parseGave(gaveValue);
         File tempDir;
         try {
            tempDir = getArchiveFromUrl(urlParameter.getValue().toString(), gaveValue);
         } catch (IOException e) {
            throw new CommandException(e);
         }
         inputDir = new DefaultParameter<>(inputDirName, tempDir.getAbsolutePath());
      }

      DefaultParameterCollection parameters = new DefaultParameterCollection();
      parameters.addParameters(userInputParameters.getAllParameters());
      if (inputDir != null) {
         parameters.addParameter(inputDir);
      }
      if (gaveParameter != null) {
         parameters.addParameter(gaveParameter);
      }
      
      return parameters;
   }
   
   /**
    * This method converts into an {@link IJellyFishCommandOptions} object. The input directory should be the root
    * directory of a system descriptor project. At minimum, the project root should contain the directories of
    * src/main/sd and src/test/gherkin. If these requirements are met and the system descriptor files are valid
    * syntactically, the {@link ISystemDescriptor} model will be loaded into the {@link IJellyFishCommandOptions}
    * object. Otherwise, the application will exit with a thrown exception of {@link IllegalArgumentException} for
    * illegal directory structure
    *
    * @param userInputParameters the parameters that the user input on the command line
    * @param templateParameters the parameters that were fulfilled by the templateContent.properties file in the
    *           templateContent
    * @param command the command to execute
    * @return the JellyFish command options
    */
   private IJellyFishCommandOptions createCommandOptions(IParameterCollection userInputParameters,
            IParameterCollection templateParameters,
            IJellyFishCommand command) {

      DefaultJellyFishCommandOptions options = new DefaultJellyFishCommandOptions();

      IParameter<?> inputDir = userInputParameters.getParameter("inputDir");

      DefaultParameterCollection parameters = new DefaultParameterCollection();
      parameters.addParameters(userInputParameters.getAllParameters());
      if (templateParameters != null) {
         parameters.addParameters(templateParameters.getAllParameters());
      }
      options.setParameters(parameters);

      Path path = Paths.get(inputDir.getStringValue());
      options.setParsingResult(getParsingResult(path, doesCommandRequireValidSystemDescriptor(command)));
      options.setSystemDescriptorProjectPath(path);
      return options;
   }

   /**
    * This method uses the {@link ISystemDescriptorService} to parse the provided project. If errors occur, a {@link
    * ParsingException} is thrown along with a list of issues.
    *
    * @param path system descriptor project path
    * @param isValidDescriptorRequired if true and they system descriptor is invalid, a {@code CommandException} is
    *           thrown
    * @return the results of parsing
    */
   private IParsingResult getParsingResult(Path path, boolean isValidDescriptorRequired) {
      IParsingResult result = null;
      boolean isValid = path.toFile().isDirectory();
      if (!isValid && isValidDescriptorRequired) {
         throw new CommandException(String.format("%s either does not exists or is not a directory!", path));
      } else if (isValid) {
         result = doParseProject(path);
         isValid = result.isSuccessful();
         if (!isValid && isValidDescriptorRequired) {
            result.getIssues()
                  .stream()
                  .filter(issue -> issue.getSeverity() == Severity.ERROR)
                  .forEach(issue -> logService.error(JellyFishCommandProvider.class, issue));
            throw new CommandException("Command requires a valid SystemDescriptor but errors were encountered!");
         }
      }
      return result;
   }

   /**
    * Invokes the service to parse the project, converting any parsing exception to a {@link FailedParsingResult}.
    */
   private IParsingResult doParseProject(Path path) {
      IParsingResult result;
      try {
         result = sdService.parseProject(path);
      } catch (ParsingException | IllegalArgumentException e) {
         result = FailedParsingResult.fromException(e);
      }
      return result;
   }

   /**
    * This method looks up the {@link IJellyFishCommand} corresponding with the given string.
    *
    * @param cmd the string representation of a JellyFish command
    * @return the JellyFish command
    */
   private IJellyFishCommand lookupCommand(String cmd) {
      return commandMap.get(cmd);
   }

   /**
    * This method downloads an archive from the provided url and gave(group/artifact/version/extension) info
    *
    * @param url the string representation of the repository url
    * @param gave the string representation of the archive info
    */
   public File getArchiveFromUrl(String url, String gave) throws IOException {
      File tempDir = FileUtils.getTempDirectory();
      String[] fileName = gave.split("/");
      URL myUrl = new URL(url + gave);
      String destDirFileName = fileName[fileName.length - 1];
      String nameOfNewDir = FilenameUtils.removeExtension(destDirFileName);
      File destination = new File(tempDir.toString() + File.separator + nameOfNewDir);
      File file = new File(tempDir, destDirFileName);
      FileUtils.copyURLToFile(myUrl, file);

      // unzip the file to the given destination
      uZip(file.toString(), destination);

      logService.info(JellyFishCommandProvider.class, "temp archive location: " + destination);

      return destination;
   }

   /**
    * This method parses the gave(group/artifact/version/extension) info to create a path and file string from the
    * parameter input
    *
    * @param gave the string representation of the archive info
    */
   public String parseGave(String gave) {
      String[] parsed = CommonParameters.parseGave(gave);
      String group = parsed[0];
      String artifact = parsed[1];
      String version = parsed[2];
      String extension = parsed[3];

      String[] temp = group.split("\\.");
      String url = temp[0] + "/";
      for (int i = 1; i < temp.length; i++) {
         url = url + temp[i] + "/";
      }

      url += artifact + "/" + version + "/";
      url += artifact + "-" + version + "." + extension;

      return url;
   }

   /**
    * This method extracts a .zip file to a given destination
    *
    * @param zipFile the string representation of the path to the zip file
    * @param dest the string representation of the destination
    */
   public void uZip(String zipFile, File dest) throws FileNotFoundException {
      byte[] buffer = new byte[1024];
      File folder = new File(dest.toString());
      if (!folder.exists()) {
         folder.mkdir();
      }

      try {
         ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
         ZipEntry ze = zis.getNextEntry();

         while (ze != null) {

            // if zip entry is a directory - get next
            if (ze.isDirectory()) {
               ze = zis.getNextEntry();
               continue;
            }

            String fileName = ze.getName();
            File newFile = new File(dest + File.separator + fileName);

            // make parent dirs
            new File(newFile.getParent()).mkdirs();

            FileOutputStream fos = new FileOutputStream(newFile);

            int len;
            while ((len = zis.read(buffer)) > 0) {
               fos.write(buffer, 0, len);
            }
            fos.close();
            zis.closeEntry();
            ze = zis.getNextEntry();
         }
         zis.close();
         logService.debug(getClass(), "Extraction of SD project complete.");
      } catch (IOException e) {
         logService.error(getClass(), "Extraction of SD project failed!", e);
         throw new RuntimeException(e.getMessage(), e);
      }
   }

   private static boolean isCommandConfiguredForTemplateService(IJellyFishCommand command) {
      JellyFishCommandConfiguration config = command.getClass().getAnnotation(JellyFishCommandConfiguration.class);
      return config == null || config.autoTemplateProcessing();
   }

   private static boolean doesCommandRequireValidSystemDescriptor(IJellyFishCommand command) {
      JellyFishCommandConfiguration config = command.getClass().getAnnotation(JellyFishCommandConfiguration.class);
      return config == null || config.requireValidSystemDescriptor();
   }
}
