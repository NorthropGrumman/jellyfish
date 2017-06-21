package com.ngc.seaside.jellyfish.impl.provider;

import com.google.common.base.Preconditions;
import com.ngc.blocs.component.impl.common.DeferredDynamicReference;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.IBootstrapCommandProvider;
import com.ngc.seaside.bootstrap.service.parameter.api.IParameterService;
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
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default implementation of the IJellyFishCommandProvider interface.
 */
@Component(service = IJellyFishCommandProvider.class)
public class JellyFishCommandProvider implements IJellyFishCommandProvider {
   private final Map<String, IJellyFishCommand> commandMap = new ConcurrentHashMap<>();
   private ILogService logService;
   private IBootstrapCommandProvider bootstrapCommandProvider;
   private IParameterService parameterService;
   private ISystemDescriptorService sdService;
   
   /**
    * Ensure the dynamic references are added only after the activation of this Component.
    */
   private DeferredDynamicReference<IJellyFishCommand> commands =
            new DeferredDynamicReference<IJellyFishCommand>() {
               @Override
               protected void addPostActivate(IJellyFishCommand command) {
                  addCommand(command);
               }

               @Override
               protected void removePostActivate(IJellyFishCommand command) {
                  removeCommand(command);
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
      return new IUsage() {
         
         @Override
         public List<IParameter> getRequiredParameters() {
            return Collections.singletonList(new IParameter() {
               
               @Override
               public boolean isRequired() {
                  return true;
               }
               
               @Override
               public String getValue() {
                  return null;
               }
               
               @Override
               public String getName() {
                  return "root";
               }
            });
         }
         
         @Override
         public String getDescription() {
            return "JellyFish Description";
         }
         
         @Override
         public List<IParameter> getAllParameters() {
            return getRequiredParameters();
         }
      };
   }

   @Override
   public void addCommand(IJellyFishCommand command) {
      Preconditions.checkNotNull(command, "Command is null");
      Preconditions.checkNotNull(command.getName(), "Command name is null %s", command);
      Preconditions
               .checkArgument(!command.getName().isEmpty(), "Command Name is empty %s", command);

      logService.trace(getClass(), "Adding command '%s'", command.getName());
      commandMap.put(command.getName(), command);
   }

   @Override
   public void removeCommand(IJellyFishCommand command) {
      Preconditions.checkNotNull(command, "Command is null");
      Preconditions.checkNotNull(command.getName(), "Command name is null %s", command);
      logService.trace(getClass(), "Removing command '%s'", command.getName());
      commandMap.remove(command.getName()); 
   }
   
   /**
    * TODO this should be in the parameter service.
    *
    * @param output the template service's output
    * @return the collection of parameters.
    */
   private IJellyFishCommandOptions convert(IParameterCollection output) {
      IParameter rootDir = output.getParameter("root");
      Path path = Paths.get(rootDir.getValue());
      if (! Files.isDirectory(path)) {
         throw new IllegalArgumentException(rootDir.getValue() + " does not exist as a directory"); 
      }
      if (! Files.isDirectory(path.resolve("src").resolve("main").resolve("sd"))) {
         throw new IllegalArgumentException(rootDir.getValue() + " does not contain src/main/sd"); 
      }
      if (! Files.isDirectory(path.resolve("src").resolve("test").resolve("gherkin"))) {
            throw new IllegalArgumentException(rootDir.getValue() + " does not contain src/test/gherkin"); 
      }
      DefaultJellyFishCommandOptions def = new DefaultJellyFishCommandOptions();
      def.setParameters(output);
      def.setSystemDescriptor(getSystemDescriptor(path));
      return def;
   }
   
   private ISystemDescriptor getSystemDescriptor(Path path) {
      IParsingResult result = sdService.parseProject(path);
      if (!result.isSuccessful()) {
         result.getIssues().forEach(issue -> logService.error(this.getClass(), issue.getMessage()));
         throw new IllegalArgumentException();
      }
      return result.getSystemDescriptor();
   }
   
   private IJellyFishCommand lookupCommand(String cmd) {
      IJellyFishCommand command = commandMap.get(cmd);
      return commandMap.get(cmd);
   }

   @Override
   public void run(String[] arguments) {
      Preconditions.checkNotNull(arguments, "Arguments must not be null.");
      Preconditions.checkArgument(arguments.length > 0, "Arguments must not be empty.");
      // TODO Call IBootstrapCommandProvider from here???
      // What do I pass it?
      // Do I parse the -d option here and then pass the rest of the args to IBootstrapCommandProvider?
      
      bootstrapCommandProvider.run(arguments);   
      
      IParameterCollection collection = parameterService.parseParameters(getUsage(), Arrays.asList(arguments).subList(1, arguments.length));
      
      IJellyFishCommandOptions options = convert(collection);
      
      IJellyFishCommand command = lookupCommand(arguments[0]);
      if (command != null) {
         command.run(options);
      }
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
    * Set the IBootstrapCommandProvider.
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.STATIC,
            unbind = "removeIBootstrapCommandProvider")
   public void setIBootstrapCommandProvider(IBootstrapCommandProvider ref) {
      this.bootstrapCommandProvider = ref;
   }

   /**
    * Remove log service.
    */
   public void removeIBootstrapCommandProvider(IBootstrapCommandProvider ref) {
      setIBootstrapCommandProvider(null);
   }

   /**
    * Set the IParameterService.
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.STATIC,
            unbind = "removeIParameterService")
   public void setIParameterService(IParameterService ref) {
      this.parameterService = ref;
   }

   /**
    * Remove IParameterCollection.
    */
   public void removeIParameterService(IParameterService ref) {
      setIParameterService(null);
   }
   
   /**
    * Set the ISystemDescriptorService.
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.STATIC,
            unbind = "removeISystemDescriptorService")
   public void setISystemDescriptorService(ISystemDescriptorService ref) {
      this.sdService = ref;
   }

   /**
    * Remove ISystemDescriptorService.
    */
   public void removeISystemDescriptorService(ISystemDescriptorService ref) {
      setISystemDescriptorService(null);
   }
  
}
