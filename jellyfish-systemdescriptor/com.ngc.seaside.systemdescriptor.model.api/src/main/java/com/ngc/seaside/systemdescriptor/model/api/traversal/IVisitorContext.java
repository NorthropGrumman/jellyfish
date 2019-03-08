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
package com.ngc.seaside.systemdescriptor.model.api.traversal;

import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;

/**
 * A context object provided to an {@link IVisitor} during a traversal.  Visitors may return a result from the traversal
 * using {@link #setResult(Object)}.
 *
 * @see ISystemDescriptor#traverse(IVisitor)
 */
public interface IVisitorContext {

   /**
    * Gets the {@link ISystemDescriptor} that is being visited.
    */
   ISystemDescriptor getSystemDescriptor();

   /**
    * Gets the result of the traversal that has been set via {@link #setResult(Object)} or {@code null} if no result is
    * set.
    *
    * @return the result of the traversal
    */
   Object getResult();

   /**
    * Sets the result of the traversal.
    *
    * @param result the result of the traversal
    */
   void setResult(Object result);

   /**
    * Aborts the traversal.  The visitor will no longer be invoked after calling this method.
    */
   void stop();
}
