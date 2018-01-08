package com.ngc.seaside.systemdescriptor.model.api;

import java.util.Collection;
import java.util.Optional;

/**
 * A collection type for {@link INamedChild} objects.  This collection behaves similar to a map in that there is at most
 * one child associated with a single name.  Adding a new child object with the same name as an existing child will
 * replace the existing child.  Operations that change the state of this object may throw {@code
 * UnsupportedOperationException}s if the object is immutable.
 *
 * @param <P> the type of parent for the named children
 * @param <T> the actual type of the named children
 */
public interface INamedChildCollection<P, T extends INamedChild<P>> extends Collection<T> {

   /**
    * Gets an optional that contains the named child with given name.
    *
    * @param name the name of the child
    * @return an optional that contains the child or an empty optional if there is no child with the given name
    */
   Optional<T> getByName(String name);
}
