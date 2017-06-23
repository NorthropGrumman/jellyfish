package com.ngc.seaside.jellyfish.impl.provider;

import com.google.common.base.Preconditions;
import com.ngc.blocs.component.impl.common.DeferredDynamicReference;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.IBootstrapCommandProvider;
import com.ngc.seaside.bootstrap.service.parameter.api.IParameterService;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultUsage;
import com.ngc.seaside.command.api.IParameter;
import com.ngc.seaside.command.api.IParameterCollection;
import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.DefaultJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandProvider;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;
import com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService;
import com.ngc.seaside.systemdescriptor.service.api.ParsingException;

import org.eclipse.xtext.parser.ParseException;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * Default implementation of the IJellyFishCommandProvider interface.
 */
@Component(service = IJellyFishCommandProvider.class)
public class JellyFishCommandProvider implements IJellyFishCommandProvider
{

   private final Map<String, IJellyFishCommand> commandMap = new ConcurrentHashMap<>();
   private ILogService logService;
   private IBootstrapCommandProvider bootstrapCommandProvider;
   private IParameterService parameterService;
   private ISystemDescriptorService sdService;

   /**
    * Ensure the dynamic references are added only after the activation of this
    * Component.
    */
   private DeferredDynamicReference<IJellyFishCommand> commands = new DeferredDynamicReference<IJellyFishCommand>()
   {
      @Override
      protected void addPostActivate(IJellyFishCommand command)
      {
         addCommand(command);
      }

      @Override
      protected void removePostActivate(IJellyFishCommand command)
      {
         removeCommand(command);
      }
   };

   @Activate
   public void activate()
   {
      commands.markActivated();
   }

   @Deactivate
   public void deactivate()
   {

   }

   @Override
   public IUsage getUsage()
   {
      DefaultUsage usage = new DefaultUsage("JellyFish Description", Collections.singletonList(new DefaultParameter("inputDir", true)));
      return usage;
   }

   @Override
   public void addCommand(IJellyFishCommand command)
   {
      Preconditions.checkNotNull(command, "Command is null");
      Preconditions.checkNotNull(command.getName(), "Command name is null %s", command);
      Preconditions.checkArgument(!command.getName().isEmpty(), "Command Name is empty %s", command);
      logService.trace(getClass(), "Adding command '%s'", command.getName());
      commandMap.put(command.getName(), command);
   }

   @Override
   public void removeCommand(IJellyFishCommand command)
   {
      Preconditions.checkNotNull(command, "Command is null");
      Preconditions.checkNotNull(command.getName(), "Command name is null %s", command);
      logService.trace(getClass(), "Removing command '%s'", command.getName());
      commandMap.remove(command.getName());
   }

   @Override
   public void run(String[] arguments)
   {
      String[] validatedArgs;

      // If no input directory is provided, look in working directory
      if (arguments.length == 0) {
         throw new IllegalArgumentException("");
      }
      else {
         Pattern p = Pattern.compile("[a-zA-Z_][\\w-]*");
         if (!p.matcher(arguments[0]).matches()) {
            throw new IllegalArgumentException("Invalid command: " + arguments[0] + ". Expected format: " + p.pattern());
         }
         if (arguments.length == 1) {
            validatedArgs = new String[] { arguments[0], "-DinputDir=" + System.getProperty("user.dir") };
         }
         else {
            validatedArgs = arguments;
         }
      }

      IParameterCollection collection = parameterService.parseParameters(getUsage(), Arrays.asList(validatedArgs).subList(1, validatedArgs.length));
      IJellyFishCommandOptions jellyFishCommandOptions = convert(collection);

      IJellyFishCommand command = lookupCommand(validatedArgs[0]);

      if (command != null) {
         command.run(jellyFishCommandOptions);
      }
   }

   /**
    * Sets log service.
    *
    * @param ref
    *           the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeLogService")
   public void setLogService(ILogService ref)
   {
      this.logService = ref;
   }

   /**
    * Remove log service.
    */
   public void removeLogService(ILogService ref)
   {
      setLogService(null);
   }

