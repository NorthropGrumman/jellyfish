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
