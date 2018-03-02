package com.ngc.seaside.jellyfish.utilities.command;

import com.google.common.base.Preconditions;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.CommandException;
import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IParameterCollection;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.jellyfish.service.name.api.IProjectInformation;
import com.ngc.seaside.jellyfish.service.name.api.IProjectNamingService;
import com.ngc.seaside.jellyfish.service.template.api.ITemplateOutput;
import com.ngc.seaside.jellyfish.service.template.api.ITemplateService;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class AbstractJellyfishCommand implements IJellyFishCommand {

   private final String name;

   private final IUsage usage;

   private IJellyFishCommandOptions options;

   protected ILogService logService;

   protected IBuildManagementService buildManagementService;

   protected ITemplateService templateService;

   protected IProjectNamingService projectNamingService;

   protected IPackageNamingService packageNamingService;

   protected AbstractJellyfishCommand(String name) {
      Preconditions.checkNotNull(name, "name may not be null!");
      Preconditions.checkArgument(!name.trim().isEmpty(), "name may not be empty!");
      this.name = name;
      this.usage = createUsage();
   }

   @Override
   public String getName() {
      return name;
   }

   @Override
   public IUsage getUsage() {
      return usage;
   }

   @Override
   public void run(IJellyFishCommandOptions options) {
      try {
         this.options = options;
         doRun();
      } finally {
         this.options = null;
      }
   }

   public void activate() {
   }

   public void deactivate() {
   }

   public void setLogService(ILogService ref) {
      this.logService = ref;
   }

   public void removeLogService(ILogService ref) {
      setLogService(null);
   }

   public void setBuildManagementService(IBuildManagementService ref) {
      this.buildManagementService = ref;
   }

   public void removeBuildManagementService(IBuildManagementService ref) {
      setBuildManagementService(null);
   }

   public void setTemplateService(ITemplateService ref) {
      this.templateService = ref;
   }

   public void removeTemplateService(ITemplateService ref) {
      setTemplateService(null);
   }

   public void setProjectNamingService(IProjectNamingService ref) {
      this.projectNamingService = ref;
   }

   public void removeProjectNamingService(IProjectNamingService ref) {
      setProjectNamingService(null);
   }

   public void setPackageNamingService(IPackageNamingService ref) {
      this.packageNamingService = ref;
   }

   public void removePackageNamingService(IPackageNamingService ref) {
      setPackageNamingService(null);
   }

   protected abstract void doRun();

   protected abstract IUsage createUsage();

   protected IJellyFishCommandOptions getOptions() {
      Preconditions.checkState(options != null, "request made to get options outside of run(..) invocation!");
      return options;
   }

   protected IModel getModel() {
      ISystemDescriptor sd = getOptions().getSystemDescriptor();
      IParameterCollection parameters = getOptions().getParameters();
      final String modelName = parameters.getParameter(CommonParameters.MODEL.getName()).getStringValue();
      return sd.findModel(modelName).orElseThrow(() -> new CommandException("Model not found: " + modelName));
   }

   protected Path getOutputDirectory() {
      return Paths.get(getOptions()
                             .getParameters()
                             .getParameter(CommonParameters.OUTPUT_DIRECTORY.getName())
                             .getStringValue());
   }

   protected boolean getBooleanParameter(String parameterName) {
      return CommonParameters.evaluateBooleanParameter(getOptions().getParameters(), parameterName);
   }

   protected void registerProject(IProjectInformation project) {
      Preconditions.checkNotNull(project, "project may not be null!");
      Preconditions.checkState(buildManagementService != null, "build mgmt service not set!");
      buildManagementService.registerProject(options, project);
   }

   protected ITemplateOutput unpackDefaultTemplate(IParameterCollection parameters,
                                                   Path outputDirectory,
                                                   boolean clean) {
      Preconditions.checkState(templateService != null, "template service not set!");
      return templateService.unpack(getClass().getPackage().getName(), parameters, outputDirectory, clean);
   }

   protected ITemplateOutput unpackSuffixedTemplate(String templateSuffix,
                                                    IParameterCollection parameters,
                                                    Path outputDirectory,
                                                    boolean clean) {
      Preconditions.checkState(templateService != null, "template service not set!");
      return templateService.unpack(getClass().getPackage().getName() + "-" + templateSuffix,
                                    parameters,
                                    outputDirectory,
                                    clean);
   }
}
