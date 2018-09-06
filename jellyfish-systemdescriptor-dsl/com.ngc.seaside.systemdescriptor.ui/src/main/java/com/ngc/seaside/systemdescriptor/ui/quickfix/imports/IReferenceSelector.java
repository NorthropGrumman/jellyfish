/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
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
