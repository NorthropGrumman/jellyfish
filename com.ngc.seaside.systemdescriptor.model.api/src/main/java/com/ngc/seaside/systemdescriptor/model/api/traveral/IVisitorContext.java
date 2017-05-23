package com.ngc.seaside.systemdescriptor.model.api.traveral;

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
