/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
package com.ngc.seaside.jellyfish.utilities.command;

import com.google.common.base.Preconditions;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.CommandException;
import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.ICommand;
import com.ngc.seaside.jellyfish.api.ICommandOptions;
import com.ngc.seaside.jellyfish.api.IParameterCollection;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;
import com.ngc.seaside.jellyfish.service.name.api.IProjectInformation;
import com.ngc.seaside.jellyfish.service.template.api.ITemplateOutput;
import com.ngc.seaside.jellyfish.service.template.api.ITemplateService;
import com.ngc.seaside.jellyfish.service.user.api.IJellyfishUserService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

/**
 * A base class for non-system descriptor Jellyfish commands.  This class provides various convenience operations.
 * Notable ones include
 * <pre>
 * <ol>
 *    <li>{@link #getOptions()}</li>
 *    <li>{@link #getOutputDirectory()}</li>
 *    <li>{@link #unpackDefaultTemplate(IParameterCollection, Path, boolean)}</li>
 *    <li>{@link #unpackSuffixedTemplate(String, IParameterCollection, Path, boolean)} </li>
 * </ol>
 * </pre>
 * <p/>
 * Commands that deal with system descriptor projects may extend {@link AbstractJellyfishCommand}.
 * 
 * @param <T> type of command options
 */
public abstract class AbstractCommand<T extends ICommandOptions> implements ICommand<T> {

   /**
    * The name of the parameter that can be referenced in all unpacked templates which resolves to the
    * {@link IBuildManagementService}.
    */
   public static final String BUILT_MANAGEMENT_HELPER_TEMPLATE_VARIABLE = "build";


   /**
    * The name of the parameter that can be referenced in all unpacked templates which resolves to the
    * {@link FileHeader}.  This can be referenced by templates to include a common header in all files.
    */
   public static final String FILE_HEADER_TEMPLATE_VARIABLE = "header";

   /**
    * The name of the property in $JELLYFISH_USER_HOME/jellyfish.properties for identifying the default
    * header file to be used.
    */
   public static final String JELLYFISH_USER_HOME_HEADER_PROPERTY = "jellyfish.generated.header.file";

   protected ILogService logService;

   protected IJellyfishUserService jellyfishUserService;

   protected IBuildManagementService buildManagementService;

   protected ITemplateService templateService;

   private final String name;

   private IUsage usage;
   
   private T options;
   
   /**
    * Creates a new command with the given name.
    *
    * @param name the name of the command
    */
   protected AbstractCommand(String name) {
      Preconditions.checkNotNull(name, "name may not be null!");
      Preconditions.checkArgument(!name.trim().isEmpty(), "name may not be empty!");
      this.name = name;
   }

   @Override
   public String getName() {
      return name;
   }

   @Override
   public synchronized IUsage getUsage() {
      if (usage == null) {
         usage = createUsage();
      }
      return usage;
   }

   @Override
   public void run(T options) {
      try {
         this.options = options;
         doRun();
      } finally {
         this.options = null;
      }
   }

   /**
    * Activates this command.  Extending classes may override this method.
    */
   public void activate() {
   }

   /**
    * Deactivates this command.  Extending classes may override this method.
    */
   public void deactivate() {
   }

   public void setLogService(ILogService ref) {
      this.logService = ref;
   }

   public void removeLogService(ILogService ref) {
      setLogService(null);
   }

   public void setJellyfishUserService(IJellyfishUserService ref) {
      this.jellyfishUserService = ref;
   }

