package com.ngc.seaside.jellyfish.service.buildmgmt.api;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;

import java.util.Collection;

/**
 * The build management service is responsible for manging build information for <i>generated</i> projects.  Commands
 * use this service to option information about dependencies when these commands generate projects.  Commands can use
 * the {@link #registerDependency(IJellyFishCommandOptions, String) register} operations to both obtain information
 * about a dependencies and record that that dependency is required to build the newly generated project.
 */
public interface IBuildManagementService {

   /**
    * Gets all dependencies of the given type that have been {@link #registerDependency(IJellyFishCommandOptions,
    * String) registered}.
    *
    * @param options the options used in the current invocation of Jellyfish
    * @param type    the type of dependencies to get
    * @return the registered dependencies of the given type
    */
   Collection<IBuildDependency> getRegisteredDependencies(IJellyFishCommandOptions options, DependencyType type);

   /**
    * Gets the dependency with the given group and artifact ID and registers the dependency as required.  Once this
    * operation is invoked, the dependency will be included in the results of {@link
    * #getRegisteredDependencies(IJellyFishCommandOptions, DependencyType)}.
    *
    * @param options    the options used in the current invocation of Jellyfish
    * @param groupId    the group ID of the dependency
    * @param artifactId the artifact ID of the dependency
    * @return the dependency with the given group and artifact ID
    * @throws IllegalArgumentException if no dependency with the given group ID and artifact ID has been configured
    */
   IBuildDependency registerDependency(IJellyFishCommandOptions options, String groupId, String artifactId);

   /**
    * Gets the dependency with the given group and artifact ID and registers the dependency as required.  The format of
    * the group and artifact ID should be {@code <groupId>:<artifactId>}.  Once this operation is invoked, the
    * dependency will be included in the results of {@link #getRegisteredDependencies(IJellyFishCommandOptions,
    * DependencyType)}.
    *
    * @param options          the options used in the current invocation of Jellyfish
    * @param groupAndArtifact the group and artifact ID in the format {@code <groupId>:<artifactId>}
    * @return the dependency with the given group and artifact ID
    * @throws IllegalArgumentException if no dependency with the given group ID and artifact ID has been configured
    */
   IBuildDependency registerDependency(IJellyFishCommandOptions options, String groupAndArtifact);

   /**
    * Gets the dependency with the given group and artifact ID.  This operation does not {@link
    * #registerDependency(IJellyFishCommandOptions, String, String) register} the dependency
    *
    * @param options    the options used in the current invocation of Jellyfish
    * @param groupId    the group ID of the dependency
    * @param artifactId the artifact ID of the dependency
    * @return the dependency with the given group and artifact ID
    * @throws IllegalArgumentException if no dependency with the given group ID and artifact ID has been configured
    */
   IBuildDependency getDependency(IJellyFishCommandOptions options, String groupId, String artifactId);

   /**
    * Gets the dependency with the given group and artifact ID.  The format of the group and artifact ID should be
    * {@code <groupId>:<artifactId>}.  This operation does not {@link #registerDependency(IJellyFishCommandOptions,
    * String) register} the dependency
    *
    * @param options          the options used in the current invocation of Jellyfish
    * @param groupAndArtifact the group and artifact ID in the format {@code <groupId>:<artifactId>}
    * @return the dependency with the given group and artifact ID
    * @throws IllegalArgumentException if no dependency with the given group ID and artifact ID has been configured
    */
   IBuildDependency getDependency(IJellyFishCommandOptions options, String groupAndArtifact);
}
