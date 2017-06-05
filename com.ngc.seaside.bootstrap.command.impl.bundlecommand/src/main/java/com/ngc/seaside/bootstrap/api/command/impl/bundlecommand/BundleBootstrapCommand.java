package com.ngc.seaside.bootstrap.api.command.impl.bundlecommand;

import com.google.inject.Inject;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.api.IBootstrapCommand;
import com.ngc.seaside.bootstrap.api.IBootstrapCommandOptions;
import com.ngc.seaside.command.api.DefaultUsage;
import com.ngc.seaside.command.api.IUsage;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 * @author justan.provence@ngc.com
 */
public class BundleBootstrapCommand implements IBootstrapCommand {
   private final static String NAME = "bundle";

   private ILogService logService;

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
   @Inject
   public void setLogService(ILogService ref) {
      this.logService = ref;
   }

   /**
    * Remove log service.
    */
   public void removeLogService(ILogService ref) {
      setLogService(null);
   }

   @Override
   public String getName() {
      return NAME;
   }

   @Override
   public IUsage getUsage() {
      return new DefaultUsage("Fix me");
   }

   @Override
   public void run(IBootstrapCommandOptions commandOptions) {
      logService.info(getClass(), "Running command %s", NAME);
   }

   @Override
   public String toString() {
       return getName();
   }
}