   /**
    * Set the IBootstrapCommandProvider.
    *
    * @param ref
    *           the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeIBootstrapCommandProvider")
   public void setIBootstrapCommandProvider(IBootstrapCommandProvider ref)
   {
      this.bootstrapCommandProvider = ref;
   }

   /**
    * Remove bootstrap command provider.
    */
   public void removeIBootstrapCommandProvider(IBootstrapCommandProvider ref)
   {
      setIBootstrapCommandProvider(null);
   }

   /**
    * Set the IParameterService.
    *
    * @param ref
    *           the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeIParameterService")
   public void setIParameterService(IParameterService ref)
   {
      this.parameterService = ref;
   }

   /**
    * Remove paramater service.
    */
   public void removeIParameterService(IParameterService ref)
   {
      setIParameterService(null);
   }

   /**
    * Set the ISystemDescriptorService.
    *
    * @param ref
    *           the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeISystemDescriptorService")
   public void setISystemDescriptorService(ISystemDescriptorService ref)
   {
      this.sdService = ref;
   }

   /**
    * Remove the system descriptor service.
    */
   public void removeISystemDescriptorService(ISystemDescriptorService ref)
   {
      setISystemDescriptorService(null);
   }

   /**
    * This method converts into an {@link IJellyFishCommandOptions} object. The input directory should
    * be the root directory of a system descriptor project. At minimum, the project root should contain
    * the directories of src/main/sd and src/test/gherkin. If these requirements are met and the
    * system descriptor files are valid syntactically, the {@link ISystemDescriptor} model will be loaded
    * into the {@link IJellyFishCommandOptions} object. Otherwise, the application will exit with a
    * thrown exception of {@link IllegalArgumentException} for illegal directory structure or
    * {@link ParseException} for invalid system descriptor file contents.
    *
    * @param the
    *           parameter collection
    * @return the JellyFish command options
    */
   private IJellyFishCommandOptions convert(IParameterCollection output)
   {
      IParameter inputDir = output.getParameter("inputDir");
      Path path = Paths.get(inputDir.getValue());
      if (!Files.isDirectory(path)) {
         logService.error(getClass(), inputDir.getValue() + " does not exist as a directory");
         throw new IllegalArgumentException(inputDir.getValue() + " does not exist as a directory");
      }
      if (!Files.isDirectory(path.resolve("src").resolve("main").resolve("sd"))) {
         logService.error(getClass(), inputDir.getValue() + " does not contain src/main/sd");
         throw new IllegalArgumentException(inputDir.getValue() + " does not contain src/main/sd");
      }
      if (!Files.isDirectory(path.resolve("src").resolve("test").resolve("gherkin"))) {
         logService.error(getClass(), inputDir.getValue() + " does not contain src/test/gherkin");
         throw new IllegalArgumentException(inputDir.getValue() + " does not contain src/test/gherkin");
      }
      DefaultJellyFishCommandOptions def = new DefaultJellyFishCommandOptions();
      def.setParameters(output);
      def.setSystemDescriptor(getSystemDescriptor(path));
      return def;
   }

   /**
    * This method uses the {@link ISystemDescriptorService} to parse the provided project.
    * If errors occur, a {@link ParsingException} is thrown along with a list of issues.
    * 
    * @param path system descriptor project path
    * @return the system descriptor
    */
   private ISystemDescriptor getSystemDescriptor(Path path) {
      IParsingResult result = sdService.parseProject(path);
      if (!result.isSuccessful()) {
         result.getIssues().forEach(issue -> logService.error(this.getClass(), issue));
         throw new ParsingException("Error occurred parsing project");
      }
      return result.getSystemDescriptor();
   }

   /**
    * This method looks up the {@link IJellyFishCommand} corresponding with the given
    * string.
    * 
    * @param cmd the string representation of a JellyFish command
    * @return the JellyFish command
    */
   private IJellyFishCommand lookupCommand(String cmd) {
      return commandMap.get(cmd);
   }

}
