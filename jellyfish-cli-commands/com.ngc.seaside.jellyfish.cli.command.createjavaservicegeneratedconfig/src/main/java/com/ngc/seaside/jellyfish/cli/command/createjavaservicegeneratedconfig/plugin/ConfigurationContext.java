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
package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin;

import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.name.api.IProjectInformation;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.util.Optional;

/**
 * Context for plugins adding generated configuration.
 */
public class ConfigurationContext {

   private IJellyFishCommandOptions options;
   private IModel model;
   private IModel deploymentModel;
   private IProjectInformation projectInformation;
   private String basePackage;
   private ConfigurationType type;

   /**
    * Returns the jellyfish options.
    * 
    * @return the jellyfish options
    */
   public IJellyFishCommandOptions getOptions() {
      return options;
   }

   public ConfigurationContext setOptions(IJellyFishCommandOptions options) {
      this.options = options;
      return this;
   }

   /**
    * Returns the model to be configured.
    * 
    * @return the model to be configured
    */
   public IModel getModel() {
      return model;
   }

   public ConfigurationContext setModel(IModel model) {
      this.model = model;
      return this;
   }

   /**
    * Returns the deployment model, or {@link Optional#empty()} if there is none.
    * 
    * @return the deployment model
    */
   public Optional<IModel> getDeploymentModel() {
      return Optional.ofNullable(deploymentModel);
   }

   public ConfigurationContext setDeploymentModel(IModel deploymentModel) {
      this.deploymentModel = deploymentModel;
      return this;
   }

   /**
    * Returns the project information for the configuration.
    * 
    * @return the project information for the configuration
    */
   public IProjectInformation getProjectInformation() {
      return projectInformation;
   }

   public ConfigurationContext setProjectInformation(IProjectInformation projectInformation) {
      this.projectInformation = projectInformation;
      return this;
   }

   /**
    * Returns the base package for the configuration project.
    * 
    * @return the base package for the configuration project
    */
   public String getBasePackage() {
      return basePackage;
   }

   public ConfigurationContext setBasePackage(String basePackage) {
      this.basePackage = basePackage;
      return this;
   }

   /**
    * Returns the type of configuration being generated.
    * 
    * @return the type of configuration being generated
    */
   public ConfigurationType getConfigurationType() {
      return type;
   }

   public ConfigurationContext setConfigurationType(ConfigurationType type) {
      this.type = type;
      return this;
   }

   public boolean isSystemModel() {
      return CommonParameters.evaluateBooleanParameter(options.getParameters(), CommonParameters.SYSTEM.getName(),
               false);
   }
}
