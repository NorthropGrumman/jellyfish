package com.ngc.seaside.systemdescriptor.service.repository.api;

import java.nio.file.Path;
import java.util.Set;

/**
 * This interface abstracts the process accessing artifacts from a repository.
 */
public interface IRepositoryService {

   /**
    * Returns the path to an artifact with the given identifier.
    *
    * @param identifier artifact identifier
    * @return the path to an artifact with the given identifier.
    */
   Path getArtifact(String identifier);

   /**
    * Returns the set of paths to the artifacts that the artifact with the given identifier depends on. If the given
    * artifact has no dependencies, returns an empty set.
    *
    * @param identifier artifact identifier
    * @param transitive if {@code false}, returns the immediate dependencies of the artifact; otherwise, returns all
    *                   dependencies, including dependencies of dependencies
    * @return the set of paths to the dependent artifacts
    */
   Set<Path> getArtifactDependencies(String identifier, boolean transitive);

}