   public void removeJellyfishUserService(IJellyfishUserService ref) {
      setJellyfishUserService(null);
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

   /**
    * Invoked to run this command.
    */
   protected abstract void doRun();

   /**
    * Invoked to create the usage description of this command.
    *
    * @return the usage description of this command
    */
   protected abstract IUsage createUsage();

   /**
    * Gets the options the command was invoked with.
    */
   protected T getOptions() {
      Preconditions.checkState(options != null, "request made to get options outside of run(..) invocation!");
      return options;
   }

   /**
    * Gets the output directory referenced by the command options.
    */
   protected Path getOutputDirectory() {
      return Paths.get(getOptions()
                             .getParameters()
                             .getParameter(CommonParameters.OUTPUT_DIRECTORY.getName())
                             .getStringValue());
   }

   /**
    * Gets the value of the given boolean value.
    */
   protected boolean getBooleanParameter(String parameterName) {
      return CommonParameters.evaluateBooleanParameter(getOptions().getParameters(), parameterName);
   }

   /**
    * Register a generated projects described by {@code project} with the {@code IBuildManagementService}.
    *
    * @param project the description of the generated project
    */
   protected void registerProject(IProjectInformation project) {
      Preconditions.checkNotNull(project, "project may not be null!");
      Preconditions.checkState(buildManagementService != null, "build mgmt service not set!");
      buildManagementService.registerProject(options, project);
   }

   /**
    * Adds default parameters that can be referenced by templates when unpacking templates.
    *
    * @param parameters the parameters provided by the client
    * @return the parameters to use when unpacking templates
    */
   protected IParameterCollection addDefaultUnpackParameters(IParameterCollection parameters) {
      DefaultParameterCollection mutableParams = new DefaultParameterCollection(parameters);

      if (buildManagementService != null) {
         mutableParams.addParameter(new DefaultParameter<>(
               BUILT_MANAGEMENT_HELPER_TEMPLATE_VARIABLE,
               new BuildManagementHelper(buildManagementService, getOptions())).advanced());
      }

      if (mutableParams.containsParameter(CommonParameters.HEADER_FILE.getName())) {
         // Make sure the parameter resolves to a file.
         Path header = Paths.get(mutableParams.getParameter(CommonParameters.HEADER_FILE.getName()).getStringValue());
         if (!Files.isReadable(header)) {
            throw new CommandException(String.format(
                  "the file given by the parameter %s is either not a file or is not readable!  Expected to find a"
                  + " file at %s.",
                  CommonParameters.HEADER_FILE.getName(),
                  header.toAbsolutePath()));
         }
         mutableParams.addParameter(new DefaultParameter<>(FILE_HEADER_TEMPLATE_VARIABLE, new FileHeader(header)));
      } else {
         Map<String, String> jellyfishProperties = jellyfishUserService.getJellyfishUserProperties();
         String licenseFile = jellyfishProperties.get(JELLYFISH_USER_HOME_HEADER_PROPERTY);
         Path licensePath = licenseFile == null ? null : Paths.get(licenseFile);
         FileHeader license = FileHeader.DEFAULT_HEADER;
         if (licensePath != null) {
            if (Files.isRegularFile(licensePath)) {
               license = new FileHeader(licensePath);
            } else {
               logService.error(AbstractCommand.class, "jellyfish.properties property "
                        + JELLYFISH_USER_HOME_HEADER_PROPERTY + "=" + licenseFile + " does not exist");
            }
         }
         mutableParams.addParameter(new DefaultParameter<>(FILE_HEADER_TEMPLATE_VARIABLE, license));
      }

      return mutableParams;
   }

   /**
    * Uses the {@code ITemplateService} to unpack and expand a template for this command.  This operation will
    * automatically place a parameter named {@link #BUILT_MANAGEMENT_HELPER_TEMPLATE_VARIABLE} in the parameter
    * collection.  The value of this parameter is a {@link BuildManagementHelper} object which can be used by a template
    * to interact with the {@link IBuildManagementService}.
    * <p/>
    * Use this operation when the command only has a single template.  Use {@link #unpackSuffixedTemplate(String,
    * IParameterCollection, Path, boolean)} if this command has multiple templates.
    *
    * @param parameters      the parameters to unpack the template with
    * @param outputDirectory the directory that will contain the unpacked and expanded template
    * @param clean           if true, any existing files in {@code outputDirectory} will be removed
    * @return the template output
    */
   protected ITemplateOutput unpackDefaultTemplate(IParameterCollection parameters,
                                                   Path outputDirectory,
                                                   boolean clean) {
      Preconditions.checkState(templateService != null, "template service not set!");
      return templateService.unpack(getClass().getPackage().getName(),
                                    addDefaultUnpackParameters(parameters),
                                    outputDirectory,
                                    clean);
   }

   /**
    * Uses the {@code ITemplateService} to unpack and expand a template for this command that has the given syntax. This
    * operation will automatically place a parameter named {@link #BUILT_MANAGEMENT_HELPER_TEMPLATE_VARIABLE} in the
    * parameter collection.  The value of this parameter is a {@link BuildManagementHelper} object which can be used by
    * a template to interact with the {@link IBuildManagementService}.
    * <p/>
    * This operation is used when a command has multiple templates under a {@code src/main/templates} directory.
    *
    * @param templateSuffix  the suffix of the template to unpack.  This does not include the command name; it is
    *                        usually name of a directory immediately under the {@code templates} directory.
    * @param parameters      the parameters to unpack the template with
    * @param outputDirectory the directory that will contain the unpacked and expanded template
    * @param clean           if true, any existing files in {@code outputDirectory} will be removed
    * @return the template output
    */
   protected ITemplateOutput unpackSuffixedTemplate(String templateSuffix,
                                                    IParameterCollection parameters,
                                                    Path outputDirectory,
                                                    boolean clean) {
      Preconditions.checkState(templateService != null, "template service not set!");
      return templateService.unpack(getClass().getPackage().getName() + "-" + templateSuffix,
                                    addDefaultUnpackParameters(parameters),
                                    outputDirectory,
                                    clean);
   }
}
