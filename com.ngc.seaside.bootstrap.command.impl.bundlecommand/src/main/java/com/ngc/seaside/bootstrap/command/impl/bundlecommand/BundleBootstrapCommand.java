package com.ngc.seaside.bootstrap.command.impl.bundlecommand;

import com.google.inject.Inject;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.IBootstrapCommand;
import com.ngc.seaside.bootstrap.IBootstrapCommandOptions;
import com.ngc.seaside.command.api.DefaultUsage;
import com.ngc.seaside.command.api.IParameterCollection;
import com.ngc.seaside.command.api.IUsage;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

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
      logService.trace(getClass(), "Running command %s", NAME);

      IParameterCollection parameters = commandOptions.getParameters();
      if(parameters.containsParameter("outputDirectory")) {
         Path outputDirectory = Paths.get(parameters.getParameter("outputDirectory").getValue());

         Path settings = Paths.get(outputDirectory.getParent().normalize().toString(), "settings.gradle");

         if(Files.exists(settings)) {
            try {
               String bundleName = String.format("%s.%s",
                                                  parameters.getParameter("groupId").getValue(),
                                                  parameters.getParameter("artifactId").getValue());

               Files.write(settings,
                          String.format("%ninclude '%s'", bundleName).getBytes(),
                           StandardOpenOption.APPEND);

               Files.write(settings,
                           String.format("%nproject(':%s').name = '%s'%n",
                                         bundleName,
                                         parameters.getParameter("artifactId").getValue()).getBytes(),
                           StandardOpenOption.APPEND);
            } catch (IOException ioe) {
               logService.error(getClass(), ioe, "Unable to append to the settings file.");
            }
         }
      }
   }

   @Override
   public String toString() {
       return getName();
   }
}
