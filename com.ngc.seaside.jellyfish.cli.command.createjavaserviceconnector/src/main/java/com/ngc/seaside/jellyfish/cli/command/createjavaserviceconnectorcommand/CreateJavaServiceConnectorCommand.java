package com.ngc.seaside.jellyfish.cli.command.createjavaserviceconnectorcommand;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.service.promptuser.api.IPromptUserService;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultUsage;
import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component(service = IJellyFishCommand.class)
public class CreateJavaServiceConnectorCommand implements IJellyFishCommand {

   private static final String NAME = "create-java-service-connector";
   private static final IUsage USAGE = createUsage();

   public static final String EXAMPLE_PROPERTY = "example";

   private ILogService logService;
   private IPromptUserService promptService;

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
      // TODO Auto-generated method stub
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
      // TODO Auto-generated method stub
      return new DefaultUsage("Description of create-java-service-connector command", new DefaultParameter(EXAMPLE_PROPERTY).setDescription("Description of example property").setRequired(false));
   }

}
