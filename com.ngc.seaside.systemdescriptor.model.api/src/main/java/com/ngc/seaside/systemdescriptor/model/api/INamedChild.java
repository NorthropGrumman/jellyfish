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
