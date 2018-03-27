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
}
