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
import com.ngc.seaside.jellyfish.api.CommandException;
import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IParameterCollection;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.jellyfish.service.name.api.IProjectInformation;
import com.ngc.seaside.jellyfish.service.name.api.IProjectNamingService;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.nio.file.Path;

/**
 * A base class for Jellyfish commands.  This class provides various convenience operations.  Notable ones include
 * <pre>
 * <ol>
 *    <li>{@link #getOptions()}</li>
 *    <li>{@link #getOutputDirectory()}</li>
 *    <li>{@link #getModel()}</li>
 *    <li>{@link #registerProject(IProjectInformation)}</li>
 *    <li>{@link #unpackDefaultTemplate(IParameterCollection, Path, boolean)}</li>
 *    <li>{@link #unpackSuffixedTemplate(String, IParameterCollection, Path, boolean)} </li>
 * </ol>
 * </pre>
 * <p/>
 * Commands that deal with multiple {@link JellyfishCommandPhase phases} may extend {@link
 * AbstractMultiphaseJellyfishCommand}.
 */
public abstract class AbstractJellyfishCommand extends AbstractCommand<IJellyFishCommandOptions>
         implements IJellyFishCommand {

   protected IProjectNamingService projectNamingService;

   protected IPackageNamingService packageNamingService;

   /**
    * Creates a new command with the given name.
    *
    * @param name the name of the command
    */
   protected AbstractJellyfishCommand(String name) {
      super(name);
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

   /**
    * Gets the model referenced by the command options.
    */
   protected IModel getModel() {
      ISystemDescriptor sd = getOptions().getSystemDescriptor();
      IParameterCollection parameters = getOptions().getParameters();
      final String modelName = parameters.getParameter(CommonParameters.MODEL.getName()).getStringValue();
      return sd.findModel(modelName).orElseThrow(() -> new CommandException("Model not found: " + modelName));
   }

   /**
    * Register a generated projects described by {@code project} with the {@code IBuildManagementService}.
    *
    * @param project the description of the generated project
    */
   protected void registerProject(IProjectInformation project) {
      Preconditions.checkNotNull(project, "project may not be null!");
      Preconditions.checkState(buildManagementService != null, "build mgmt service not set!");
      buildManagementService.registerProject(getOptions(), project);
   }

}
