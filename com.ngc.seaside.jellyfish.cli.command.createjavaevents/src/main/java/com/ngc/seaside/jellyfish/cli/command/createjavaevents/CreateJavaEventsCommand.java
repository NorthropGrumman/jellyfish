package com.ngc.seaside.jellyfish.cli.command.createjavaevents;

import com.google.common.base.Preconditions;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.DefaultJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandProvider;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component(service = IJellyFishCommand.class)
public class CreateJavaEventsCommand implements IJellyFishCommand {

   static final String DEFAULT_PACKAGE_SUFFIX = "events";
   static final String NAME = "create-java-events";

   static final String PACKAGE_SUFFIX_PROPERTY = "packageSuffix";
   static final String CREATE_DOMAIN_COMMAND_NAME = "create-domain";

   private ILogService logService;
   private IJellyFishCommandProvider jellyFishCommandProvider;

   @Override
   public String getName() {
      return NAME;
   }

   @Override
   public IUsage getUsage() {
      IJellyFishCommand command = jellyFishCommandProvider.getCommand(CREATE_DOMAIN_COMMAND_NAME);
      Preconditions.checkState(command != null,
                               "this command requires the 'crate-domain' command to be available!");
      return command.getUsage();
   }

   @Override
   public void run(IJellyFishCommandOptions commandOptions) {
      // If the packageSuffixProperty is not provided, set it to the default.
      IJellyFishCommandOptions delegateOptions = commandOptions;
      if (!delegateOptions.getParameters().containsParameter(PACKAGE_SUFFIX_PROPERTY)) {
         delegateOptions = DefaultJellyFishCommandOptions.mergeWith(
               commandOptions,
               new DefaultParameter<>(PACKAGE_SUFFIX_PROPERTY, DEFAULT_PACKAGE_SUFFIX));
      }
      jellyFishCommandProvider.run(CREATE_DOMAIN_COMMAND_NAME, delegateOptions);
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
    * @param ref the log service
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         unbind = "removeLogService")
   public void setLogService(ILogService ref) {
      this.logService = ref;
   }

   /**
    * Removes log service.
    */
   public void removeLogService(ILogService ref) {
      setLogService(null);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         unbind = "removeLogService")
   public void setJellyFishCommandProvider(IJellyFishCommandProvider ref) {
      this.jellyFishCommandProvider = ref;
   }

   public void removeJellyFishCommandProvider(IJellyFishCommandProvider ref) {
      setJellyFishCommandProvider(null);
   }

}
