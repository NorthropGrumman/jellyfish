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
