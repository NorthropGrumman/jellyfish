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
package com.ngc.seaside.jellyfish.service.name.api;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

/**
 * Generates project names and other information for various types of models.
 */
public interface IProjectNamingService {

   /**
    * Gets the human readable name of a root project given a model.
    *
    * @param options options the options the current command is being executed with
    * @param model   the model to get the root project name for
    * @return the name of a root project
    */
   String getRootProjectName(IJellyFishCommandOptions options, IModel model);

   /**
    * Gets the project information for a domain project given a model.
    *
    * @param options options the options the current command is being executed with
    * @param model   the model to get the root project name for
    * @return the project information for a domain project
    */
   IProjectInformation getDomainProjectName(IJellyFishCommandOptions options, IModel model);

   /**
    * Gets the project information for a events project given a model.
    *
    * @param options options the options the current command is being executed with
    * @param model   the model to get the root project name for
    * @return the project information for a events project
    */
   IProjectInformation getEventsProjectName(IJellyFishCommandOptions options, IModel model);

   /**
    * Gets the project information for a connector project given a model.
    *
    * @param options options the options the current command is being executed with
    * @param model   the model to get the root project name for
    * @return the project information for a connector project
    */
   IProjectInformation getConnectorProjectName(IJellyFishCommandOptions options, IModel model);

   /**
    * Gets the project information for a service project given a model.
    *
    * @param options options the options the current command is being executed with
    * @param model   the model to get the root project name for
    * @return the project information for a service project
    */
   IProjectInformation getServiceProjectName(IJellyFishCommandOptions options, IModel model);

   /**
    * Gets the project information for a base service project given a model.
    *
    * @param options options the options the current command is being executed with
    * @param model   the model to get the root project name for
    * @return the project information for a base service project
    */
   IProjectInformation getBaseServiceProjectName(IJellyFishCommandOptions options, IModel model);

   /**
    * Gets the project information for a messages project given a model.
    *
    * @param options options the options the current command is being executed with
    * @param model   the model to get the root project name for
    * @return the project information for a messages project
    */
   IProjectInformation getMessageProjectName(IJellyFishCommandOptions options, IModel model);

   /**
    * Gets the project information for a distribution project given a model.
    *
    * @param options options the options the current command is being executed with
    * @param model   the model to get the root project name for
    * @return the project information for a distribution project
    */
   IProjectInformation getDistributionProjectName(IJellyFishCommandOptions options, IModel model);

   /**
    * Gets the project information for a cucumber tests project given a model.
    *
    * @param options options the options the current command is being executed with
    * @param model   the model to get the root project name for
    * @return the project information for a cucumber tests project
    */
   IProjectInformation getCucumberTestsProjectName(IJellyFishCommandOptions options, IModel model);

   /**
    * Gets the project information for a config project given a model.
    *
    * @param options options the options the current command is being executed with
    * @param model   the model to get the root project name for
    * @return the project information for a cucumber tests project
    */
   IProjectInformation getConfigProjectName(IJellyFishCommandOptions options, IModel model);

   /**
    * Gets the project information for a generated config project given a model.
    *
    * @param options options the options the current command is being executed with
    * @param model   the model to get the root project name for
    * @return the project information for a cucumber tests project
    */
   IProjectInformation getGeneratedConfigProjectName(IJellyFishCommandOptions options, IModel model);

   /**
    * Gets the project information for a config project given a model.
    *
    * @param options options the options the current command is being executed with
    * @param model   the model to get the root project name for
    * @return the project information for a cucumber tests project
    */
   IProjectInformation getCucumberTestsConfigProjectName(IJellyFishCommandOptions options, IModel model);

   /**
    * Gets the project information for a config project given a model.
    *
    * @param options options the options the current command is being executed with
    * @param model   the model to get the root project name for
    * @return the project information for a Publish Subscribe Bridge project
    */
   IProjectInformation getPubSubBridgeProjectName(IJellyFishCommandOptions options, IModel model);
}
