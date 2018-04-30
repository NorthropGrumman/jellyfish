package com.ngc.seaside.jellyfish.cli.command.samplecommand;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.DefaultUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IUsage;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 * The default java bundle built using Gradle. This command does have a template and should be modified to support any
 * structural changes to the project.
 */
@Component(service = IJellyFishCommand.class)
public class SampleCommand implements IJellyFishCommand {

   private static final String NAME = "sample";
   private static final IUsage USAGE = createUsage();

   private static final String OUTPUT_DIR_PROPERTY = CommonParameters.OUTPUT_DIRECTORY.getName();
   private static final String GROUP_ID_PROPERTY = CommonParameters.GROUP_ID.getName();
   private static final String ARTIFACT_ID_PROPERTY = CommonParameters.ARTIFACT_ID.getName();
   private static final String PACKAGE_PROPERTY = CommonParameters.PACKAGE.getName();
   private static final String CLASSNAME_PROPERTY = CommonParameters.CLASSNAME.getName();

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
      return USAGE;
   }

   @Override
   public void run(IJellyFishCommandOptions commandOptions) {
   }

   /**
    * Create the usage for this command. Some of these parameters are provided in the template.
    *
    * @return the usage.
    */
   private static IUsage createUsage() {
      return new DefaultUsage(
            "Create a new Java bundle. This requires that a settings.gradle file be present in the output "
                    + "directory. See the init-java-gradle-repo command.",
            CommonParameters.CLASSNAME.required(),
            CommonParameters.GROUP_ID.required(),
            CommonParameters.ARTIFACT_ID.required(),
            CommonParameters.PACKAGE.required(),
            CommonParameters.OUTPUT_DIRECTORY.required());
   }
}
