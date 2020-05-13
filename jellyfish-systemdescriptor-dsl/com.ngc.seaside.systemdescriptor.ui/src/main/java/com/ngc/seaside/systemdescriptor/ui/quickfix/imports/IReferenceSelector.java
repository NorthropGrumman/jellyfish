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

import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.util.ITextRegion;

import java.util.List;
import java.util.OptionalInt;

/**
 * Interface for selecting an element from a list of elements
 */
@FunctionalInterface
@ImplementedBy(DialogSelector.class)
public interface IReferenceSelector {

   /**
    * Selects one of the given choices given the resource and usage context.
    *
    * @param choices  list of choices
    * @param resource resource
    * @param usage    text region when making the choice
    * @return the index of the choice made, or {@link OptionalInt#empty()} if no choice was made
    */
   <T> OptionalInt select(List<? extends T> choices, XtextResource resource, ITextRegion usage);

}
