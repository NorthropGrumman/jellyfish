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

/**
 * A named child is a child element of parent element that has a unique name.  A parent may not contain more than one
 * child with the same name within the same {@link INamedChildCollection}.  A child's name is always immutable.
 *
 * @param <T> the type of the parent that contains this child.
 */
public interface INamedChild<T> {

   /**
    * Gets the name of this child.
    *
    * @return the name of this child
    */
   String getName();

   /**
    * Gets the parent that contains this child.
    *
    * @return the parent that contains this child
    */
   T getParent();
}
