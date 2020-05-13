/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
