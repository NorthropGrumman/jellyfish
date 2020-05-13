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
package com.ngc.seaside.systemdescriptor.ui.quickfix.imports;

import com.google.inject.ImplementedBy;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;

import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.util.ITextRegion;

/**
 * Interface for determining the text location of regions for imports in a package.
 */
@ImplementedBy(DefaultImportsRegionIdentifier.class)
public interface IImportsRegionIdentifier {

   /**
    * Returns the text region of the imports section of the given package. If the package does not have imports,
    * returns the region where the imports can be placed.
    *
    * @param pkg        package
    * @param resource   xtext resource
    * @param fullRegion if true, returns the region immediately following the package's package declaration and up to
    *                   the start of the package's element body
    * @return the text region of the imports section
    */
   ITextRegion getImportsRegion(Package pkg, XtextResource resource, boolean fullRegion);

   /**
    * Returns the text region immediately following the the imports section and up to the start of the package's element
    * body.
    *
    * @param pkg      package
    * @param resource xtext resource
    * @return the text region between the imports section and the package's element body
    */
   ITextRegion getImportsAppendRegion(Package pkg, XtextResource resource);

}
