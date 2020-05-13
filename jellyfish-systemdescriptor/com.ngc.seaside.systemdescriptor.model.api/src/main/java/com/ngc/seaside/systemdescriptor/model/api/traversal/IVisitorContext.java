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
