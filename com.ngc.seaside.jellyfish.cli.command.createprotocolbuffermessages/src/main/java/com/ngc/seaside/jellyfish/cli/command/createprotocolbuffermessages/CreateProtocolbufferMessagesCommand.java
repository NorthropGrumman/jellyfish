package com.ngc.seaside.jellyfish.cli.command.createprotocolbuffermessages;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultUsage;
import com.ngc.seaside.command.api.IParameterCollection;
import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.DefaultJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandProvider;
import com.ngc.seaside.jellyfish.cli.command.createdomain.CreateDomainCommand;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component(service = IJellyFishCommand.class)
public class CreateProtocolbufferMessagesCommand implements IJellyFishCommand {

   private static final String NAME = "create-protocolbuffer-messages";
   private static final IUsage USAGE = createUsage();

   public static final String EXAMPLE_PROPERTY = "example";

   private ILogService logService;
   private IJellyFishCommandProvider jellyfishCommandProvider;

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
      
      //jellyfishCommandProvider.run("create-domain", commandOptions);
      
      jellyfishCommandProvider.run("create-domain", DefaultJellyFishCommandOptions.mergeWith(commandOptions,
         new DefaultParameter<String>(CreateDomainCommand.EXTENSION_PROPERTY).setValue("proto"),
         new DefaultParameter<String>(CreateDomainCommand.BUILD_GRADLE_TEMPLATE_PROPERTY).setValue("ProtoBufferMsgsBuildGradle")));

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
    * Sets the JellyFish Command Provider
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeJellyFishCommandProvider")
   public void setJellyFishCommandProvider(IJellyFishCommandProvider ref) {
      this.jellyfishCommandProvider = ref;
   }
   
   /**
    * Remove the JellyFish Command Provider
    */
   public void removeJellyFishCommandProvider(IJellyFishCommandProvider ref) {
      setJellyFishCommandProvider(null);
   }
   
   /**
    * Create the usage for this command.
    *
    * @return the usage.
    */
   private static IUsage createUsage() {
      // TODO Auto-generated method stub
      return new DefaultUsage("Description of create-protocolbuffer-messages command", new DefaultParameter(EXAMPLE_PROPERTY).setDescription("Description of example property").setRequired(false));
   }
}
